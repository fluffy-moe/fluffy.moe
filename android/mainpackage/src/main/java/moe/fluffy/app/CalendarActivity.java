package moe.fluffy.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;

import java.util.Date;

import moe.fluffy.app.types.DateWithMark;

import static moe.fluffy.app.assistant.Utils.px;

public class CalendarActivity extends AppCompatActivity {


	CalendarDateView mCalendarDateView;
	ListView mList;
	TextView mTitle;
	TextView viewYear;
	TextView lastSelected = null;

	private static String TAG = "log_dingding";

	void find_view() {
		mCalendarDateView = findViewById(R.id.calendarDateView);
		mList = findViewById(R.id.list);
		mTitle = findViewById(R.id.txtCalendarTitle);
		viewYear = findViewById(R.id.txtYearNumber);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dingding);
		find_view();
		final DateWithMark[] dm = {
				new DateWithMark("2019/11/11", R.color.event_c1),
				new DateWithMark("2019/11/13", R.color.event_c2),
				new DateWithMark("2019/11/15", R.color.event_c3),
				new DateWithMark("2019/11/17", R.color.event_c4),
				new DateWithMark("2019/11/16", R.color.event_c5),
		};
		mCalendarDateView.setAdapter((convertView, parentView, bean) -> {
			TextView viewMonth;
			View underlineView;
			if (convertView == null) {
				convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
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


			// TODO: use another method to improve speed
			for (DateWithMark d : dm) {
				if (d.equals(bean)) {
					underlineView.setBackgroundResource(d.getColor());
					// marked
					break;
				}
			}

			return convertView;
		});

		mCalendarDateView.setOnItemClickListener((view, postion, bean) -> {
			mTitle.setText(getMonthString(bean.moth));
			viewYear.setText(String.valueOf(bean.year));
			if (lastSelected != null) {
				lastSelected.setTextColor(getColor(R.color.calendarBlack));
			}
			lastSelected = view.findViewById(R.id.txtDay);
			lastSelected.setTextColor(getColor(R.color.calendarOnSelect));
		});

		int[] data = CalendarUtil.getYMD(new Date());
		mTitle.setText(getMonthString(data[1]));
		viewYear.setText(String.valueOf(data[0]));

		mList.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return 100;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(CalendarActivity.this).inflate(android.R.layout.simple_list_item_1, null);
				}

				TextView textView = (TextView) convertView;
				textView.setText("item" + position);

				return convertView;
			}
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
