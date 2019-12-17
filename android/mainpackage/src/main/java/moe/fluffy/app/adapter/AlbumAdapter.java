package moe.fluffy.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;

import moe.fluffy.app.R;
import moe.fluffy.app.types.AlbumFiles;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ImageViewHolder> {

	private static final String TAG = "log_AlbumAdapter";

	private AlbumFiles albumFiles;

	public AlbumAdapter(AlbumFiles _albumFiles){
		albumFiles = _albumFiles;
	}

	@NonNull
	@Override
	public AlbumAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
		return new ImageViewHolder(v, (view, position) -> {

		});
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
