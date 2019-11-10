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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import moe.fluffy.app.assistant.PopupDialog;

public class HomeActivity extends AppCompatActivity {

	private static String TAG = "log_HomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
		init();
	}

	void init() {
		findViewById(R.id.btnChangeToSearch).setOnClickListener(
				v -> startActivity(new Intent(HomeActivity.this, SearchActivity.class)));
		findViewById(R.id.btnChangeToCarouseDemo).setOnClickListener(
				v -> startActivity(new Intent(HomeActivity.this, ArticleActivity.class)));
		findViewById(R.id.btnChangeToScan).setOnClickListener(
				v -> startActivity(new Intent(HomeActivity.this, BoostScanActivity.class)));
		findViewById(R.id.btnChangeToLoginPage).setOnClickListener(
				v -> startActivity(new Intent(HomeActivity.this, LoginActivity.class)));
		initFirebase();
	}


	void initFirebase() {
		FirebaseInstanceId.getInstance().getInstanceId()
				.addOnCompleteListener(task -> {
					if (!task.isSuccessful()) {
						Log.w(TAG, "getInstanceId failed", task.getException());
						PopupDialog.build(HomeActivity.this, task.getException());
						return;
					}

					// Get new Instance ID token
					String token;
					try {
						token = task.getResult().getToken();

						// Log and toast
						String msg = String.format("token: %s", token);
						Log.d(TAG, msg);
						//Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
					catch (NullPointerException e) {
						PopupDialog.build(HomeActivity.this, null, "Token is null");
					}
				});
	}
}
