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

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import moe.fluffy.app.R;

public class DeinsectizaionItem {
	private Date date;
	private String deinsectizaion;
	private int status;

	private static String columnNobivac, columnDate, columnStatus;

	public static void initColumn(@NonNull Context context) {
		columnDate = context.getString(R.string.jsonFieldDate);
		columnNobivac = context.getString(R.string.jsonFieldNobivac);
		columnStatus = context.getString(R.string.jsonFieldStatus);
	}

	DeinsectizaionItem(String _date, String _deinsectizaion, int _status) {
		date = new Date(_date);
		deinsectizaion = _deinsectizaion;
		status = _status;
	}

	public DeinsectizaionItem(JSONObject j) throws JSONException {
		date = new Date(j.getJSONObject(columnDate));
		deinsectizaion = j.getString(columnNobivac);
		status = j.getInt(columnStatus);
	}

	public String getDeinsectizaion() {
		return deinsectizaion;
	}

	public int getYear() {
		return date.getYear();
	}

	public int getMonth() {
		return date.getMonth();
	}

	public int getDay() {
		return date.getDay();
	}

	public int getStatus() {
		return status;
	}
}
