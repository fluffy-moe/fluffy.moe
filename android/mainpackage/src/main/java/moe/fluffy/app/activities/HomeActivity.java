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
package moe.fluffy.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

import cz.ackee.useragent.UserAgent;
import moe.fluffy.app.R;
import moe.fluffy.app.assistant.Connect;
import moe.fluffy.app.assistant.ConnectPath;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.DeinsectizaionType;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.FoodViewType;
import moe.fluffy.app.types.PetInfo;
import moe.fluffy.app.types.VaccinationType;

public class HomeActivity extends AppCompatActivity {
	@SuppressLint("StaticFieldLeak")
	public static DatabaseHelper dbHelper;
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
		Connect.setUserAgent(UserAgent.getInstance(this).getUserAgentString(""));
		ConnectPath.loadConfig(this);
		PetInfo.initStrings(this);
		Date.initWeekName(this);
		EventsType.getColumnName(this);
		VaccinationType.initColumn(this);
		DeinsectizaionType.initColumnName(this);
		FoodViewType.initColumn(this);
		dbHelper = new DatabaseHelper(this);

		findViewById(R.id.btnChangeToCamera).setOnClickListener(
				v -> startActivity(new Intent(this, CameraActivity.class)));
		findViewById(R.id.btnChangeToCarouseDemo).setOnClickListener(
				v -> startActivity(new Intent(this, ArticleActivity.class)));
		findViewById(R.id.btnChangeToScan).setOnClickListener(
				v -> startActivity(new Intent(this, BootstrapScannerActivity.class)));
		findViewById(R.id.btnChangeToLoginPage).setOnClickListener(
				v -> startActivity(new Intent(this, LoginActivity.class)));
		findViewById(R.id.btnChangeToWelcome).setOnClickListener(
				v -> startActivity(new Intent(this, WelcomeActivity.class)));
		findViewById(R.id.btnDymanic).setOnClickListener(
				v -> startActivity(new Intent(this, CalendarActivity.class)));
		findViewById(R.id.btnChangeToMedical).setOnClickListener(
				v -> startActivity(new Intent(this, MedicalActivity.class)));
		findViewById(R.id.btnShowFoodHistory).setOnClickListener(
				v -> startActivity(new Intent(this, FoodHistoryActivity.class)));
		findViewById(R.id.btnChangeToProfile).setOnClickListener(
				v -> startActivity(new Intent(this, ProfileActivity.class)));
		findViewById(R.id.btnChangeToTest).setOnClickListener(
				v -> startActivity(new Intent(this, TestCameraActivity.class)));
		initFirebase();
	}


	void initFirebase() {
		FirebaseInstanceId.getInstance().getInstanceId()
				.addOnCompleteListener(task -> {
					if (!task.isSuccessful()) {
						Log.w(TAG, "getInstanceId failed", task.getException());
						PopupDialog.build(this, Objects.requireNonNull(task.getException()));
						return;
					}

					// Get new Instance ID token
					String token;
					try {
						token = Objects.requireNonNull(task.getResult()).getToken();
						// Log and toast
						String msg = String.format("token: %s", token);
						Log.d(TAG, msg);
						//Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
					catch (NullPointerException e) {
						PopupDialog.build(this, null, "Token is null");
					}
				});
	}

	@Override
	protected void onDestroy() {
		if (dbHelper != null)
			dbHelper.close();
		super.onDestroy();
	}
}
