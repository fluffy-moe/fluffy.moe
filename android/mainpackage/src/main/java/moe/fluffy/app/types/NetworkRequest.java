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

import android.text.Editable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static moe.fluffy.app.assistant.SHA512Support.getHashedPassword;

/**
 *	NetworkRequest type class for network params
 */
public class NetworkRequest {
	private HashMap<String, String> params, headers;
	NetworkRequest(@Nullable HashMap<String, String> _params,
				   @Nullable HashMap<String, String> _headers){
		params = _params;
		if (params == null) {
			params = new HashMap<>();
		}
		headers = _headers;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public static NetworkRequest generateLogoutParams(String session_string) {
		HashMap<String, String> _headers = new HashMap<>();
		_headers.put("A-auth", session_string);
		return new NetworkRequest(null, _headers);
	}

	public static NetworkRequest generateVerifyParams(String user, String session_string) {
		HashMap<String, String> _headers = new HashMap<>();
		_headers.put("A-user", user);
		_headers.put("A-auth", session_string);
		return new NetworkRequest(null, _headers);
	}

	@NotNull
	public static NetworkRequest generateRegisterParams(String user, String password, String email)
			throws NoSuchAlgorithmException{
		return _generateAccountAction(user, password);
	}

	@NotNull
	public static NetworkRequest generateLoginParams(Editable user, Editable password)
			throws NoSuchAlgorithmException{
		return generateLoginParams(user.toString(), password.toString());
	}

	@NotNull
	public static NetworkRequest generateLoginParams(String user, String password)
			throws NoSuchAlgorithmException{
		return _generateAccountAction(user, password);
	}


	public static NetworkRequest generateFetchMedicalInformation(String sessionStr) {
		// session str should
		return new NetworkRequest(null, null);
	}

	@NotNull
	@Contract("_, _ -> new")
	public static NetworkRequest generateRegisterFirebaseIDParams(String firebaseID, String sessionStr){
		HashMap<String, String> params = new HashMap<>();
		params.put("token", firebaseID);
		HashMap<String, String> _headers = new HashMap<>();
		_headers.put("A-auth", sessionStr);
		return new NetworkRequest(params, _headers);
	}

	public static NetworkRequest generateFetchNotificationParams(String sessionStr) {
		HashMap<String, String> headerParams = new HashMap<>();
		headerParams.put("A-auth", sessionStr);
		return new NetworkRequest(null, headerParams);
	}

	private
	static NetworkRequest _generateAccountAction(String user, String password)
			throws NoSuchAlgorithmException {
		HashMap<String, String> params = new HashMap<>();
		params.put("user", user);
		params.put("password", getHashedPassword(password));
		return new NetworkRequest(params, null);
	}

	private
	static NetworkRequest _generateAccountAction(String user, String password, String email)
			throws NoSuchAlgorithmException {
		HashMap<String, String> params = new HashMap<>();
		params.put("user", user);
		params.put("password", getHashedPassword(password));
		params.put("email", email);
		return new NetworkRequest(params, null);
	}
}
