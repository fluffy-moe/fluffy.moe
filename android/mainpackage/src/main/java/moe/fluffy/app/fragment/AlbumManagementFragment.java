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
package moe.fluffy.app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import moe.fluffy.app.R;
import moe.fluffy.app.dialogs.RoundDialog;

public class AlbumManagementFragment extends BottomSheetDialogFragment {

	private View.OnClickListener editAlbumListener, selectPhotoListener, confirmDeleteListener;

	public AlbumManagementFragment(@NonNull View.OnClickListener editAlbumListener,
								   @NonNull View.OnClickListener selectPhotoListener,
								   @NonNull View.OnClickListener confirmDeleteListener) {
		this.editAlbumListener = editAlbumListener;
		this.selectPhotoListener = selectPhotoListener;
		this.confirmDeleteListener = confirmDeleteListener;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.bottom_album_management, container, false);
		ImageButton imgbtnEditAlbum, imgbtnSelectPhoto, imgbtnDeleteAlbum;
		imgbtnEditAlbum = convertView.findViewById(R.id.imgbtnRenameAlbum);
		imgbtnSelectPhoto = convertView.findViewById(R.id.imgbtnSelectPhoto);
		imgbtnDeleteAlbum = convertView.findViewById(R.id.imgbtnDeleteAlbum);

		imgbtnEditAlbum.setOnClickListener( v -> {
			editAlbumListener.onClick(v);
			dismiss();
		});
		imgbtnSelectPhoto.setOnClickListener( v -> {
			selectPhotoListener.onClick(v);
			dismiss();
		});

		imgbtnDeleteAlbum.setOnClickListener(l -> {
			RoundDialog dialog = new RoundDialog(inflater.getContext());
			dialog.initDialog(inflater, "Confirm delete", "Confirm", v -> {
				dialog.dismiss();
				confirmDeleteListener.onClick(v);
			}, null).show();
		});
		return convertView;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
		Dialog d = super.onCreateDialog(savedInstanceState);
		Window w = d.getWindow();
		if (w != null) {
			w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			w.requestFeature(Window.FEATURE_NO_TITLE);
		}
		return d;
	}
}
