package com.airlocksoftware.holo.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/** A WebView that implements the canScrollHor method to interact correctly with WebViewPager. **/
public class WebViewPagerCompat extends WebView {

	public WebViewPagerCompat(Context context) {
		super(context);
	}

	public WebViewPagerCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean canScrollHor(int direction) {
		final int offset = computeHorizontalScrollOffset();
		final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
		if (range == 0) return false;
		if (direction < 0) {
			return offset > 0;
		} else {
			return offset < range - 1;
		}
	}
}