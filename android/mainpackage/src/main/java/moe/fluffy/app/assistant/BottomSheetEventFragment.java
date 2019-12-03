package moe.fluffy.app.assistant;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import moe.fluffy.app.CalendarActivity;
import moe.fluffy.app.HomeActivity;
import moe.fluffy.app.R;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.FragmentBundle;

public class BottomSheetEventFragment extends BottomSheetDialogFragment {
	private View mView;
	private TimePickerView pvTime;
	private FrameLayout mFrameLayout;
	private SimpleCallback listener;

	public int _year, _month, _day, _hour, _minute;


	private ImageButton btnColorSelected;
	private Button categorySelected;
	private String categorySelectedText;
	@ColorRes
	int colorSelected;

	@Nullable
	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = getLayoutInflater().inflate(R.layout.calendar_add_event_bottom, container, false);
		View viewAddEventPopup = getLayoutInflater().inflate(R.layout.calendar_add_event_bottom, null);
		ImageButton btnConfirm = viewAddEventPopup.findViewById(R.id.imgbtnCalendarSave);
		EditText etBody = viewAddEventPopup.findViewById(R.id.etCalendarBody);
		Switch swAlarm = viewAddEventPopup.findViewById(R.id.swCalendarAlarm);
		mFrameLayout = viewAddEventPopup.findViewById(R.id.viewCalendarPick);
		/*Window w = getWindow();
		if (w != null) {
			w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				/*w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				w.requestFeature(Window.FEATURE_NO_TITLE);
		}

		dialog.setContentView(viewAddEventPopup);*/
		//timePicker.setIs24HourView(true);
		Bundle bundle = getArguments();
		if (bundle != null) {
			FragmentBundle fragmentBundle = (FragmentBundle) bundle.getSerializable("0");
			if (fragmentBundle != null){
				listener = fragmentBundle.getListener();
			}
		}
		initPopupColorPick(viewAddEventPopup);
		initCategoryButton(viewAddEventPopup);
		etBody.setOnFocusChangeListener((view, hasFocus) ->
				Utils.onFocusChange(hasFocus, safeGetContext(), etBody, R.string.etCalendarAddEventHint, false));
		btnConfirm.setOnClickListener( l -> {
			EventsType et = new EventsType(this, colorSelected, categorySelectedText,
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
		initTimePicker();
		return mView;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFrameLayout = mView.findViewById(R.id.fragment_date_picker);
		initTimePicker();
	}*/

	private void initTimePicker() {
		Calendar selectedDate = Calendar.getInstance();

		pvTime = new TimePickerBuilder(getActivity(), (date, v) -> {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			_year = c.get(Calendar.YEAR);
			_month = c.get(Calendar.MONTH);
			_day = c.get(Calendar.DAY_OF_MONTH);
			_hour = c.get(Calendar.HOUR_OF_DAY);
			_minute = c.get(Calendar.MINUTE);
		})
				.setType(new boolean[]{true, true, true, true, true, false})
				.setLabel("", "", "", "", "", "")
				.setDividerColor(Color.TRANSPARENT)
				.setContentTextSize(20)
				.setDate(selectedDate)
				.setItemVisibleCount(5)
				.setCycle(true)
				.setDecorView(mFrameLayout)
				.setOutSideColor(0x00000000)
				.setOutSideCancelable(false)
				.build();

		pvTime.setKeyBackCancelable(false);
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
					Utils.onFocusChange(hasFocus, safeGetContext(), etBody, R.string.etCalendarAddWaterHint, false));

		} else {
			txtTitle.setText(R.string.text_title_for_calendar);
			if (!etBody.isFocused())
				etBody.setText(R.string.etCalendarAddEventHint);
			etBody.setOnFocusChangeListener((view, hasFocus) ->
					Utils.onFocusChange(hasFocus, safeGetContext(), etBody, R.string.etCalendarAddEventHint, false));
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
		return safeGetContext().getColor(color);
	}

	@SuppressLint("DefaultLocale")
	@ColorRes
	private int getColorRes(int id) {
		return getResources().getIdentifier(getString(R.string.fmt_event_color, id), "color", safeGetContext().getPackageName());
	}

	private void initColorPick(ImageButton imgbtn, int color) {
		changeStrokeColor(imgbtn, getColorRes(color));
		// Make sure circle is hollow
		changeClickColor(imgbtn, android.R.color.transparent);
		imgbtn.setOnClickListener(v ->
				colorOnClickListener(imgbtn, color));
	}


	public int getYear() {
		return _year;
	}

	public int getMonth() {
		return _month;
	}

	public int getDay() {
		return _day;
	}

	public int getHour() {
		return _hour;
	}

	public int getMinute() {
		return _minute;
	}

	Context safeGetContext() {
		Context context = getContext();
		if (context == null)
			throw new RuntimeException("null context");
		return context;
	}
}
