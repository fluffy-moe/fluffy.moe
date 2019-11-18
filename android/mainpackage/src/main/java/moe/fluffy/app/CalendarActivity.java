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
package moe.fluffy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import com.codbking.calendar.CalendarDateView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventDashboardType;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.adapter.EventDashboardAdapter;

import static moe.fluffy.app.assistant.Utils.px;

public class CalendarActivity extends AppCompatActivity {

	private static String TAG = "log_CalendarViewActivity";

	CalendarDateView mCalendarDateView;
	ListView lvEventDashboard;
	TextView txtMonth;
	TextView txtYear;
	TextView lastDateSelected = null;

	Button btnAddEvent;

	private ImageButton btnColorSelected, categorySelected;
	private String categorySelectedText;
	@ColorRes int colorSelected;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

	private static ArrayList<EventsType> planedEvents;
	ArrayList<EventsType> todayEvent = new ArrayList<>(), featureEvent = new ArrayList<>();
	ArrayList<EventDashboardType> eventDashboardTypes = new ArrayList<>();
	EventDashboardAdapter eventDashboardAdapter;

	void find_view() {
		mCalendarDateView = findViewById(R.id.calendarDateView);
		lvEventDashboard = findViewById(R.id.listEvents);
		txtMonth = findViewById(R.id.txtCalendarTitle);
		txtYear = findViewById(R.id.txtYearNumber);
		btnAddEvent = findViewById(R.id.btnAddEvent);
		findNavigationBar();
	}


	void findNavigationBar() {
		imgbtnNavBarCamera = findViewById(R.id.imgbtnCameraPage);
		imgbtnNavBarMedical = findViewById(R.id.imgbtnMedicalPage);
		imgbtnNavBarCalendar = findViewById(R.id.imgbtnCalendarPage);
		imgbtnNavBarArticle = findViewById(R.id.imgbtnArticlePage);
		imgbtnNavBarUser = findViewById(R.id.imgbtnUserPage);

		imgbtnNavBarCalendar.setImageResource(R.drawable.calendar_orange);


		imgbtnNavBarCamera.setOnClickListener(v ->
				startActivity(new Intent(CalendarActivity.this, BoostScanActivity.class)));

		imgbtnNavBarArticle.setOnClickListener(v ->
				startActivity(new Intent(CalendarActivity.this, ArticleActivity.class)));

		imgbtnNavBarMedical.setOnClickListener(v ->
				startActivity(new Intent(CalendarActivity.this, MedicalActivity.class)));

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_calendar_viewer);
		find_view();
		init();
	}

	void init() {

		eventDashboardAdapter = new EventDashboardAdapter(this, eventDashboardTypes);
		updateEventsDashboard(false);
		lvEventDashboard.setAdapter(eventDashboardAdapter);

		mCalendarDateView.setAdapter((convertView, parentView, bean) -> {
			TextView viewMonth;
			View underlineView;
			if (convertView == null) {
				convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.event_item_calendar, null);
				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(55), px(55));
				convertView.setLayoutParams(params);
			}

			viewMonth = convertView.findViewById(R.id.txtDay);
			underlineView = convertView.findViewById(R.id.viewUnderLine);

			viewMonth.setText(String.valueOf(bean.day));


			if (bean.mothFlag != 0) {
				viewMonth.setTextColor(getColor(R.color.calendarWeekTitle));
			} else {
				viewMonth.setTextColor(getColor(R.color.calendarBlack));
			}

			//Log.d(TAG, "init: color_id => " + color_id);
			underlineView.setBackgroundColor(
					getColor(HomeActivity.dbHelper.getTodayColorID(bean.year, bean.moth, bean.day)));

			return convertView;
		});

		mCalendarDateView.setOnItemClickListener((view, position, bean) -> {
			txtMonth.setText(getMonthString(bean.moth));
			txtYear.setText(String.valueOf(bean.year));
			if (lastDateSelected != null) {
				lastDateSelected.setTextColor(getColor(R.color.calendarBlack));
			}
			lastDateSelected = view.findViewById(R.id.txtDay);
			lastDateSelected.setTextColor(getColor(R.color.calendarOnSelect));
		});



		txtMonth.setText(getMonthString(Date.getToday().getMonth()));
		txtYear.setText(String.valueOf(Date.getToday().getYear()));


		btnAddEvent.setOnClickListener(_v -> {

			View viewAddEventPopup = getLayoutInflater().inflate(R.layout.calendar_add_event_bottom, null);
			TimePicker timePicker = viewAddEventPopup.findViewById(R.id.tpEventInsert);
			DatePicker datePicker = viewAddEventPopup.findViewById(R.id.dpCalendarEventInsert);
			ImageButton btnConfirm = viewAddEventPopup.findViewById(R.id.imgbtnCalendarSave);
			EditText etBody = viewAddEventPopup.findViewById(R.id.etCalendarBody);
			Switch swAlarm = viewAddEventPopup.findViewById(R.id.swCalendarAlarm);
			BottomSheetDialog dialog = new BottomSheetDialog(this);
			Window w = dialog.getWindow();

			if (w != null) {
				w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				/*w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				w.requestFeature(Window.FEATURE_NO_TITLE);*/
			}

			dialog.setContentView(viewAddEventPopup);
			timePicker.setIs24HourView(true);
			initPopupColorPick(viewAddEventPopup);
			initCategoryButton(viewAddEventPopup);
			etBody.setOnFocusChangeListener((view, hasFocus) ->
					Utils.onFocusChange(hasFocus, CalendarActivity.this, etBody, R.string.etCalendarAddEventHint, false));
			btnConfirm.setOnClickListener( l -> {
				EventsType et = new EventsType(datePicker, timePicker, colorSelected, categorySelectedText,
						etBody.getText().toString(), swAlarm.isChecked());
				HomeActivity.dbHelper.insertEvent(et);
				//planedEvents.add(et);
				if (et.getDayBody().moreThanOrEqual(Date.getToday())) {
					planedEvents.add(et);
					updateEventsDashboard(true);
					mCalendarDateView.updateView();
				}
				//Log.d(TAG, "init: Insert successful");
				btnColorSelected = null;
				colorSelected = 0;
				dialog.dismiss();
			});
			dialog.show();
		});
	}

	void updateEventsDashboard(boolean requestUpdateAdapter) {
		if (!requestUpdateAdapter)
			planedEvents = HomeActivity.dbHelper.getCurrentAndFeatureEvent();

		todayEvent.clear();
		featureEvent.clear();
		eventDashboardTypes.clear();

		planedEvents.forEach(eventsType -> {
			if (eventsType.equals(Date.getToday())) {
				todayEvent.add(eventsType);
			} else {
				featureEvent.add(eventsType);
			}
		});
		if (todayEvent.size() != 0) {
			eventDashboardTypes.add(new EventDashboardType(true, todayEvent));
		}
		if (featureEvent.size() != 0) {
			eventDashboardTypes.add(new EventDashboardType(false, featureEvent));
		}

		if (requestUpdateAdapter)
			eventDashboardAdapter.notifyDataSetChanged();
	}

	void initCategoryButton(View viewAddEventPopup) {
		Button btnEvent, btnNote, btnSymptom, btnWater;
		btnEvent = viewAddEventPopup.findViewById(R.id.btnCalendarEvent);
		btnNote = viewAddEventPopup.findViewById(R.id.btnCalendarNote);
		btnSymptom = viewAddEventPopup.findViewById(R.id.btnCalendarSymptom);
		btnWater = viewAddEventPopup.findViewById(R.id.btnCalendarWater);
		categorySelectedText = getString(R.string.categoryEvent);
		btnEvent.setOnClickListener(v ->
				categoryOnClick(btnEvent, getString(R.string.categoryEvent)));
		btnNote.setOnClickListener(v ->
				categoryOnClick(btnNote, getString(R.string.categoryNote)));
		btnSymptom.setOnClickListener(v ->
				categoryOnClick(btnSymptom, getString(R.string.categorySymptom)));
		btnWater.setOnClickListener(v ->
				categoryOnClick(btnWater, getString(R.string.categoryWater)));
	}

	void categoryOnClick(Button btn, String category) {
		Log.v(TAG, "categoryOnClick => " + category);
		// do something
		categorySelectedText = category;
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
		((GradientDrawable) v.getBackground()).setStroke(5, getColor(color));
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

	@SuppressLint("DefaultLocale")
	@ColorRes
	private int getColorRes(int id) {
		return getResources().getIdentifier(getString(R.string.fmt_event_color, id), "color", getPackageName());
	}

	private void initColorPick(ImageButton imgbtn, int color) {
		changeStrokeColor(imgbtn, getColorRes(color));
		// Make sure circle is hollow
		changeClickColor(imgbtn, android.R.color.transparent);
		imgbtn.setOnClickListener(v ->
				colorOnClickListener(imgbtn, color));
	}

	private
	String getMonthString(int num) {
		if (BuildConfig.DEBUG && !(num > 0 && num < 13)) {
			Log.e(TAG, "getMonthString: assert error occurred. num => " + num);
			throw new AssertionError();
		}
		return getResources().getStringArray(R.array.month)[num - 1];
	}

}
