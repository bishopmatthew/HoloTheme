package com.airlocksoftware.holo.activities;

import android.content.Intent;
import android.os.Bundle;

import com.airlocksoftware.holo.tab.TabPager;

public class TabActivity extends MapActivity {

	protected TabPager mPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_activity);

		mPager = (TabPager) findViewById(R.id.tab_pager);

		setupActionBar();
	}

	private void setupActionBar() {
		// TODO
	}

	public void onDestroy() {
		ViewGroup parentView = (ViewGroup) this.mMapView.getParent();
		if (parentView != null) parentView.removeView(this.mMapView);
		super.onDestroy();
	}

	public TabPager getPager() {
		return mPager;
	}

	@Override
	public void onResume() {
		super.onResume();
		mPager.setOnTabChangeListener(new OnTabChangeListener() {

			@Override
			public void onTabChange(Page oldTab, Page newTab) {
				overlayManager().hideAllViews();
				oldTab.cleanupActionBar(mActionBar);
				newTab.setupActionBar(mActionBar);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (mPager.getCurrentFragment() instanceof OnActivityResultListener) {
			((OnActivityResultListener) mPager.getCurrentFragment()).onActivityResult(requestCode, resultCode, intent);
		}
	}

	@Override
	public void onBackPressed() {
		if (!mPager.getCurrentTab().onBackPressed()) super.onBackPressed();
	}

}