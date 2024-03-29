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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.previewlibrary.enitity.ThumbViewInfo;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.DatabaseHelper;

public class AlbumFiles {
	private static String columnPath, columnBucketName, columnMimeType, columnAddDate,
			columnLatitude, columnLongitude, columnSize, columnDuration, columnThumbPath,
			columnMediaType, columnChecked, columnDisable, columnCategory;

	private static final String TAG = "log_AlbumFiles";

	public Integer getCategory() {
		return category;
	}

	public int getCount() {
		return dbFriendlyAlbumFile.size();
	}

	public static class dbFriendlyAlbumFile extends AlbumFile {

		private Integer category;

		public dbFriendlyAlbumFile(AlbumFile a, int _category) {
			setPath(a.getPath());
			setBucketName(a.getBucketName());
			setMimeType(a.getMimeType());
			setAddDate(a.getAddDate());
			setLatitude(a.getLatitude());
			setLongitude(a.getLongitude());
			setSize(a.getSize());
			setDuration(a.getDuration());
			setThumbPath(a.getThumbPath());
			setMediaType(a.getMediaType());
			setChecked(a.isChecked());
			setDisable(a.isDisable());
			category = _category;
		}

		public AlbumFile getAlbumFile() {
			AlbumFile albumFile = new AlbumFile();
			albumFile.setPath(getPath());
			albumFile.setBucketName(getBucketName());
			albumFile.setMimeType(getMimeType());
			albumFile.setAddDate(getAddDate());
			albumFile.setLatitude(getLatitude());
			albumFile.setLongitude(getLongitude());
			albumFile.setSize(getSize());
			albumFile.setDuration(getDuration());
			albumFile.setThumbPath(getThumbPath());
			albumFile.setMediaType(getMediaType());
			albumFile.setChecked(isChecked());
			albumFile.setDisable(isDisable());
			return albumFile;
		}

		public dbFriendlyAlbumFile(Cursor cursor) {
			//id = cursor.getInt(cursor.getColumnIndexOrThrow(AlbumFiles.columnId));
			setPath(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnPath)));
			setBucketName(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnBucketName)));
			setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnMimeType)));
			setAddDate(cursor.getLong(cursor.getColumnIndexOrThrow(AlbumFiles.columnAddDate)));
			setLatitude(cursor.getFloat(cursor.getColumnIndexOrThrow(AlbumFiles.columnLatitude)));
			setLongitude(cursor.getFloat(cursor.getColumnIndexOrThrow(AlbumFiles.columnLongitude)));
			setSize(cursor.getLong(cursor.getColumnIndexOrThrow(AlbumFiles.columnSize)));
			setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(AlbumFiles.columnDuration)));
			setThumbPath(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnThumbPath)));
			setMediaType(cursor.getInt(cursor.getColumnIndexOrThrow(AlbumFiles.columnMediaType)));
			setChecked(Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnChecked))));
			setDisable(Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnDisable))));
			category = cursor.getInt(cursor.getColumnIndexOrThrow(AlbumFiles.columnCategory));
		}

		public ContentValues getContentValues() {
			ContentValues cv = new ContentValues();
			/*if (id != null)
				cv.put(AlbumFiles.columnId, id);*/
			cv.put(AlbumFiles.columnPath, getPath());
			cv.put(AlbumFiles.columnBucketName, getBucketName());
			cv.put(AlbumFiles.columnMimeType, getMimeType());
			cv.put(AlbumFiles.columnAddDate, getAddDate());
			cv.put(AlbumFiles.columnLatitude, getLatitude());
			cv.put(AlbumFiles.columnLongitude, getLongitude());
			cv.put(AlbumFiles.columnSize, getSize());
			cv.put(AlbumFiles.columnDuration, getDuration());
			cv.put(AlbumFiles.columnThumbPath, getThumbPath());
			cv.put(AlbumFiles.columnMediaType, getMediaType());
			cv.put(AlbumFiles.columnChecked, isChecked());
			cv.put(AlbumFiles.columnDisable, isDisable());
			cv.put(AlbumFiles.columnCategory, category);
			return cv;
		}

	}

	public static void initColumn(@NonNull Context context) {
		//columnId = context.getString(R.string.dbAlbumId);
		columnPath = context.getString(R.string.dbAlbumPath);
		columnBucketName = context.getString(R.string.dbAlbumBucketName);
		columnMimeType = context.getString(R.string.dbAlbumMimeType);
		columnAddDate = context.getString(R.string.dbAlbumAddDate);
		columnLatitude = context.getString(R.string.dbAlbumLatitude);
		columnLongitude = context.getString(R.string.dbAlbumLongitude);
		columnSize = context.getString(R.string.dbAlbumSize);
		columnDuration = context.getString(R.string.dbAlbumDuration);
		columnThumbPath = context.getString(R.string.dbAlbumThumbPath);
		columnMediaType = context.getString(R.string.dbAlbumMediaType);
		columnChecked = context.getString(R.string.dbAlbumChecked);
		columnDisable = context.getString(R.string.dbAlbumDisable);
		columnCategory = context.getString(R.string.dbAlbumCategory);
	}

	private ArrayList<dbFriendlyAlbumFile> dbFriendlyAlbumFile;

	private boolean isNeedUpdateThumbViewInfo;

	private ArrayList<ThumbViewInfo> thumbViewInfos;

	private Integer category;

	public ArrayList<dbFriendlyAlbumFile> setAlbumList(
			ArrayList<dbFriendlyAlbumFile> files) {
		dbFriendlyAlbumFile = files;
		return dbFriendlyAlbumFile;
	}

	public AlbumFiles() {
		dbFriendlyAlbumFile = new ArrayList<>();
	}

	public AlbumFiles(ArrayList<AlbumFile> albumFiles) {
		dbFriendlyAlbumFile = new ArrayList<>();
		update(albumFiles);
	}

	public ArrayList<ThumbViewInfo> getThumbViewInfo(boolean forceUpdate) {
		if (forceUpdate)
			isNeedUpdateThumbViewInfo = forceUpdate;
		return getThumbViewInfo();
	}

	public ArrayList<ThumbViewInfo> getThumbViewInfo() {
		if (isNeedUpdateThumbViewInfo) {
			if (thumbViewInfos == null)
				thumbViewInfos = new ArrayList<>();
			thumbViewInfos.clear();
			dbFriendlyAlbumFile.forEach(file -> {
				thumbViewInfos.add(new ThumbViewInfo(file.getPath()));
			});
			isNeedUpdateThumbViewInfo = false;
		}
		return thumbViewInfos;
	}

	public AlbumFiles update(ArrayList<AlbumFile> albumFiles) {
		dbFriendlyAlbumFile.clear();
		for (AlbumFile albumFile : albumFiles) {
			dbFriendlyAlbumFile.add(new dbFriendlyAlbumFile(albumFile, category));
		}
		isNeedUpdateThumbViewInfo = true;
		return this;
	}

	public AlbumFiles setCategory(@Nullable Integer category) {
		this.category = category;
		return this;
	}

	public AlbumFiles queryFromDatabase(@NonNull DatabaseHelper dbHelper) {
		Log.v(TAG, "Request from database, category => " + category);
		dbFriendlyAlbumFile =  dbHelper.getPhotos(category);
		return this;
	}

	public int size() {
		return dbFriendlyAlbumFile.size();
	}

	public ArrayList<dbFriendlyAlbumFile> getList() {
		return dbFriendlyAlbumFile;
	}

	public AlbumFile get(int position) {
		return dbFriendlyAlbumFile.get(position).getAlbumFile();
	}

	public ArrayList<AlbumFile> getAlbumList() {
		ArrayList<AlbumFile> list = new ArrayList<>();
		dbFriendlyAlbumFile.forEach(file -> list.add(file.getAlbumFile()));
		return list;
	}
}
