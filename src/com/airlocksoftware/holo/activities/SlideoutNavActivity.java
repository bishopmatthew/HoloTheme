package com.airlocksoftware.holo.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.checkable.CheckableViewGroup;
import com.airlocksoftware.holo.checkable.CheckableViewGroup.OnCheckedViewChangedListener;
import com.airlocksoftware.holo.pages.Page;
import com.airlocksoftware.holo.pages.PageHolder;
import com.airlocksoftware.holo.slideout.SlideoutList;
import com.airlocksoftware.holo.slideout.SlideoutListItem;

public abstract class SlideoutNavActivity extends SlideoutActivity {

	// STATE
	SlideoutList mNavList;
	PageHolder mHolder;
	
	// CONSTANTS
	private static final int DEFAULT_LAYOUT = R.layout.slideout_nav_activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(DEFAULT_LAYOUT);
		
		mHolder = (PageHolder) findViewById(R.id.page_holder);
	}

	public void setSlideoutNavList(int listLayoutResId) {
		// LayoutInflater inflater = getLayoutInflater();
		mNavList = (SlideoutList) new SlideoutList(this, null);
		mNavList.setListLayout(listLayoutResId);
		mNavList.setHeaderLayout(R.layout.default_slideout_header);
		mSlideoutFrame.setSlideoutContent(mNavList);
		
		ArrayList<Page> pages = new ArrayList<Page>();
		for(SlideoutListItem item : mNavList.getListItems()) {
			pages.add(item.getPage());
		}
		
		mHolder.addPages(pages);
		mNavList.setOnItemSelectedListener(navListener);
	}
	
	private OnCheckedViewChangedListener navListener = new OnCheckedViewChangedListener() {

		public void onCheckedViewChanged(CheckableViewGroup group, int newPosition, int oldPosition) {
			mHolder.setCurrentItem(group.getChildAt(newPosition).getPageId(), oldPosition);
		}

	};

}
