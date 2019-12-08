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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import moe.fluffy.app.assistant.BottomSheetEventFragment;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventDashboardType;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.FragmentBundle;
import moe.fluffy.app.types.adapter.EventDashboardAdapter;

import static moe.fluffy.app.assistant.Utils.px;

public class CalendarActivity extends AppCompatActivity {

	private static final String TAG = "log_CalendarViewActivity";

	CalendarDateView mCalendarDateView;
	ListView lvEventDashboard;
	TextView txtMonth;
	TextView txtYear;
	TextView lastDateSelected = null;

	Button btnAddEvent;

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
				startActivity(new Intent(CalendarActivity.this, BootstrapScannerActivity.class)));

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

		mCalendarDateView.setAdapter(//Log.d(TAG, "init: color_id => " + color_id);
				this::getCalendarView);

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
			BottomSheetDialogFragment eventFragment = new BottomSheetEventFragment();
			Bundle bundle = new Bundle();
			FragmentBundle bundle1 = new FragmentBundle(o -> {
				EventsType et = (EventsType) o;
				planedEvents.add(et);
				updateEventsDashboard(true);
				mCalendarDateView.setAdapter(this::getCalendarView);
				mCalendarDateView.updateView();
			});
			bundle.putSerializable(BottomSheetEventFragment.argTag, bundle1);
			eventFragment.setArguments(bundle);
			eventFragment.show(getSupportFragmentManager(), eventFragment.getTag());
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
			convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.event_item_calendar, null);
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
				getColor(HomeActivity.dbHelper.getTodayColorID(bean.year, bean.moth, bean.day)));

		return convertView;
	}
}
