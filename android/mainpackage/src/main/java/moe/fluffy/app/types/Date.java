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
package moe.fluffy.app.types;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import moe.fluffy.app.BuildConfig;
import moe.fluffy.app.R;

@SuppressLint("DefaultLocale")
public class Date {
	public static String Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday,
		miniMonday, miniTuesday, miniWednesday, miniThursday, miniFriday, miniSaturday, miniSunday;

	public static Date today;

	protected int year, month, day;
	private static String TAG = "log_DateType";

	public Date() {
		getToday();
		year = today.getYear();
		month = today.getMonth();
		day = today.getDay();
	}

	public static int getMaxDate(int month) {
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 9:
			case 11:
				return 31;
			case 2:
				return 28;
			default:
				return 30;
		}
	}

	public static int getPreviousMonthMaxDate(int month) {
		int newMonth = month - 1;
		if (newMonth < 1)
			return getMaxDate(12);
		return getMaxDate(newMonth);
	}

	public static Date getToday() {
		if (today == null) {
			today = new Date(CalendarUtil.getYMD(new java.util.Date()));
			Log.v(TAG, "Today is " + today.year + "/" + today.month + "/" + today.day);
		}
		return today;
	}

	public Date(String _year, String _month, String _day) {
		this(Integer.parseInt(_year.trim()),
			Integer.parseInt(_month.trim()),
			Integer.parseInt(_day.trim()));
	}

	public Date(int _year, int _month, int _day) {
		year = _year;
		month = _month;
		day = _day;
	}

	public Date(CalendarBean c) {
		this(c.year, c.moth, c.day);
	}

	public Date(int[] s) {
		this(s[0], s[1], s[2]);
	}

	public Date(JSONObject j) throws JSONException {
		this(j.getInt("year"),
			j.getInt("month"),
			j.getInt("day"));
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

	public static void initWeekName(Context context) {
		Monday = context.getString(R.string.strMonday);
		Tuesday = context.getString(R.string.strTuesday);
		Wednesday = context.getString(R.string.strWednesday);
		Thursday = context.getString(R.string.strThursday);
		Friday = context.getString(R.string.strFriday);
		Saturday = context.getString(R.string.strSaturday);
		Sunday = context.getString(R.string.strSunday);
		miniMonday = context.getString(R.string.strMiniMonday);
		miniTuesday = context.getString(R.string.strMiniTuesday);
		miniWednesday = context.getString(R.string.strMiniWednesday);
		miniThursday = context.getString(R.string.strMiniThursday);
		miniFriday = context.getString(R.string.strMiniFriday);
		miniSaturday = context.getString(R.string.strMiniSaturday);
		miniSunday = context.getString(R.string.strMiniSunday);
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

	public String[] getStringSz() {
		return new String[]{String.valueOf(this.year), String.valueOf(this.month), String.valueOf(this.day)};
	}


	public boolean equals(@Nullable CalendarBean c) {
		if (c == null) return false;
		return (c.year == this.year) && (c.moth == this.month) && (c.day == this.day);
	}

	public boolean equals(@Nullable Date d) {
		return d != null && (d.year == this.year) && (d.month == this.month) && (d.day == this.day);
	}


	public int getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public long toTimestamp() {
		try {
			DateFormat f = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date date = f.parse(String.format("%d/%02d/%02d", year, month, day));
			return date.getTime();
		} catch (ParseException | NullPointerException e){
			Log.e(TAG, "toTimestamp: Error while parse timestamp", e);
			return 0;
		}
	}

	public boolean moreThan(Date d) {
		return toTimestamp() > d.toTimestamp();
	}

	public boolean moreThanOrEqual(Date d) {
		return equals(d) || moreThan(d);
	}

	public boolean smallThan(Date d) {
		return !moreThanOrEqual(d);
	}

	public boolean smallThanOrEqual(Date d) {
		return !moreThan(d);
	}

	public boolean different(Date d) {
		return !equals(d);
	}

	public static String getDayOfWeekString(int dayOfWeek) {
		switch (dayOfWeek) {
			case 1:
				return Date.Monday;
			case 2:
				return Date.Tuesday;
			case 3:
				return Date.Wednesday;
			case 4:
				return Date.Thursday;
			case 5:
				return Date.Friday;
			case 6:
				return Date.Saturday;
			case 7:
				return Date.Sunday;
			default:
				throw new IllegalStateException(String.format("%s => %s", ERROR.UNEXPECTED_VALUE, dayOfWeek));
		}
	}

	public static String getShortDayOfWeekString(int dayOfWeek) {
		switch (dayOfWeek) {
			case 1:
				return Date.miniMonday;
			case 2:
				return Date.miniTuesday;
			case 3:
				return Date.miniWednesday;
			case 4:
				return Date.miniThursday;
			case 5:
				return Date.miniFriday;
			case 6:
				return Date.miniSaturday;
			case 7:
				return Date.miniSunday;
			default:
				throw new IllegalStateException(String.format("%s => %s", ERROR.UNEXPECTED_VALUE, dayOfWeek));
		}
	}

	public Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("%d/%d/%d", year, month, day);
	}
}
