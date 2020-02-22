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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import moe.fluffy.app.R;

public class AlbumCover {
	private String name;
	private Integer category;
	private Date date;

	private static String columnName, columnCategory, columnDate;

	public static void initColumn(@NonNull Context context) {
		columnCategory = context.getString(R.string.dbAlbumCategory);
		columnDate = context.getString(R.string.dbAlbumDate);
		columnName = context.getString(R.string.dbAlbumName);
	}

	public AlbumCover(String _name) {
		this(_name, null, Date.getToday().toString());

	}

	public AlbumCover(String _name, Integer category, String dateString) {
		name = _name;
		this.category = category;
		date = new Date(dateString);
	}

	public AlbumCover(Cursor cursor) {
		name = cursor.getString(cursor.getColumnIndexOrThrow(columnName));
		date = new Date(cursor.getString(cursor.getColumnIndexOrThrow(columnDate)));
		category = cursor.getInt(cursor.getColumnIndexOrThrow(columnCategory));
	}

	public ContentValues getContentValue() {
		ContentValues contentValues = new ContentValues();
		if (category != null)
			contentValues.put(columnCategory, category);
		contentValues.put(columnName, name);
		contentValues.put(columnDate, date.toString());
		return contentValues;
	}

	public String getName() {
		return name;
	}

	public Integer getCategory() {
		return category;
	}

	public String getDate() {
		return date.toString();
	}

	public AlbumCover setCategory(@NonNull Integer category) {
		this.category = category;
		return this;
	}
}
