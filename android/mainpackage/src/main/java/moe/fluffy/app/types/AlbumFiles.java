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

import com.previewlibrary.enitity.ThumbViewInfo;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import moe.fluffy.app.R;

public class AlbumFiles {
	private static String columnId, columnPath, columnBucketName, columnMimeType, columnAddDate,
			columnLatitude, columnLongitude, columnSize, columnDuration, columnThumbPath,
			columnMediaType, columnChecked, columnDisable;

	private static final String TAG = "log_AlbumFiles";

	public static class dbFriendlyAlbumFiles extends AlbumFile {

		private Integer id;

		public dbFriendlyAlbumFiles(AlbumFile a) {
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

		public dbFriendlyAlbumFiles(Cursor cursor) {
			id = cursor.getInt(cursor.getColumnIndexOrThrow(AlbumFiles.columnId));
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
		}

		public ContentValues getContentValues() {
			ContentValues cv = new ContentValues();
			if (id != null)
				cv.put(AlbumFiles.columnId, id);
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
			return cv;
		}

	}

	public static void initColumnName(Context context) {
		columnId = context.getString(R.string.dbAlbumId);
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
	}

	private ArrayList<dbFriendlyAlbumFiles> dbFriendlyAlbumFiles;

	private boolean isNeedUpdateThumbViewInfo;

	private ArrayList<ThumbViewInfo> thumbViewInfos;


	public ArrayList<dbFriendlyAlbumFiles> setAlbumList(
			ArrayList<dbFriendlyAlbumFiles> files) {
		dbFriendlyAlbumFiles = files;
		return dbFriendlyAlbumFiles;
	}

	public AlbumFiles() {
		dbFriendlyAlbumFiles = new ArrayList<>();
	}

	public AlbumFiles(ArrayList<AlbumFile> albumFiles) {
		dbFriendlyAlbumFiles = new ArrayList<>();
		update(albumFiles);
	}

	public ArrayList<ThumbViewInfo> getThumbViewInfo() {
		if (isNeedUpdateThumbViewInfo) {
			if (thumbViewInfos == null)
				thumbViewInfos = new ArrayList<>();
			thumbViewInfos.clear();
			dbFriendlyAlbumFiles.forEach(file -> {
				thumbViewInfos.add(new ThumbViewInfo(file.getPath()));
			});
			isNeedUpdateThumbViewInfo = false;
		}
		return thumbViewInfos;
	}

	public AlbumFiles update(ArrayList<AlbumFile> albumFiles) {
		dbFriendlyAlbumFiles.clear();
		for (AlbumFile albumFile : albumFiles) {
			dbFriendlyAlbumFiles.add(new dbFriendlyAlbumFiles(albumFile));
		}
		isNeedUpdateThumbViewInfo = true;
		return this;
	}

	public int size() {
		return dbFriendlyAlbumFiles.size();
	}

	public ArrayList<dbFriendlyAlbumFiles> getList() {
		return dbFriendlyAlbumFiles;
	}

	public AlbumFile get(int position) {
		return dbFriendlyAlbumFiles.get(position).getAlbumFile();
	}

	public ArrayList<AlbumFile> getAlbumList() {
		ArrayList<AlbumFile> list = new ArrayList<>();
		dbFriendlyAlbumFiles.forEach(file -> list.add(file.getAlbumFile()));
		return list;
	}
}
