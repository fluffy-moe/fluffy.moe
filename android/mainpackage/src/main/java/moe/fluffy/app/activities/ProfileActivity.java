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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.adapter.AlbumCoverAdapter;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.assistant.PetInfoEx;
import moe.fluffy.app.dialogs.EditTextDialog;
import moe.fluffy.app.fragment.AccountManagementFragment;
import moe.fluffy.app.types.AlbumCover;
import moe.fluffy.app.types.divider.HorizontalPaddingItemDecoration;

public class ProfileActivity extends AppCompatActivity {
	private static final String TAG = "log_ProfileActivity";

	ImageButton imgbtnMore;

	public static final int SHOW_ALBUM_DETAIL = 0x12;

	ImageButton btnAddAlbum;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

	TextView textName, textBirthday, textWeight;

	RecyclerView rvAlbums;
	ArrayList<AlbumCover> albumCovers;
	AlbumCoverAdapter albumCoverAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_profile);
		init();
	}

	void init() {
		rvAlbums = findViewById(R.id.rvAlbumList);
		btnAddAlbum = findViewById(R.id.imgbtnProfileAddAlbum);

		textWeight = findViewById(R.id.txtProfileKg);
		textName = findViewById(R.id.txtProfileName);
		textBirthday = findViewById(R.id.txtProfileYearold);

		albumCovers = DatabaseHelper.getInstance().getAlbums();

		albumCoverAdapter = new AlbumCoverAdapter(albumCovers,
				o -> {
					Integer category = (Integer) o;
					Intent intent = new Intent(this, AlbumPageActivity.class);
					intent.putExtra(AlbumPageActivity.INT_CATEGORY, category);
					startActivityForResult(intent, SHOW_ALBUM_DETAIL);
				});
		rvAlbums.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		//rvAlbums.addItemDecoration(new HorizontalPaddingItemDecoration(73));
		rvAlbums.setAdapter(albumCoverAdapter);

		btnAddAlbum.setOnClickListener(v-> {
			EditTextDialog.build(this, _editText -> {
				EditText editText = (EditText) _editText;
				Log.v(TAG, "init: getCoversCount => " + albumCovers.size());
				AlbumCover albumCover = DatabaseHelper.getInstance().createAlbum(editText.getText().toString());
				albumCovers.add(albumCover);
				albumCoverAdapter.notifyItemInserted(albumCovers.size() - 1);
			}, null, "New album", "Add").show();
		});

		imgbtnMore = findViewById(R.id.imgbtnProfileDot);
		imgbtnMore.setOnClickListener(v -> {
			AccountManagementFragment sheetFragment = new AccountManagementFragment();
			sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
		});
		initNavigationBar();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == SHOW_ALBUM_DETAIL) {
			albumCovers = DatabaseHelper.getInstance().getAlbums();
			albumCoverAdapter.update(albumCovers);
			albumCoverAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("SetTextI18n")
	@Override
	protected void onResume() {
		super.onResume();
		PetInfoEx petInfoEx = DatabaseHelper.getInstance().getPetInfoEx();
		if (petInfoEx != null) {
			//birthday.setText(petInfoEx.getBirthday().toString());
			textName.setText(petInfoEx.getName());
			textWeight.setText(petInfoEx.getWeight() + "kg");
		}
	}


	void initNavigationBar() {
		imgbtnNavBarCamera = findViewById(R.id.imgbtnCameraPage);
		imgbtnNavBarMedical = findViewById(R.id.imgbtnMedicalPage);
		imgbtnNavBarCalendar = findViewById(R.id.imgbtnCalendarPage);
		imgbtnNavBarArticle = findViewById(R.id.imgbtnArticlePage);
		imgbtnNavBarUser = findViewById(R.id.imgbtnUserPage);

		imgbtnNavBarUser.setImageResource(R.drawable.home_orange);

		imgbtnNavBarCamera.setOnClickListener(v ->
				startActivity(new Intent(this, BootstrapScannerActivity.class)));

		imgbtnNavBarArticle.setOnClickListener(v ->
				startActivity(new Intent(this, ArticleActivity.class)));

		imgbtnNavBarCalendar.setOnClickListener(v ->
				startActivity(new Intent(this, CalendarActivity.class)));

		imgbtnNavBarMedical.setOnClickListener(v ->
				startActivity(new Intent(this, MedicalActivity.class)));

	}
}
