package moe.fluffy.app.assistant;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

import moe.fluffy.app.HomeActivity;
import moe.fluffy.app.R;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventsType;

public class BottomSheetEventDialog extends BottomSheetDialog {
	private final String TAG = "log_BottomSheetEventDialog";

	private View viewAddEventPopup;
	private DateTimeWheelView dateTimeWheelView;
	private SimpleCallback listener;


	private ImageButton btnColorSelected;
	private Button categorySelected;
	private String categorySelectedText;
	@ColorRes private int colorSelected;
	BottomSheetBehavior bottomSheetBehavior;
	Context context;
	LinearLayout linearLayout;

	public BottomSheetEventDialog(@NonNull Context _context, SimpleCallback _listener) {
		super(_context);
		context = _context;
		listener = _listener;
	}


	@Override
	public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	private String getString(@StringRes int id) {
		return context.getString(id);
	}

	private String getString(@StringRes int id, Object... args) {
		return context.getString(id, args);
	}

	float startY; float moveY = 0;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d(TAG, "onTouchEvent: x => " + ev.getX() + " y => " + ev.getY());
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreateView: test");
		viewAddEventPopup = LayoutInflater.from(getContext()).inflate(R.layout.calendar_add_event_bottom, null);
		ImageButton btnConfirm = viewAddEventPopup.findViewById(R.id.imgbtnCalendarSave);
		EditText etBody = viewAddEventPopup.findViewById(R.id.etCalendarBody);
		Switch swAlarm = viewAddEventPopup.findViewById(R.id.swCalendarAlarm);
		linearLayout = viewAddEventPopup.findViewById(R.id.pickerLiner);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			etBody.setFocusedByDefault(false);
		}

		dateTimeWheelView = new DateTimeWheelView(viewAddEventPopup, 20);
		Window w = getWindow();
		if (w != null) {
			w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			w.requestFeature(Window.FEATURE_NO_TITLE);
		}
		//timePicker.setIs24HourView(true);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar.getTime());
		dateTimeWheelView.setPicker(calendar);
		dateTimeWheelView.setItemsVisible(5);

		/*FragmentManager childManager = getChildFragmentManager();
		FragmentTransaction childTransaction = childManager.beginTransaction();
		pvTime = new TimePickerFragment();*/
		/*bundle.putSerializable("1", new FragmentBundle(mFrameLayout));
		pvTime.setArguments(bundle);*/
		initPopupColorPick(viewAddEventPopup);
		initCategoryButton(viewAddEventPopup);
		etBody.setOnFocusChangeListener((view, hasFocus) ->
				Utils.onFocusChange(hasFocus, getContext(), etBody, R.string.etCalendarAddEventHint, false));
		btnConfirm.setOnClickListener( l -> {
			EventsType et = new EventsType(dateTimeWheelView, colorSelected, categorySelectedText,
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
		setContentView(viewAddEventPopup);
		/*setmBottomSheetCallback(viewAddEventPopup.findViewById(R.id.bottom_main));
		BottomSheetBehavior.from(viewAddEventPopup.findViewById(R.id.bottom_main)).setHideable(false);*/
	}

	/*@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		return true;
	}
*/
	/*@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFrameLayout = mView.findViewById(R.id.fragment_date_picker);
		initTimePicker();
	}*/

	private void initCategoryButton(View viewAddEventPopup) {
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

	private void categoryOnClick(Button btn, String category, View viewAddEventPopup) {
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

	private void initPopupColorPick(View viewAddEventPopup) {
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

	private void initColorPick(ImageButton imgbtn, int color) {
		changeStrokeColor(imgbtn, getColorRes(color));
		// Make sure circle is hollow
		changeClickColor(imgbtn, android.R.color.transparent);
		imgbtn.setOnClickListener(v ->
				colorOnClickListener(imgbtn, color));
	}
	private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
			= new BottomSheetBehavior.BottomSheetCallback() {
		@Override
		public void onStateChanged(@NonNull View bottomSheet,
								   @BottomSheetBehavior.State int newState) {
		}

		@Override
		public void onSlide(@NonNull View bottomSheet, float slideOffset) {
		}
	};

	public void setmBottomSheetCallback(View sheetView) {
		if (bottomSheetBehavior == null) {
			bottomSheetBehavior = BottomSheetBehavior.from(sheetView);
		}
		bottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
}