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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;

import moe.fluffy.app.R;
import moe.fluffy.app.activities.EditAccountActivity;

public class AccountManagementBottomSheetFragment extends BottomSheetDialogFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View convertView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_account_management, container, false);
		ImageButton imgbtnEditAccount, imgbtnFavoriteArticle, imgbtnLogout;
		imgbtnEditAccount = convertView.findViewById(R.id.imgbtnEditAccount);
		imgbtnFavoriteArticle = convertView.findViewById(R.id.imgbtnYourArticle);
		imgbtnLogout = convertView.findViewById(R.id.imgbtnLogOut);
		imgbtnLogout.setOnClickListener(l -> {
			final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.datetimepicker_with_button, null);
			AlertDialog.Builder logoutConfirm = new AlertDialog.Builder(getContext());
			logoutConfirm.setView(dialogView);
			ImageButton imgbtnCancel, imgbtnConfirm;
			imgbtnCancel = dialogView.findViewById(R.id.imgbtnLogoutCancel);
			imgbtnConfirm = dialogView.findViewById(R.id.imgbtnLogoutLogout);
			AlertDialog dialog = logoutConfirm.create();
			imgbtnCancel.setOnClickListener(v -> dialog.dismiss());
			imgbtnConfirm.setOnClickListener(v -> {
				// do something
				dialog.dismiss();
			});
			dialog.show();
		});
		imgbtnEditAccount.setOnClickListener(v -> startActivity(new Intent(getContext(), EditAccountActivity.class)));
		return convertView;
	}

	@NotNull
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
