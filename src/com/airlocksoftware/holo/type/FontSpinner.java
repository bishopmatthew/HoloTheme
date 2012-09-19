package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.adapters.SpinnerArrayAdapter;
import com.airlocksoftware.holo.dialog.IcsDialog;
import com.airlocksoftware.holo.utils.Utils;

public class FontSpinner extends FrameLayout {

	Context mContext;

	FontText mText;
	
	SpinnerArrayAdapter<?> mAdapter;
	IcsDialog mDialog;

	String mPrompt;
	String mDialogTitle;

	private View.OnClickListener spinnerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			showDialog();
		}
	};

	public FontSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.vw_spinner, this);
		mText = (FontText) findViewById(R.id.text);

		retrieveAttrs(attrs);
		setOnClickListener(spinnerListener);
	}

	private void retrieveAttrs(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ListSpinner);

		String prompt = a.getString(R.styleable.ListSpinner_spinner_prompt);
		setPrompt(prompt);

		int textColor = a.getColor(R.styleable.ListSpinner_android_textColor, -1);
		int textColorHint = a.getColor(R.styleable.ListSpinner_android_textColorHint, -1);

		if (textColor != -1) mText.setTextColor(textColor);
		if (textColorHint != -1) mText.setHintTextColor(textColorHint);

		a.recycle();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	private void showDialog() {
		if (mDialog == null) {
			mDialog = new IcsDialog(mContext);

			ListView list = (ListView) LayoutInflater.from(mContext).inflate(R.layout.dialog_spinner, null);
			list.setAdapter(mAdapter);
			mDialog.setContentView(list);

			mDialog.clearTitle();
		}

		mDialog.show();
	}

	public void hideDialog() {
		mDialog.dismiss();
	}

	private void setPrompt(String prompt) {
		this.mPrompt = prompt;
		setText(prompt);
	}

	public void setAdapter(SpinnerArrayAdapter<?> adapter) {
		mAdapter = adapter;
	}
	
	public void setText(String text) {
		mText.setText(text);
	}

	public IcsDialog getDialog() {
		return mDialog;
	}
}
