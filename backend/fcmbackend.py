# -*- coding: utf-8 -*-
# fcmbackend.py
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
import time
from pyfcm import FCMNotification
from typing import Union, List

class _FcmService:
	def __init__(self, api_key: str):
		self.push_service = FCMNotification(api_key)
	
	def push_services(self, device_id: str or list, title: str, body: str):
		if isinstance(device_id, str):
			return self.push_service.notify_single_device(device_id, message_title=title, message_body=body)
		else:
			return self.push_service.notify_multiple_devices(device_id, message_title=title, message_body=body)

class FcmService(_FcmService):
	_self = None

	@staticmethod
	def init_instance(api_key: str) -> _FcmService:
		FcmService._self = _FcmService(api_key)
		return FcmService._self
	
	@staticmethod
	def get_instance() -> _FcmService:
		return FcmService._self

