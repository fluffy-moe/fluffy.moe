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

import moe.fluffy.app.R;


public class FoodViewType {
	private String foodName, foodNote, barcode;
	private Date d;
	private boolean liked;
	private String imageSource;
	private Long id;

	private static String columnFoodName, columnFoodNote, columnYear, columnMonth, columnDay,
			columnImageSource, columnLiked, fmtDate, columnBarcode, columnId = "id";

	public static void initColumn(Context context) {
		columnFoodName = context.getString(R.string.dbFoodHistoryName);
		columnFoodNote = context.getString(R.string.dbFoodHistoryNote);
		columnYear = context.getString(R.string.dbEventsYear);
		columnMonth = context.getString(R.string.dbEventsMonth);
		columnDay = context.getString(R.string.dbEventsDay);
		columnLiked = context.getString(R.string.dbFoodHistoryLiked);
		columnImageSource = context.getString(R.string.dbFoodHistoryImage);
		fmtDate = context.getString(R.string.fmt_date);
		columnBarcode = context.getString(R.string.dbFoodHistoryBarcode);
	}

	public FoodViewType(String _barcode, String note) {
		barcode = _barcode;
		foodNote = note;
		foodName = "";
		liked = false;
		d = Date.getToday();
	}

	FoodViewType(String date, String _foodName, String _foodFullName, boolean l) {
		id = null;
		d = new Date(date);
		foodName = _foodName;
		foodNote = _foodFullName;
		liked = l;
	}

	public FoodViewType(Cursor cursor) {
		id = cursor.getLong(cursor.getColumnIndexOrThrow(columnId));
		d = new Date(cursor.getInt(cursor.getColumnIndexOrThrow(columnYear)),
				cursor.getInt(cursor.getColumnIndexOrThrow(columnMonth)),
				cursor.getInt(cursor.getColumnIndexOrThrow(columnDay)));
		foodName = cursor.getString(cursor.getColumnIndexOrThrow(columnFoodName));
		foodNote = cursor.getString(cursor.getColumnIndexOrThrow(columnFoodNote));
		liked = cursor.getString(cursor.getColumnIndexOrThrow(columnLiked)).equals(String.valueOf(true));
		barcode = cursor.getString(cursor.getColumnIndexOrThrow(columnBarcode));
	}

	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(columnFoodName, foodName);
		cv.put(columnFoodNote, foodNote);
		cv.put(columnLiked, String.valueOf(liked));
		cv.put(columnDay, d.getDay());
		cv.put(columnYear, d.getYear());
		cv.put(columnMonth, d.getMonth());
		if (!needInsert())
			cv.put(columnId, id);
		cv.put(columnBarcode, barcode);
		return cv;
	}

	public boolean needInsert() {
		return id == null;
	}

	public String getFoodName() {
		return foodName;
	}

	public String getFoodNote() {
		return foodNote;
	}

	public String getDate() {
		return String.format(fmtDate, d.getYear(), d.getMonth(), d.getDay());
	}

	public void setFoodNote(String foodNote) {
		this.foodNote = foodNote;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public void setDate(final String y, final String m, final String d) {
		this.d = new Date(y, m, d);
	}

	public void setDate(String s[]) {
		setDate(s[0], s[1], s[2]);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
