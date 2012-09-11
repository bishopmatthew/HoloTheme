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

public class FontSpinner extends FrameLayout {

	Context mContext;

	SpinnerArrayAdapter<?> mAdapter;
	IcsDialog mDialog;

	String mPrompt;
	FontText mFontText;

	private View.OnClickListener spinnerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			showDialog();
		}
	};

	public FontSpinner(Context context, AttributeSet attrs) {
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

			ListView list = (ListView) LayoutInflater.from(mContext).inflate(R.layout.dialog_spinner,
					null);
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
		if (mFontText == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			inflater.inflate(R.layout.vw_spinner, this);
			mFontText = (FontText) findViewById(R.id.text);
		}
		mFontText.setText(text);
	}

	public IcsDialog getDialog() {
		return mDialog;
	}
}
