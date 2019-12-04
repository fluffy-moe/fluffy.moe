package moe.fluffy.app.assistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import moe.fluffy.app.HomeActivity;
import moe.fluffy.app.R;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.BottomSheetBundle;

public class BottomSheetEventDialog extends BottomSheetDialog {

	private SimpleCallback listener;
	private ImageButton btnColorSelected;
	private Button categorySelected;
	private String categorySelectedText;
	@ColorRes
	int colorSelected;

	View contentView;

	BottomSheetEventFragment btf;

	public BottomSheetEventDialog(@NonNull Context context) {
		super(context);
		contentView = View.inflate(getContext(), R.layout.calendar_add_event_bottom, null);
		setContentView(contentView);
		configureBottomSheetBehavior(contentView);
	}

	public BottomSheetEventDialog setCallback(SimpleCallback _listener) {
		listener = _listener;
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		ImageButton btnConfirm  = contentView.findViewById(R.id.imgbtnCalendarSave);
		EditText etBody = contentView.findViewById(R.id.etCalendarBody);
		Switch swAlarm = contentView.findViewById(R.id.swCalendarAlarm);
		btf = new BottomSheetEventFragment();
		Window w = getWindow();
		if (w != null) {
			w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				w.requestFeature(Window.FEATURE_NO_TITLE);
		}
		initPopupColorPick(contentView);
		initCategoryButton(contentView);
		etBody.setOnFocusChangeListener((view, hasFocus) ->
				Utils.onFocusChange(hasFocus, getContext(), etBody, R.string.etCalendarAddEventHint, false));
		btnConfirm.setOnClickListener( l -> {
			EventsType et = new EventsType(btf, colorSelected, categorySelectedText,
					etBody.getText().toString(), swAlarm.isChecked());
			HomeActivity.dbHelper.insertEvent(et);
			//planedEvents.add(et);
			if (et.getDayBody().moreThanOrEqual(Date.getToday())) {
				if (listener != null)
					listener.OnFinished(et);
			}
			//Log.d(TAG, "init: Insert successful");
			btnColorSelected = null;
			colorSelected = 0;
			this.dismiss();
		});
	}


	public View getView() {
		return contentView;
	}
	private void configureBottomSheetBehavior(View contentView) {
		BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());

		if (mBottomSheetBehavior != null) {
			mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

				@Override
				public void onStateChanged(@NonNull View bottomSheet, int newState) {
					//showing the different states
					switch (newState) {
						case BottomSheetBehavior.STATE_HIDDEN:
							dismiss(); //if you want the modal to be dismissed when user drags the bottom sheet down
						case BottomSheetBehavior.STATE_EXPANDED:
						case BottomSheetBehavior.STATE_COLLAPSED:
						case BottomSheetBehavior.STATE_DRAGGING:
						case BottomSheetBehavior.STATE_SETTLING:
						case BottomSheetBehavior.STATE_HALF_EXPANDED:
							break;
					}
				}

				@Override
				public void onSlide(@NonNull View bottomSheet, float slideOffset) {
				}
			});
		}
	}



	void initCategoryButton(View viewAddEventPopup) {
		Button btnEvent, btnNote, btnSymptom, btnWater;
		btnEvent = viewAddEventPopup.findViewById(R.id.btnCalendarEvent);
		btnNote = viewAddEventPopup.findViewById(R.id.btnCalendarNote);
		btnSymptom = viewAddEventPopup.findViewById(R.id.btnCalendarSymptom);
		btnWater = viewAddEventPopup.findViewById(R.id.btnCalendarWater);
		categorySelectedText = getString(R.string.categoryEvent);
		categorySelected = btnEvent;
		btnEvent.setOnClickListener(v ->
				categoryOnClick(btnEvent, getString(R.string.categoryEvent), viewAddEventPopup));
		btnNote.setOnClickListener(v ->
				categoryOnClick(btnNote, getString(R.string.categoryNote), viewAddEventPopup));
		btnSymptom.setOnClickListener(v ->
				categoryOnClick(btnSymptom, getString(R.string.categorySymptom), viewAddEventPopup));
		btnWater.setOnClickListener(v ->
				categoryOnClick(btnWater, getString(R.string.categoryWater), viewAddEventPopup));
	}

	void categoryOnClick(Button btn, String category, View viewAddEventPopup) {
		//Log.v(TAG, "categoryOnClick => " + category);
		EditText etBody = viewAddEventPopup.findViewById(R.id.etCalendarBody);
		TextView txtTitle = viewAddEventPopup.findViewById(R.id.txtCalendarTitle);
		if (categorySelected != null) {
			categorySelected.setBackgroundResource(R.drawable.ic_rectangle_225);
			categorySelected.setTextColor(getColor(R.color.colorNotSelect));
		}
		btn.setBackgroundResource(R.drawable.ic_rectangle_227);
		btn.setTextColor(getColor(R.color.colorSelect));
		categorySelectedText = category;
		categorySelected = btn;
		if (category.equals(getString(R.string.categoryWater))) {
			txtTitle.setText(R.string.title_for_water);
			if (!etBody.isFocused())
				etBody.setText(R.string.etCalendarAddWaterHint);
			etBody.setOnFocusChangeListener((view, hasFocus) ->
					Utils.onFocusChange(hasFocus, getContext(), etBody, R.string.etCalendarAddWaterHint, false));

		} else {
			txtTitle.setText(R.string.text_title_for_calendar);
			if (!etBody.isFocused())
				etBody.setText(R.string.etCalendarAddEventHint);
			etBody.setOnFocusChangeListener((view, hasFocus) ->
					Utils.onFocusChange(hasFocus, getContext(), etBody, R.string.etCalendarAddEventHint, false));
		}
	}

	void initPopupColorPick(View viewAddEventPopup) {
		ImageButton imgbtnColor1, imgbtnColor2, imgbtnColor3, imgbtnColor4, imgbtnColor5;
		imgbtnColor1 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarOrange);
		imgbtnColor2 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarRed);
		imgbtnColor3 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarBlue);
		imgbtnColor4 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarGreen);
		imgbtnColor5 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarPink);
		initColorPick(imgbtnColor1, 1);
		initColorPick(imgbtnColor2, 2);
		initColorPick(imgbtnColor3, 3);
		initColorPick(imgbtnColor4, 4);
		initColorPick(imgbtnColor5, 5);

		// let button 1 to color default
		changeClickColor(imgbtnColor1, R.color.event_c1);
		btnColorSelected = imgbtnColor1;
		colorSelected = 1;
	}

	/**
	 * Reference: https://stackoverflow.com/a/29204632
	 *
	 * @param v image button
	 * @param color color from color resource file id
	 */
	private void changeStrokeColor(View v, @ColorRes int color) {
		((GradientDrawable) v.getBackground()).setStroke(10, getColor(color));
	}

	/**
	 * Reference https://stackoverflow.com/a/17825210
	 *
	 * @see #changeStrokeColor(View, int)
	 */
	private void changeClickColor(View v, @ColorRes int color) {
		((GradientDrawable)v.getBackground()).setColor(getColor(color));
	}

	private void colorOnClickListener(ImageButton imgbtn, int color) {
		if (btnColorSelected != null && btnColorSelected != imgbtn) {
			changeClickColor(btnColorSelected, android.R.color.transparent);
		}
		changeClickColor(imgbtn, getColorRes(color));
		btnColorSelected = imgbtn;
		colorSelected = color;
	}

	@ColorInt
	private int getColor(int color){
		return getContext().getColor(color);
	}

	@SuppressLint("DefaultLocale")
	@ColorRes
	private int getColorRes(int id) {
		return getContext().getResources().getIdentifier(getString(R.string.fmt_event_color, id), "color", getContext().getPackageName());
	}


	private String getString(@StringRes int id, Object... args) {
		return getContext().getString(id, args);
	}

	private void initColorPick(ImageButton imgbtn, int color) {
		changeStrokeColor(imgbtn, getColorRes(color));
		// Make sure circle is hollow
		changeClickColor(imgbtn, android.R.color.transparent);
		imgbtn.setOnClickListener(v ->
				colorOnClickListener(imgbtn, color));
	}

}
