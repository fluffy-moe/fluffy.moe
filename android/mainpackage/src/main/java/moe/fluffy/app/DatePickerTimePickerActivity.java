package moe.fluffy.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Copied from https://www.dev2qa.com/android-datepicker-timepicker-example/
 */

public class DatePickerTimePickerActivity extends AppCompatActivity {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int seconds;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dap);

		// Get current calendar date and time.
		Calendar currCalendar = Calendar.getInstance();

		// Set the timezone which you want to display time.
		currCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

		year = currCalendar.get(Calendar.YEAR);
		month = currCalendar.get(Calendar.MONTH);
		day = currCalendar.get(Calendar.DAY_OF_MONTH);
		hour = currCalendar.get(Calendar.HOUR_OF_DAY);
		minute = currCalendar.get(Calendar.MINUTE);
		seconds = currCalendar.get(Calendar.SECOND);

		showUserSelectDateTime();

		// Get date picker object.
		DatePicker datePicker = findViewById(R.id.datePickerExample);
		datePicker.init(year - 1, month  + 1, day + 5, new DatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
				DatePickerTimePickerActivity.this.year = year;
				DatePickerTimePickerActivity.this.month = month;
				DatePickerTimePickerActivity.this.day = day;

				showUserSelectDateTime();
			}
		});

		// Get time picker object.
		TimePicker timePicker = findViewById(R.id.timePickerExample);
		timePicker.setHour(this.hour);
		timePicker.setMinute(this.minute);

		timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
				DatePickerTimePickerActivity.this.hour = hour;
				DatePickerTimePickerActivity.this.minute = minute;

				showUserSelectDateTime();
			}
		});

	}

	/* Show user selected date time in bottom text vew area. */
	private void showUserSelectDateTime()
	{
		// Get TextView object which is used to show user pick date and time.
		TextView textView = findViewById(R.id.textViewShowDateTime);

		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("You selected date time : ");
		strBuffer.append(this.year);
		strBuffer.append("-");
		strBuffer.append(this.month+1);
		strBuffer.append("-");
		strBuffer.append(this.day);
		strBuffer.append(" ");
		strBuffer.append(this.hour);
		strBuffer.append(":");
		strBuffer.append(this.minute);
		strBuffer.append(":");
		strBuffer.append(this.seconds);

		textView.setText(strBuffer.toString());
		textView.setTextColor(Color.BLUE);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(20);
	}
}