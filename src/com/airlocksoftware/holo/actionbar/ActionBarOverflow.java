package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.airlocksoftware.holo.R;

public class ActionBarOverflow extends ScrollView {

	private Context mContext;
	private LinearLayout mActionBarButtons, mCustomViews;

	private boolean mLayoutFinished = false;
	private boolean mHasCustomViews = false;

	// CONSTANTS
	private static final int DEFAULT_LAYOUT = R.layout.vw_actionbar_overflow;

	public ActionBarOverflow(Context context) {
		this(context, null);
	}

	public ActionBarOverflow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		TypedValue tv = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.overflowMenuBg, tv, true);
		int background = tv.resourceId;
		setBackgroundResource(background);

		int width = mContext.getResources().getDimensionPixelSize(R.dimen.overflow_menu_width);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
		params.gravity = Gravity.RIGHT;
		
		setLayoutParams(params);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(DEFAULT_LAYOUT, this);
		mLayoutFinished = true;

		mActionBarButtons = (LinearLayout) findViewById(R.id.cnt_actionbar_buttons);
		mCustomViews = (LinearLayout) findViewById(R.id.cnt_custom_views);

	}

	public boolean hasCustomViews() {
		return mHasCustomViews;
	}
	
	public void removeActionBarButtons() {
		mActionBarButtons.removeAllViews();
	}
	
	public void removeCustomViews() {
		mCustomViews.removeAllViews();
	}

	@Override
	public void removeView(View v) {
		if (!mLayoutFinished) super.removeView(v);
		else {
			if (v instanceof ActionBarButton) mActionBarButtons.removeView(v);
			else {
				mCustomViews.removeView(v);
				// check if there are any custom views left
				if (mCustomViews != null) mHasCustomViews = mCustomViews.getChildCount() != 0;
			}
		}
	}
	
	public void addButton(ActionBarButton b) {
		mActionBarButtons.addView(b);
	}
	
	public void addCustomView(View v) {
		mCustomViews.addView(v);
	}

//	// OVERRIDE THIS addView SINCE ALL OTHERS ARE ROUTED THROUGH IT
//	@Override
//	public void addView(View child, int index, ViewGroup.LayoutParams params) {
//		if (!mLayoutFinished) super.addView(child, index, params);
//		else {
//			if (child instanceof ActionBarButton) {
//				mActionBarButtons.addView(child);
//			} else {
//				mCustomViews.addView(child);
//			}
//		}
//	}

}
