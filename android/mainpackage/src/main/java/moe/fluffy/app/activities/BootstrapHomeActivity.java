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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import moe.fluffy.app.R;

public class BootstrapHomeActivity extends AppCompatActivity {

	BroadcastReceiver cameraReceiver, medicalReceiver, calendarReceiver, articleReceiver, profileReceiver;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_empty);
		init();
	}

	void init() {
		cameraReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				startActivity(new Intent(BootstrapHomeActivity.this, BootstrapScannerActivity.class));
			}
		};
		medicalReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				startActivity(new Intent(BootstrapHomeActivity.this, MedicalActivity.class));
			}
		};
		calendarReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				startActivity(new Intent(BootstrapHomeActivity.this, CalendarActivity.class));
			}
		};
		articleReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				startActivity(new Intent(BootstrapHomeActivity.this, ArticleActivity.class));
			}
		};
		profileReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				startActivity(new Intent(BootstrapHomeActivity.this, ProfileActivity.class));
			}
		};

		LocalBroadcastManager.getInstance(this).registerReceiver(cameraReceiver,
				new IntentFilter(getString(R.string.IntentFilter_open_camera)));
		LocalBroadcastManager.getInstance(this).registerReceiver(medicalReceiver,
				new IntentFilter(getString(R.string.IntentFilter_open_medical)));
		LocalBroadcastManager.getInstance(this).registerReceiver(calendarReceiver,
				new IntentFilter(getString(R.string.IntentFilter_open_calendar)));
		LocalBroadcastManager.getInstance(this).registerReceiver(articleReceiver,
				new IntentFilter(getString(R.string.IntentFilter_open_article)));
		LocalBroadcastManager.getInstance(this).registerReceiver(profileReceiver,
				new IntentFilter(getString(R.string.IntentFilter_open_profile)));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(cameraReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(medicalReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(calendarReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(articleReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(profileReceiver);

	}
}
