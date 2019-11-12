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
package moe.fluffy.app.assistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.StringRes;

import moe.fluffy.app.R;

/**
 * `option` is key value mapping table
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private Context context;
	private static final int DATABASE_VERSION = 1;
	private final static String DATABASE_NAME = "f1uf4y.db";
	//private final static String TABLE_ACCOUNT = "Account";
	private final static String TABLE_OPTION = "Option";
	private final static String CREATE_OPTION = "CREATE TABLE `option` (`key` TEXT PRIMARY KEY, `value` TEXT)";

	private final static String TAG = "log_Database";

	DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}


	DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_OPTION);
		ContentValues cv = new ContentValues();
		cv.put(getString(R.string.dbOptionUser), "");
		cv.put(getString(R.string.dbOptionSession), "");
		db.insert(TABLE_OPTION, null, cv);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION);
		onCreate(db);
	}

	/**
	 * Simply way to get string from string resource id
	 *
	 * @param id string resource id
	 * @return string from pre-defined xml file
	 */
	private String getString(@StringRes int id) {
		return context.getString(id);
	}

	/**
	 * @param user is logined in main activity
	 */
	void setLoginedUser(String user) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues ct = new ContentValues();
		ct.put(getString(R.string.dbOptionUser), user);
		db.update(TABLE_OPTION, ct, getString(R.string.dbQueryKey), new String[]{getString(R.string.dbOptionUser)});
		db.close();
	}

	/**
	 * @return string that stored user name
	 */
	String getLoginedUser() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor =db.query(TABLE_OPTION,
				new String[]{getString(R.string.dbOptionsValueName)},
				getString(R.string.dbQueryKey),
				new String[]{getString(R.string.dbOptionUser)},
				null, null, null);
		cursor.moveToFirst();
		String username = cursor.getString(
				cursor.getColumnIndexOrThrow(getString(R.string.dbOptionsValueName)));
		cursor.close();
		return username;
	}

	/**
	 * Set session that can verify user status
	 *
	 * @param sessionString session string returned by server
	 */
	void setSessionString(String sessionString) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("value", sessionString);
		sqLiteDatabase.update(TABLE_OPTION, contentValues, getString(R.string.dbQueryKey), new String[]{getString(R.string.dbOptionSession)});
		sqLiteDatabase.close();
	}

	String getSessionString() {
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.query(TABLE_OPTION,
				new String[]{getString(R.string.dbOptionsValueName)},
				getString(R.string.dbQueryKey),
				new String[]{getString(R.string.dbOptionSession)},
				null, null, null);
		cursor.moveToFirst();
		String string = cursor.getString(cursor.getColumnIndexOrThrow(getString(R.string.dbOptionsValueName)));
		cursor.close();
		return string;
	}

}