# -*- coding: utf-8 -*-
# Objects.py
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
import json
from typing import List

class FirebaseIDObj:
	def __init__(self, obj: dict):
		self._user_id = obj['user_id']
		self._token = obj['token']
	
	@property
	def user_id(self) -> str:
		return self._user_id
	
	@property
	def token(self) -> str:
		return self._token
	
	def __len__(self) -> int:
		return len(self._token)

class UserObject:
	def __init__(self, obj: dict):
		self._user = obj['user']
		self._password = obj['password']
	
	@property
	def user(self) -> str:
		return self._user
	
	@property
	def password(self) -> str:
		return self._password

	def __len__(self) -> int:
		return len(self._user)

class _PassThroughArgs:
	def __init__(self, _trust_ip: List[str]):
		self._trust_ip = _trust_ip
	
	@property
	def trust_ip(self) -> list:
		return self._trust_ip
	
	def check_ip_trust(self, ip_addr: str) -> bool:
		return ip_addr in self._trust_ip

class PassThroughArgs(_PassThroughArgs):
	_self = None

	@staticmethod
	def get_instance() -> _PassThroughArgs:
		return PassThroughArgs._self

	@staticmethod
	def init_instance(_trust_ip: List[str]) -> _PassThroughArgs:
		PassThroughArgs._self = _PassThroughArgs(_trust_ip)
		return PassThroughArgs._self
