# -*- coding: utf-8 -*-
# server.py
# Copyright (C) 2019 KunoiSayami
#
# This module is part of 1081-NoticeDemo and is released under
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
import time
import json
from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import cgi

def build_status_json(status: int, options: list, errors: dict):
	'''
		errors structure should be: {code: ... , info: ...}
	'''
	return json.dumps({'status': status, 'options': options, 'errors': errors}).encode()

# https://gist.github.com/nitaku/10d0662536f37a087e1b
class Server(BaseHTTPRequestHandler):

	conn = None
	mdict = {}
	fcmbackend = None

	def _set_headers(self):
		self.send_response(200)
		self.send_header('Content-type', 'application/json')
		self.end_headers()

	def do_HEAD(self):
		self._set_headers()

	def _do_GET(self):
		return 200, [], {'code': 0, 'info': ''}

	# GET sends back a Hello world message
	def do_GET(self):
		self.update_real_ip()
		status, options, errors = self._do_GET()
		self._set_headers()
		self.wfile.write(build_status_json(status, options, errors))

	def _do_POST(self, _jsonObject: dict):
		return self._do_GET()

	def update_real_ip(self):
		X_REAL_IP = self.headers.get('X-Real-IP')
		if X_REAL_IP:
			self.client_address = (X_REAL_IP, self.client_address[1])

	# POST echoes the message adding a JSON field
	def do_POST(self):
		self.update_real_ip()
		ctype, pdict = cgi.parse_header(self.headers.get_content_type())

		# refuse to receive non-json content
		if ctype != 'application/json':
			self.send_response(400)
			self.end_headers()
			return

		# read the message and convert it into a python dictionary
		length = int(self.headers.get('content-length'))
		input_json = json.loads(self.rfile.read(length))

		# add a property to the object, just to mess with data
		status, options, errors = self._do_POST(input_json)

		# send the message back
		self._set_headers()
		self.wfile.write(build_status_json(status, options, errors))

if __name__ == "__main__":
		hts = HTTPServer(('127.0.0.1', 8080), Server)
		hts.serve_forever()