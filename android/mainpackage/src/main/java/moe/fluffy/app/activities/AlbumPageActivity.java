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
import moe.fluffy.app.types.AlbumFiles;

public class AlbumPageActivity extends AppCompatActivity {

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

	ArrayList<AlbumFile> mAlbumFiles;

	RecyclerView rvImages;

	AlbumFiles albumFiles;

	AlbumAdapter albumAdapter;

	private Integer category;

	private static boolean albumInited;

	public static String INT_CATEGORY = "DZFMZR2oWX";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		if (category == -1)
			category = null;

		albumFiles = new AlbumFiles()
				.setCategory(category)
				.queryFromDatabase(HomeActivity.dbHelper);

		txtDateAndSize = findViewById(R.id.txtAlbumPhotosDate);
		rvImages = findViewById(R.id.rvAlbumList);
		rvImages.setLayoutManager(new GridLayoutManager(this, 3));
		rvImages.setHasFixedSize(true);
		txtDateAndSize.setOnClickListener(v -> {
			selectPhoto();
		});
		albumAdapter = new AlbumAdapter(albumFiles, (view, position) -> {
			GPreviewBuilder.from(this)
					.setData(albumFiles.getThumbViewInfo())
					.setCurrentIndex(position)
					.setSingleFling(true)
					.setType(GPreviewBuilder.IndicatorType.Dot)
					.start();
		});
		rvImages.setAdapter(albumAdapter);
	}

	void selectPhoto() {
		Album.album(this)
				.multipleChoice()
				.columnCount(2)
				.camera(true)
				.cameraVideoQuality(1)
				.checkedList(mAlbumFiles)
				.onResult(result -> {
					mAlbumFiles = result;
					albumFiles.update(result);
					albumAdapter.notifyDataSetChanged();
				})
				.start();
	}

}
