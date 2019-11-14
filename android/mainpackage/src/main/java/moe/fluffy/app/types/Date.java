/*
 ** Copyright (C) 2019 KunoiSayami
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
package moe.fluffy.app.types;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codbking.calendar.CalendarBean;

import moe.fluffy.app.BuildConfig;

@SuppressLint("DefaultLocale")
public class Date {
	protected int year, month, day;
	private static String TAG = "log_DateType";
	Date(int _year, int _month, int _day) {
		year = _year;
		month = _month;
		day = _day;
	}

	/**
	 * Init class from string which is query from database
	 *
	 * @param dateString query from database
	 */
	public Date(String dateString) {
		String[] s = dateString.split("/");
		if (BuildConfig.DEBUG && s.length != 3) {
			Log.e(TAG, "Date: " + ERROR.UNEXPECTED_LENGTH);
			throw new AssertionError(ERROR.UNEXPECTED_LENGTH);
		}
		year = Integer.parseInt(s[0]);
		month = Integer.parseInt(s[1]);
		day = Integer.parseInt(s[2]);
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public boolean equals(@Nullable CalendarBean c) {
		if (c == null) return false;
		return (c.year == this.year) && (c.moth == this.month) && (c.day == this.day);
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("%d/%d/%d", year, month, day);
	}
}
