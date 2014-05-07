package com.airlocksoftware.holo.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * A WebView that implements the canScrollHor method to interact correctly with WebViewPager. *
 */
public class ObservableWebView extends WebView {

  private OnScrollChangedListener mOnScrollChangedListener;

  public ObservableWebView(Context context) {
    super(context);
  }

  public ObservableWebView(Context context, AttributeSet attrs) {
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

  @Override
  protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    if (mOnScrollChangedListener != null) mOnScrollChangedListener.onScroll(l, t);
  }

  public OnScrollChangedListener getOnScrollChangedListener() {
    return mOnScrollChangedListener;
  }

  public void setOnScrollChangedListener(final OnScrollChangedListener onScrollChangedListener) {
    mOnScrollChangedListener = onScrollChangedListener;
  }

  /**
   * Impliment in the activity/fragment/view that you want to listen to the webview
   */
  public static interface OnScrollChangedListener {
    public void onScroll(int left, int top);
  }
}