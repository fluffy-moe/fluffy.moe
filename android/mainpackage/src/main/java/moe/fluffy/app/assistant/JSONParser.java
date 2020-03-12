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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import moe.fluffy.app.types.HttpRawResponse;

public class JSONParser {
	private static String TAG = "log_JSONParser";

	static public String szLoadJSONFromAsset(InputStream is) throws IOException {
		int size = is.available();
		byte[] buffer = new byte[size];
		is.read(buffer);
		is.close();
			return new String(buffer, StandardCharsets.UTF_8);
	}

	static public JSONObject loadJSONFromAsset(InputStream is) {
		JSONObject json;
		try {
			json = new JSONObject(szLoadJSONFromAsset(is));
		} catch (IOException | JSONException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}


	static JSONObject[] getJson(String json_text) throws JSONException{
		JSONObject[] jsonObjects = new JSONObject[2];
		JSONObject obj = new JSONObject(json_text);
		//JSONObject pageObj = obj.getJSONObject("pages");
		jsonObjects[0] = obj;
		//jsonObjects[1] = pageObj;
		return jsonObjects;
	}

	static HttpRawResponse networkJsonDecode(String json_text) {
		//Structure:
		// RAW | STATUS | OPTIONS | ERRORS
		HttpRawResponse httpRawResponse;
		//Log.d(TAG, "networkJsonDecode: json_text => "+ json_text);
		try{
			JSONObject obj = new JSONObject(json_text);
			httpRawResponse = new HttpRawResponse(obj);
			return httpRawResponse;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}

