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

import androidx.annotation.ColorRes;

import com.codbking.calendar.CalendarBean;

import java.util.ArrayList;

import moe.fluffy.app.R;

public class ColorCache {

	private static final String TAG = "log_ColorCache";

	// Calendar require at least 5 month to get color
	private static final int cache_size = 5;

	private int year, month;

	private ArrayMap<Integer, ArrayMap<Integer, Integer>> integerArrayMapArrayMap;

	private static String columnColor;

	public static void initColumnName(Context context) {
		columnColor = context.getString(R.string.dbEventColor);
	}

	public void push(int _month, ArrayMap<Integer, Integer> items) {
		integerArrayMapArrayMap.put(_month, items);
	}

	public ColorCache(int _year, int _month) {
		year = _year;
		month = _month;
		integerArrayMapArrayMap = new ArrayMap<>();
	}

	public void init() {
		if (integerArrayMapArrayMap.size() != cache_size) {
			throw new RuntimeException("Cache size not matched");
		}
	}

	public Integer getColor(CalendarBean bean) {
	Integer color_id = android.R.color.transparent;
	ArrayMap<Integer, Integer> _current = integerArrayMapArrayMap.get(bean.moth);
		if (_current != null) {
		color_id = _current.get(bean.day);
	}
		return color_id == null ? android.R.color.transparent : color_id;
}
}
