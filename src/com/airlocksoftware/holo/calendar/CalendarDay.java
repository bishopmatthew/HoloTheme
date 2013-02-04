package com.airlocksoftware.holo.calendar;

import java.util.Calendar;
import java.util.Date;

/** Basic model for a day in the MonthView. **/
public class CalendarDay {
	
	public Date mDate;
	private boolean mIsEmpty = true;
	private boolean mIsCurrentMonth = true;
	
	public CalendarDay(Date date) {
		mDate = date;
	}
	
	/** Returns Calendar.get(DAY_OF_MONTH) (1-based index) **/
	public int getIntegerDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(mDate);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/** Override this if you want to do some kind of checks on fields, etc**/
	public boolean getIsEmpty() {
		return mIsEmpty;
	}
	
	public void setIsEmpty(boolean isEmpty) {
		mIsEmpty = isEmpty;
	}

	public boolean isCurrentMonth() {
		return mIsCurrentMonth;
	}

	public void setIsCurrentMonth(boolean isCurrentMonth) {
		this.mIsCurrentMonth = isCurrentMonth;
	}
	
}
