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

import moe.fluffy.app.R;

public class ColorCache {
	private int year, month;

	private ArrayMap<Integer, ArrayMap<Integer, Integer>> integerArrayMapArrayMap;

	private static String columnColor;

	public static void initColumnName(Context context) {
		columnColor = context.getString(R.string.dbEventColor);
	}

	public ColorCache(int _year, int _month, Cursor cursor) {
		year = _year;
		month = _month;
		integerArrayMapArrayMap = new ArrayMap<>();
		ArrayMap<Integer, Integer> arrayMap = new ArrayMap<>();
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				cursor.getInt(cursor.getColumnIndexOrThrow(columnColor));
			} while (cursor.moveToNext());
		}
	}
}
