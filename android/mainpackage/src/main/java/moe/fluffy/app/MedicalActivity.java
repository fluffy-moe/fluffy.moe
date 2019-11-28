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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.Connect;
import moe.fluffy.app.assistant.ConnectPath;
import moe.fluffy.app.assistant.JSONParser;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.types.DeinsectizaionType;
import moe.fluffy.app.types.HttpRawResponse;
import moe.fluffy.app.types.NetworkRequestType;
import moe.fluffy.app.types.VaccinationType;
import moe.fluffy.app.types.adapter.DeinsectizaionAdapter;
import moe.fluffy.app.types.adapter.VaccinationAdapter;

public class MedicalActivity extends AppCompatActivity {

	TextView previousClickText;
	View previousView;

	TextView txtBar1, txtBar2, txtBar3;
	View vBarUnderline1, vBarUnderline2, vBarUnderline3;

	ImageButton imgbtnSearchHospital;

	TextView txtWelcome, txtAge, txtWeight;

	TextView txtNextTimeTxt, txtNextDate;

	ImageView imgPet, imgBackground;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

	ListView lvItems, lvBloodTestItems;


	JSONObject medicalObject;
	ArrayList<VaccinationType> vacArray;
	ArrayList<DeinsectizaionType> deiArray;

	BroadcastReceiver updateDataReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_medical);
		initJson(null);
		init();
	}

	private void initJson(String jsonInput) {
		if (medicalObject == null) {
			vacArray = new ArrayList<>();
			deiArray = new ArrayList<>();
			try {
				if (jsonInput == null)
					medicalObject = JSONParser.loadJSONFromAsset(getResources().openRawResource(R.raw.medical));
				else
					medicalObject = new JSONObject(jsonInput);
				if (medicalObject == null) {
					PopupDialog.build(this, new NullPointerException("Object is null"));
				}
				JSONArray m = medicalObject.getJSONArray(getString(R.string.jsonDeinsectizaionRoot));
				for (int i = 0; i < m.length() ; i++) {
					deiArray.add(new DeinsectizaionType(m.getJSONObject(i)));
				}
				m = medicalObject.getJSONArray(getString(R.string.jsonVaccinationRoot));
				for (int i=0;i<m.length();i++) {
					vacArray.add(new VaccinationType(m.getJSONObject(i)));
				}
			} catch (JSONException e) {
				PopupDialog.build(this, e);
			}
		}
	}

	private void init() {
		previousClickText = txtBar1 = findViewById(R.id.txtMedicalBar);
		txtBar2 = findViewById(R.id.txtMedicalBar2);
		txtBar3 = findViewById(R.id.txtMedicalBar3);
		previousView = vBarUnderline1 = findViewById(R.id.vacUnderline);
		vBarUnderline2 = findViewById(R.id.deinUnderline);
		vBarUnderline3 = findViewById(R.id.bloodUnderline);

		imgPet = findViewById(R.id.imgMediacalPetAvatar);
		txtWelcome = findViewById(R.id.txtMedicalPetName);
		txtAge = findViewById(R.id.txtMedicalPetAge);
		txtWeight = findViewById(R.id.txtMedicalPetWeight);

		imgbtnSearchHospital = findViewById(R.id.imgbtnMedicalClinicSearchIcon);

		imgBackground = findViewById(R.id.imgMedicalNextBackground);
		txtNextTimeTxt = findViewById(R.id.txtMedicalNext);
		txtNextDate = findViewById(R.id.txtMedicalNextTime);

		lvItems = findViewById(R.id.lvMedicalNextTimeRecord);
		lvBloodTestItems = findViewById(R.id.lvBloodTestRecord);

		updateDataReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				new Connect(NetworkRequestType.generateFetchMedicalInformation(null), ConnectPath.fetch_medical_information, new Callback() {
					@Override
					public void onSuccess(Object o) {
						HttpRawResponse raw = (HttpRawResponse) o;
						try {
							String str = raw.getOptions().getString(0);
							initJson(str);
							resetView();
							Toast.makeText(MedicalActivity.this, "Updated", Toast.LENGTH_SHORT).show();
						} catch (JSONException ignore) {

						}
					}

					@Override
					public void onFailure(Object o, Throwable e) {

					}

					@Override
					public void onFinish(Object o, Throwable e) {

					}
				}).execute();
			}
		};

		initNavigationBar();

		txtBar1.setOnClickListener( v -> setOnClickChangeView(txtBar1, vBarUnderline1, View.VISIBLE,
				new VaccinationAdapter(this, vacArray), null));
		txtBar2.setOnClickListener( v -> setOnClickChangeView(txtBar2, vBarUnderline2, View.VISIBLE,
				new DeinsectizaionAdapter(this, deiArray), null));
		txtBar3.setOnClickListener( v -> setOnClickChangeView(txtBar3, vBarUnderline3, View.INVISIBLE,
				null, null));

		lvItems.setAdapter(new VaccinationAdapter(this, vacArray));

		imgbtnSearchHospital.setOnClickListener(v ->
				startActivity(new Intent(MedicalActivity.this, SearchActivity.class)));

		LocalBroadcastManager.getInstance(this).registerReceiver(
				updateDataReceiver, new IntentFilter(getString(R.string.IntentFilter_request_medical_information)));
	}

	void resetView() {
		previousClickText.setTextColor(getColor(R.color.colorBackground));
		previousView.setBackgroundColor(getColor(android.R.color.transparent));
		txtBar1.setTextColor(getColor(R.color.colorMain));
		vBarUnderline1.setBackgroundColor(getColor(R.color.colorMain));
		lvBloodTestItems.setVisibility(View.INVISIBLE);
		lvItems.setVisibility(View.VISIBLE);
		lvItems.setAdapter(new VaccinationAdapter(this, vacArray));
		previousClickText = txtBar1;
		previousView = vBarUnderline1;
	}

	void initNavigationBar() {
		imgbtnNavBarCamera = findViewById(R.id.imgbtnCameraPage);
		imgbtnNavBarMedical = findViewById(R.id.imgbtnMedicalPage);
		imgbtnNavBarCalendar = findViewById(R.id.imgbtnCalendarPage);
		imgbtnNavBarArticle = findViewById(R.id.imgbtnArticlePage);
		imgbtnNavBarUser = findViewById(R.id.imgbtnUserPage);

		imgbtnNavBarMedical.setImageResource(R.drawable.medical_orange);

		imgbtnNavBarCamera.setOnClickListener(v ->
				startActivity(new Intent(MedicalActivity.this, BootstrapScannerActivity.class)));

		imgbtnNavBarArticle.setOnClickListener(v ->
				startActivity(new Intent(MedicalActivity.this, ArticleActivity.class)));

		imgbtnNavBarCalendar.setOnClickListener(v ->
				startActivity(new Intent(MedicalActivity.this, CalendarActivity.class)));

	}

	private void setBarVisibility(int visibility) {
		imgBackground.setVisibility(visibility);
		txtNextTimeTxt.setVisibility(visibility);
		txtNextDate.setVisibility(visibility);
	}

	private void setOnClickChangeView(TextView tView, View vUnderline, int barVisibility, ArrayAdapter<?> arrayAdapter1, ArrayAdapter<?> arrayAdapter2) {
		previousClickText.setTextColor(getColor(R.color.colorBackground));
		previousView.setBackgroundColor(getColor(android.R.color.transparent));
		tView.setTextColor(getColor(R.color.colorMain));
		vUnderline.setBackgroundColor(getColor(R.color.colorMain));
		previousClickText = tView;
		previousView = vUnderline;
		setBarVisibility(barVisibility);
		if (arrayAdapter1 != null) {
			lvBloodTestItems.setVisibility(View.INVISIBLE);
			lvItems.setVisibility(View.VISIBLE);
			lvItems.setAdapter(arrayAdapter1);
		}
		if (arrayAdapter2 != null) {
			lvItems.setVisibility(View.INVISIBLE);
			lvBloodTestItems.setVisibility(View.VISIBLE);
			lvBloodTestItems.setAdapter(arrayAdapter2);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(updateDataReceiver);
	}
}
