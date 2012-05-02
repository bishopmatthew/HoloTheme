package com.airlocksoftware.holo.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.grid.FixedGridAdapter;
import com.airlocksoftware.holo.type.FontText;

public class MonthAdapter extends FixedGridAdapter<CalendarDay> {

	// DATA
	// This is what determines which month is showing on the calendar
	private Date mFirstDayOfMonth;
	private Context mContext;

	// LISTENERS
	private OnDayClickListener mDayClickListener;
	private OnDayLongClickListener mDayLongClickListener;
	private OnMonthChangedListener mMonthChangedListener;

	// CONSTANTS
	public static final int DEFAULT_DAY_LAYOUT = R.layout.month_view_day_view;
	public static final int NUM_WEEKS = 6;
	public static final int NUM_DAYS = 7;

	// CONSTRUCTORS
	public MonthAdapter(Context context) {
		super(context);
		init(context);
	}

	// PRIVATE METHODS
	private void init(Context context) {
		mContext = context;
		mFirstDayOfMonth = getFirstDayOfMonth(Calendar.getInstance());
	}

	// PUBLIC METHODS

	@Override
	/** Override this method if you don't want the default type of view **/
	public View getView(int position, View convertView) {
		FontText view;
		if (convertView == null) {
			view = (FontText) LayoutInflater.from(mContext).inflate(DEFAULT_DAY_LAYOUT, null);
			view.setLayoutParams(getGridLayoutParams());
		} else {
			view = (FontText) convertView;
		}
		
		CalendarDay item = getItem(position);
		view.setText(Integer.toString(item.getIntegerDay()));
		view.setEnabled(!item.getIsEmpty());
		if(!item.isCurrentMonth()) {
			view.setTextColor(mContext.getResources().getColor(R.color.grey_40));
		}
		return view;
	}
	
	@Override
	public int getCount() {
		return NUM_WEEKS * NUM_DAYS;
	}

	@Override
	public boolean isEnabled(int position) {
		CalendarDay item = getItem(position);
		return item != null && !item.getIsEmpty();
	}

	/** All data setting should go through here **/
	public void changeMonth(int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(mFirstDayOfMonth);
		cal.add(Calendar.MONTH, months);
		mFirstDayOfMonth = cal.getTime();
		if (mMonthChangedListener != null) mMonthChangedListener.onMonthChanged(this, getDate());
		notifyDataSetChanged();
	}

	/** Creates a new Date initialized to the first instant of the month **/
	public static Date getFirstDayOfMonth(Calendar cal) {
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	public void setData(ArrayList<CalendarDay> data) {
		// disable change notifications while clearing / filling array
		super.setNotifyOnChange(false);
		super.clear();

		Calendar cal = Calendar.getInstance();
		cal.setTime(mFirstDayOfMonth);
		int offset = cal.get(Calendar.DAY_OF_WEEK) - 1;

		// fill pre-month
		cal.add(Calendar.DAY_OF_YEAR, -offset);
		for (int i = 0; i < offset; i++) {
			CalendarDay prevMonth = new CalendarDay(cal.getTime());
			prevMonth.setIsCurrentMonth(false);
			prevMonth.setIsEmpty(true);
			add(prevMonth);
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}

		// fill month
		for (CalendarDay item : data) {
			int day = item.getIntegerDay();
			while (cal.get(Calendar.DAY_OF_MONTH) < day) {
				CalendarDay empty = new CalendarDay(cal.getTime());
				empty.setIsCurrentMonth(true);
				empty.setIsEmpty(true);
				add(empty);
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			add(item);
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}

		// fill post-month
		int lastRecord = data.get(data.size() - 1).getIntegerDay();
		Calendar lastDayOfMonth = Calendar.getInstance();
		lastDayOfMonth.setTime(mFirstDayOfMonth);
		lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));

		for (int i = 0; i < getCount() - (lastRecord + offset); i++) {
			CalendarDay empty = new CalendarDay(cal.getTime());
			boolean isCurrentMonth = cal.before(lastDayOfMonth);
			
			empty.setIsCurrentMonth(isCurrentMonth);
			empty.setIsEmpty(true);
			add(empty);
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		super.setNotifyOnChange(true);
		notifyDataSetChanged();
	}

	// SET LISTENERS
	public void setOnDayClickListener(OnDayClickListener listener) {
		mDayClickListener = listener;
	}

	public void setOnDayLongClickListener(OnDayLongClickListener listener) {
		mDayLongClickListener = listener;
	}

	/** Load new data here **/
	public void setOnMonthChangedListener(OnMonthChangedListener listener) {
		mMonthChangedListener = listener;
	}

	// INNER CLASSES
	public abstract static class OnDayClickListener {
		public abstract void onDayClick(View clicked, Date date, Object data, int position);
	}

	public abstract static class OnDayLongClickListener {
		public abstract void onDayLongClick(View clicked, Date date, Object data, int position);
	}

	public interface OnMonthChangedListener {
		public abstract void onMonthChanged(MonthAdapter monthAdapter, Date firstDayOfMonth);
	}

	public Date getDate() {
		return (Date) mFirstDayOfMonth.clone();
	}

	public void refreshData() {
		changeMonth(0);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		// TODO?
	}

}
