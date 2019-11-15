package moe.fluffy.app.types;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import java.util.Calendar;

import moe.fluffy.app.R;

public class EventsType {
	//private int year, month, day;
	private DateWithMark date;
	private String category;
	private String body;

	private static String columnYear, columnMonth, columnDay, columnCategory, columnBody, columnColor;

	void getColumnName(Context c){
		columnYear = c.getString(R.string.dbEventsYear);
		columnMonth = c.getString(R.string.dbEventsMonth);
		columnDay = c.getString(R.string.dbEventsDay);
		columnCategory = c.getString(R.string.dbEventsCategory);
		columnBody = c.getString(R.string.dbEventsBody);
	}

	EventsType(DateWithMark d, String c, String b) {
		date = d;
		category = c;
		body = b;
	}

	EventsType(String dateStr, int color, String c, String b) {
		date = new DateWithMark(dateStr, color);
		category = c;
		body = b;
	}

	public EventsType(Cursor cursor) {
		int year, month, day;
		year = cursor.getInt(cursor.getColumnIndexOrThrow(columnYear));
		month = cursor.getInt(cursor.getColumnIndexOrThrow(columnMonth));
		day = cursor.getInt(cursor.getColumnIndexOrThrow(columnDay));
		date = new DateWithMark(year, month, day, cursor.getInt(cursor.getColumnIndexOrThrow(columnColor)));
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
		cv.put(columnCategory, category);
		cv.put(columnBody, body);
		cv.put(columnColor, date.getColor());
		return cv;
	}

	public String getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.set(date.getYear(), date.getMonth(), date.getDay());
		int w = c.get(Calendar.DAY_OF_WEEK);
		switch (w) {
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
				throw new IllegalStateException(String.format("%s => %s", ERROR.UNEXPECTED_VALUE, w));
		}
	}
}
