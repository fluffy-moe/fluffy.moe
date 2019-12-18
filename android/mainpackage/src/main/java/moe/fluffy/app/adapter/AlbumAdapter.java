package moe.fluffy.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;

import moe.fluffy.app.R;
import moe.fluffy.app.types.AlbumFiles;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ImageViewHolder> {

	private static final String TAG = "log_AlbumAdapter";

	private OnItemClickListener onClickListener;
	public static class ImageLoader implements IZoomMediaLoader {
		RequestOptions options;

		{
			options = new RequestOptions()
					.centerCrop()
					/*.placeholder(R.drawable.ic_default_image)
					.error(R.drawable.ic_default_image)*/
					.priority(Priority.HIGH);
		}
		@Override
		public void displayImage(@NonNull Fragment fragment, @NonNull String path, @NonNull MySimpleTarget<Bitmap> simpleTarget) {
			Glide.with(fragment)
					.asBitmap()
					.load(path)
					.apply(options)
					.into(new CustomTarget<Bitmap>() {
						@Override
						public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
							simpleTarget.onResourceReady(resource);

						}

						@Override
						public void onLoadCleared(@Nullable Drawable placeholder) {

						}
					});
		}

		@Override
		public void onStop(@NonNull Fragment fragment) {
			Glide.with(fragment).onStop();
		}

		@Override
		public void clearMemory(@NonNull Context c) {
			Glide.get(c).clearMemory();
		}
	}

	private AlbumFiles albumFiles;

	public AlbumAdapter(AlbumFiles _albumFiles, OnItemClickListener _onClickListener){
		albumFiles = _albumFiles;
		onClickListener = _onClickListener;
	}

	@NonNull
	@Override
	public AlbumAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
		return new ImageViewHolder(v, onClickListener);
	}

	@Override
	public void onBindViewHolder(@NonNull AlbumAdapter.ImageViewHolder holder, int position) {
		holder.setData(albumFiles.get(position));
	}

	@Override
	public int getItemCount() {
		return albumFiles.size();
	}

	public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final OnItemClickListener mItemClickListener;
		private ImageView mIvImage;

		ImageViewHolder(View itemView, OnItemClickListener itemClickListener) {
			super(itemView);
			this.mItemClickListener = itemClickListener;
			this.mIvImage = itemView.findViewById(R.id.iv_album_content_image);
			itemView.setOnClickListener(this);
		}

		public void setData(AlbumFile albumFile) {
			Album.getAlbumConfig().
					getAlbumLoader().
					load(mIvImage, albumFile);

		}

		@Override
		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getAdapterPosition());
			}
		}
	}
}
