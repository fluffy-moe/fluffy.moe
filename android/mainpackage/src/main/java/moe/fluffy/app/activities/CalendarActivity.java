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
package moe.fluffy.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import moe.fluffy.app.BuildConfig;
import moe.fluffy.app.R;
import moe.fluffy.app.adapter.EventDashboardAdapter;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.fragment.AddEventFragment;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.Datetime;
import moe.fluffy.app.types.EventDashboardItem;
import moe.fluffy.app.types.EventsItem;
import moe.fluffy.app.types.SerializableBundle;

import static moe.fluffy.app.assistant.Utils.px;

public class CalendarActivity extends AppCompatActivity {

	private static final String TAG = "log_CalendarViewActivity";

	CalendarDateView mCalendarDateView;
	ListView lvEventDashboard;
	TextView txtMonth;
	TextView txtYear;
	TextView lastDateSelected = null;

	//Button btnAddEvent;
	ImageButton btnAddEvent;

	Datetime selected_date;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

	private static ArrayList<EventsItem> planedEvents;
	ArrayList<EventsItem> todayEvent = new ArrayList<>(), featureEvent = new ArrayList<>();
	ArrayList<EventDashboardItem> eventDashboardItems = new ArrayList<>();
	EventDashboardAdapter eventDashboardAdapter;

	private static class click {
		private static Date date;
		click() {
			updateTimestamp(null);
		}

		static void updateTimestamp(CalendarBean bean) {
			if (bean != null)
				date = new Date(bean);
			else {
				date = Date.getToday();
			}
			//lastTimestamp = new java.util.Date().getTime();
		}

		static boolean checkClick(CalendarBean bean) {
			return date != null && date.equals(bean);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_real_calendar);
		findView();
		init();
	}

	void findView() {
		mCalendarDateView = findViewById(R.id.calendarDateView);
		lvEventDashboard = findViewById(R.id.listEvents);
		txtMonth = findViewById(R.id.txtCalendarTitle);
		txtYear = findViewById(R.id.txtYearNumber);
		btnAddEvent = findViewById(R.id.imgbtnAddEvent);
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
				startActivity(new Intent(CalendarActivity.this, BootstrapScannerActivity.class)));

		imgbtnNavBarArticle.setOnClickListener(v ->
				startActivity(new Intent(CalendarActivity.this, ArticleActivity.class)));

		imgbtnNavBarMedical.setOnClickListener(v ->
				startActivity(new Intent(CalendarActivity.this, MedicalActivity.class)));

		imgbtnNavBarUser.setOnClickListener(v ->
				startActivity(new Intent(CalendarActivity.this, ProfileActivity.class)));
	}


	void init() {

		eventDashboardAdapter = new EventDashboardAdapter(this, eventDashboardItems);
		updateEventsDashboard(false);
		lvEventDashboard.setAdapter(eventDashboardAdapter);

		mCalendarDateView.setAdapter(//Log.d(TAG, "init: color_id => " + color_id);
				this::getCalendarView);

		selected_date = Datetime.getToday();

		/* Calendar onClick */
		mCalendarDateView.setOnItemClickListener((view, position, bean) -> {
			txtMonth.setText(getMonthString(bean.moth));
			txtYear.setText(String.valueOf(bean.year));
			if (lastDateSelected != null) {
				lastDateSelected.setTextColor(getColor(R.color.calendarBlack));
			}
			lastDateSelected = view.findViewById(R.id.txtDay);
			lastDateSelected.setTextColor(getColor(R.color.calendarOnSelect));

			if (click.checkClick(bean)) {
				Intent intent = new Intent(this, DayActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(DayActivity.keyName, new SerializableBundle(bean));
				intent.putExtras(bundle);
				startActivity(intent);
			}
			click.updateTimestamp(bean);
			selected_date = new Datetime(bean);
		});

		txtMonth.setText(getMonthString(Date.getToday().getMonth()));
		txtYear.setText(String.valueOf(Date.getToday().getYear()));

		/* addEventButton onClick */
		btnAddEvent.setOnClickListener(_v -> {
			BottomSheetDialogFragment eventFragment = new AddEventFragment(selected_date);
			Bundle bundle = new Bundle();
			SerializableBundle bundle1 = new SerializableBundle(o -> {
				EventsItem et = (EventsItem) o;
				planedEvents.add(et);
				updateEventsDashboard(true);
				mCalendarDateView.setAdapter(this::getCalendarView);
				mCalendarDateView.updateView();
			});
			bundle.putSerializable(AddEventFragment.keyName, bundle1);
			eventFragment.setArguments(bundle);
			eventFragment.show(getSupportFragmentManager(), eventFragment.getTag());
		});
	}

	void updateEventsDashboard(boolean requestUpdateAdapter) {
		if (!requestUpdateAdapter)
			planedEvents = DatabaseHelper.getInstance().getCurrentAndFeatureEvent();

		todayEvent.clear();
		featureEvent.clear();
		eventDashboardItems.clear();

		planedEvents.forEach(eventsType -> {
			if (eventsType.equals(Date.getToday())) {
				todayEvent.add(eventsType);
			} else {
				featureEvent.add(eventsType);
			}
		});
		if (todayEvent.size() != 0) {
			eventDashboardItems.add(new EventDashboardItem(true, todayEvent));
		}
		if (featureEvent.size() != 0) {
			eventDashboardItems.add(new EventDashboardItem(false, featureEvent));
		}

		if (requestUpdateAdapter)
			eventDashboardAdapter.notifyDataSetChanged();
	}

	private
	String getMonthString(int num) {
		if (BuildConfig.DEBUG && !(num > 0 && num < 13)) {
			Log.e(TAG, "getMonthString: assert error occurred. num => " + num);
			throw new AssertionError();
		}
		return getResources().getStringArray(R.array.month)[num - 1];
	}

	private View getCalendarView(View convertView, ViewGroup parentView, CalendarBean bean) {
		TextView viewDay;
		View underlineView;
		if (convertView == null) {
			convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.calendar_item, null);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(55), px(55));
			convertView.setLayoutParams(params);
		}

		viewDay = convertView.findViewById(R.id.txtDay);
		underlineView = convertView.findViewById(R.id.viewUnderLine);

		viewDay.setText(String.valueOf(bean.day));
/*

		if (Date.getToday().equals(bean)) {
			lastDateSelected = viewDay;
			viewDay.setTextColor(getColor(R.color.calendarOnSelect));
		}
*/

		if (bean.mothFlag != 0) {
			viewDay.setTextColor(getColor(R.color.calendarWeekTitle));
		} else {
			viewDay.setTextColor(getColor(R.color.calendarBlack));
		}


		//Log.d(TAG, "init: color_id => " + color_id);
		underlineView.setBackgroundColor(
				getColor(DatabaseHelper.getInstance().getTodayColorID(bean.year, bean.moth, bean.day)));

		return convertView;
	}
}
