package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airlocksoftware.holo.type.FontText;

public class ActionBarButton extends View {

	// STATE
	ViewGroup mContentView;
	FontText mText;
	ImageView mImage;

	Context mContext;

	ButtonMode mButtonMode;

	// CONSTRUCTORS
	public ActionBarButton(Context context) {
		this(context, null);
	}

	public ActionBarButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	// PUBLIC API
	/** Set the mode of this button. **/
	public ActionBarButton buttonMode(ButtonMode mode) {
		mButtonMode = mode;
		// TODO inflate the right layout, start passing OnMeasure, OnDraw, etc.
		return this;
	}

	/** Get the mode of this button. **/
	public ButtonMode buttonMode() {
		return mButtonMode;
	}

	/** Get the text size in pixels **/
	public float textSize() {
		if(mText != null) {
			return mText.getTextSize();
		} else {
			return -1.0f;
		}
	}

	/** Set the text size in SP (scaled pixel) **/
	public ActionBarButton textSize(float size) {
		if(mText != null) mText.setTextSize(size);
		return this;
	}
	
	/** Set the text size with specified unit (see TypedValue for possible units) **/
	public ActionBarButton textSize(int unit, float size) {
		if(mText != null) mText.setTextSize(unit, size);
		return this;
	}


	// ENUMS & INNER CLASSES
	public enum ButtonMode {
		ICON_ONLY(0), TEXT_ONLY(1), ICON_AND_LABEL(2), OVERFLOW(3);

		private final int mLayoutResId;

		ButtonMode(int layoutResId) {
			mLayoutResId = layoutResId;
		}

		public int layoutResId() {
			return mLayoutResId;
		}
	}

}
