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
package moe.fluffy.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.SimpleCallback;
import moe.fluffy.app.assistant.Utils;

public class EditTextDialog extends Dialog {

	public EditTextDialog(@NonNull Context context) {
		super(context, R.style.round_dialog);
	}



	public EditTextDialog initView(@NonNull LayoutInflater inflater, @NonNull SimpleCallback onClickListener,
								   CharSequence originalName) {
		return this.initView(inflater,onClickListener, originalName, null, null);
	}

	public EditTextDialog initView(@NonNull LayoutInflater inflater, @NonNull SimpleCallback onClickListener,
								   @Nullable CharSequence editText, @Nullable String title, @Nullable String confirmText) {
		Button btnCancel, btnConfirm;
		View convertView = inflater.inflate(R.layout.dialog_edit_album, null);
		EditText etName = convertView.findViewById(R.id.etRenameBlock);
		TextView txtTitle = convertView.findViewById(R.id.txtRenameAlbumTitle);
		btnCancel = convertView.findViewById(R.id.btnCancel);
		btnConfirm = convertView.findViewById(R.id.btnConfirm);

		if (title != null)
			txtTitle.setText(title);

		if (confirmText != null)
			btnConfirm.setText(confirmText);

		if (editText == null)
			etName.setOnFocusChangeListener((view, hasFocus) ->
					Utils.onFocusChange(hasFocus, inflater.getContext(), etName, R.string.text_write_the_name, false));

		btnCancel.setOnClickListener( l -> dismiss());
		etName.setText(editText);
		btnConfirm.setOnClickListener( l -> {
			dismiss();
			onClickListener.OnFinished(etName);
		});
		setContentView(convertView);
		return this;
	}

	@CheckResult
	@NonNull
	public static EditTextDialog build(@NonNull Context context, @NonNull SimpleCallback onClickListener,
									   CharSequence originalName) {
		return build(context, onClickListener, originalName, null, null);
	}

	@CheckResult
	@NonNull
	public static EditTextDialog build(@NonNull Context context, @NonNull SimpleCallback onClickListener,
									   @Nullable CharSequence defaultName, @Nullable String title, @Nullable String confirmText) {
		EditTextDialog dialog = new EditTextDialog(context);
		dialog.initView(LayoutInflater.from(context), onClickListener, defaultName, title, confirmText);
		return dialog;
	}


	@Override
	public void show() {
		super.show();
		Window w = getWindow();
		if (w != null) {
			w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}
	}
}
