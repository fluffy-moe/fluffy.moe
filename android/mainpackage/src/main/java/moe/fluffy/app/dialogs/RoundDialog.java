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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import moe.fluffy.app.R;

public class RoundDialog extends Dialog {
	private RoundDialog(@NonNull Context context, String body, String confirmText, @Nullable View.OnClickListener onConfirmListener,
					   @Nullable View.OnClickListener onCancelListener) {
		super(context, R.style.round_dialog);
		initDialog(LayoutInflater.from(context), body, confirmText, onConfirmListener, onCancelListener);
	}

	public RoundDialog(@NonNull Context context) {
		super(context, R.style.round_dialog);
	}

	public RoundDialog initDialog(@NonNull LayoutInflater inflater, String body, String confirmText, @Nullable View.OnClickListener onConfirmListener,
						   @Nullable View.OnClickListener onCancelListener) {
		View convertView = inflater.inflate(R.layout.dialog_temple, null);
		TextView txtBody = convertView.findViewById(R.id.txtDialogTitle);
		Button btnConfirm = convertView.findViewById(R.id.btnConfirm),
				btnCancel = convertView.findViewById(R.id.btnCancel);
		setContentView(convertView);
		txtBody.setText(body);
		btnConfirm.setText(confirmText);
		if (onCancelListener == null)
			btnCancel.setOnClickListener(v -> dismiss());
		else
			btnCancel.setOnClickListener(onCancelListener);
		if (onConfirmListener == null)
			btnConfirm.setOnClickListener(v -> dismiss());
		else
			btnConfirm.setOnClickListener(onConfirmListener);
		return this;
	}

	@CheckResult
	@NonNull
	public static RoundDialog build(@NonNull Context context, String body, String confirmText, @Nullable View.OnClickListener onConfirmListener,
							 @Nullable View.OnClickListener onCancelListener) {
		return new RoundDialog(context, body, confirmText, onConfirmListener, onCancelListener);
	}

	@CheckResult
	@NonNull
	public static RoundDialog build(@NonNull LayoutInflater inflater, String body, String confirmText, @Nullable View.OnClickListener onConfirmListener,
									@Nullable View.OnClickListener onCancelListener) {
		RoundDialog roundDialog =  new RoundDialog(inflater.getContext());
		roundDialog.initDialog(inflater, body, confirmText, onConfirmListener, onCancelListener);
		return roundDialog;
	}
}
