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

import android.content.Context;
import android.database.Cursor;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.ColorRes;

import com.codbking.calendar.CalendarBean;

import java.util.ArrayList;
import java.util.HashMap;

import moe.fluffy.app.R;

public class ColorCache {

	private static final String TAG = "log_ColorCache";

	private HashMap<MonthType, ArrayMap<Integer, Integer>> cacheMap;

	private static String columnDay, columnColor;

	public static void initColumnName(Context context) {
		columnDay = context.getString(R.string.dbEventsDay);
		columnColor = context.getString(R.string.dbEventColor);
	}

	public void push(int year, int month, ArrayMap<Integer, Integer> items) {
		cacheMap.put(new MonthType(year, month), items);
	}

	/**
	 * Insert cache
	 *
	 * @param year cached year
	 * @param month cached month
	 * @param cursor checked cursor (must not none)
	 */
	public void push(int year, int month, Cursor cursor) {
		ArrayMap<Integer, Integer> current_month_cache = new ArrayMap<>();
		//Log.d(TAG, "safe_push: cursor size => " + cursor.getCount());
		cursor.moveToFirst();
		do {
			current_month_cache.put(
					cursor.getInt(cursor.getColumnIndexOrThrow(columnDay)),
					cursor.getInt(cursor.getColumnIndexOrThrow(columnColor))
			);
		} while (cursor.moveToNext());
		cacheMap.put(new MonthType(year, month), current_month_cache);
	}

	public ColorCache() {
		cacheMap = new HashMap<>();
	}

	public boolean checkMonthInCache(int year, int month) {
		return cacheMap.get(new MonthType(year, month)) != null;
	}

	public Integer getColor(int year, int month, int day) {
		Integer color_id = android.R.color.transparent;
		ArrayMap<Integer, Integer> _current = cacheMap.get(new MonthType(year, month));
		if (_current != null) {
			color_id = _current.get(day);
		}
		return color_id == null ? android.R.color.transparent : color_id;
	}
}
