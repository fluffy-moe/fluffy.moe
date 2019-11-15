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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import moe.fluffy.app.R;

public class EventsType {
	//private int year, month, day;
	private DateWithMark date;
	private String category;
	private String body;

	private static String columnYear, columnMonth, columnDay, columnCategory, columnBody, columnHour, columnMinute, columnColor;

	void getColumnName(Context c){
		columnYear = c.getString(R.string.dbEventsYear);
		columnMonth = c.getString(R.string.dbEventsMonth);
		columnDay = c.getString(R.string.dbEventsDay);
		columnCategory = c.getString(R.string.dbEventsCategory);
		columnBody = c.getString(R.string.dbEventsBody);
		columnHour = c.getString(R.string.dbEventsHour);
		columnMinute = c.getString(R.string.dbEventsMinute);
		columnColor = c.getString(R.string.dbEventColor);
	}

	EventsType(DateWithMark d, String c, String b) {
		date = d;
		category = c;
		body = b;
	}

	EventsType(String strDate, String strTime, int color, String c, String b) {
		date = new DateWithMark(strDate, strTime, color);
		category = c;
		body = b;
	}

	public EventsType(Cursor cursor) {
		int year, month, day, hour, minute;
		year = cursor.getInt(cursor.getColumnIndexOrThrow(columnYear));
		month = cursor.getInt(cursor.getColumnIndexOrThrow(columnMonth));
		day = cursor.getInt(cursor.getColumnIndexOrThrow(columnDay));
		hour = cursor.getInt(cursor.getColumnIndexOrThrow(columnHour));
		minute = cursor.getInt(cursor.getColumnIndexOrThrow(columnMinute));
		date = new DateWithMark(year, month, day, hour, minute, 0, 0, cursor.getInt(cursor.getColumnIndexOrThrow(columnColor)));
		category = cursor.getString(cursor.getColumnIndexOrThrow(columnCategory));
		body = cursor.getString(cursor.getColumnIndexOrThrow(columnBody));
	}

	public int getYear() {
		return date.getYear();
	}

	public int getMonth() {
		return date.getMonth();
	}

	public int getDay() {
		return date.getDay();
	}

	public String getCategory() {
		return category;
	}

	public String getBody() {
		return body;
	}

	public int getColor() {
		return date.getColor();
	}

	@Override
	public boolean equals(@Nullable Object o) {
		return date.equals(o);
	}

	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(columnYear, date.getYear());
		cv.put(columnMonth, date.getMonth());
		cv.put(columnDay, date.getDay());
		cv.put(columnHour, date.getHour());
		cv.put(columnMinute, date.getMinute());
		cv.put(columnCategory, category);
		cv.put(columnBody, body);
		cv.put(columnColor, date.getColor());
		return cv;
	}

	public String getDayOfWeek() {
		return Date.getDayOfWeekString(date.getDayOfWeek());
	}
}
