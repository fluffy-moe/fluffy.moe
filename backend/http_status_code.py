# -*- coding: utf-8 -*-
# http_status_code.py
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
from typing import Tuple, List
class http_status_codes:
	def generate_error_dict(self, code: int, info: str) -> dict:
		return {'code': code, 'info': info}

	@property
	def SUCCESS_200OK(self) -> Tuple[int, List, dict]:
		return (200, [], self.generate_error_dict(0, ''))

	def SUCCESS_LOGIN(self, user_name: str, session_string: str) -> Tuple[int, List, dict]:
		return (200, [user_name, session_string,], self.generate_error_dict(0, ''))

	@property
	def SUCCESS_REGISTER(self) -> Tuple[int, List, dict]:
		return self.SUCCESS_200OK

	def _SUCCESS_WITH_OPTION(self, option: list) -> Tuple[int, List, dict]:
		return (200, option, self.generate_error_dict(0, ''))

	@property
	def ERROR_INVALID_PASSWORD_OR_USER(self) -> Tuple[int, List, dict]:
		return (403, [], self.generate_error_dict(1, 'Invalid password or username'))

	@property
	def ERROR_USERNAME_ALREADY_EXIST(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(2, 'Username already exist'))

	@property
	def ERROR_USERNAME_TOO_LONG(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(3, 'User name should be shorter then 16'))

	@property
	def ERROR_USER_SESSION_INVALID(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(4, 'User session invalid'))

	@property
	def ERROR_USER_SESSION_EXPIRED(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(5, 'User session expired'))

	@property
	def SUCCESS_REGISTER_FIREBASE_ID(self) -> Tuple[int, List, dict]:
		return self.SUCCESS_200OK

	@property
	def SUCCESS_VERIFY_SESSION(self) -> Tuple[int, List, dict]:
		return self.SUCCESS_200OK

	@property
	def ERROR_USER_SESSION_MISSING(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(6, 'Session string missing'))

	@property
	def SUCCESS_LOGOUT(self) -> Tuple[int, List, dict]:
		return self.SUCCESS_200OK

	@property
	def ERROR_INVALID_REQUEST(self) -> Tuple[int, List, dict]:
		return (403, [], self.generate_error_dict(7, 'Invalid request'))

	@property
	def ERROR_400_BAD_REQUEST(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(400, 'Bad Request'))

	@property
	def ERROR_403_FORBIDDEN(self) -> Tuple[int, List, dict]:
		return (403, [], self.generate_error_dict(403, 'Forbidden'))

	@property
	def SUCCESS_SEND_FIREBASE_NOTIFICATION(self) -> Tuple[int, List, dict]:
		return self.SUCCESS_200OK

	@property
	def ERROR_SEND_FIREBASE_NOTIFICATION_FAILURE(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(1001, 'Send notification failure'))

	@property
	def ERROR_SEND_FIREBASE_NOTIFICATION_PARTILAL_FAILURE(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(1002, 'Send notification partial failure'))

	def SUCCESS_FETCH_NOTIFICATIONS(self, jsonObj: str) -> Tuple[int, List, dict]:
		return (200, [jsonObj,], self.generate_error_dict(0, ''))

	@property
	def ERROR_FIREBASE_TOKEN_LENGTH_TOO_SHORT(self) -> Tuple[int, List, dict]:
		return (400, [], self.generate_error_dict(8, 'Firebase token length too short'))

	@property
	def SUCCESS_UPDATE_INFO(self) -> Tuple[int, List, dict]:
		return self.SUCCESS_200OK

	def SUCCESS_INSERT(self, user_id: int) -> Tuple[int, List, dict]:
		return self._SUCCESS_WITH_OPTION([user_id,])

HTTP_STATUS_CODES = http_status_codes()