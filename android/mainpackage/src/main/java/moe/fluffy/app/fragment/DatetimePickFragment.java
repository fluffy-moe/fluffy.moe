package moe.fluffy.app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.SimpleCallback;

public class DatetimePickFragment extends DialogFragment {
	SimpleCallback listener;

	DatePicker datePicker;

	public DatetimePickFragment(@NonNull SimpleCallback listener) {
		this.listener = listener;
	}

	public DatetimePickFragment() {

	}

	public DatetimePickFragment setListener(SimpleCallback listener) {
		this.listener = listener;
		return this;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View convertView =  inflater.inflate(R.layout.datetimepicker_with_button, container, false);
		Button btnSet = convertView.findViewById(R.id.btnPickConfirm);
		datePicker = convertView.findViewById(R.id.dpEventInsert);
		btnSet.setOnClickListener(v -> {
			if (listener != null)
				listener.OnFinished(v);
		});
		return convertView;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
		Dialog d = super.onCreateDialog(savedInstanceState);
		Window w = d.getWindow();
		if (w != null) {
			w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			w.requestFeature(Window.FEATURE_NO_TITLE);
			w.setLayout(770, 700);
		}
		return d;
	}

	public DatePicker getDatePicker() {
		return datePicker;
	}
}
