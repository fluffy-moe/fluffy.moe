package moe.fluffy.app.assistant;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;

import moe.fluffy.app.R;

public class TimePickerFragment extends Fragment {
	private final String TAG = "log_timepickerfragment";
	private View mView;
	private TimePickerView pvTime;
	private FrameLayout mFrameLayout;


	public int _year, _month, _day, _hour, _minute;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: ");
		//return super.onCreateView(inflater, container, savedInstanceState);
		return mView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated: on activity created");
		initTimePicker();
	}


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
				.setLayoutRes(R.layout.pickerview_time, null)
				.setType(new boolean[]{true, true, true, true, true, false})
				.setLabel("", "", "", "", "", "")
				.setDividerColor(Color.TRANSPARENT)
				.setContentTextSize(20)
				.setDate(selectedDate)
				//.setRangDate(startDate, endDate)
				.setItemVisibleCount(5)
				.setCycle(true)
				.setDecorView(mFrameLayout)
				.setOutSideColor(0x00000000)
				.setOutSideCancelable(false)
				.build();

		pvTime.setKeyBackCancelable(false);
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

}
