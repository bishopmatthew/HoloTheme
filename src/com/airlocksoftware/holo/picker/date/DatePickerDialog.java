package com.airlocksoftware.holo.picker.date;

import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.dialog.HoloDialog;

/** A HoloDialog that holds a DatePicker.**/
public class DatePickerDialog extends HoloDialog {
	
	private Date mDate;
	DatePicker mDatePicker;
//	NumberPicker mDayPicker;
//	NumberPicker mMonthPicker;
//	NumberPicker mYearPicker;
	LinearLayout mPickerContainer;
	
	private OnDateChangedListener mDateListener;
	
	private static final int DEFAULT_LAYOUT = R.layout.dialog_holo;

	public DatePickerDialog(Context context, OnDateChangedListener onDateChangedListener, int year, int month, int day) {
		super(context, DEFAULT_LAYOUT);
		
		this.mDateListener = onDateChangedListener;
		
		mDate = new Date();
		mDate.setDate(day);
		mDate.setMonth(month);
		mDate.setYear(year);
		
		inflateLayout();
	}
	
	private void inflateLayout() {
		mDatePicker = (DatePicker) LayoutInflater.from(mContext).inflate(R.layout.dialog_date_picker, null);
		
		setContentView(mDatePicker);
		setTitle(null);

		setButtonLayoutResId(R.layout.btn_dialog);
		addButton(mContext.getString(android.R.string.cancel), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		addButton(mContext.getString(android.R.string.ok), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mDateListener != null) {
					int year = mDatePicker.getYear();
					int month = mDatePicker.getMonth();
					int day = mDatePicker.getDayOfMonth();
					mDateListener.onDateChanged(mDatePicker, year, month, day);
				}
				dismiss();
			}
		});
	}

	public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *            with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
        void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

}
