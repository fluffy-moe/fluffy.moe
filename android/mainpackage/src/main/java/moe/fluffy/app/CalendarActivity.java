
package moe.fluffy.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import moe.fluffy.app.types.DateWithMark;
import moe.fluffy.app.types.EventView;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.adapter.EventViewAdapter;

import static moe.fluffy.app.assistant.Utils.px;

public class CalendarActivity extends AppCompatActivity {


	CalendarDateView mCalendarDateView;
	ListView lvEventDashboard;
	TextView txtMonth;
	TextView txtYear;
	TextView lastSelected = null;

	Button btnAddEvent;

	private static String TAG = "log_CalendarViewActivity";

	void find_view() {
		mCalendarDateView = findViewById(R.id.calendarDateView);
		lvEventDashboard = findViewById(R.id.listEvents);
		txtMonth = findViewById(R.id.txtCalendarTitle);
		txtYear = findViewById(R.id.txtYearNumber);
		btnAddEvent = findViewById(R.id.btnAddEvent);
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
		final EventsType[] dm = {
				new EventsType(new DateWithMark("2019/11/21", "00:00:00", R.color.event_c1), "c", "bbb"),
				new EventsType(new DateWithMark("2019/11/31", "00:00:00", R.color.event_c2), "c", "bbb"),
				new EventsType(new DateWithMark("2019/11/15", "00:00:00", R.color.event_c3), "c", "bbb"),
				new EventsType(new DateWithMark("2019/11/17", "00:00:00", R.color.event_c4), "c", "bbb"),
				new EventsType(new DateWithMark("2019/11/16", "00:00:00", R.color.event_c5), "c", "bbb"),
		};
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

			if (BuildConfig.DEBUG)
				if (BuildConfig.enableDatabase)
					underlineView.setBackgroundColor(
							getColor(HomeActivity.dbHelper.getTodayColorID(bean.year, bean.moth, bean.day)));
				else
					// TODO: use another method to improve speed
					for (EventsType d : dm) {
						if (d.equals(bean)) {
							underlineView.setBackgroundResource(d.getColor());
							// marked
							break;
						}
					}

			return convertView;
		});

		mCalendarDateView.setOnItemClickListener((view, position, bean) -> {
			txtMonth.setText(getMonthString(bean.moth));
			txtYear.setText(String.valueOf(bean.year));
			if (lastSelected != null) {
				lastSelected.setTextColor(getColor(R.color.calendarBlack));
			}
			lastSelected = view.findViewById(R.id.txtDay);
			lastSelected.setTextColor(getColor(R.color.calendarOnSelect));
		});

		int[] data = CalendarUtil.getYMD(new Date());
		txtMonth.setText(getMonthString(data[1]));
		txtYear.setText(String.valueOf(data[0]));

		ArrayList<EventView> es = new ArrayList<>();
		ArrayList<EventsType> s = new ArrayList<>(Arrays.asList(dm));
		es.add(new EventView(true, new ArrayList<>(Arrays.asList(dm))));
		s.addAll(Arrays.asList(dm));
		es.add(new EventView(false, s));
		EventViewAdapter ea = new EventViewAdapter(this, es);
		lvEventDashboard.setAdapter(ea);


		btnAddEvent.setOnClickListener(v -> {
			View viewAddEventPopup = getLayoutInflater().inflate(R.layout.calendar_addevent_bottom, null);
			BottomSheetDialog dialog = new BottomSheetDialog(this);
			dialog.setContentView(viewAddEventPopup);
			Window w = dialog.getWindow();
			if (w != null)
				w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			TimePicker timePicker = viewAddEventPopup.findViewById(R.id.timePickerExample);
			timePicker.setIs24HourView(true);
			dialog.show();
		});
	}

	private
	String getMonthString(int num) {
		Log.d(TAG, "getMonthString: num => " + num);
		if (BuildConfig.DEBUG && !(num > 0 && num < 13)) {
			throw new AssertionError();
		}
		return getResources().getStringArray(R.array.month)[num - 1];
	}

}
