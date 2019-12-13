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

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codbking.calendar.CalendarUtil;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import moe.fluffy.app.BuildConfig;
import moe.fluffy.app.R;
import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.Connect;
import moe.fluffy.app.assistant.ConnectPath;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.HttpRawResponse;
import moe.fluffy.app.types.NetworkRequestType;
import moe.fluffy.app.types.PetInfo;

/**
 *	Must start this activity with startActivityForResult();
 */

public class RegisterActivity extends AppCompatActivity {

	private static final String TAG = "log_RegisterActivity";
	EditText etName, etEmail, etPassword, etPassword2;
	ImageButton imgbtnConfirm;

	EditText etPetName, etBreed, etBirthday;
	RadioGroup rgGender, rgNeuter;
	RadioButton rbM, rbW, rbNeuterY, rbNeuterN;
	RadioGroup rgWeights;
	RadioButton rb10, rb20, rb30, rb40, rbWeightLastSelect;
	private int weightSelect = 1;

	ImageButton imgbtnCat, imgbtnDog, imgbtnBird, imgbtnOther;
	ImageButton imgbtnPrevClicked, imgbtnBack;

	boolean genderM = true, Neuterd = false;

	private String selected_type;

	private final float alphaValue = 0.5f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_register);
		initRegisterPage();
	}

	private void initRegisterPage() {
		etName = findViewById(R.id.etRegisterName);
		etEmail = findViewById(R.id.etRegisterEmail);
		etPassword = findViewById(R.id.etRegisterPassword);
		etPassword2 = findViewById(R.id.etRegisterPassword2);
		imgbtnConfirm = findViewById(R.id.imgbtnRegisterConfirm);


		etName.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etName, R.string.users_name, false));
		etEmail.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etEmail, R.string.users_email, false));
		etPassword.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etPassword, R.string.users_password, true));
		etPassword2.setOnFocusChangeListener((v, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etPassword2, R.string.users_confirm_password, true));

		imgbtnConfirm.setOnClickListener(v -> {
			// TODO: Validation register params
			if (BuildConfig.DEBUG) {
				setContentView(R.layout.activity_choose_pet_type);
				initChoosePetTypeView();
				return ;
			}

			try {
				new Connect(
						NetworkRequestType.generateRegisterParams(
								etName.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString()
						),
						ConnectPath.register_path,
						new Callback() {
							@Override
							public void onSuccess(Object o) {
								HttpRawResponse h = (HttpRawResponse) o;
								if (h.getStatus() == 200) {
									Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
									putExtras(true, 0, "");
									setContentView(R.layout.activity_choose_pet_type);
									initChoosePetTypeView();
								}
								else {
									Toast.makeText(RegisterActivity.this, h.getErrorString(), Toast.LENGTH_SHORT).show();
									putExtras(false, h.getLastError(), h.getErrorString());
								}
							}

							@Override
							public void onFailure(Object o, Throwable e) {
								PopupDialog.build(RegisterActivity.this, e);
								putExtras(false, -1, Utils.exceptionToString(e));
							}

							@Override
							public void onFinish(Object o, Throwable e) {}
				}).execute();
			} catch (NoSuchAlgorithmException e) {
				PopupDialog.build(RegisterActivity.this, e);
			}
		});
	}

	private void putExtras(boolean isRegisterSuccess, int errorCode, String errorString) {
		getIntent().putExtra(getString(R.string.Intent_EventSuccess), isRegisterSuccess);
		getIntent().putExtra(getString(R.string.Intent_EventErrorCode), errorCode);
		getIntent().putExtra(getString(R.string.Intent_EventErrorMessage), errorString);
	}

	private void initCompleteRegisterView() {
		etPetName = findViewById(R.id.et_name);
		etBreed = findViewById(R.id.et_breed);
		etBirthday = findViewById(R.id.et_birthday);
		imgbtnBack = findViewById(R.id.imgbtnRegister2Back);
		imgbtnConfirm = findViewById(R.id.imgbtnRegister2Next);
		rgGender = findViewById(R.id.rbg_gender);
		rgNeuter = findViewById(R.id.rbg_yes_no);
		rgWeights = findViewById(R.id.rbg_weight);
		rbM = findViewById(R.id.rb_male);
		rbW = findViewById(R.id.rb_female);
		rbNeuterY = findViewById(R.id.rb_yes);
		rbNeuterN = findViewById(R.id.rb_no);

		rbWeightLastSelect = rb10 = findViewById(R.id.rb_10kg);
		rb20 = findViewById(R.id.rb_20kg);
		rb30 = findViewById(R.id.rb_30kg);
		rb40 = findViewById(R.id.rb_40kg);

		etPetName.setOnFocusChangeListener( (view, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etPetName, R.string.pets_name, false));
		etBreed.setOnFocusChangeListener( (view, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etBreed, R.string.pets_breed, false));
		etBirthday.setFocusable(false); // https://stackoverflow.com/a/14091107
		int[] c = CalendarUtil.getYMD(new Date());
		etBirthday.setText(String.format("%s/%s/%s", c[0], c[1], c[2]));
		etBirthday.setOnClickListener(_v -> {
			final View dialogView = LayoutInflater.from(this).inflate(R.layout.datetimepicker_with_button, null);
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

			dialogView.findViewById(R.id.btnPickConfirm).setOnClickListener(v -> {
				DatePicker datePicker = dialogView.findViewById(R.id.dpEventInsert);
				etBirthday.setText(String.format("%s/%s/%s", datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
				alertDialog.dismiss();
			});
			alertDialog.setView(dialogView);
			alertDialog.show();
			Window w = alertDialog.getWindow();
			if (w == null)
				throw new RuntimeException("Window should not null");
			w.setLayout(770, 700);
			//w.setBackgroundDrawableResource(R.drawable.round_background);
		});

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
					Neuterd = true;
					break;
				case R.id.rb_no:
					rbNeuterN.setBackground(getDrawable(R.drawable.radio_button_checked));
					rbNeuterY.setBackground(null);
					Neuterd = false;
			}
		});

		rgWeights.setOnCheckedChangeListener((group, checkedId) -> {
			switch (checkedId) {
				case R.id.rb_10kg:
					onWeightClick(rb10, 1);
					break;
				case R.id.rb_20kg:
					onWeightClick(rb20, 2);
					break;
				case R.id.rb_30kg:
					onWeightClick(rb30, 3);
					break;
				case R.id.rb_40kg:
					onWeightClick(rb40, 4);
			}
		});

		imgbtnConfirm.setOnClickListener(v -> {
			// some verify method here
			if (!BuildConfig.isDemoMode) {
				try {
					PetInfo p = new PetInfo(etName.getText(), etBreed.getText(), etBirthday.getText(), selected_type, genderM, Neuterd, weightSelect);
					HomeActivity.dbHelper.updatePetInfo(p);
				} catch (Exception ignore) {

				}
				putExtras(true, 0, "");
			}
			else
				startActivity(new Intent(RegisterActivity.this, CalendarActivity.class));
		});

		imgbtnBack.setOnClickListener( v -> {
			setContentView(R.layout.activity_choose_pet_type);
			initChoosePetTypeView();
		});
	}

	private void onWeightClick(RadioButton rb, int select_id) {
		weightSelect = select_id;
		rbWeightLastSelect.setBackground(null);
		rb.setBackground(getDrawable(R.drawable.radio_button_checked));
		rbWeightLastSelect = rb;
	}

	private void alterAlpha(ImageButton imgbtnClicked) {
		if (imgbtnPrevClicked != null) {
			// reset other clicked
			imgbtnPrevClicked.setAlpha(1.0f);
		}
		imgbtnClicked.setAlpha(alphaValue);
		imgbtnPrevClicked = imgbtnClicked;
	}

	private void initChoosePetTypeView() {
		imgbtnCat = findViewById(R.id.imgbtncat);
		imgbtnDog = findViewById(R.id.imgbtn_dog);
		imgbtnBird = findViewById(R.id.imgbtn_bird);
		imgbtnOther = findViewById(R.id.imgbtn_other);
		imgbtnBack = findViewById(R.id.imgbtnChooseTypeBack);
		imgbtnConfirm = findViewById(R.id.imgbtnChooseTypeNext);

		// Change alpha on click
		imgbtnBird.setOnClickListener(v -> alterAlpha(imgbtnBird));
		imgbtnDog.setOnClickListener(v -> alterAlpha(imgbtnDog));
		imgbtnOther.setOnClickListener(v -> alterAlpha(imgbtnOther));
		imgbtnCat.setOnClickListener(v -> alterAlpha(imgbtnCat));

		imgbtnConfirm.setOnClickListener( v -> {
			if ((imgbtnDog.getAlpha() == imgbtnCat.getAlpha()) && (imgbtnBird.getAlpha() == imgbtnOther.getAlpha())) {
				Toast.makeText(this, R.string.petTypeNotSelected, Toast.LENGTH_SHORT).show();
				return ;
			}
			if (imgbtnDog.getAlpha() == alphaValue) {
				selected_type = getString(R.string.typeDog);
			} else if (imgbtnCat.getAlpha() == alphaValue) {
				selected_type = getString(R.string.typeCat);
			} else if (imgbtnBird.getAlpha() == alphaValue) {
				selected_type = getString(R.string.typeBird);
			} else if (imgbtnOther.getAlpha() == alphaValue) {
				selected_type = getString(R.string.typeOther);
			} else {
				throw new IllegalStateException(getString(R.string.ERROR_UNEXPECTED_VALUE));
			}
			setContentView(R.layout.activity_complete_register);
			initCompleteRegisterView();
		});

		imgbtnBack.setOnClickListener( v -> {
			setContentView(R.layout.activity_register);
			initRegisterPage();
		});
	}
}
