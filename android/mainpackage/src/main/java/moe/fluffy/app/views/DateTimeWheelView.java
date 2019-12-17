package moe.fluffy.app.views;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.bigkoo.pickerview.listener.ISelectTimeCallback;
import com.contrarywind.view.WheelView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.Datetime;

@SuppressWarnings({"SimpleDateFormat", "WeakerAccess"})
public class DateTimeWheelView {
	private static final String TAG = "log_DateTimeWheelView";
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_minutes;
	//private WheelView wv_seconds;
	private int gravity;

	//private boolean[] type;
	private static final int DEFAULT_START_YEAR = 1900;
	private static final int DEFAULT_END_YEAR = 2100;
	private static final int DEFAULT_START_MONTH = 1;
	private static final int DEFAULT_END_MONTH = 12;
	private static final int DEFAULT_START_DAY = 1;
	private static final int DEFAULT_END_DAY = 31;

	private int startYear = DEFAULT_START_YEAR;
	private int endYear = DEFAULT_END_YEAR;
	private int startMonth = DEFAULT_START_MONTH;
	private int endMonth = DEFAULT_END_MONTH;
	private int startDay = DEFAULT_START_DAY;
	private int endDay = DEFAULT_END_DAY;
	private int currentYear;

	private int textSize;

	//private boolean isLunarCalendar = false;
	private ISelectTimeCallback mSelectChangeCallback;

	public DateTimeWheelView(View view, int textSize) {
		Log.d(TAG, "DateTimeWheelView: view");
		this.view = view;
		//this.type = type;
		this.gravity = Gravity.CENTER;
		this.textSize = textSize;
	}


	public void setPicker(int year, int month, int day) {
		this.setPicker(year, month, day, 0, 0);
	}

	public void setPicker(int year, final int month, int day, int h, int m) {
		setSolar(year, month, day, h, m);
	}

	public void setPicker(@NotNull Calendar calendar) {
		setSolar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE));
	}

	public void setPicker(@NotNull Date d){
		setSolar(d.getYear(), d.getMonth(), d.getDay(), 0, 0);
	}

	public void setPicker(@NotNull Datetime d) {
		setSolar(d.getYear(), d.getMonth(), d.getDay(), d.getHour(), d.getMinute());
	}

	/**
	 * 设置公历
	 */
	private void setSolar(int year, final int month, int day, int h, int m) {
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
		String[] months_little = {"4", "6", "9", "11"};

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		currentYear = year;
		// 年
		wv_year = view.findViewById(com.bigkoo.pickerview.R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据


		wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据
		wv_year.setGravity(gravity);
		// 月
		wv_month = view.findViewById(com.bigkoo.pickerview.R.id.month);
		if (startYear == endYear) {//开始年等于终止年
			wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
			wv_month.setCurrentItem(month + 1 - startMonth);
		} else if (year == startYear) {
			//起始日期的月份控制
			wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
			wv_month.setCurrentItem(month + 1 - startMonth);
		} else if (year == endYear) {
			//终止日期的月份控制
			wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
			wv_month.setCurrentItem(month);
		} else {
			wv_month.setAdapter(new NumericWheelAdapter(1, 12));
			wv_month.setCurrentItem(month);
		}
		wv_month.setGravity(gravity);
		// 日
		wv_day = view.findViewById(com.bigkoo.pickerview.R.id.day);

		boolean leapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
		if (startYear == endYear && startMonth == endMonth) {
			if (list_big.contains(String.valueOf(month + 1))) {
				if (endDay > 31) {
					endDay = 31;
				}
				wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				if (endDay > 30) {
					endDay = 30;
				}
				wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
			} else {
				// 闰年
				if (leapYear) {
					if (endDay > 29) {
						endDay = 29;
					}
					wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
				} else {
					if (endDay > 28) {
						endDay = 28;
					}
					wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
				}
			}
			wv_day.setCurrentItem(day - startDay);
		} else if (year == startYear && month + 1 == startMonth) {
			// 起始日期的天数控制
			if (list_big.contains(String.valueOf(month + 1))) {

				wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
			} else if (list_little.contains(String.valueOf(month + 1))) {

				wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
			} else {
				// 闰年 29，平年 28
				wv_day.setAdapter(new NumericWheelAdapter(startDay, leapYear ? 29 : 28));
			}
			wv_day.setCurrentItem(day - startDay);
		} else if (year == endYear && month + 1 == endMonth) {
			// 终止日期的天数控制
			if (list_big.contains(String.valueOf(month + 1))) {
				if (endDay > 31) {
					endDay = 31;
				}
				wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				if (endDay > 30) {
					endDay = 30;
				}
				wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
			} else {
				// 闰年
				if (leapYear) {
					if (endDay > 29) {
						endDay = 29;
					}
					wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
				} else {
					if (endDay > 28) {
						endDay = 28;
					}
					wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
				}
			}
			wv_day.setCurrentItem(day - 1);
		} else {
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				// 闰年 29，平年 28
				wv_day.setAdapter(new NumericWheelAdapter(startDay, leapYear ? 29 : 28));
			}
			wv_day.setCurrentItem(day - 1);
		}

		wv_day.setGravity(gravity);
		//时
		wv_hours = view.findViewById(com.bigkoo.pickerview.R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));

		wv_hours.setCurrentItem(h);
		wv_hours.setGravity(gravity);
		//分
		wv_minutes = view.findViewById(com.bigkoo.pickerview.R.id.min);
		wv_minutes.setAdapter(new NumericWheelAdapter(0, 59));

		wv_minutes.setCurrentItem(m);
		wv_minutes.setGravity(gravity);

		// 添加"年"监听
		wv_year.setOnItemSelectedListener(index -> {
			int year_num = index + startYear;
			currentYear = year_num;
			int currentMonthItem = wv_month.getCurrentItem();//记录上一次的item位置
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (startYear == endYear) {
				//重新设置月份
				wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));

				if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
					currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
					wv_month.setCurrentItem(currentMonthItem);
				}

				int monthNum = currentMonthItem + startMonth;

				if (startMonth == endMonth) {
					//重新设置日
					setReDay(year_num, monthNum, startDay, endDay, list_big, list_little);
				} else if (monthNum == startMonth) {
					//重新设置日
					setReDay(year_num, monthNum, startDay, 31, list_big, list_little);
				} else if (monthNum == endMonth) {
					setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
				} else {//重新设置日
					setReDay(year_num, monthNum, 1, 31, list_big, list_little);
				}
			} else if (year_num == startYear) {//等于开始的年
				//重新设置月份
				wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));

				if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
					currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
					wv_month.setCurrentItem(currentMonthItem);
				}

				int month1 = currentMonthItem + startMonth;
				if (month1 == startMonth) {
					//重新设置日
					setReDay(year_num, month1, startDay, 31, list_big, list_little);
				} else {
					//重新设置日
					setReDay(year_num, month1, 1, 31, list_big, list_little);
				}

			} else if (year_num == endYear) {
				//重新设置月份
				wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
				if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
					currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
					wv_month.setCurrentItem(currentMonthItem);
				}
				int monthNum = currentMonthItem + 1;

				if (monthNum == endMonth) {
					//重新设置日
					setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
				} else {
					//重新设置日
					setReDay(year_num, monthNum, 1, 31, list_big, list_little);
				}

			} else {
				//重新设置月份
				wv_month.setAdapter(new NumericWheelAdapter(1, 12));
				//重新设置日
				setReDay(year_num, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
			}

			if (mSelectChangeCallback != null) {
				mSelectChangeCallback.onTimeSelectChanged();
			}
		});


		// 添加"月"监听
		wv_month.setOnItemSelectedListener(index -> {
			int month_num = index + 1;

			if (startYear == endYear) {
				month_num = month_num + startMonth - 1;
				if (startMonth == endMonth) {
					//重新设置日
					setReDay(currentYear, month_num, startDay, endDay, list_big, list_little);
				} else if (startMonth == month_num) {

					//重新设置日
					setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
				} else if (endMonth == month_num) {
					setReDay(currentYear, month_num, 1, endDay, list_big, list_little);
				} else {
					setReDay(currentYear, month_num, 1, 31, list_big, list_little);
				}
			} else if (currentYear == startYear) {
				month_num = month_num + startMonth - 1;
				if (month_num == startMonth) {
					//重新设置日
					setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
				} else {
					//重新设置日
					setReDay(currentYear, month_num, 1, 31, list_big, list_little);
				}

			} else if (currentYear == endYear) {
				if (month_num == endMonth) {
					//重新设置日
					setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, endDay, list_big, list_little);
				} else {
					setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
				}

			} else {
				//重新设置日
				setReDay(currentYear, month_num, 1, 31, list_big, list_little);
			}

			if (mSelectChangeCallback != null) {
				mSelectChangeCallback.onTimeSelectChanged();
			}
		});

		setChangedListener(wv_day);
		setChangedListener(wv_hours);
		setChangedListener(wv_minutes);
		setContentTextSize();
	}

	private void setChangedListener(WheelView wheelView) {
		if (mSelectChangeCallback != null) {
			wheelView.setOnItemSelectedListener(index -> mSelectChangeCallback.onTimeSelectChanged());
		}

	}


	private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
		int currentItem = wv_day.getCurrentItem();

		if (list_big.contains(String.valueOf(monthNum))) {
			if (endD > 31) {
				endD = 31;
			}
			wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
		} else if (list_little.contains(String.valueOf(monthNum))) {
			if (endD > 30) {
				endD = 30;
			}
			wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
//        maxItem = endD;
		} else {
			if ((year_num % 4 == 0 && year_num % 100 != 0)
					|| year_num % 400 == 0) {
				if (endD > 29) {
					endD = 29;
				}
				wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
//            maxItem = endD;
			} else {
				if (endD > 28) {
					endD = 28;
				}
				wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
//            maxItem = endD;
			}
		}

		if (currentItem > wv_day.getAdapter().getItemsCount() - 1) {
			currentItem = wv_day.getAdapter().getItemsCount() - 1;
			wv_day.setCurrentItem(currentItem);
		}
	}


	private void setContentTextSize() {
		wv_day.setTextSize(textSize);
		wv_month.setTextSize(textSize);
		wv_year.setTextSize(textSize);
		wv_hours.setTextSize(textSize);
		wv_minutes.setTextSize(textSize);
		//wv_seconds.setTextSize(textSize);
	}

	public void setTextXOffset(int x_offset_year, int x_offset_month, int x_offset_day,
							   int x_offset_hours, int x_offset_minutes, int x_offset_seconds) {
		wv_year.setTextXOffset(x_offset_year);
		wv_month.setTextXOffset(x_offset_month);
		wv_day.setTextXOffset(x_offset_day);
		wv_hours.setTextXOffset(x_offset_hours);
		wv_minutes.setTextXOffset(x_offset_minutes);
		//wv_seconds.setTextXOffset(x_offset_seconds);
	}

	/**
	 * 设置是否循环滚动
	 */
	public void setCyclic(boolean cyclic) {
		wv_year.setCyclic(cyclic);
		wv_month.setCyclic(cyclic);
		wv_day.setCyclic(cyclic);
		wv_hours.setCyclic(cyclic);
		wv_minutes.setCyclic(cyclic);
		//wv_seconds.setCyclic(cyclic);
	}

	public String getTime() {
		StringBuilder sb = new StringBuilder();
		if (currentYear == startYear) {
			if ((wv_month.getCurrentItem() + startMonth) == startMonth) {
				sb.append(getYear()).append("-")
						.append((wv_month.getCurrentItem() + startMonth)).append("-")
						.append((wv_day.getCurrentItem() + startDay)).append(" ")
						.append(wv_hours.getCurrentItem()).append(":")
						.append(wv_minutes.getCurrentItem()).append(":");
				//.append(wv_seconds.getCurrentItem());
			} else {
				sb.append(getYear()).append("-")
						.append((wv_month.getCurrentItem() + startMonth)).append("-")
						.append((wv_day.getCurrentItem() + 1)).append(" ")
						.append(wv_hours.getCurrentItem()).append(":")
						.append(wv_minutes.getCurrentItem()).append(":");
				//.append(wv_seconds.getCurrentItem());
			}

		} else {
			sb.append(getYear()).append("-")
					.append((wv_month.getCurrentItem() + 1)).append("-")
					.append((wv_day.getCurrentItem() + 1)).append(" ")
					.append(wv_hours.getCurrentItem()).append(":")
					.append(wv_minutes.getCurrentItem()).append(":");
			//.append(wv_seconds.getCurrentItem());
		}

		return sb.toString();
	}

	public int getYear() {
		return wv_year.getCurrentItem() + startYear;
	}

	public int getMonth() {
		return wv_month.getCurrentItem() + ((wv_month.getCurrentItem() + startMonth) == startMonth? startMonth : 1);
	}

	public int getDay() {
		return wv_day.getCurrentItem() + ((currentYear == startYear && (wv_month.getCurrentItem() + startMonth) == startMonth )?startDay: 1);
	}

	public int getHour() {
		return wv_hours.getCurrentItem();
	}

	public int getMinute() {
		return wv_minutes.getCurrentItem();
	}
	public View getView() {
		return view;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}


	/**
	 * 设置间距倍数,但是只能在1.0-4.0f之间
	 *
	 */
	public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
		wv_day.setLineSpacingMultiplier(lineSpacingMultiplier);
		wv_month.setLineSpacingMultiplier(lineSpacingMultiplier);
		wv_year.setLineSpacingMultiplier(lineSpacingMultiplier);
		wv_hours.setLineSpacingMultiplier(lineSpacingMultiplier);
		wv_minutes.setLineSpacingMultiplier(lineSpacingMultiplier);
		//wv_seconds.setLineSpacingMultiplier(lineSpacingMultiplier);
	}

	/**
	 * 设置分割线的颜色
	 *
	 */
	public void setDividerColor(int dividerColor) {
		wv_day.setDividerColor(dividerColor);
		wv_month.setDividerColor(dividerColor);
		wv_year.setDividerColor(dividerColor);
		wv_hours.setDividerColor(dividerColor);
		wv_minutes.setDividerColor(dividerColor);
		//wv_seconds.setDividerColor(dividerColor);
	}

	/**
	 * 设置分割线的类型
	 *
	 */
	public void setDividerType(WheelView.DividerType dividerType) {
		wv_day.setDividerType(dividerType);
		wv_month.setDividerType(dividerType);
		wv_year.setDividerType(dividerType);
		wv_hours.setDividerType(dividerType);
		wv_minutes.setDividerType(dividerType);
		//wv_seconds.setDividerType(dividerType);
	}

	/**
	 * 设置分割线之间的文字的颜色
	 *
	 */
	public void setTextColorCenter(int textColorCenter) {
		wv_day.setTextColorCenter(textColorCenter);
		wv_month.setTextColorCenter(textColorCenter);
		wv_year.setTextColorCenter(textColorCenter);
		wv_hours.setTextColorCenter(textColorCenter);
		wv_minutes.setTextColorCenter(textColorCenter);
		//wv_seconds.setTextColorCenter(textColorCenter);
	}

	/**
	 * 设置分割线以外文字的颜色
	 *
	 */
	public void setTextColorOut(int textColorOut) {
		wv_day.setTextColorOut(textColorOut);
		wv_month.setTextColorOut(textColorOut);
		wv_year.setTextColorOut(textColorOut);
		wv_hours.setTextColorOut(textColorOut);
		wv_minutes.setTextColorOut(textColorOut);
		//wv_seconds.setTextColorOut(textColorOut);
	}

	/**
	 * @param isCenterLabel 是否只显示中间选中项的
	 */
	public void isCenterLabel(boolean isCenterLabel) {
		wv_day.isCenterLabel(isCenterLabel);
		wv_month.isCenterLabel(isCenterLabel);
		wv_year.isCenterLabel(isCenterLabel);
		wv_hours.isCenterLabel(isCenterLabel);
		wv_minutes.isCenterLabel(isCenterLabel);
		//wv_seconds.isCenterLabel(isCenterLabel);
	}

	public void setSelectChangeCallback(ISelectTimeCallback mSelectChangeCallback) {
		this.mSelectChangeCallback = mSelectChangeCallback;
	}

	public void setItemsVisible(int itemsVisibleCount) {
		wv_day.setItemsVisibleCount(itemsVisibleCount);
		wv_month.setItemsVisibleCount(itemsVisibleCount);
		wv_year.setItemsVisibleCount(itemsVisibleCount);
		wv_hours.setItemsVisibleCount(itemsVisibleCount);
		wv_minutes.setItemsVisibleCount(itemsVisibleCount);
		//wv_seconds.setItemsVisibleCount(itemsVisibleCount);
	}
}
