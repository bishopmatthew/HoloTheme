package com.airlocksoftware.holo.webview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/** Extends ViewPager to allow you to enable / disable swiping between fragments by calling setPagingEnable(). **/
public class DisableableViewPager extends ViewPager {

	private boolean mEnabled = true;

	public DisableableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mEnabled) return super.onTouchEvent(event);
		else return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (mEnabled) return super.onInterceptTouchEvent(event);
		else return false;
	}

	/** Whether or not you can swipe between pages **/
	public void setPagingEnabled(boolean enabled) {
		this.mEnabled = enabled;
	}

}