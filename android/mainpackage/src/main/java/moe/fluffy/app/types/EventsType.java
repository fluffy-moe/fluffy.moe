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
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.codbking.calendar.CalendarBean;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.TestFragment;

public class EventsType {
	private DateWithMark date;
	private String category;
	private String body;
	private boolean alarm;

	private static String columnYear, columnMonth, columnDay, columnCategory, columnBody, columnHour,
			columnMinute, columnColor, columnAlarm;

	private static String TAG = "log_EventsType";

	public static void getColumnName(Context context){
		columnYear = context.getString(R.string.dbEventsYear);
		columnMonth = context.getString(R.string.dbEventsMonth);
		columnDay = context.getString(R.string.dbEventsDay);
		columnCategory = context.getString(R.string.dbEventsCategory);
		columnBody = context.getString(R.string.dbEventsBody);
		columnHour = context.getString(R.string.dbEventsHour);
		columnMinute = context.getString(R.string.dbEventsMinute);
		columnColor = context.getString(R.string.dbEventColor);
		columnAlarm = context.getString(R.string.dbEventNeedAlarm);
	}

	public EventsType(DateWithMark d, String c, String b, boolean _alarm) {
		date = d;
		category = c;
		body = b;
		alarm = _alarm;
	}

	public EventsType(int _year, int _month, int _day, int _hour, int _minute, int color, String c, String b, boolean _alarm) {
		this(new DateWithMark(_year, _month, _day, _hour, _minute, 0,0, color),
				c,
				b,
				_alarm);
	}

	public EventsType(TestFragment t, int color, String c, String b, boolean _alarm) {
		this(new DateWithMark(t.getYear(), t.getMonth(), t.getDay(), t.getHour(), t.getMinute(), 0, 0, color), c, b, _alarm);
	}

	public EventsType(DatePicker dp, TimePicker tp, int color, String c, String b, boolean _alarm) {
		this(new DateWithMark(dp.getYear(), dp.getMonth() + 1, dp.getDayOfMonth(), tp.getHour(), tp.getMinute(), 0, 0, color),
				c,
				b,
				_alarm);
	}

	public EventsType(String strDate, String strTime, int color, String c, String b, boolean _alarm) {
		this(new DateWithMark(strDate, strTime, color),
				c,
				b,
				_alarm);
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
		alarm = cursor.getString(cursor.getColumnIndexOrThrow(columnAlarm)).equals("true");
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

	public Date getDayBody() {
		return date;
	}

	public String getCategory() {
		return category;
	}

	public String getBody() {
		return body;
	}

	public int getHour() {
		return date.getHour();
	}

	public int getMinute() {
		return date.getMinute();
	}

	@ColorRes
	public int getColor() {
		return date.getColor();
	}

	public boolean equals(@Nullable CalendarBean b) {
		return b != null && date.equals(b);
	}

	public boolean equals(@Nullable Date d) {
		return date.equals(d);
	}

	public ContentValues getContentValues() {
		Log.d(TAG, "getContentValues: " + getEventDetail());
		ContentValues cv = new ContentValues();
		cv.put(columnYear, date.getYear());
		cv.put(columnMonth, date.getMonth());
		cv.put(columnDay, date.getDay());
		cv.put(columnHour, date.getHour());
		cv.put(columnMinute, date.getMinute());
		cv.put(columnCategory, category);
		cv.put(columnBody, body);
		cv.put(columnColor, date.getColor());
		cv.put(columnAlarm, alarm ? "true": "false");
		return cv;
	}

	@SuppressLint("DefaultLocale")
	public String getEventDetail() {
		return String.format("Event: %s; Date: %04d/%02d/%02d Time: %02d:%02d; Category: %s; color: %d",
				body, date.getYear(), date.getMonth(), getDay(), getHour(), getMinute(), category, date.getColor());
	}

	public String getDayOfWeek() {
		return Date.getShortDayOfWeekString(date.getDayOfWeek());
	}
}
