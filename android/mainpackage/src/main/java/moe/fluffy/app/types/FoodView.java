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

import androidx.annotation.NonNull;

import moe.fluffy.app.R;


public class FoodView {
	private String foodName, foodNote, barcode;
	private Date d;
	private boolean liked;
	private String imageSource;
	private Long id;

	private static String columnFoodName, columnFoodNote, columnYear, columnMonth, columnDay,
			columnImageSource, columnLiked, fmtDate, columnBarcode, columnId = "id";

	public static void initColumn(@NonNull Context context) {
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

	public FoodView(String _barcode, String note) {
		this(Date.getToday(), "", note, _barcode, false, null);
	}

	FoodView(Date date, String _foodName, String _foodFullName, String _barcode, boolean l, String _imageSource) {
		id = null;
		barcode = _barcode;
		d = date;
		foodName = _foodName;
		foodNote = _foodFullName;
		liked = l;
		imageSource = _imageSource;
	}

	public FoodView(Cursor cursor) {
		id = cursor.getLong(cursor.getColumnIndexOrThrow(columnId));
		d = new Date(cursor.getInt(cursor.getColumnIndexOrThrow(columnYear)),
				cursor.getInt(cursor.getColumnIndexOrThrow(columnMonth)),
				cursor.getInt(cursor.getColumnIndexOrThrow(columnDay)));
		foodName = cursor.getString(cursor.getColumnIndexOrThrow(columnFoodName));
		foodNote = cursor.getString(cursor.getColumnIndexOrThrow(columnFoodNote));
		liked = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(columnLiked)));
		barcode = cursor.getString(cursor.getColumnIndexOrThrow(columnBarcode));
		imageSource = cursor.getString(cursor.getColumnIndexOrThrow(columnImageSource));
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
		cv.put(columnImageSource, imageSource);
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

	public boolean isLiked() {
		return liked;
	}

	public boolean toggleLike() {
		liked = !liked;
		return liked;
	}

	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}

	public String getImageSource() {
		return imageSource;
	}

	public void setDate(String y, String m, String d) {
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
