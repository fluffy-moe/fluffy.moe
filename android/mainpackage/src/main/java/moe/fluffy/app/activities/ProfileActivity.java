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

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.adapter.AlbumCoverAdapter;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.fragment.AlbumManagementFragment;
import moe.fluffy.app.types.AlbumCover;
import moe.fluffy.app.types.divider.HorizontalPaddingItemDecoration;

public class ProfileActivity extends AppCompatActivity {
	private static final String TAG = "log_ProfileActivity";

	ImageButton imgbtnMore;


	Button btnAddAlbum;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

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
		btnAddAlbum = findViewById(R.id.btnAddAlbum);

		albumCovers = HomeActivity.dbHelper.getAlbums();

		albumCoverAdapter = new AlbumCoverAdapter(albumCovers,
				new Intent(this, AlbumPageActivity.class));
		rvAlbums.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		rvAlbums.addItemDecoration(new HorizontalPaddingItemDecoration(73));
		rvAlbums.setAdapter(albumCoverAdapter);

		btnAddAlbum.setOnClickListener(v-> {
			AlbumCover albumCover = HomeActivity.dbHelper.createAlbum(Utils.generateRandomString(5));
			albumCovers.add(albumCover);
			albumCoverAdapter.notifyItemInserted(albumCovers.size() - 1);
		});

		imgbtnMore = findViewById(R.id.imgbtnProfileDot);
		imgbtnMore.setOnClickListener(v -> {
			AlbumManagementFragment sheetFragment = new AlbumManagementFragment();
			sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
		});
		initNavigationBar();
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
