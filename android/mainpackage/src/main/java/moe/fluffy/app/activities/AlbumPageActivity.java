package moe.fluffy.app.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.adapter.AlbumAdapter;

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
					.into(imageView);
		}
	}

	ArrayList<AlbumFile> albumFiles;

	private AlbumAdapter mAdapter;
	private ArrayList<AlbumFile> mAlbumFiles;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_content);
		Album.initialize(AlbumConfig.newBuilder(this).setAlbumLoader(new MediaLoader()).build());

		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

		mAdapter = new AlbumAdapter(this,
				(view, position) -> previewAlbum(position));
		recyclerView.setAdapter(mAdapter);
		selectAlbum();
	}

	private void selectAlbum() {

		Album.album(this)
				.multipleChoice()
				.columnCount(2)
				.checkedList(albumFiles)
				.onResult(result -> {
					albumFiles = result;
					mAdapter.notifyDataSetChanged();
				})
				.onCancel(new Action<String>() {
					@Override
					public void onAction(@NonNull String result) {
						Toast.makeText(AlbumPageActivity.this, "Canceled", Toast.LENGTH_LONG).show();
					}
				})
				.start();;
	}

	/**
	 * Preview image, to album.
	 */
	private void previewAlbum(int position) {
		if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
			Toast.makeText(this, "Please select, first.", Toast.LENGTH_LONG).show();
		} else {
			Album.galleryAlbum(this)
					.checkable(true)
					.checkedList(mAlbumFiles)
					.currentPosition(position)
					/*.widget(
							Widget.newDarkBuilder(this)
									.title(mToolbar.getTitle().toString())
									.build()
					)*/
					.onResult(result -> {
						mAlbumFiles = result;
						mAdapter.notifyDataSetChanged(mAlbumFiles);
						//mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
					})
					.start();
		}
	}
}
