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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import moe.fluffy.app.R;

public class ConnectPath {
	public static String server_address = "";
	public static String login_path = "";
	public static String token_path = "";
	public static String register_path = "";
	public static String logout_path = "";
	public static String verify_session_path = "";
	public static String firebase_id_register_path = "";
	public static String fetch_notification_path = "";
	public static String fetch_medical_information = "";
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
			login_path = jsonObjects[1].getString(context.getString(R.string.login_field));
			token_path = jsonObjects[1].getString(context.getString(R.string.token_field));
			register_path = jsonObjects[1].getString(context.getString(R.string.register_field));
			verify_session_path = jsonObjects[1].getString(context.getString(R.string.verify_session_field));
			firebase_id_register_path = jsonObjects[1].getString(context.getString(R.string.firebase_id_register_field));
			logout_path = jsonObjects[1].getString(context.getString(R.string.logout_field));
			fetch_notification_path = jsonObjects[1].getString(context.getString(R.string.fetch_notification_field));
			fetch_medical_information = jsonObjects[1].getString(context.getString(R.string.fetch_medical_information));
			isInitialized = true;
		} catch (IOException | JSONException e) {
			PopupDialog.build(context, e);
		}

	}
}
