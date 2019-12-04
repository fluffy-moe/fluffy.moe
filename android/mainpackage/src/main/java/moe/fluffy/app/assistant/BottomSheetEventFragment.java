package moe.fluffy.app.assistant;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import moe.fluffy.app.R;

public class BottomSheetEventFragment extends Fragment{
	private View mView;
	private TimePickerView pvTime;
	private FrameLayout mFrameLayout;

	public int _year, _month, _day, _hour, _minute;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//mView = inflater.inflate(R.layout.fragment_container, null);
		return null;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public BottomSheetEventFragment setView(View v) {
		mView = v;
		return this;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFrameLayout = mView.findViewById(R.id.viewCalendarPick);
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
