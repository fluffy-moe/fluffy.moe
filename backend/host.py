# -*- coding: utf-8 -*-
# host.py
# Copyright (C) 2019-2020 KunoiSayami
#
# This module is part of Fluffy and is released under
# the AGPL v3 License: https://www.gnu.org/licenses/agpl-3.0.txt
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program. If not, see <https://www.gnu.org/licenses/>.
import datetime
import hashlib
import json
import os
import random
import time
import zlib
from configparser import ConfigParser
from http.server import HTTPServer
from typing import List, Tuple, Union

import Objects
from fcmbackend import FcmService
from http_status_code import HTTP_STATUS_CODES
from libpy3.mysqldb import mysqldb as MySqlDB
from server import Server as _exServer

expire_day = 2 * 60 * 60 * 24

class Server(_exServer):
	def log_login_attmept(self, user: str, b: bool):
		MySqlDB.get_instance().execute("INSERT INTO `log_login` (`attempt_user`, `success`) VALUE (%s, %s)", (user, 'Y' if b else 'N'))
		if b:
			MySqlDB.get_instance().execute("UPDATE `accounts` SET `last_login` = CURRENT_TIMESTAMP() WHERE `username` = %s", user)

	def verify_user_session(self, A_auth: str) -> Tuple[bool, Tuple[int, List, dict], Union[None, Tuple]]:
		if A_auth is None:
			return False, HTTP_STATUS_CODES.ERROR_USER_SESSION_MISSING, None
		sqlObj = MySqlDB.get_instance().query1("SELECT `user_id`, `timestamp` FROM `user_session` WHERE `session` = %s", A_auth)
		if sqlObj is None:
			return False, HTTP_STATUS_CODES.ERROR_USER_SESSION_INVALID, None
		# Update session timestamp if session still in use
		MySqlDB.get_instance().execute("UPDATE `user_session` SET `timestamp` = CURRENT_TIMESTAMP() WHERE `session` = %s", A_auth)
		if (datetime.datetime.now() - sqlObj['timestamp']).total_seconds() > expire_day:
			return False, HTTP_STATUS_CODES.ERROR_USER_SESSION_EXPIRED, sqlObj
		return True, HTTP_STATUS_CODES.SUCCESS_VERIFY_SESSION, sqlObj

	def _do_GET(self):
		A_auth = self.headers.get('A-auth')

		if self.path == '/fetchNotification':
			if A_auth is None:
				user_id = 0
			else:
				sqlObj = MySqlDB.get_instance().query1("SELECT `user_id` FROM `user_session` WHERE `session` = %s", A_auth)
				if sqlObj is None:
					user_id = 0
				else:
					user_id = sqlObj['user_id']
			sqlObj = MySqlDB.get_instance().query("SELECT `title`, `body`, `timestamp` FROM `notifications` WHERE "
				"(`affected_user` LIKE '%%{}%%' OR `affected_user` = 'all') AND `timestamp` > DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 15 DAY) ".format(user_id) +
				"AND `available` = 'Y' ORDER BY `id` DESC LIMIT 15")
			return HTTP_STATUS_CODES.SUCCESS_FETCH_NOTIFICATIONS([{'title': x['title'], 'body': x['body'], 'timestamp': str(x['timestamp'])} for x in sqlObj])

		return HTTP_STATUS_CODES.ERROR_400_BAD_REQUEST

	def _do_POST(self, jsonObject: dict):
		A_auth = self.headers.get('A-auth')

		# Process user login
		if self.path == '/login':
			sqlObj = MySqlDB.get_instance().query1("SELECT * FROM `accounts` WHERE `username` = %s", jsonObject['user'])
			if sqlObj is None:
				self.log_login_attmept(jsonObject['user'], False)
				return HTTP_STATUS_CODES.ERROR_INVALID_PASSWORD_OR_USER
			else:
				if sqlObj['password'] != jsonObject['password']:
					self.log_login_attmept(jsonObject['user'], False)
					return HTTP_STATUS_CODES.ERROR_INVALID_PASSWORD_OR_USER
				else:
					self.log_login_attmept(jsonObject['user'], True)
					session = self.generate_new_session_str(jsonObject['user'])
					MySqlDB.get_instance().execute("INSERT INTO `user_session` (`session`, `user_id`) VALUE (%s, %s)", (session, sqlObj['id']))
					return HTTP_STATUS_CODES.SUCCESS_LOGIN(jsonObject['user'], session)

		# Process register user
		elif self.path == '/register':
			obj = Objects.UserObject(jsonObject)
			if len(obj) > 16:
				return HTTP_STATUS_CODES.ERROR_USERNAME_TOO_LONG
			sqlObj = MySqlDB.get_instance().query1("SELECT * FROM `accounts` WHERE `username` = %s", obj.user)
			if sqlObj is None:
				MySqlDB.get_instance().execute("INSERT INTO `accounts` (`username`, `password`) VALUE (%s, %s)",
					(obj.user, obj.password))
				return HTTP_STATUS_CODES.SUCCESS_REGISTER
			else:
				return HTTP_STATUS_CODES.ERROR_USERNAME_ALREADY_EXIST

		# Process register firebase ID
		elif self.path == '/register_firebase':
			r, rt_value, sqlObj = self.verify_user_session(A_auth)
			if not r: return rt_value
			obj = Objects.FirebaseIDObj(jsonObject)
			if not len(obj):
				return HTTP_STATUS_CODES.ERROR_400_BAD_REQUEST
			sqlObj1 = MySqlDB.get_instance().query1("SELECT `user_id` FROM `firebasetoken` WHERE `token` = %s", obj.token)
			if sqlObj1 is None:
				MySqlDB.get_instance().execute("INSERT INTO `firebasetoken` (`user_id`, `token`) VALUE (%s, %s)", (sqlObj['user_id'], obj.token))
			elif sqlObj['user_id'] != sqlObj1['user_id']:
				MySqlDB.get_instance().execute("UPDATE `firebasetoken` SET `user_id` = %s WHERE `token` = %s", (sqlObj['user_id'], obj.token))
			else:
				MySqlDB.get_instance().execute("UPDATE `firebasetoken` SET `register_date` = CURRENT_TIMESTAMP() WHERE `token` = %s", obj.token)
			return HTTP_STATUS_CODES.SUCCESS_REGISTER_FIREBASE_ID

		# Process verify user session string
		elif self.path == '/verify':
			_r, rt_value, _ = self.verify_user_session(A_auth)
			return rt_value

		# Process user logout
		elif self.path == '/logout':
			if A_auth is None:
				return HTTP_STATUS_CODES.ERROR_USER_SESSION_MISSING
			MySqlDB.get_instance().execute("DELETE FROM `user_session` WHERE `session` = %s", A_auth)
			return HTTP_STATUS_CODES.SUCCESS_LOGOUT

		elif self.path == '/admin':
			return self.handle_manage_html_post(jsonObject)

		return HTTP_STATUS_CODES.ERROR_INVALID_REQUEST

	def handle_manage_html_post(self, d: dict):
		# Not behind reversed proxy, bypass it
		if self.headers.get('X-Real-IP') and Objects.PassThroughArgs.get_instance().check_ip_trust(self.headers.get('X-Real-IP')):
			return HTTP_STATUS_CODES.ERROR_403_FORBIDDEN
		if 't' not in d:
			return HTTP_STATUS_CODES.ERROR_400_BAD_REQUEST
		if d['t'] == 'update_person':
			if d['uid'] != '':
				MySqlDB.get_instance().execute("UPDATE `feeder_information` SET `realname` = %s, `phone` = %s, `address` = %s, `email` = %s WHERE `id` = %s",
					(d['name'], d['phone'], d['email'], d['address'], d['uid']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `feeder_information` (`realname`, `phone`, `address`, `email`) VALUE (%s, %s, %s, %s)",
					(d['name'], d['phone'], d['email'], d['address']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		elif d['t'] == 'update_pet':
			if d['pet_no'] != '':
				MySqlDB.get_instance().execute("UPDATE `pet_information` SET `name` = %s, `gender` = %s, `breed` = %s, `color` = %s, `birthday` = %s, `weight` = %s, `neuter` = %s WHERE `id` = %s",
									(d['petname'], d['gender'], d['varity'], d['color'], d['birthday'], d['weight'], d['neuter'], d['pet_no']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `pet_information` (`belong`, `name`, `gender`, `breed`, `color`, `birthday`, `weight`, `neuter`) VALUE (%s, %s, %s, %s, %s, %s, %s, %s)",
									(d['belong'], d['petname'], d['gender'], d['varity'], d['color'], d['birthday'], d['weight'], d['neuter']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		elif d['t'] == 'update_vac':
			if d['id'] != '':
				MySqlDB.get_instance().execute("UPDATE `vaccination_record` SET `date` = %s, `product` = %s, `injection_site` = %s,`doctor` = %s WHERE `id` = %s",
									(d['date'], d['prodoct'], d['injection_site'], d['doctor'],d['id']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `vaccination_record` (`date`, `product`, `injection_site`, `doctor`) VALUE (%s, %s,%s,%s)",
									(d['date'], d['prodoct'], d['injection_site'], d['doctor']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		elif d['t'] == 'update_dein':
			if d['id'] !='':
				MySqlDB.get_instance().execute("UPDATE `deinsectzation_record` SET `date` = %s, `product` = %s, `doctor` = %s WHERE `id` = %s",
									(d['date'], d['product'], d['doctor'], d['id']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `deinsectzation_record` (`date`, `product`, `doctor`) VALUE (%s, %s, %s)",
									(d['date'], d['product'], d['doctor']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		elif d['t'] == 'update_hs':
			if d['id'] !='':
				MySqlDB.get_instance().execute("UPDATE `hospital_admission_record` SET `start_date` = %s, `end_date` = %s WHERE `id` = %s",
									(d['start_date'], d['end_date'], d['id']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `hospital_admission_record` (`start_date`, `end_date`) VALUE (%s, %s)",
									(d['start_date'], d['end_date']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		elif d['t'] =='update_opc':
			if d['id'] !='':
				MySqlDB.get_instance().execute("UPDATE `outpatient_clinic_record` SET `date` = %s, `symptom` = %s WHERE `id` = %s",
									(d['date'], d['symptom'], d['id']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `outpatient_clinic_record` (`date`, `symptom`) VALUE (%s, %s)",
									(d['date'], d['symptom']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		elif d['t'] == 'update_hem':
			if d['id'] !='':
				MySqlDB.get_instance().execute("UPDATE `hematology_test_record` SET `date` = %s, `RBC` = %s, `HCT` = %s, `CGB` = %s, `MCH` = %s, `MCHC` = %s WHERE `id` = %s",
									(d['date'], d['RBC'], d['HCT'], d['CGB'], d['MCH'], d['MCHC'], d['id']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `hematology_test_record` (`date`, `RBC`, `HCT`, `CGB`, `MCH`, `MCHC`) VALUE (%s, %s, %s, %s, %s, %s)",
									(d['date'], d['RBC'], d['HCT'], d['CGB'], d['MCH'], d['MCHC']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		elif d['t'] == 'update_kid':
			if d['id'] !='':
				MySqlDB.get_instance().execute("UPDATE `kidney_test_record` SET `date` = %s, `CREA` = %s, `BUM` = %s, `PHOS` = %s, `CA` = %s, `ALB` = %s, `CHOL` = %s, `PCT` = %s WHERE `id` = %s",
									(d['date'], d['CREA'], d['BUM'], d['PHOS'], d['CA'], d['ALB'], d['CHOL'], d['PCT'], d['id']))
				return HTTP_STATUS_CODES.SUCCESS_UPDATE_INFO
			else:
				MySqlDB.get_instance().execute("INSERT `kidney_test_record` (`date`, `CREA`, `BUM`, `PHOS`, `CA`, `ALB`, `CHOL`, `PCT`) VALUE (%s, %s, %s, %s, %s, %s, %s, %s)",
									(d['date'], d['CREA'], d['BUM'], d['PHOS'], d['CA'], d['ALB'], d['CHOL'], d['PCT']))
				return HTTP_STATUS_CODES.SUCCESS_INSERT(MySqlDB.get_instance().query1("SELECT LAST_INSERT_ID() AS `id`")['id'])
		return HTTP_STATUS_CODES.ERROR_INVALID_REQUEST

	def handle_manage_request(self, d: dict):
		# Not behind reversed proxy, bypass it
		if self.headers.get('X-Real-IP') and Objects.PassThroughArgs.get_instance().check_ip_trust(self.headers.get('X-Real-IP')):
			return HTTP_STATUS_CODES.ERROR_403_FORBIDDEN
		if 't' not in d:
			return HTTP_STATUS_CODES.ERROR_400_BAD_REQUEST
		if d['t'] == 'firebase_post':
			if d['select_user'] == 'all':
				sqlObj = MySqlDB.get_instance().query("SELECT `token` FROM `firebasetoken` WHERE `register_date` > DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 2 MONTH)")
				if len(sqlObj) == 1:
					r = FcmService.get_instance().push_services(sqlObj[0]['token'], d['title'], d['body'])
				else:
					r = FcmService.get_instance().push_services([x['token'] for x in sqlObj], d['title'], d['body'])
			else: # part of user
				devices = []
				for user in d['select_user']:
					sqlObj = MySqlDB.get_instance().query("SELECT `token` FROM `firebasetoken` WHERE `user_id` = %s AND `register_date` > DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 2 MONTH)", user)
					devices.extend(x['token'] for x in sqlObj)
				if len(devices) == 1:
					r = FcmService.get_instance().push_services(devices[0], d['title'], d['body'])
				else:
					r = FcmService.get_instance().push_services(devices, d['title'], d['body'])
			if r['failure'] > 0:
				if r['success'] == 0:
					return HTTP_STATUS_CODES.ERROR_SEND_FIREBASE_NOTIFICATION_FAILURE
				else:
					return HTTP_STATUS_CODES.ERROR_SEND_FIREBASE_NOTIFICATION_PARTILAL_FAILURE
			u = ','.join(d['select_user']) if d['select_user'] != 'all' else 'all'
			MySqlDB.get_instance().execute("INSERT INTO `notifications` (`title`, `body`, `affected_user`) VALUE (%s, %s, %s)",
				(d['title'], d['body'], u))
			return HTTP_STATUS_CODES.SUCCESS_200OK
		elif d['t'] == 'notification_manage':
			MySqlDB.get_instance().execute("UPDATE `notifications` SET `available` = 'N' WHERE `id` IN ({})".format(', '.join(d['unchecked'])))
			MySqlDB.get_instance().execute("UPDATE `notifications` SET `available` = 'Y' WHERE `id` IN ({})".format(', '.join(d['checked'])))
			return HTTP_STATUS_CODES.SUCCESS_200OK
		return HTTP_STATUS_CODES.ERROR_400_BAD_REQUEST

	@staticmethod
	def generate_new_session_str(user_name: str):
		return ''.join(x.hexdigest() for x in map(
			hashlib.sha256, [
				os.urandom(16),
				str(time.time()).encode(),
				os.urandom(16),
				user_name.encode()
			]))

	@staticmethod
	def crc32(s: str):
		return hex(zlib.crc32(s.encode()) & 0xffffffff)[2:].upper()

	def get_userid_from_username(self, user_name: str):
		sqlObj = MySqlDB.get_instance().query1("SELECT `id` FROM `accounts` WHERE `username` = %s AND `enabled` = 'Y'", user_name)
		if sqlObj is not None:
			return sqlObj['id']
		return None

class appServer:
	def __init__(self):
		config = ConfigParser()
		config.read('config.ini')

		self.mysql_conn = MySqlDB.init_instance(
			config['mysql']['host'],
			config['mysql']['user'],
			config['mysql']['password'],
			config['mysql']['database'],
			autocommit=True
		)

		Objects.PassThroughArgs.init_instance(list(map(lambda x: x.strip(), config.get('server', 'trust_ip', fallback='').split(','))))

		self.fcmService = FcmService.init_instance(config['firebase']['api_key'])

		self.server_handle = HTTPServer(
			(config['server']['address'], config.getint('server', 'port')),
			Server)

	def run(self):
		self.mysql_conn.do_keepalive()
		self.server_handle.serve_forever()

	def close(self):
		self.mysql_conn.close()

if __name__ == "__main__":
	appserver = appServer()
	try:
		appserver.run()
	except InterruptedError:
		appserver.close()
		raise
