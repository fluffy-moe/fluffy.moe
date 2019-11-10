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
package moe.fluffy.app.assistant;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import moe.fluffy.app.types.HttpRawResponse;
import moe.fluffy.app.types.NetworkRequestType;

class ConnectException extends Exception {
	int status;
	ConnectException(int _status) {
		status = _status;
	}

	String getInfo() {
		return "Server returned: " + status;
	}
}

/*
	used in perform api request
 */
class NetworkRequestException extends ConnectException {
	int error_code;
	String error_message;
	NetworkRequestException(int _status, int _error_code, String _error_message) {
		super(_status);
		error_code = _error_code;
		error_message = _error_message;
	}

	NetworkRequestException(int _status, JSONObject error_class) {
		super(_status);
		if (error_class != null) {
			try {
				error_code = error_class.getInt("code");
				error_message = error_class.getString("info");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else {
			error_code = -1;
			error_message = "Json object return null";
		}
	}

	@Override
	String getInfo() {
		return "Server returned => " + status + " Error code: " + error_code + " Additional info: " + error_message;
	}
}

/*
	used in connect server
 */
class ServerException extends ConnectException {
	ServerException(int _status) {
		super(_status);
	}
}

public class Connect extends AsyncTask<URL, Integer, Long> {
	private static final String TAG = "log_Connect";

	private String response = "";
	private String method, requestPath;
	static private String userAgent;


	private Callback listener;

	private HashMap<String, String> headerParams, postParams;


	Connect(HashMap<String, String> _headerParams,
			HashMap<String, String> _postParams,
			String _requestPath,
			Callback _listener,
			boolean is_post) {
		headerParams = _headerParams;
		postParams = _postParams;
		requestPath = _requestPath;
		listener = _listener;
		method = is_post? "POST" : "GET";
	}

	public Connect(NetworkRequestType nrt,
				   String _requestPath,
				   Callback _listener) {
		headerParams = nrt.getHeaders();
		postParams = nrt.getParams();
		requestPath = _requestPath;
		listener = _listener;
		method = "POST";
	}

	Connect(NetworkRequestType nrt,
			String _requestPath,
			Callback _listener,
			boolean is_post) {
		headerParams = nrt.getHeaders();
		postParams = nrt.getParams();
		requestPath = _requestPath;
		listener = _listener;
		method = is_post? "POST" : "GET";
	}

	private
	void doConnect() throws IOException, ConnectException {
		Log.d(TAG, "doConnect: target => " + requestPath);
		StringBuilder stringBuilder = new StringBuilder();
		URL url = new URL(requestPath);
		HttpsURLConnection client = null;
		try {
			client = (HttpsURLConnection) url.openConnection();
			client.setRequestMethod(method);

			client.setRequestProperty("Accept-Charset", "utf8");
			client.setRequestProperty("Content-Type", "application/json");
			client.setRequestProperty("User-Agent", userAgent);

			if (headerParams != null) {
				Iterator it = headerParams.entrySet().iterator();
				while (it.hasNext()) {
					HashMap.Entry pair = (HashMap.Entry)it.next();
					client.setRequestProperty((String)pair.getKey(), (String)pair.getValue());
					it.remove();
				}
			}

			client.setDoInput(true);

			if (method.equals("POST")) {
				client.setDoOutput(true);
				String strParams = new JSONObject(postParams).toString();
				OutputStream os = client.getOutputStream();
				BufferedWriter bufferedWriter = new BufferedWriter(
						new OutputStreamWriter(os, StandardCharsets.UTF_8)
				);
				bufferedWriter.write(strParams);

				bufferedWriter.flush();
				bufferedWriter.close();
				os.close();
			}

			int responseCode = client.getResponseCode();
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(client.getInputStream())
				);
				while ((line = bufferedReader.readLine()) != null){
					//Log.d(TAG, "postData: line => " + line);
					stringBuilder.append(line);
					//response += line;
				}
				response = stringBuilder.toString();
				//response = response.substring(1, response.length() - 1);
				bufferedReader.close();
			}
			else {
				response = "{}";
				throw new ServerException(responseCode);
			}
		}
		catch (MalformedURLException e){
			e.printStackTrace();
		}
		finally {
			if (client != null)
				client.disconnect();
			//Log.d(TAG, "postData: Finish");
		}
		//Log.d(TAG, "postData: Finish Response => " + response);
		HttpRawResponse o = JSONParser.networkJsonDecode(response);
		if (o!= null) {
			if (o.getStatus() != 200)
				throw new NetworkRequestException(o.getStatus(), o.getErrors());
			}
		else
			throw new NetworkRequestException(200, null);
	}

	public static
	void setUserAgent(String _userAgent) {
		userAgent = _userAgent;
	}

	@Override
	protected Long doInBackground(URL... params) {
		try {
			doConnect();
		} catch (IOException | ConnectException e) {
			e.printStackTrace();
			if (listener != null){
				listener.onFailure(this, e);
				listener.finish(this, e);
			}
		}

		final byte[] result = response.getBytes();
		return (long) result.length;
	}

	protected void onPostExecute(Long _reserved) {
		super.onPostExecute(_reserved);
		if (listener != null) {
			listener.onSuccess(JSONParser.networkJsonDecode(response));
			listener.finish(this, null);
		}
	}
}
