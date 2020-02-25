<?php
	/**
	*	This module is part of Fluffy and is released under
	*	the AGPL v3 License: https://www.gnu.org/licenses/agpl-3.0.txt
	*
	*	This program is free software: you can redistribute it and/or modify
	*	it under the terms of the GNU Affero General Public License as published by
	*	the Free Software Foundation, either version 3 of the License, or
	*	any later version.
	*
	*	This program is distributed in the hope that it will be useful,
	*	but WITHOUT ANY WARRANTY; without even the implied warranty of
	*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	*	GNU Affero General Public License for more details.
	*
	*	You should have received a copy of the GNU Affero General Public License
	*	along with this program. If not, see <https://www.gnu.org/licenses/>.
	*/
	@session_start();
	ini_set("display_errors","Off");  //set this to "On" for debugging, especially when no reason blank shows up.
	//ini_set("session.cookie_httponly", 1);
	header('X-Frame-Options:SAMEORIGIN');

	static $SITE_NAME = '';

	static $DB_HOST = '';
	static $DB_PORT = 3306;
	static $DB_USER = '';
	static $DB_PASSWORD = '';
	static $DB_NAME = '';

	static $BACKEND_SERVER_ADMIN_PAGE = ''; // Should in localhost will more secure
?>