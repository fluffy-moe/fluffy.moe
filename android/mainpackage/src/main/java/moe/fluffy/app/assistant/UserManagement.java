/*
 ** Copyright (C) 2019-2020 KunoiSayami
 **
 ** This file is part of Fluffy and is released under
 ** the AGPL v3 License: https://www.gnu.org/licenses/agpl-3.0.txt
 **
 ** This program is free software: you can redistribute it and/or modify
 ** it under the terms of the GNU Affero General Public License as published by
 ** the Free Software Foundation, either version 3 of the License, or
 ** any later version.
 **
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 ** GNU Affero General Public License for more details.
 **
 ** You should have received a copy of the GNU Affero General Public License
 ** along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package moe.fluffy.app.assistant;

import androidx.annotation.NonNull;

public class UserManagement {
	private static UserManagement instance;

	private String username, session;

	@NonNull
	public static UserManagement getInstance() {
		if (instance == null)
			instance = new UserManagement();
		return instance;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setSession(String session) {
		this.session = session;
	}

	String getSession() {
		return session;
	}

	public String getUsername() {
		return username;
	}
}
