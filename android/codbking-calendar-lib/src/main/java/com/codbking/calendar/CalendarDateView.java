package com.codbking.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.codbking.calendar.CalendarFactory.getMonthOfDayList;

/**
 * Created by codbking on 2016/12/18.
 * email:codbking@gmail.com
 * github:https://github.com/codbking
 * blog:http://www.jianshu.com/users/49d47538a2dd/latest_articles
 */

public class CalendarDateView extends ViewPager implements CalendarTopView {

	HashMap<Integer, CalendarView> views = new HashMap<>();
	private CalendarTopViewChangeListener mCalendarLayoutChangeListener;
	private CalendarView.OnItemClickListener onItemClickListener;

	private LinkedList<CalendarView> cache = new LinkedList();

	private int MAXCOUNT = 6;

	private static final String TAG = "CalendarDateView";

	private int row = 6;

	private CalendarAdapter mAdapter;
	private int calendarItemHeight = 0;

	public void setAdapter(CalendarAdapter adapter) {
		mAdapter = adapter;
		initData();
	}

	public void setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public CalendarDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateView);
		row = a.getInteger(R.styleable.CalendarDateView_cbd_calendar_row, 6);
		a.recycle();
		setPadding(94, 0, 94, 0);
		init();
	}

	public void updateView() {
        /*Log.d(TAG, "updateView: Will update view now.");
        init();*/
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int calendarHeight = 0;
		if (getAdapter() != null) {
			CalendarView view = (CalendarView) getChildAt(0);
			if (view != null) {
				calendarHeight = view.getMeasuredHeight();
				calendarItemHeight = view.getItemHeight();
			}
		}
		setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
	}

	private void init() {
		final int[] dateArr = CalendarUtil.getYMD(new Date());

		setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return Integer.MAX_VALUE;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, final int position) {

				CalendarView view;

				if (!cache.isEmpty()) {
					view = cache.removeFirst();
				} else {
					view = new CalendarView(container.getContext(), row);
				}

				view.setOnItemClickListener(onItemClickListener);
				view.setAdapter(mAdapter);

				view.setData(getMonthOfDayList(dateArr[0], dateArr[1] + position - Integer.MAX_VALUE / 2), position == Integer.MAX_VALUE / 2);
				container.addView(view);
				views.put(position, view);

				return view;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
				cache.addLast((CalendarView) object);
				views.remove(position);
			}
		});

		addOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);

				if (onItemClickListener != null) {
					CalendarView view = views.get(position);
					Object[] obs = view.getSelect();
					onItemClickListener.onItemClick((View) obs[0], (int) obs[1], (CalendarBean) obs[2]);
				}

				mCalendarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
			}
		});
	}


	private void initData() {
		setCurrentItem(Integer.MAX_VALUE / 2, false);
		getAdapter().notifyDataSetChanged();
	}

	@Override
	public int[] getCurrentSelectPosition() {
		CalendarView view = views.get(getCurrentItem());
		if (view == null) {
			view = (CalendarView) getChildAt(0);
		}
		if (view != null) {
			return view.getSelectPosition();
		}
		return new int[4];
	}

	@Override
	public int getItemHeight() {
		return calendarItemHeight;
	}

	@Override
	public void setCalendarTopViewChangeListener(CalendarTopViewChangeListener listener) {
		mCalendarLayoutChangeListener = listener;
	}


}
