package moe.fluffy.app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

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
					.load(url)/*
					.error(R.drawable.placeholder)
					.placeholder(R.drawable.placeholder)
					.crossFade()*/
					.into(imageView);
		}
	}

	Button btnSelectPhoto;

	ArrayList<AlbumFile> mAlbumFiles;

	RecyclerView rvImages;

	AlbumFiles albumFiles;

	AlbumAdapter albumAdapter;

	private static boolean albumInited;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
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
		albumFiles = new AlbumFiles();
		btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
		rvImages = findViewById(R.id.rvAlbumList);
		rvImages.setLayoutManager(new GridLayoutManager(this, 3));
		rvImages.setHasFixedSize(true);
		btnSelectPhoto.setOnClickListener(v -> {
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
				.onCancel(result -> {})
				.start();
	}

}
