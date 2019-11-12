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
import android.text.Editable;

import moe.fluffy.app.R;

public class PetInfo {
	private static String columnName, columnBreed, columnBirthday;

	// TODO: Gender, Spayed or Neutered, Weight column
	private String name, breed;
	private Date birthday;


	public static void initStrings(Context context) {
		columnName = context.getString(R.string.dbOptionsPetName);
		columnBreed = context.getString(R.string.dbOptionsPetBreed);
		columnBirthday = context.getString(R.string.dbOptionsPetBirthday);
	}

	public PetInfo(Editable _name, Editable _breed, Editable _birthday) {
		name = _name.toString();
		breed = _breed.toString();
		birthday = new Date(_birthday.toString());

	}

	public PetInfo(String _name, String _breed, Date _birthday) {
		name = _name;
		breed = _breed;
		birthday = _birthday;
	}

	public PetInfo(String _name, String _breed, String _birthday) {
		name = _name;
		breed = _breed;
		birthday = new Date(_birthday);
	}

	public String getPetName() {
		return name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getBreed() {
		return breed;
	}

	public ContentValues getContextValues() {
		ContentValues cv = new ContentValues();
		cv.put(columnName, name);
		cv.put(columnBreed, breed);
		cv.put(columnBirthday, birthday.toString());
		return cv;
	}
}
