package com.airlocksoftware.holo.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/** A ViewPager that correctly handles a WebView as a child. Additionally,**/
public class WebViewPager extends DisableableViewPager {
	public WebViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ObservableWebView) {
			return ((ObservableWebView) v).canScrollHor(-dx);
		} else {
			return super.canScroll(v, checkV, dx, x, y);
		}
	}
}