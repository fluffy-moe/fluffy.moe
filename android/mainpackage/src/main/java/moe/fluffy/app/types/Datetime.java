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

import com.codbking.calendar.CalendarBean;

import java.util.Calendar;

public class Datetime extends Date {
	protected int hour, minute, second, millionSecond;
	private static Datetime today;
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

	public static Datetime getToday() {
		if (today == null) {
			java.util.Date d = new java.util.Date();
			Calendar c = Calendar.getInstance();
			c.setTime(new java.util.Date());
			today = new Datetime(c);
			//Log.v(TAG, "Today is " + today.year + "/" + today.month + "/" + today.day);
		}
		return today;
	}

	Datetime(Calendar c) {
		this(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND));
	}

	public Datetime(CalendarBean c) {
		super(c);
		Datetime d = getToday();
		hour = d.getHour();
		minute = d.getMinute();
		second = d.getSecond();
		millionSecond = d.getMillionSecond();
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
	public Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, hour, minute);
		return c;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return super.equals(obj);
	}
}
