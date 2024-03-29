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
package moe.fluffy.app.assistant;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moe.fluffy.app.BuildConfig;
import moe.fluffy.app.R;
import moe.fluffy.app.types.AlbumCover;
import moe.fluffy.app.types.AlbumFiles;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventsItem;
import moe.fluffy.app.types.FoodView;
import moe.fluffy.app.types.PetInfo;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper dbHelper;
	private static boolean destroyed;
	private Context context;
	private static final int DATABASE_VERSION = 1;
	private final static String DATABASE_NAME = "f1uf4y.db";
	private final static String TABLE_PET = "pet";
	private final static String TABLE_OPTION = "option";
	private final static String TABLE_OPTION_EX = "option_ex";
	private final static String TABLE_EVENTS = "events";
	private final static String TABLE_FOOD_HISTORY = "food";
	private final static String TABLE_PHOTOS = "photos";
	private final static String TABLE_ALBUM = "album";
	private final static String CREATE_OPTION = "CREATE TABLE `option` (`key` TEXT PRIMARY KEY, `value` TEXT)";
	private final static String CREATE_PET = "CREATE TABLE `pet` (`name` TEXT PRIMARY KEY, `birthday` TEXT, `breed` TEXT, `type` TEXT, " +
			"`weight` INTEGER, `gender` TEXT, `spayed` TEXT)";
	private final static String CREATE_EVENTS = "CREATE TABLE `events` (" +
			"`year` INTEGER, `month` INTEGER, `day` INTEGER, `hour` INTEGER, `minute` INTEGER, " +
			"`category` TEXT, `body` TEXT, `color` INTEGER, `alarm` TEXT)";
	private final static String CREATE_FOOD_HISTORY = "CREATE TABLE `food` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
			"`year` INTEGER, `month` INTEGER, `day` INTEGER, `name` TEXT, `note` TEXT, `liked` TEXT, " +
			"`barcode` TEXT, `source` TEXT)";
	private final static String CREATE_PHOTO_TABLE = "CREATE TABLE `photos` (" +
			"`Path` TEXT, `BucketName` TEXT, `MimeType` TEXT, `AddDate` INTEGER, " +
			"`Latitude` REAL, `Longitude` REAL, `Size` INTEGER, `Duration` INTEGER, `ThumbPath` TEXT, " +
			"`MediaType` INTEGER, `Checked` TEXT, `Disable` TEXT, `category` INTEGER);";
	private final static String CREATE_ALBUM_TABLE = "CREATE TABLE `album` (`category` INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"`date` TEXT NOT NULL, `name` TEXT NOT NULL)";
	private final static String CREATE_OPTION_EX = "CREATE TABLE `option_ex` (`key` TEXT PRIMARY KEY, `value` TEXT)";

	private final static String DROP_STATEMENT = "DROP TABLE IF EXISTS ";

	private final static String TAG = "log_Database";

	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null , DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_OPTION);
		db.execSQL(CREATE_PET);
		db.execSQL(CREATE_EVENTS);
		db.execSQL(CREATE_FOOD_HISTORY);
		db.execSQL(CREATE_PHOTO_TABLE);
		db.execSQL(CREATE_ALBUM_TABLE);
		db.execSQL(CREATE_OPTION_EX);
		ContentValues cv = new ContentValues();
		for (String str: new String[]{getString(R.string.dbOptionUser),
				getString(R.string.dbOptionSession)}) {
			cv.put(getString(R.string.dbOptionsKeyName), str);
			cv.put(getString(R.string.dbOptionsValueName), "");
			db.insert(TABLE_OPTION, null, cv);
		}
		cv = new PetInfo("", "", getString(R.string.dbDefaultBirthday), getString(R.string.typeCat), false, false, 1).getContextValues();
		db.insert(TABLE_PET, null, cv);
		db.insert(TABLE_ALBUM, null, new AlbumCover("Sample").getContentValue());
	}

	// TODO: backup then drop next time
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_STATEMENT + TABLE_OPTION);
		db.execSQL(DROP_STATEMENT + TABLE_PET);
		db.execSQL(DROP_STATEMENT + TABLE_EVENTS);
		db.execSQL(DROP_STATEMENT + TABLE_FOOD_HISTORY);
		db.execSQL(DROP_STATEMENT + TABLE_PHOTOS);
		db.execSQL(DROP_STATEMENT + TABLE_ALBUM);
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
					getCursorString(cursor, getString(R.string.dbOptionsPetBirthday)),
					getCursorString(cursor, getString(R.string.dbOptionPetType)),
					Boolean.parseBoolean(getCursorString(cursor, getString(R.string.dbOptionsPetGenderM))),
					Boolean.parseBoolean(getCursorString(cursor, getString(R.string.dbOptionsPetSpayed))),
					cursor.getInt(cursor.getColumnIndexOrThrow(getString(R.string.dbOptionsPetWeight)))
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

	public void insertPhoto(AlbumFiles.dbFriendlyAlbumFile dbFriendlyAlbumFile) {
		SQLiteDatabase s = this.getWritableDatabase();
		s.insert(TABLE_PHOTOS, null, dbFriendlyAlbumFile.getContentValues());
		s.close();
	}

	public void insertPhotos(@NonNull AlbumFiles albumFiles) {
		insertPhotos(albumFiles.getList(), albumFiles.getCategory());
	}

	public void insertPhotos(@NonNull ArrayList<AlbumFiles.dbFriendlyAlbumFile> albumFiles, Integer category) {
		SQLiteDatabase s = this.getWritableDatabase();
		s.execSQL(getString(R.string.dbDeleteWhereSthIs, TABLE_PHOTOS, getString(R.string.dbAlbumCategory)),
				new String[]{String.valueOf(category)});
		albumFiles.forEach(albumFile -> {
			s.insert(TABLE_PHOTOS, null, albumFile.getContentValues());
		});
		s.close();
	}

	public void updatePhotos(@NonNull AlbumFiles albumFiles) {
		Log.v(TAG, "updatePhotos: Update category => " + albumFiles.getCategory());
		ArrayList<ContentValues> cvList = new ArrayList<>();
		albumFiles.getList().forEach(item -> {
			cvList.add(item.getContentValues());
		});
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		if (albumFiles.getCategory() != null) {
			sqLiteDatabase.execSQL(getString(R.string.dbDeleteWhereSthIs, TABLE_PHOTOS, getString(R.string.dbAlbumCategory)),
					new String[]{String.valueOf(albumFiles.getCategory())});
		}
		cvList.forEach(item -> {
			sqLiteDatabase.insert(TABLE_PHOTOS, null, item);
		});
		sqLiteDatabase.close();
	}

	public ArrayList<AlbumFiles.dbFriendlyAlbumFile> getPhotos(@Nullable Integer category) {
		SQLiteDatabase s = this.getReadableDatabase();
		ArrayList<AlbumFiles.dbFriendlyAlbumFile> arrayList = new ArrayList<>();
		Cursor cursor;
		if (category == null)
			cursor = s.rawQuery(getString(R.string.dbRawQuery, TABLE_PHOTOS), null);
		else
			cursor = s.rawQuery(getString(R.string.dbRawQueryBySth, TABLE_PHOTOS, getString(R.string.dbAlbumCategory)),
					new String[]{String.valueOf(category)});
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				arrayList.add(new AlbumFiles.dbFriendlyAlbumFile(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return arrayList;
	}

	public AlbumCover createAlbum(String name) {
		AlbumCover albumCover = new AlbumCover(name);
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		sqLiteDatabase.insert(TABLE_ALBUM, null, albumCover.getContentValue());
		Cursor cursor = sqLiteDatabase.rawQuery(getString(R.string.dbLastInsert), null);
		cursor.moveToFirst();
		Integer category = cursor.getInt(cursor.getColumnIndexOrThrow(cursor.getColumnNames()[0]));
		albumCover.setCategory(category);
		cursor.close();
		sqLiteDatabase.close();
		return albumCover;
	}

	public ArrayList<AlbumCover> getAlbums() {
		ArrayList<AlbumCover> albumCovers = new ArrayList<>();
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(getString(R.string.dbRawQuery, TABLE_ALBUM), null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				albumCovers.add(new AlbumCover(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return albumCovers;
	}

	public AlbumFiles.dbFriendlyAlbumFile getOnePhoto(@Nullable Integer category) {
		if (category == null) return null;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(getString(R.string.dbRawQueryBySthLimit1, TABLE_PHOTOS, getString(R.string.dbAlbumCategory)),
				new String[]{String.valueOf(category)});
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			AlbumFiles.dbFriendlyAlbumFile albumFile = new AlbumFiles.dbFriendlyAlbumFile(cursor);
			cursor.close();
			return albumFile;
		}
		return null;
	}

	/**
	 * Call this method to delete album by category
	 * @param category Non Null integer category that specify category what to delete
	 */
	public void deleteAlbum(@NonNull Integer category) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		sqLiteDatabase.execSQL(getString(R.string.dbDeleteWhereSthIs, TABLE_ALBUM, getString(R.string.dbAlbumCategory)), new String[]{String.valueOf(category)});
		sqLiteDatabase.execSQL(getString(R.string.dbDeleteWhereSthIs, TABLE_PHOTOS, getString(R.string.dbAlbumCategory)), new String[]{String.valueOf(category)});
		sqLiteDatabase.close();
	}

	/**
	 * Use this method to change album name
	 * @param category integer
	 * @param editText EditText that can get new name
	 */
	public void editAlbumName(@NonNull Integer category, @NonNull EditText editText) {
		editAlbumName(category, editText.getText().toString());
	}

	public void editAlbumName(@NonNull Integer category, String newName) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		sqLiteDatabase.execSQL(getString(R.string.dbUpdateSomeWhereBySth, TABLE_ALBUM,
				getString(R.string.dbAlbumName), getString(R.string.dbAlbumCategory)),
				new String[]{newName, String.valueOf(category)});
		sqLiteDatabase.close();
	}

	@NonNull
	public AlbumCover getAlbumFromCategoryOrThrow(@NonNull Integer category) {
		AlbumCover r = getAlbumFromCategory(category);
		if (r == null)
			throw new NullPointerException("Throw! category => " + category);
		return r;
	}

	/**
	 * This function will return album cover by query specify category.
	 *
	 * @param category nullable integer
	 * @return null if category is null, otherwise return {@link AlbumCover} if query successful
	 */
	@Nullable
	public AlbumCover getAlbumFromCategory(@Nullable Integer category) {
		if (category == null) return null;
		Log.v(TAG, "getAlbumFromCategory: category => " + category);
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(
				getString(R.string.dbRawQueryBySth, TABLE_ALBUM, getString(R.string.dbAlbumCategory)), new String[]{String.valueOf(category)});
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		AlbumCover albumCover = new AlbumCover(cursor);
		cursor.close();
		return albumCover;
	}

	public Integer getAlbumSize(@NonNull Integer category) {
		SQLiteDatabase s = this.getReadableDatabase();
		Cursor cursor = s.rawQuery(getString(R.string.dbCountQuery, TABLE_PHOTOS, getString(R.string.dbAlbumCategory)),
				new String[]{String.valueOf(category)});
		cursor.moveToFirst();
		Integer count = cursor.getInt(cursor.getColumnIndexOrThrow(cursor.getColumnNames()[0]));
		cursor.close();
		return count;
	}

	public void insertEvent(EventsItem event){
		SQLiteDatabase s = this.getWritableDatabase();
		// TODO: should we limit an event per a day?
		s.insert(TABLE_EVENTS, null, event.getContentValues());
		s.close();
	}

	public void editEvent(EventsItem event) {
		SQLiteDatabase s = this.getWritableDatabase();
		ArrayList<String> sz = new ArrayList<>(Arrays.asList(event.getDateBody().getStringSz()));
		sz.add(event.getPrevious_body());
		sz.add(event.getCategory());
		String[] _s  = sz.toArray(new String[0]);
		s.update(TABLE_EVENTS, event.getContentValues(), "year = ? AND `month` = ? AND `day` = ? AND `body` = ? AND `category` = ?", _s);
		s.close();
	}

	@Nullable
	public ArrayList<EventsItem> getEventByDay(int year, int month, int day) {
		SQLiteDatabase s = this.getReadableDatabase();
		Cursor cursor = s.rawQuery(getString(R.string.dbRawQueryAccurateDay, TABLE_EVENTS), new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});
		ArrayList<EventsItem> items = null;
		if (cursor.getCount() > 0) {
			items = new ArrayList<>();
			cursor.moveToFirst();
			do {
				items.add(new EventsItem(cursor));
			} while (cursor.moveToNext());
		}
		return items;
	}

	@Nullable
	public ArrayList<EventsItem> getEventByDay(Date date) {
		return getEventByDay(date.getYear(), date.getMonth(), date.getDay());
	}

	public ArrayList<EventsItem> getCurrentAndFeatureEvent() {
		SQLiteDatabase s = this.getReadableDatabase();
		ArrayList<EventsItem> arrayList = new ArrayList<>();
		Date d = Date.getToday();
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsBeyondYear, new String[]{String.valueOf(d.getYear())}));
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsBeyondYearMonth,
				new String[]{String.valueOf(d.getYear()), String.valueOf(d.getMonth())}));
		return arrayList;
	}

	private static int getYearBeforeMonth(int year, int month) {
		return month != 1 ? year: year -1;
	}

	private static int getYearAfterMonth(int year, int month) {
		return month != 12 ? year: year + 1;
	}

	private static int getBeforeMonth(int month) {
		return month != 1 ? month - 1 : 12;
	}

	private static int getAfterMonth(int month) {
		return month != 12 ? month + 1 : 1;
	}

	// FIXME: should store all info
	public void insertPetInfoEx(PetInfoEx petInfoEx) {
		SQLiteDatabase s = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		try {
			cv.put("value", petInfoEx.getContentValues().toString());
			cv.put("key", "petinfoex");
			s.execSQL(DROP_STATEMENT + TABLE_OPTION_EX);
			s.execSQL(CREATE_OPTION_EX);
			s.insert(TABLE_OPTION_EX, null, cv);
		} catch (JSONException ignore) {

		}
		s.close();
	}

	@Nullable
	public PetInfoEx getPetInfoEx() {
		SQLiteDatabase s = this.getReadableDatabase();
		Cursor c = s.rawQuery("SELECT * FROM `" + TABLE_OPTION_EX +"` WHERE `key` = ?", new String[]{"petinfoex"});
		PetInfoEx petInfoEx = null;
		if (c.getCount() > 0) {
			c.moveToFirst();
			try {
				petInfoEx = new PetInfoEx(new JSONObject(c.getString(c.getColumnIndex("value"))));
			}
			catch (Exception ignore) {

			}
		}
		c.close();
		return petInfoEx;
	}

	public ArrayList<EventsItem> getEventInfo(int year, int month) {
		//colorCache = new ColorCache(year, month);
		SQLiteDatabase s = this.getReadableDatabase();
		ArrayList<EventsItem> arrayList = new ArrayList<>();

		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsFromYearMonth,
				new String[]{String.valueOf(year), String.valueOf(month)}));
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsFromYearMonth,
				new String[]{String.valueOf(getYearAfterMonth(year, month)), String.valueOf(getAfterMonth(month))}));
		arrayList.addAll(_getEvent(s, R.string.dbRawQueryEventsFromYearMonth,
				new String[]{String.valueOf(getYearBeforeMonth(year, month)), String.valueOf(getBeforeMonth(month))}));

		return arrayList;
	}

	private ArrayList<EventsItem> _getEvent(SQLiteDatabase s, @StringRes int strId, String[] args) {
		ArrayList<EventsItem> a = new ArrayList<>();
		Cursor c = s.rawQuery(getString(strId, TABLE_EVENTS), args);
		if (c.getCount() != 0) {
			c.moveToFirst();
			do {
				a.add(new EventsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		return a;
	}

	@SuppressLint("DefaultLocale")
	public int getTodayColorID(int year, int month, int day) {
		int color = android.R.color.transparent;
		SQLiteDatabase s = this.getReadableDatabase();
		Cursor c = s.rawQuery(getString(R.string.dbRawQueryAccurateDay, TABLE_EVENTS),
				new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});
		if (c.getCount() != 0) {
			c.moveToFirst();
			color = c.getInt(c.getColumnIndexOrThrow(getString(R.string.dbEventColor)));
		}
		c.close();
		//Log.d(TAG, "getTodayColorID: month => " +  month + " day => "+ day + " Color => " + color);
		if (color != android.R.color.transparent)
			return context.getResources().getIdentifier(getString(R.string.fmt_event_color, color), "color", context.getPackageName());
		return color;
	}

	public void writeFoodHistory(FoodView fd) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (fd.needInsert()) {
			long last_insert = db.insert(TABLE_FOOD_HISTORY, null, fd.getContentValues());
			fd.setId(last_insert);
		} else {
			db.update(TABLE_FOOD_HISTORY, fd.getContentValues(), "id = ?",
					new String[]{String.valueOf(fd.getId())});
		}
		db.close();
	}

	public ArrayList<FoodView> getFoodHistory() {
		ArrayList<FoodView> foodList = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(getString(R.string.dbRawQuery, TABLE_FOOD_HISTORY), null);
		Log.d(TAG, "getFoodHistory: count: => " +c.getCount());
		if (c.getCount() != 0){
			c.moveToFirst();
			do {
				foodList.add(new FoodView(c));
			} while (c.moveToNext());
		}
		c.close();
		return foodList;
	}

	@NonNull
	public static DatabaseHelper getInstance() {
		if (destroyed) {
			throw new RuntimeException("Database has been destroyed");
		}
		if (dbHelper == null) {
			throw new NullPointerException("dbHelp not initialized");
		}
		return dbHelper;
	}

	@NonNull
	public static DatabaseHelper createInstance(@NonNull Context context) {
		Log.v(TAG, "createInstance: Created");
		dbHelper = new DatabaseHelper(context);
		return dbHelper;
	}

	@NonNull
	public static DatabaseHelper getInstance(@NonNull Context context) {
		if (destroyed) {
			throw new RuntimeException("Database has been destroyed");
		}
		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(context);
		}
		return dbHelper;
	}

	public static DatabaseHelper closeDatabase() {
		if (dbHelper == null) {
			throw new NullPointerException("Database has been closed");
		}
		dbHelper.close();
		destroyed = true;
		return dbHelper;
	}

	String queryOptions(String key) {
		Cursor cursor = queryOptionsEx(key);
		assert cursor.getCount() > 0;
		Log.v(TAG, "queryOptions: columncount =>" + cursor.getColumnNames()[0]);
		cursor.moveToFirst();
		//if (cursor.getCount() > 0)
		String str = cursor.getString(cursor.getColumnIndexOrThrow(getString(R.string.dbOptionsValueName)));
		cursor.close();
		return str;
	}

	Cursor queryOptionsEx(String key) {
		return this.getReadableDatabase().query(TABLE_OPTION, new String[]{getString(R.string.dbOptionsValueName)},
				getString(R.string.dbQueryKey), new String[]{key}, null, null, null);
	}

	@Deprecated
	Cursor _rawQuery(String sql) {
		return this.getReadableDatabase().rawQuery(sql, null);
	}

	@Deprecated
	Cursor _rawQuery(String sql, String [] args) {
		return this.getReadableDatabase().rawQuery(sql, args);
	}


	private void _updateOptionsTable(String key, String value, boolean isUpdate) {
		SQLiteDatabase s = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(getString(R.string.dbOptionsKeyName), key);
		cv.put(getString(R.string.dbOptionsValueName), value);
		if (isUpdate)
			s.update(TABLE_OPTION, cv, getString(R.string.dbQueryKey), new String[]{key});
		else
			s.insert(TABLE_OPTION, null, cv);
		s.close();
	}

	void _writeOption(String key, String value) {
		this._updateOptionsTable(key, value, true);
	}

	void _addOption(String key, String value) {
		this._updateOptionsTable(key, value, false);
	}

	void __dropOptions() {
		if (BuildConfig.isDemoMode) {
			Log.w(TAG, "initInstance: Drop options");
			SQLiteDatabase s = this.getWritableDatabase();
			s.delete(TABLE_OPTION, null, null);
			s.close();
		}
	}
}