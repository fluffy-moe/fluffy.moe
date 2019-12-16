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

import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import moe.fluffy.app.R;

public class AlbumFiles {
	private static String columnId, columnPath, columnBucketName, columnMimeType, columnAddDate,
			columnLatitude, columnLongitude, columnSize, columnDuration, columnThumbPath,
			columnMediaType, columnChecked, columnDisable;

/*	public static class dbFriendlyAlbumFiles {
		private String mPath, mBucketName, mMimeType;
		private long mAddDate;
		private float mLatitude;
		private float mLongitude;
		private long mSize;
		private long mDuration;
		private String mThumbPath;
		private int mMediaType;
		private boolean isChecked;
		private boolean isDisable;

		dbFriendlyAlbumFiles(Cursor cursor) {
			mPath = cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnPath));
			mBucketName = cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnBucketName));
			mMimeType = cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnMimeType));
			mAddDate = cursor.getLong(cursor.getColumnIndexOrThrow(AlbumFiles.columnAddDate));
			mLatitude = cursor.getFloat(cursor.getColumnIndexOrThrow(AlbumFiles.columnLatitude));
			mLongitude = cursor.getFloat(cursor.getColumnIndexOrThrow(AlbumFiles.columnLongitude));
			mSize = cursor.getLong(cursor.getColumnIndexOrThrow(AlbumFiles.columnSize));
			mDuration = cursor.getLong(cursor.getColumnIndexOrThrow(AlbumFiles.columnDuration));
			mThumbPath = cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnThumbPath));
			mMediaType = cursor.getInt(cursor.getColumnIndexOrThrow(AlbumFiles.columnMediaType));
			isChecked = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnChecked)));
			isDisable = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AlbumFiles.columnDisable)));
		}

		public ContentValues getContentValues() {
			ContentValues cv = new ContentValues();
			cv.put(AlbumFiles.columnPath, mPath);
			cv.put(AlbumFiles.columnBucketName, mBucketName);
			cv.put(AlbumFiles.columnMimeType, mMimeType);
			cv.put(AlbumFiles.columnAddDate, mAddDate);
			cv.put(AlbumFiles.columnLatitude, mLatitude);
			cv.put(AlbumFiles.columnLongitude, mLongitude);
			cv.put(AlbumFiles.columnSize, mSize);
			cv.put(AlbumFiles.columnDuration, mDuration);
			cv.put(AlbumFiles.columnThumbPath, mThumbPath);
			cv.put(AlbumFiles.columnMediaType, mMediaType);
			cv.put(AlbumFiles.columnChecked, isChecked);
			cv.put(AlbumFiles.columnDisable, isDisable);
			return cv;
		}

	}*/

	public static class dbFriendlyAlbumFiles extends AlbumFile {

		private Integer id;

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

	public ArrayList<dbFriendlyAlbumFiles> setAlbumList(
			ArrayList<dbFriendlyAlbumFiles> files) {
		dbFriendlyAlbumFiles = files;
		return dbFriendlyAlbumFiles;
	}

	public ArrayList<dbFriendlyAlbumFiles> getList() {
		return dbFriendlyAlbumFiles;
	}

	public ArrayList<AlbumFile> getAlbumList() {
		ArrayList<AlbumFile> list = new ArrayList<>();
		dbFriendlyAlbumFiles.forEach(file -> {
			AlbumFile albumFile = new AlbumFile();
			albumFile.setPath(file.getPath());
			albumFile.setBucketName(file.getBucketName());
			albumFile.setMimeType(file.getMimeType());
			albumFile.setAddDate(file.getAddDate());
			albumFile.setLatitude(file.getLatitude());
			albumFile.setLongitude(file.getLongitude());
			albumFile.setSize(file.getSize());
			albumFile.setDuration(file.getDuration());
			albumFile.setThumbPath(file.getThumbPath());
			albumFile.setMediaType(file.getMediaType());
			albumFile.setChecked(file.isChecked());
			albumFile.setDisable(file.isDisable());
			list.add(albumFile);
		});
		return list;
	}
}
