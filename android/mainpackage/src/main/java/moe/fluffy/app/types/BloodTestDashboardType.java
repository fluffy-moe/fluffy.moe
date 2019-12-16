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

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import moe.fluffy.app.R;

public class BloodTestDashboardType {
	private String bloodTestName;
	private Date date;
	private ArrayList<BloodTestSubType> testItems;

	private static String columnTestName, columnDate, columnSubItems, columnResultValue,
			columnExamineName, columnResultValueReferenceDown, columnResultValueReferenceUp,
			columnResultUnit;

	public static void initColumn(Context context) {
		columnTestName = context.getString(R.string.jsonFieldBloodTestName);
		columnDate = context.getString(R.string.jsonFieldDate);
		columnSubItems = context.getString(R.string.jsonFieldBloodTestResult);
		columnExamineName = context.getString(R.string.jsonFieldExamineItem);
		columnResultValue = context.getString(R.string.jsonFieldBloodTestResultValue);
		columnResultValueReferenceDown = context.getString(R.string.jsonFieldBloodTestResultReferenceDown);
		columnResultValueReferenceUp = context.getString(R.string.jsonFieldBloodTestResultReferenceUp);
		columnResultUnit = context.getString(R.string.jsonFieldBloodTestResultUnit);
	}

	BloodTestDashboardType(Date _date, String testName, ArrayList<BloodTestSubType> items) {
		date = _date;
		bloodTestName = testName;
		testItems = items;
	}

	public BloodTestDashboardType(JSONObject j) throws JSONException {
		date = new Date(j.getJSONObject(columnDate));
		bloodTestName = j.getString(columnTestName);
		testItems = new ArrayList<>();
		JSONArray results = j.getJSONArray(columnSubItems);
		for (int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			testItems.add(new BloodTestSubType(result.getString(columnExamineName),
					result.getDouble(columnResultValue),
					result.getDouble(columnResultValueReferenceDown),
					result.getDouble(columnResultValueReferenceUp),
					result.getString(columnResultUnit))
			);
		}
	}

	public String getBloodTestName() {
		return bloodTestName;
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

	public ArrayList<BloodTestSubType> getTestItems() {
		return testItems;
	}
}
