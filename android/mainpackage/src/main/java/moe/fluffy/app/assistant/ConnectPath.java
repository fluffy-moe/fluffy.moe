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

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import moe.fluffy.app.R;

public class ConnectPath {
	private static final String TAG = "log_ConnectPath";
	public static String server_address = "";
	public static String login_path = "";
	//public static String token_path = "";
	public static String register_path = "";
	public static String logout_path = "";
	public static String verify_session_path = "";
	public static String firebase_id_register_path = "";
	//public static String fetch_notification_path = "";
	//public static String fetch_medical_information = "";
	public static String info_path = "";
	private static boolean isInitialized = false;

	public static void loadConfig(Context context) {
		if (!isInitialized) {
			_loadConfig(context);
		}
	}
	private static void _loadConfig(Context context) {
		try {
			JSONObject[] jsonObjects = JSONParser.getJson(
					JSONParser.szLoadJSONFromAsset(context.getResources().openRawResource(R.raw.config))
			);
			server_address = jsonObjects[0].getString(context.getString(R.string.server_address_field));
			isInitialized = true;
		} catch (IOException | JSONException e) {
			PopupDialog.build(context, e);
		}
	}

	public static void pastePath(Context context, JSONArray options) {
		try {
			JSONObject jsonObject = options.getJSONObject(0);
			login_path = jsonObject.getString(context.getString(R.string.login_field));
			info_path = jsonObject.getString(context.getString(R.string.fetch_info_field));
			//token_path = jsonObjects[1].getString(context.getString(R.string.token_field));
			register_path = jsonObject.getString(context.getString(R.string.register_field));
			verify_session_path = jsonObject.getString(context.getString(R.string.verify_session_field));
			firebase_id_register_path = jsonObject.getString(context.getString(R.string.firebase_id_register_field));
			logout_path = jsonObject.getString(context.getString(R.string.logout_field));
			//fetch_notification_path = jsonObjects[1].getString(context.getString(R.string.fetch_notification_field));
			//fetch_medical_information = jsonObjects[1].getString(context.getString(R.string.fetch_medical_information));
		} catch (JSONException e) {
			Log.v(TAG, "pastePath: String raw =>" + options);
			PopupDialog.build(context, e);
		}
	}
}
