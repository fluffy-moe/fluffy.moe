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
	private static boolean isInitialized = false;

	public static void loadConfig(Context context) {
		if (!isInitialized) {
			_loadConfig(context);
		}
	}
	private static void _loadConfig(Context context) {
		try {
			JSONObject[] jsonObjects = JSONParser.getJson(
					JSONParser.szloadJSONFromAsset(context.getResources().openRawResource(R.raw.config))
			);
			server_address = jsonObjects[0].get(context.getString(R.string.server_address_field)).toString();
			login_path = jsonObjects[1].get(context.getString(R.string.login_field)).toString();
			token_path = jsonObjects[1].get(context.getString(R.string.token_field)).toString();
			register_path = jsonObjects[1].get(context.getString(R.string.register_field)).toString();
			verify_session_path = jsonObjects[1].get(context.getString(R.string.verify_session_field)).toString();
			firebase_id_register_path = jsonObjects[1].get(context.getString(R.string.firebase_id_register_field)).toString();
			logout_path = jsonObjects[1].get(context.getString(R.string.logout_field)).toString();
			fetch_notification_path = jsonObjects[1].get(context.getString(R.string.fetch_notification_field)).toString();
			isInitialized = true;
		} catch (IOException | JSONException e) {
			PopupDialog.build(context, e);
		}

	}
}
