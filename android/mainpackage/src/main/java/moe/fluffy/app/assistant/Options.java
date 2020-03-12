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

import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Options {
	private static final String TAG = "log_Options";
	private static Options instance;

	public static void initInstance() {
		instance = new Options();
		DatabaseHelper dbhelper = DatabaseHelper.getInstance();
		Cursor c = dbhelper.queryOptionsEx("first_run");
		if (true||c.getCount() == 0) {
			dbhelper.__dropOptions();
			dbhelper._addOption("first_run", parse(false));
			dbhelper._addOption("remember_password", parse(false));
			dbhelper._addOption("username", "");
			dbhelper._addOption("password", "");
			dbhelper._addOption("session", "");
			dbhelper._addOption("user", ""); // TODO: check is duplicated?
			instance.setRememberPassword(false);
			instance.setFirstRun(true);
		}
		else {
			instance.setFirstRun(false);
		}
		c.close();
		instance.setRememberPassword(parse(dbhelper.queryOptions("remember_password")));
		instance.setUsername(dbhelper.queryOptions("username"));
		instance.setPassword(dbhelper.queryOptions("password"));
	}

	private String username = "", password = "";

	public String getUsername() {
		return username;
	}

	private void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		this.password = password;
	}

	public boolean isFirstRun() {
		return firstRun;
	}

	private void setFirstRun(boolean firstRun) {
		this.firstRun = firstRun;
	}

	public boolean isRememberPassword() {
		return rememberPassword;
	}

	private void setRememberPassword(boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}

	public void togglePasswordRemember(boolean rememberPassword) {
		this.rememberPassword = !rememberPassword;
	}

	private boolean firstRun, rememberPassword;

	private static boolean parse(@NonNull String value) {
		return value.toLowerCase().equals("true");
	}

	@NonNull
	private static String parse(boolean value) {
		return Boolean.toString(value);
	}

	@Contract(pure = true)
	@NonNull
	public static Options getInstance() {
		return instance;
	}
}
