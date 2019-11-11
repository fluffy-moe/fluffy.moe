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
package moe.fluffy.app;

import android.os.Bundle;
import android.text.InputType;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;

import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.Connect;
import moe.fluffy.app.assistant.ConnectPath;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.HttpRawResponse;
import moe.fluffy.app.types.NetworkRequestType;


public class LoginActivity extends AppCompatActivity {

	EditText etUser, etPassword;
	ImageButton imgbtnLogin;

	private static String TAG = "log_Login";


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

		etUser.setOnFocusChangeListener((v, hasFocus) -> {
			if (hasFocus) {
				if (etUser.getText().toString().equals(getString(R.string.users_email))) {
					etUser.setText("");
				}
			} else {
				if (etUser.getText().length() == 0) {
					etUser.setText(R.string.users_email);
				}
			}
		});

		etPassword.setOnFocusChangeListener((v, hasFocus) -> {
			if (hasFocus) {
				if (etPassword.getText().toString().equals(getString(R.string.users_password))) {
					// clear default text
					etPassword.setText("");
					// https://stackoverflow.com/a/9893496
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
			else {
				if (etPassword.getText().length() == 0) {
					etPassword.setText(R.string.users_password);
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}
		});

		imgbtnLogin.setOnClickListener( v -> login());

		// TODO: Validation login
	}


	private void putExtras(boolean isLoginSuccess, int errorCode, String errorString) {
		getIntent().putExtra(getString(R.string.Intent_LoginSuccess), isLoginSuccess);
		getIntent().putExtra(getString(R.string.Intent_LoginErrorCode), errorCode);
		getIntent().putExtra(getString(R.string.Intent_LoginErrorMessage), errorString);
	}

	private void login() {
		try {
			new Connect(NetworkRequestType.generateLoginParams(etUser.getText(), etPassword.getText()),
					ConnectPath.login_path,
					new Callback() {
						@Override
						public void onSuccess(Object o)  {
							HttpRawResponse r = (HttpRawResponse) o;
							if (r.getStatus() == 200) {
								Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
								putExtras(true, 0, "");
							}
							else {
								PopupDialog.build(LoginActivity.this, "Login fail", r.getErrorString());
								putExtras(false, r.getLastError(), r.getErrorString());
							}
						}

						@Override
						public void onFailure(Object o, Throwable e) {
							PopupDialog.build(LoginActivity.this, e);
							putExtras(false, -1, Utils.exceptionToString(e));
						}

						@Override
						public void finish(Object o, Throwable e) {}
			}).execute();
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
	}
}
