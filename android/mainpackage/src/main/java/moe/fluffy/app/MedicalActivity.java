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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MedicalActivity extends AppCompatActivity {

	TextView previousClickText;
	View previousView;

	TextView txtBar1, txtBar2, txtBar3;
	View vBarUnderline1, vBarUnderline2, vBarUnderline3;

	ImageButton imgbtnSearchHospital;

	TextView txtWelcome, txtAge, txtWeight;

	TextView txtNextTimeTxt, txtNextDate;

	ImageView imgPet, imgBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_medical);
		init();
	}

	private void init() {
		txtBar1 = findViewById(R.id.txtMedicalBar);
		txtBar2 = findViewById(R.id.txtMedicalBar2);
		txtBar3 = findViewById(R.id.txtMedicalBar3);
		vBarUnderline1 = findViewById(R.id.vacUnderline);
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

		txtBar1.setOnClickListener( v -> setOnClickChangeView(txtBar1, vBarUnderline1, View.VISIBLE));
		txtBar2.setOnClickListener( v -> setOnClickChangeView(txtBar2, vBarUnderline2, View.VISIBLE));
		txtBar3.setOnClickListener( v -> setOnClickChangeView(txtBar3, vBarUnderline3, View.INVISIBLE));

		imgbtnSearchHospital.setOnClickListener(v ->
				startActivity(new Intent(MedicalActivity.this, SearchActivity.class)));
	}

	private void setBarVisibility(int visibility) {
		imgBackground.setVisibility(visibility);
		txtNextTimeTxt.setVisibility(visibility);
		txtNextDate.setVisibility(visibility);
	}

	private void setOnClickChangeView(TextView tView, View vUnderline, int barVisibility) {
		if (previousClickText != null) {
			previousClickText.setTextColor(getColor(R.color.colorBackground));
		}
		if (previousView != null) {
			previousView.setBackgroundColor(getColor(R.color.colorBackground));
		}
		tView.setTextColor(getColor(R.color.colorMain));
		vUnderline.setBackgroundColor(getColor(R.color.colorMain));
		previousClickText = tView;
		previousView = vUnderline;
		setBarVisibility(barVisibility);
	}
}
