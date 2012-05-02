package com.airlocksoftware.holo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.interfaces.OnActivityResultListener;
import com.airlocksoftware.holo.pages.Page;
import com.airlocksoftware.holo.tabs.TabButtonPager;
import com.airlocksoftware.holo.tabs.TabButtonPager.OnTabChangeListener;

public class TabActivity extends MapActivity {

	protected TabButtonPager mPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_map_fragment_activity);

		mPager = (TabButtonPager) findViewById(R.id.tab_pager);

		setupActionBar();
	}


	private void setupActionBar() {
		// TODO
	}


	public void onDestroy() {
		ViewGroup parentView = (ViewGroup) this.mMapView.getParent();
		parentView.removeView(this.mMapView);
		super.onDestroy();
	}

	public TabButtonPager getPager() {
		return mPager;
	}

	@Override
	public void onResume() {
		super.onResume();
		mPager.setOnTabChangeListener(new OnTabChangeListener() {

			@Override
			public void onTabChange(Page oldTab, Page newTab) {
				getAnimationOverlayView().hideAll();
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