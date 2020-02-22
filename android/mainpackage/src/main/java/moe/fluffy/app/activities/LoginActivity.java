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
package moe.fluffy.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;

import moe.fluffy.app.BuildConfig;
import moe.fluffy.app.R;
import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.Connect;
import moe.fluffy.app.assistant.ConnectPath;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.HttpRawResponse;
import moe.fluffy.app.types.NetworkRequest;
import moe.fluffy.app.types.StaticDefinition;


public class LoginActivity extends AppCompatActivity {

	EditText etUser, etPassword;
	ImageButton imgbtnLogin;

	TextView txtCreateNewAccount;

	private static String TAG = "log_Login";

	private static int REGISTER_USER = 0x1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		init();
	}

	void init() {
		etUser = findViewById(R.id.etLoginEmail);
		etPassword = findViewById(R.id.etLoginPassword);
		imgbtnLogin = findViewById(R.id.imgbtnLogin);
		txtCreateNewAccount = findViewById(R.id.txtCreate);

		etUser.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, LoginActivity.this, etUser, R.string.users_email, false));

		etPassword.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus,LoginActivity.this, etPassword, R.string.users_password, true));

		imgbtnLogin.setOnClickListener( v -> login());

		txtCreateNewAccount.setOnClickListener(v -> {
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivityForResult(intent, REGISTER_USER);
		});

		// TODO: Validation login
	}

	private void putExtras(boolean isLoginSuccess, int errorCode, String errorString) {
		getIntent().putExtra(getString(R.string.Intent_EventSuccess), isLoginSuccess);
		getIntent().putExtra(getString(R.string.Intent_EventErrorCode), errorCode);
		getIntent().putExtra(getString(R.string.Intent_EventErrorMessage), errorString);
	}

	private void login() {
		try {
			if (!BuildConfig.isDemoMode) {
				new Connect(NetworkRequest.generateLoginParams(etUser.getText(), etPassword.getText()),
						ConnectPath.login_path,
						new Callback() {
							@Override
							public void onSuccess(Object o) {
								HttpRawResponse r = (HttpRawResponse) o;
								if (r.getStatus() == 200) {
									Toast.makeText(LoginActivity.this, getString(R.string.fmtLoginStr, getString(R.string.strSuccess)), Toast.LENGTH_SHORT).show();
									putExtras(true, 0, "");
								} else {
									PopupDialog.build(LoginActivity.this, getString(R.string.fmtLoginStr, getString(R.string.strFailure)), r.getErrorString());
									putExtras(false, r.getLastError(), r.getErrorString());
								}
							}

							@Override
							public void onFailure(Object o, Throwable e) {
								PopupDialog.build(LoginActivity.this, e);
								putExtras(false, -1, Utils.exceptionToString(e));
							}

							@Override
							public void onFinish(Object o, Throwable e) {
							}
						}).execute();
			}
			else {
				startActivity(new Intent(this, CalendarActivity.class));
			}
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REGISTER_USER) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		boolean isOk = data.getBooleanExtra(getString(R.string.Intent_EventSuccess), false);
		int errorCode = data.getIntExtra(getString(R.string.Intent_EventErrorCode), StaticDefinition.ERROR_INTENT_ERROR);
		String errorString = data.getStringExtra(getString(R.string.Intent_EventErrorCode));

		if (errorCode != StaticDefinition.ERROR_INTENT_ERROR) {
			if (isOk) {
				// TODO: onFinish logic
			}
		}


		finish();
	}
}
