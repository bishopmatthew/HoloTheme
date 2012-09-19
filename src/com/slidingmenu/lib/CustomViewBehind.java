package com.slidingmenu.lib;

/** Adapted from: https://github.com/iPaulPro/SlidingMenu 
Copyright 2012 Jeremy Feinstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
* **/

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewBehind extends CustomViewAbove {
	
	private static final String TAG = "CustomViewBehind";

	public CustomViewBehind(Context context) {
		this(context, null);
	}

	public CustomViewBehind(Context context, AttributeSet attrs) {
		super(context, attrs, false);
	}

	public int getDestScrollX() {
		if (isMenuOpen()) {
			return getBehindWidth();
		} else {
			return 0;
		}
	}

	public int getChildLeft(int i) {
		return 0;
	}

	public int getChildRight(int i) {
		return getChildLeft(i) + getChildWidth(i);
	}

	public boolean isMenuOpen() {
		return getScrollX() == 0;
	}

	public int getCustomWidth() {
		int i = isMenuOpen()? 0 : 1;
		return getChildWidth(i);
	}

	public int getChildWidth(int i) {
		if (i <= 0) {
			return getBehindWidth();
		} else {
			return getChildAt(i).getMeasuredWidth();
		}
	}

	public int getBehindWidth() {
		ViewGroup.LayoutParams params = getLayoutParams();
		return params.width;
	}

	@Override
	public void setContent(View v) {
		super.setMenu(v);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return false;
	}

}
