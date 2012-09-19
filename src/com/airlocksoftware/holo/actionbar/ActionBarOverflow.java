package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.utils.ViewUtils;

public class ActionBarOverflow extends ScrollView {

	private Context mContext;
	private LinearLayout mActionBarButtons, mCustomViews;

	private boolean mLayoutFinished = false;

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
		ViewUtils.fixBackgroundRepeat(this);

		int width = mContext.getResources().getDimensionPixelSize(R.dimen.overflow_menu_width);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,
				LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
		params.gravity = Gravity.RIGHT;

		setLayoutParams(params);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(DEFAULT_LAYOUT, this);
		mLayoutFinished = true;

		mActionBarButtons = (LinearLayout) findViewById(R.id.cnt_actionbar_buttons);
		mCustomViews = (LinearLayout) findViewById(R.id.cnt_custom_views);
	}

	public boolean hasCustomViews() {
		return (mCustomViews != null && mCustomViews.getChildCount() != 0);
	}

	public boolean hasActionBarButtons() {
		return (mActionBarButtons != null && mActionBarButtons.getChildCount() != 0);

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
			else mCustomViews.removeView(v);
		}
	}

	public void addButton(ActionBarButton b) {
		mActionBarButtons.addView(b);
	}

	public void addCustomView(View v) {
		mCustomViews.addView(v);
	}

}
