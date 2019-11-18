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

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
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
import moe.fluffy.app.types.PetInfo;

/**
 *	Must start this activity with startActivityForResult();
 */

public class RegisterActivity extends AppCompatActivity {

	EditText etName, etEmail, etPassword, etPassword2;
	ImageButton imgbtnConfirm;

	EditText etPetName, etBreed, etBirthday;
	RadioGroup rgGender, rgSpyed;
	// TODO: weight

	ImageButton imgbtnCat, imgbtnDog, imgbtnBird, imgbtnOther;
	ImageButton imgbtnPrevClicked, imgbtnBack;

	private String selected_type;

	private final float alphaValue = 0.5f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
							public void finish(Object o, Throwable e) {

							}
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

		etPetName.setOnFocusChangeListener( (view, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etPetName, R.string.pets_name, false));
		etBreed.setOnFocusChangeListener( (view, hasFocus) ->
				Utils.onFocusChange(hasFocus, RegisterActivity.this, etBreed, R.string.pets_breed, false));
		//etBirthday.setOnFocusChangeListener( (view, hasFocus) ->
		//		Utils.onFocusChange(hasFocus, RegisterActivity.this, etBirthday, R.string.pets_name, false));

		etBirthday.setOnFocusChangeListener((view, hasFocus) -> {
			if (hasFocus) {
				final View dialogView = View.inflate(RegisterActivity.this, R.layout.datetimepicker_with_button, null);
				final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();

				dialogView.findViewById(R.id.btnPickConfirm).setOnClickListener(v -> {
					DatePicker datePicker = dialogView.findViewById(R.id.dpEventInsert);
					etBirthday.setText(String.format("%s/%s/%s", datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
					alertDialog.dismiss();
				});
				alertDialog.setView(dialogView);
				alertDialog.show();
			}
		});


		imgbtnConfirm.setOnClickListener(v -> {
			// some verify method here
			if (!BuildConfig.isDemoMode) {
				try {
					PetInfo p = new PetInfo(etName.getText(), etBreed.getText(), etBirthday.getText());
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
				Toast.makeText(RegisterActivity.this, R.string.petTypeNotSelected, Toast.LENGTH_SHORT).show();
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
