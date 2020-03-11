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
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import cz.ackee.useragent.UserAgent;
import moe.fluffy.app.R;
import moe.fluffy.app.assistant.BloodTestDrawable;
import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.Connect;
import moe.fluffy.app.assistant.ConnectPath;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.assistant.Options;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.UserManagement;
import moe.fluffy.app.types.AlbumCover;
import moe.fluffy.app.types.AlbumFiles;
import moe.fluffy.app.types.BloodTestDashboard;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.DeinsectizaionItem;
import moe.fluffy.app.types.EventsItem;
import moe.fluffy.app.types.FoodView;
import moe.fluffy.app.types.HttpRawResponse;
import moe.fluffy.app.types.NetworkRequest;
import moe.fluffy.app.types.PetInfo;
import moe.fluffy.app.types.VaccinationItem;

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

	void initColumns() {
		ConnectPath.loadConfig(this);
		PetInfo.initColumn(this);
		Date.initWeekName(this);
		EventsItem.initColumn(this);
		VaccinationItem.initColumn(this);
		DeinsectizaionItem.initColumn(this);
		FoodView.initColumn(this);
		AlbumFiles.initColumn(this);
		BloodTestDashboard.initColumn(this);
		AlbumCover.initColumn(this);
	}

	/**
	 * Should show startup page to initiate connect page
	 */
	void init() {
		DatabaseHelper.getInstance(this);
		Options.initInstance();
		initColumns();
		createFolder();

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
		/*ImageView img = findViewById(R.id.imgDrawable);
		BloodTestDrawable drawable = new BloodTestDrawable(this, 80, 60, 80);
		img.setImageDrawable(drawable);*/
		initAccount();
		initFirebase();
	}

	void initAccount() {
		Connect.setUserAgent(UserAgent.getInstance(this).getUserAgentString(""));
		new Connect(NetworkRequest.fetchPath(), "update_path", new Callback() {
			@Override
			public void onSuccess(Object o) {
				ConnectPath.pastePath(HomeActivity.this, ((HttpRawResponse) o).getOptions());
			}

			@Override
			public void onFailure(Object o, Throwable e) {
				PopupDialog.build(HomeActivity.this, e);
			}

			@Override
			public void onFinish(Object o, @Nullable Throwable e) {

			}
		}, false).execute();
		UserManagement user = UserManagement.getInstance();
		user.setSession(DatabaseHelper.getInstance().getSessionString());
		user.setUsername(DatabaseHelper.getInstance().getLoginedUser());
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
						UserManagement.getInstance().setFcmToken(token);
						//Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
					catch (NullPointerException e) {
						PopupDialog.build(this, null, "Token is null");
					}
				});
	}

	private void createFolder() {
		String basePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/fluffy/";
		File dataDir = new File(basePath);
		if (!dataDir.exists() && !dataDir.mkdir()) {
			throw new RuntimeException("Cannot create folder");
		}
		dataDir = new File(basePath + "foodHistory");
		if (!dataDir.exists())
			dataDir.mkdir();
		dataDir = new File(basePath + "gallery");
		if (!dataDir.exists())
			dataDir.mkdir();
	}

	@Override
	protected void onDestroy() {
			DatabaseHelper.closeDatabase();
		super.onDestroy();
	}
}
