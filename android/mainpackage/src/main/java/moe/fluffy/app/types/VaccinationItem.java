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

public class VaccinationItem {
	public static final int GREEN = 1, RED = 0, UNDEFINED = -1;

	private Date date;
	private String nobivac, injectionSite, doctor;

	private int status;

	private static String columnNobivac, columnInjectionSite, columnDoctor, columnDate, columnStatus;

	public static void initColumn(@NonNull Context context) {
		columnNobivac = context.getString(R.string.jsonFieldNobivac);
		columnInjectionSite = context.getString(R.string.jsonFieldInjectionSite);
		columnDoctor = context.getString(R.string.jsonFieldDoctor);
		columnDate = context.getString(R.string.jsonFieldDate);
		columnStatus = context.getString(R.string.jsonFieldStatus);
	}

	VaccinationItem(Date d, String a, String b, String c, int e) {
		date = d;
		nobivac = a;
		injectionSite = b;
		doctor = c;
		status = e;
	}

	/**
	 * Json field should be following format:
	 * 		{"date": {"year": 1970, "month": 1, "day": 1}, "nobivac": ...}
	 *
	 * @param j json from server
	 * @throws JSONException Exception while get json value
	 */
	public VaccinationItem(JSONObject j) throws JSONException {
		this(new Date(j.getJSONObject(columnDate)),
				j.getString(columnNobivac),
				j.getString(columnInjectionSite),
				j.getString(columnDoctor),
				j.getInt(columnStatus));
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

	public String getNobivac() {
		return nobivac;
	}

	public String getInjectionSite() {
		return injectionSite;
	}

	public String getDoctor() {
		return doctor;
	}

	public int getStatus() {
		return status;
	}
}
