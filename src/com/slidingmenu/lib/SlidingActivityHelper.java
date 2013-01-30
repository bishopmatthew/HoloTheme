/**Copyright 2012 Jeremy Feinstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.**/

/** I've slightly modified these five files from https://github.com/jfeinstein10/SlidingMenu to support the way I am using them. **/

package com.slidingmenu.lib;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class SlidingActivityHelper {

	private Activity mActivity;

	private SlidingMenu mSlidingMenu;
	private View mViewAbove;
	private View mViewBehind;
	private boolean mBroadcasting = false;

	private boolean mOnPostCreateCalled = false;
	private boolean mEnableSlide = true;

	public SlidingActivityHelper(Activity activity) {
		mActivity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		mSlidingMenu = new SlidingMenu(mActivity, null);
		mSlidingMenu.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if (mViewBehind == null || mViewAbove == null) {
			throw new IllegalStateException("Both setBehindContentView must be called "
					+ "in onCreate in addition to setContentView.");
		}

		mOnPostCreateCalled = true;

		// get the window background
		TypedArray a = mActivity.getTheme()
														.obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
		int background = a.getResourceId(0, 0);

		if (mEnableSlide) {
			// move everything into the SlidingMenu
			ViewGroup decor = (ViewGroup) mActivity.getWindow()
																							.getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			// save ActionBar themes that have transparent assets
			decorChild.setBackgroundResource(background);
			decor.removeView(decorChild);
			mSlidingMenu.setContent(decorChild);
			decor.addView(mSlidingMenu);
		} else {
			// take the above view out of
			ViewGroup parent = (ViewGroup) mViewAbove.getParent();
			if (parent != null) {
				parent.removeView(mViewAbove);
			}
			// save people from having transparent backgrounds
			if (mViewAbove.getBackground() == null) {
				mViewAbove.setBackgroundResource(background);
			}
			mSlidingMenu.setContent(mViewAbove);
			parent.addView(mSlidingMenu, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

	public void setSlidingActionBarEnabled(boolean b) {
		if (mOnPostCreateCalled) throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
		mEnableSlide = b;
	}

	public View findViewById(int id) {
		View v;
		if (mSlidingMenu != null) {
			v = mSlidingMenu.findViewById(id);
			if (v != null) return v;
		}
		return null;
	}

	public void registerAboveContentView(View v, LayoutParams params) {
		if (!mBroadcasting) mViewAbove = v;
	}

	public void setContentView(View v) {
		mBroadcasting = true;
		mActivity.setContentView(v);
	}

	public void setBehindContentView(View v, LayoutParams params) {
		mViewBehind = v;
		mSlidingMenu.setMenu(mViewBehind);
	}

	public SlidingMenu getSlidingMenu() {
		return mSlidingMenu;
	}

	public void toggle() {
		if (mSlidingMenu.isBehindShowing()) {
			showAbove();
		} else {
			showBehind();
		}
	}

	public void showAbove() {
		mSlidingMenu.showAbove();
	}

	public void showBehind() {
		mSlidingMenu.showBehind();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isBehindShowing()) {
			showAbove();
			return true;
		}
		return false;
	}

}
