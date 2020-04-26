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

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.PetInfo;

public class PetInfoEx {
	String name;
	Date birthday;
	int weight;

	public PetInfoEx(String name, Date birthday, int weight) {
		this.name = name;
		this.birthday = birthday;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public int getWeight() {
		return weight;
	}

	JSONObject getContentValues() throws JSONException {
		//ContentValues cv = new ContentValues();
		JSONObject object = new JSONObject();
		object.put("name", this.name);
		object.put("birthday_d", this.birthday.getDay());
		object.put("birthday_y", this.birthday.getYear());
		object.put("birthday_m", this.birthday.getMonth());
		object.put("weight", this.weight);
		return object;
	}

	PetInfoEx(JSONObject c) throws JSONException {
		this(c.getString("name"),
				new Date(c.getInt("birthday_y"),
					c.getInt("birthday_m"),
					c.getInt("birthday_d")),
				c.getInt("weight"));

	}
}
