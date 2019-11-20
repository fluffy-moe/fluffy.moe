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

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarUtil;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import moe.fluffy.app.R;
import moe.fluffy.app.types.ColorCache;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.PetInfo;

public class DatabaseHelper extends SQLiteOpenHelper {
	private Context context;
	private static final int DATABASE_VERSION = 1;
	private final static String DATABASE_NAME = "f1uf4y.db";
	private final static String TABLE_PET = "pet";
	private final static String TABLE_OPTION = "option";
	private final static String TABLE_EVENTS = "events";
	private final static String TABLE_FOOD_HISTORY = "food";
	private final static String CREATE_OPTION = "CREATE TABLE `option` (`key` TEXT PRIMARY KEY, `value` TEXT)";
	private final static String CREATE_PET = "CREATE TABLE `pet` (`name` TEXT PRIMARY KEY, `birthday` TEXT, `breed` TEXT)";
	private final static String CREATE_EVENTS = "CREATE TABLE `events` (" +
			"`year` INTEGER, `month` INTEGER, `day` INTEGER, `hour` INTEGER, `minute` INTEGER, " +
			"`category` TEXT, `body` TEXT, `color` INTEGER, `alarm` TEXT)";
	private final static String CREATE_FOOD_HISTORY = "CREATE TABLE `food` (" +
			"`year` INTEGER, `month` INTEGER, `day` INTEGER, `name` TEXT, `note` TEXT, `liked` TEXT, `SOURCE` TEXT)";

	private final static String TAG = "log_Database";
	private static ColorCache colorCache;

	DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
		ColorCache.initColumnName(context);
	}

	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		if (colorCache == null) {
			ColorCache.initColumnName(context);
			colorCache = new ColorCache();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_OPTION);
		db.execSQL(CREATE_PET);
		db.execSQL(CREATE_EVENTS);
		db.execSQL(CREATE_FOOD_HISTORY);
		ContentValues cv = new ContentValues();
		for (String str: new String[]{getString(R.string.dbOptionUser),
				getString(R.string.dbOptionSession)}) {
			cv.put(getString(R.string.dbOptionsKeyName), str);
			cv.put(getString(R.string.dbOptionsValueName), "");
			db.insert(TABLE_OPTION, null, cv);
		}
		cv = new PetInfo("", "", getString(R.string.dbDefaultBirthday)).getContextValues();
		db.insert(TABLE_PET, null, cv);
	}

	// TODO: backup then drop next time
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PET);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_HISTORY);
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

	private String getString(@StringRes int id, Object... formatArgs) {
		return context.getString(id, formatArgs);
	}

	/**
	 * @param user is logined in main activity
	 */
	public void setLoginedUser(String user) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues ct = new ContentValues();
		ct.put(getString(R.string.dbOptionUser), user);
		db.update(TABLE_OPTION, ct, getString(R.string.dbQueryKey), new String[]{getString(R.string.dbOptionUser)});
		db.close();
	}

	/**
	 * @return string that stored user name
	 */
	public String getLoginedUser() {
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
	public void setSessionString(String sessionString) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(getString(R.string.dbOptionSession), sessionString);
		sqLiteDatabase.update(TABLE_OPTION, contentValues, getString(R.string.dbQueryKey), new String[]{getString(R.string.dbOptionSession)});
		sqLiteDatabase.close();
	}

	public String getSessionString() {
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

	private static String getCursorString(@NotNull Cursor cursor, String column){
		return cursor.getString(cursor.getColumnIndexOrThrow(column));
	}


	/**
	 * Get Pet information from database by name
	 *
	 * @param name pet's name
	 * @return {@link PetInfo} basic type for pet information
	 */
	public PetInfo getPetInfo(String name) {
		PetInfo petInfo = null;
		SQLiteDatabase s = this.getReadableDatabase();
		Cursor cursor = s.rawQuery(getString(R.string.dbRawQueryPetInfo, TABLE_PET), new String[]{name});
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			petInfo = new PetInfo(
					name,
					getCursorString(cursor, getString(R.string.dbOptionsPetBreed)),
					getCursorString(cursor, getString(R.string.dbOptionsPetBirthday))
			);
			cursor.close();
		}
		return petInfo;
	}

	public void updatePetInfo(PetInfo p) {
		SQLiteDatabase s = this.getWritableDatabase();
		ContentValues cv = p.getContextValues();
		String petName = cv.getAsString(getString(R.string.dbOptionsPetName));
		Cursor cursor = s.query(TABLE_PET, new String[]{getString(R.string.dbOptionsPetName)},
				getString(R.string.dbQuery, getString(R.string.dbOptionsPetName)),
				new String[]{petName},
				null,null,null);

		if (cursor.getCount() == 0){
			s.insert(TABLE_PET, null, cv);
		} else {
			s.update(TABLE_PET, cv, getString(R.string.dbQuery, getString(R.string.dbOptionsPetName)), new String[]{petName});
		}
		cursor.close();
		s.close();
	}

	public void insertEvent(EventsType event){
		SQLiteDatabase s = this.getWritableDatabase();
		// TODO: should we limit an event per a day?
		s.insert(TABLE_EVENTS, null, event.getContentValues());
		s.close();
	}

	public ArrayList<EventsType> getCurrentAndFeatureEvent() {
		SQLiteDatabase s = this.getReadableDatabase();
		ArrayList<EventsType> arrayList = new ArrayList<>();
		Date d = new Date(CalendarUtil.getYMD(new java.util.Date()));
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsBeyondYear, new String[]{String.valueOf(d.getYear())}));
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsBeyondYearMonth, new String[]{String.valueOf(d.getYear()), String.valueOf(d.getMonth())}));
		return arrayList;
	}

	public static int getYearBeforeMonth(int year, int month) {
		return month != 1 ? year: year -1;
	}

	public static int getYearAfterMonth(int year, int month) {
		return month != 12 ? year: year + 1;
	}

	public static int getBeforeMonth(int month) {
		return month != 1 ? month - 1 : 12;
	}

	public static int getAfterMonth(int month) {
		return month != 12 ? month + 1 : 1;
	}

	public ArrayList<EventsType> getEventInfo(int year, int month) {
		//colorCache = new ColorCache(year, month);
		SQLiteDatabase s = this.getReadableDatabase();
		ArrayList<EventsType> arrayList = new ArrayList<>();

		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsFromYearMonth,
				new String[]{String.valueOf(year), String.valueOf(month)}));
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsFromYearMonth,
				new String[]{String.valueOf(getYearAfterMonth(year, month)), String.valueOf(getAfterMonth(month))}));
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsFromYearMonth,
				new String[]{String.valueOf(getYearBeforeMonth(year, month)), String.valueOf(getBeforeMonth(month))}));

		return arrayList;
	}

	private ArrayList<EventsType> _getEvent(SQLiteDatabase s, @StringRes int strId, String[] args) {
		ArrayList<EventsType> a = new ArrayList<>();
		Cursor c = s.rawQuery(getString(strId, TABLE_EVENTS), args);
		if (c.getCount() != 0) {
			c.moveToFirst();
			do {
				a.add(new EventsType(c));
			} while (c.moveToNext());
		}
		c.close();
		return a;
	}

	@SuppressLint("DefaultLocale")
	public int getTodayColorID(int year, int month, int day) {
		int color;
		if (!colorCache.checkMonthInCache(year, month)) {
			SQLiteDatabase s = this.getReadableDatabase();
			Cursor c = s.rawQuery(getString(R.string.dbRawQueryEventsFromYearMonth, TABLE_EVENTS),
					new String[]{String.valueOf(year), String.valueOf(month)});
			if (c.getCount() != 0) {
				colorCache.push(year, month, c);
			}
			c.close();
			//Log.d(TAG, "getTodayColorID: month => " +  month + " day => "+ day + " Color => " + color);
		}
		color = colorCache.getColor(year, month, day);
		if (color != android.R.color.transparent)
			return context.getResources().getIdentifier(getString(R.string.fmt_event_color, color), "color", context.getPackageName());
		return color;
	}
}