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

import androidx.annotation.NonNull;

import moe.fluffy.app.R;

public class PetInfo {
	private static String columnName, columnBreed, columnBirthday, columnType,
			columnGendar, columnSpayed, columnWeight;

	// TODO: Gender, Spayed or Neutered, Weight column
	private String name, breed, type;
	private boolean gendarMan, isSpayed;
	private int weight;
	private Date birthday;

	public static void initColumn(@NonNull Context context) {
		columnName = context.getString(R.string.dbOptionsPetName);
		columnBreed = context.getString(R.string.dbOptionsPetBreed);
		columnBirthday = context.getString(R.string.dbOptionsPetBirthday);
		columnType = context.getString(R.string.dbOptionPetType);
		columnWeight = context.getString(R.string.dbOptionsPetWeight);
		columnGendar = context.getString(R.string.dbOptionsPetGenderM);
		columnSpayed = context.getString(R.string.dbOptionsPetSpayed);
	}

	public PetInfo(Editable _name, Editable _breed, Editable _birthday, String _type, boolean g, boolean s, int w) {
		this(_name.toString(), _breed.toString(), _birthday.toString(), _type, g, s, w);
	}

	public PetInfo(String _name, String _breed, Date _birthday, String _type, boolean g, boolean spayed, int w) {
		name = _name;
		breed = _breed;
		birthday = _birthday;
		type = _type;
		gendarMan = g;
		isSpayed = spayed;
		weight = w;
	}

	public PetInfo(String _name, String _breed, String _birthday, String _type, boolean g, boolean s, int w) {
		this(_name, _breed, new Date(_birthday), _type, g, s, w);
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

	public String getPetType() {
		return type;
	}

	public boolean getGendar() {
		return gendarMan;
	}

	public boolean getSpayed() {
		return isSpayed;
	}

	public int getWeight() {
		return weight;
	}


	public ContentValues getContextValues() {
		ContentValues cv = new ContentValues();
		cv.put(columnName, name);
		cv.put(columnBreed, breed);
		cv.put(columnBirthday, birthday.toString());
		cv.put(columnType, type);
		cv.put(columnGendar, String.valueOf(gendarMan));
		cv.put(columnSpayed, String.valueOf(isSpayed));
		cv.put(columnWeight, weight);
		return cv;
	}
}
