package com.airlocksoftware.holo.picker.date;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.picker.date.DatePickerDialog.OnDateChangedListener;
import com.airlocksoftware.holo.type.FontText;
import com.airlocksoftware.holo.utils.Utils;

/** Spinner for displaying a date. On click, opens a DatePickerDialog to change the date. **/
public class DateSpinner extends FrameLayout {

	protected Context mContext;
	DatePickerDialog dialog;
	String prompt;

	Date selectedDate;
	FontText selectedDateView;

	public DateSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		setAttributes(attrs);
		setOnClickListener(spinnerListener);
	}

	private void setAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ListSpinner);

		String prompt = a.getString(R.styleable.ListSpinner_spinner_prompt);
		setPrompt(prompt);

		a.recycle();
	}

	public void showDialog() {
		if (dialog == null) {
			Calendar cal = Calendar.getInstance();
			dialog = new DatePickerDialog(mContext, onDateChangedListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH));
		}
		dialog.show();
	}

	public void hideDialog() {
		dialog.dismiss();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private void setPrompt(String prompt) {
		this.prompt = prompt;
		displayText(prompt);
	}

	private View.OnClickListener spinnerListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog();
		}
	};

	public Date getDate() {
		return selectedDate;
	}

	public void displayDate(Date date) {
		displayText(Utils.getLocalizedDate(mContext, date));
	}

	public void displayText(String text) {
		if (selectedDateView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			inflater.inflate(R.layout.vw_spinner, this);
			selectedDateView = (FontText) findViewById(R.id.text);
		}
		selectedDateView.setText(text);
	}

	public DatePickerDialog getDialog() {
		return dialog;
	}

	public void setDate(Date date) {
		selectedDate = date;
		displayDate(date);
	}

	private OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
			setDate(date);
		}
	};

}
