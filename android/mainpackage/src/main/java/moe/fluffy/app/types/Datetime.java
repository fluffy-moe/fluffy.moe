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

import androidx.annotation.Nullable;

import java.util.Calendar;

public class Datetime extends Date {
	protected int hour, minute, second, millionSecond;
	Datetime(int _year, int _month, int _day, int _hour, int _minute, int _second, int _millionSecond) {
		super(_year, _month, _day);
		hour = _hour;
		minute = _minute;
		second = _second;
		millionSecond = _millionSecond;
	}

	Datetime(String s, String t) {
		super(s);
		String[] y = t.split(":");
		hour = Integer.valueOf(y[0]);
		minute = Integer.valueOf(y[1]);
		second = Integer.valueOf(y[2]);
		millionSecond = 0;
	}


	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public int getMillionSecond() {
		return millionSecond;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return super.equals(obj);
	}
}
