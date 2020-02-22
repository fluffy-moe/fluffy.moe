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
package moe.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.assistant.SimpleCallback;
import moe.fluffy.app.types.AlbumCover;
import moe.fluffy.app.types.AlbumFiles;

public class AlbumCoverAdapter extends RecyclerView.Adapter<AlbumCoverAdapter.ViewHolder> {
	private static final String TAG = "log_AlbumCoverAdapter";

	private ArrayList<AlbumCover> covers;

	private SimpleCallback listener;
	public AlbumCoverAdapter(ArrayList<AlbumCover> covers, @NonNull SimpleCallback listener) {
		this.covers = covers;
		this.listener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_cover, parent, false);
		return new ViewHolder(convertView);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.setProp(covers.get(position), v -> {
			listener.OnFinished(covers.get(position).getCategory());
		} );

	}

	public AlbumCoverAdapter update(ArrayList<AlbumCover> covers) {
		this.covers = covers;
		return this;
	}

	@Override
	public int getItemCount() {
		return covers.size();
	}


	public static class ViewHolder extends RecyclerView.ViewHolder {

		View itemView;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
			this.itemView = itemView;
		}

		private static String parsePhotoCount(Context context, Integer count) {
			//return context.getString(R.string.text_photo_count, count, "s");
			return context.getString(R.string.fmt_photo_count, count, count > 1 ? "s" :"");
		}

		ViewHolder setProp(@NonNull AlbumCover albumCover, View.OnClickListener onClickListener) {
			TextView title = itemView.findViewById(R.id.txtAlbumTitle),
					date = itemView.findViewById(R.id.txtAlbumDate),
					counts = itemView.findViewById(R.id.txtAlbumPhotoCount);
			ImageView imgView = itemView.findViewById(R.id.imgItemAlbumCover);
			title.setText(albumCover.getName());
			Integer count = DatabaseHelper.getInstance().getAlbumSize(albumCover.getCategory());
			if (count != null && count != 0) {
				AlbumFiles.dbFriendlyAlbumFile file = DatabaseHelper.getInstance().getOnePhoto(albumCover.getCategory());
				if (file != null)
					Glide.with(itemView)
							.load(file.getPath())
							.into(imgView);
			}
			counts.setText(parsePhotoCount(itemView.getContext(), count));
			date.setText(albumCover.getDate());
			itemView.setOnClickListener(onClickListener);
			return this;
		}

	}

}
