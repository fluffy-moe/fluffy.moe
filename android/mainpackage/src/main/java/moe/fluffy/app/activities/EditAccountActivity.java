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

import android.app.Dialog;
import android.gesture.GestureStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.assistant.PetInfoEx;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.Date;

public class EditAccountActivity extends AppCompatActivity {

	private ImageButton dog, bird, cat, other, back;
	private EditText breed, birthday, name, weight, password;

	private Switch mNoticifaction;

	private RadioGroup rgGender, rgNeuter;
	private RadioButton rbM, rbW, rbNeuterY, rbNeuterN;

	boolean genderM = true, Neutered = false;

	private ImageButton prevBtn;

	private static final float alphaValue = 0.6f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_account);
		init();
	}

	private void init() {
		dog = findViewById(R.id.imgbtnAccountDog);
		bird = findViewById(R.id.imgbtnAccountBird);
		cat = findViewById(R.id.imgbtnAccountCat);
		other = findViewById(R.id.imgbtnAccountOther);
		name = findViewById(R.id.etAccountName);
		breed = findViewById(R.id.etAccountBreed);
		birthday = findViewById(R.id.etAccountBirthday);
		weight = findViewById(R.id.etAccountWeight);
		password = findViewById(R.id.etAccountNewPassword);

		rgGender = findViewById(R.id.rbg_gender);
		rgNeuter = findViewById(R.id.rbg_yes_no);
		rbM = findViewById(R.id.rb_male);
		rbW = findViewById(R.id.rb_female);
		rbNeuterY = findViewById(R.id.rb_yes);
		rbNeuterN = findViewById(R.id.rb_no);

		back = findViewById(R.id.imgbtnAccountBack);

		mNoticifaction = findViewById(R.id.swAccountNotification);

		PetInfoEx petInfoEx = DatabaseHelper.getInstance().getPetInfoEx();
		if (petInfoEx != null) {
			birthday.setText(petInfoEx.getBirthday().toString());
			name.setText(petInfoEx.getName());
			weight.setText(String.valueOf(petInfoEx.getWeight()));
		}

		birthday.setFocusable(false);

		rgGender.setOnCheckedChangeListener((group, checkedId) -> {
			switch (checkedId) {
				case R.id.rb_male:
					rbM.setBackground(getDrawable(R.drawable.radio_button_checked));
					rbW.setBackground(null);
					genderM = true;
					break;
				case R.id.rb_female:
					rbW.setBackground(getDrawable(R.drawable.radio_button_checked));
					rbM.setBackground(null);
					genderM = false;
			}
		});

		rgNeuter.setOnCheckedChangeListener((group, checkedId) -> {
			switch (checkedId) {
				case R.id.rb_yes:
					rbNeuterY.setBackground(getDrawable(R.drawable.radio_button_checked));
					rbNeuterN.setBackground(null);
					Neutered = true;
					break;
				case R.id.rb_no:
					rbNeuterN.setBackground(getDrawable(R.drawable.radio_button_checked));
					rbNeuterY.setBackground(null);
					Neutered = false;
			}
		});

		name.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, this, name, R.string.pets_name, false));
		breed.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, this, breed, R.string.pets_breed, false));
		weight.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, this, weight, R.string.kilogram, false));

		password.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, this, password, R.string.text_new_password, true));


		birthday.setOnClickListener(_v -> {
			final View dialogView = LayoutInflater.from(this).inflate(R.layout.datetimepicker_with_button, null);
			final Dialog alertDialog = new Dialog(this, R.style.round_dialog);

			alertDialog.setContentView(dialogView);
			dialogView.findViewById(R.id.btnPickConfirm).setOnClickListener(v -> {
				DatePicker datePicker = dialogView.findViewById(R.id.dpEventInsert);
				birthday.setText(String.format("%s/%s/%s", datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
				alertDialog.dismiss();
			});
			//alertDialog.setView(dialogView);
			alertDialog.show();
			Window w = alertDialog.getWindow();
			if (w == null)
				throw new RuntimeException("Window should not null");
			w.setBackgroundDrawableResource(R.drawable.round_background);
		});

		dog.setOnClickListener(v -> ChangeCategory(dog));
		bird.setOnClickListener(v -> ChangeCategory(bird));
		cat.setOnClickListener(v -> ChangeCategory(cat));
		other.setOnClickListener(v -> ChangeCategory(other));

		// FIXME: Write all profile items to database
		back.setOnClickListener(v -> {
			String newName = name.getText().toString(), newBirthday = birthday.getText().toString(), newWeight = weight.getText().toString();
			if (newName.equals(getString(R.string.pets_name)))
				newName = "Chubby";
			if (newWeight.equals(getString(R.string.kilogram)))
				newWeight = "53";
			if (newBirthday.startsWith("MM"))
				newBirthday = "2020/04/01";
			DatabaseHelper.getInstance().insertPetInfoEx(new PetInfoEx(newName, new Date(newBirthday), Integer.parseInt(newWeight)));
			finish();
		});
	}

	private void ChangeCategory(ImageButton imgbtn) {
		if (prevBtn != null) {
			prevBtn.setAlpha(1.0f);
		}
		imgbtn.setAlpha(alphaValue);
		prevBtn = imgbtn;
	}
}
