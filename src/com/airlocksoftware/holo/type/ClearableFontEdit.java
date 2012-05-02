package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;

public class ClearableFontEdit extends RelativeLayout {

	// CONTEXT
	Context mContext;

	// VIEWS
	FontEdit mFontEdit;
	ImageView mImageButton;

	// LISTENERS
	private OnClickListener mEmptyListener;

	// CONSTANTS
	private static final int DEFAULT_LAYOUT = R.layout.clearable_font_edit;
	private static final int FONT_EDIT_ID = R.id.edit;
	private static final int CLEAR_BUTTON_ID = R.id.icon;

	// CONSTRUCTORS
	public ClearableFontEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	// INITIALIZE
	private void init(Context context, AttributeSet attrs) {
		mContext = context;
		inflateLayout();
		getAttrs(attrs);
	}

	// PUBLIC METHODS
	public void setHint(String hint) {
		mFontEdit.setHint(hint);
	}

	public void setIconBackground(Drawable iconBg) {
		mImageButton.setBackgroundDrawable(iconBg);
	}

	public void setClearIcon(Drawable icon) {
		mImageButton.setImageDrawable(icon);

	}

	public void setEditBackground(Drawable bg) {
		mFontEdit.setBackgroundDrawable(bg);
	}

	/**
	 * If this isn't null, it means the "clear" icon will be visible when there isn't any text entered in the
	 * FontEdit, and it will call the listener when it clicked. Can be used to hide the field (e.g. a search box)
	 **/
	public void setEmptyClickListener(View.OnClickListener listener) {
		mEmptyListener = listener;
	}

	public boolean hasText() {
		return mFontEdit != null && !mFontEdit.getText().toString().equals("");
	}

	public void clearText() {
		if (mFontEdit != null) mFontEdit.setText("");
	}

	// PRIVATE METHODS
	private void getAttrs(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ClearableFontEdit);

		Drawable bg = a.getDrawable(R.styleable.ClearableFontEdit_clearable_editBackground);
		if (bg != null) setEditBackground(bg);

		Drawable icon = a.getDrawable(R.styleable.ClearableFontEdit_clearable_icon);
		if (icon != null) setClearIcon(icon);

		Drawable iconBg = a.getDrawable(R.styleable.ClearableFontEdit_clearable_iconBackground);
		if (iconBg != null) setIconBackground(iconBg);

		String hint = a.getString(R.styleable.ClearableFontEdit_clearable_hint);
		if (hint != null) setHint(hint);

		a.recycle();
	}

	private void inflateLayout() {
		LayoutInflater.from(mContext).inflate(DEFAULT_LAYOUT, this);
		mFontEdit = (FontEdit) findViewById(FONT_EDIT_ID);
		mImageButton = (ImageView) findViewById(CLEAR_BUTTON_ID);
		
		mFontEdit.addTextChangedListener(mTextWatcher);
		mFontEdit.setText("");
	}

	private View.OnClickListener mClearListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			clearText();
		}
	};

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (count == 0) {
				if (mEmptyListener != null) {
					mImageButton.setVisibility(VISIBLE);
					mImageButton.setOnClickListener(mEmptyListener);
				} else {
					mImageButton.setVisibility(GONE);
				}
			}
			if (count > 0 && mClearListener != null) {
				mImageButton.setVisibility(VISIBLE);
				mImageButton.setOnClickListener(mClearListener);
			}
		}
	};

}
