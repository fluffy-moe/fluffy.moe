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

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.adapter.AlbumAdapter;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.dialogs.EditTextDialog;
import moe.fluffy.app.fragment.AlbumManagementFragment;
import moe.fluffy.app.types.AlbumCover;
import moe.fluffy.app.types.AlbumFiles;

public class AlbumPageActivity extends AppCompatActivity {

	private static final String TAG = "log_AlbumPageActivity";

	private static class MediaLoader implements AlbumLoader {

		@Override
		public void load(ImageView imageView, AlbumFile albumFile) {
			load(imageView, albumFile.getPath());
		}

		@Override
		public void load(ImageView imageView, String url) {
			Glide.with(imageView.getContext())
					.load(url)
					.error(R.drawable.no_image)
					.placeholder(R.drawable.no_image)
					.into(imageView);
		}
	}

	//Button btnSelectPhoto;
	TextView txtTitle, txtDateAndSize;
	ImageButton imgbtnBack, imgbtnMenu;

	ArrayList<AlbumFile> mAlbumFiles;

	RecyclerView rvImages;

	AlbumFiles albumFiles;

	AlbumAdapter albumAdapter;

	private Integer category;

	String createDate;

	private static boolean albumInited;

	public static String INT_CATEGORY = "DZFMZR2oWX";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_album_photos);
		initAlbum();
		init();
	}

	private void initAlbum() {
		if (!albumInited) {
			Album.initialize(AlbumConfig
					.newBuilder(this).setAlbumLoader(new MediaLoader()).build());
			ZoomMediaLoader.getInstance().init(new AlbumAdapter.ImageLoader());
			albumInited = true;
		}
	}

	void init() {
		category = getIntent().getIntExtra(INT_CATEGORY, -1);
		if (category == -1) {
			//category = null;
			PopupDialog.build(this, new RuntimeException("Category should not be null"));
			finish();
		}

		// init album
		albumFiles = new AlbumFiles()
				.setCategory(category)
				.queryFromDatabase(DatabaseHelper.getInstance());

		mAlbumFiles = albumFiles.getAlbumList();

		txtTitle = findViewById(R.id.txtAlbumPhotosContent);
		txtDateAndSize = findViewById(R.id.txtAlbumPhotosDate);
		imgbtnBack = findViewById(R.id.imgbtnAlbumPhotosBack);
		rvImages = findViewById(R.id.rvAlbumList);
		imgbtnMenu = findViewById(R.id.imgbtnAlbumPhotosMenu);

		rvImages.setLayoutManager(new GridLayoutManager(this, 3));
		//rvImages.setHasFixedSize(true);

		imgbtnMenu.setOnClickListener(v -> {
			AlbumManagementFragment fragment = new AlbumManagementFragment(
					o -> {
						EditTextDialog.build(this, etName -> {
							EditText newName = (EditText)etName;
							DatabaseHelper.getInstance().editAlbumName(category, newName);
							txtTitle.setText(newName.getText().toString());
						}, txtTitle.getText()).show();
					},
					o -> selectPhoto(),
					o -> {
				DatabaseHelper.getInstance().deleteAlbum(category);
				finish();
			});
			fragment.show(getSupportFragmentManager(), fragment.getTag());
		});
		imgbtnBack.setOnClickListener(v -> finish());
		int count = albumFiles.getCount();
		AlbumCover albumCover = DatabaseHelper.getInstance().getAlbumFromCategoryOrThrow(category);
		createDate = albumCover.getDate();
		txtTitle.setText(albumCover.getName());
		txtDateAndSize.setText(getDateString(count, createDate));
		albumAdapter = new AlbumAdapter(albumFiles, (view, position) -> {
			GPreviewBuilder.from(this)
					.setData(albumFiles.getThumbViewInfo(true))
					.setCurrentIndex(position)
					.setSingleFling(true)
					.setType(GPreviewBuilder.IndicatorType.Dot)
					.start();
		});
		rvImages.setAdapter(albumAdapter);
	}

	private String getDateString(int count, String date) {
		return getString(R.string.fmt_album_page_date,  count, count > 1 ? "s" : "", date);
	}

	void selectPhoto() {
		Album.album(this)
				.multipleChoice()
				.columnCount(2)
				//.camera(true)
				//.cameraVideoQuality(1)
				.checkedList(mAlbumFiles)
				.onResult(result -> {
					mAlbumFiles = result;
					albumFiles.update(result);
					DatabaseHelper.getInstance().updatePhotos(albumFiles);
					txtDateAndSize.setText(getDateString(result.size(), createDate));
					albumAdapter.notifyDataSetChanged();
				})
				.start();
	}

}
