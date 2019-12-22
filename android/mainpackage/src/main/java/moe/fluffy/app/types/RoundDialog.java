package moe.fluffy.app.types;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import moe.fluffy.app.R;

public class RoundDialog extends Dialog {
	public RoundDialog(@NonNull Context context, String body, String confirmText, @Nullable View.OnClickListener onConfirmListener,
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

	public static RoundDialog build(@NonNull Context context, String body, String confirmText, @Nullable View.OnClickListener onConfirmListener,
							 @Nullable View.OnClickListener onCancelListener) {
		return new RoundDialog(context, body, confirmText, onConfirmListener, onCancelListener);
	}

	public static RoundDialog build(@NonNull LayoutInflater inflater, String body, String confirmText, @Nullable View.OnClickListener onConfirmListener,
									@Nullable View.OnClickListener onCancelListener) {
		RoundDialog roundDialog =  new RoundDialog(inflater.getContext());
		roundDialog.initDialog(inflater, body, confirmText, onConfirmListener, onCancelListener);
		return roundDialog;
	}
}
