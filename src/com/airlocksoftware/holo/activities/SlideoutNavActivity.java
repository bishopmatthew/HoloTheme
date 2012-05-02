package com.airlocksoftware.holo.activities;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.pages.Page;
import com.airlocksoftware.holo.slideout.SlideoutList;

public abstract class SlideoutNavActivity extends SlideoutActivity {
	
	// Maps from Page.mId to the actual page
	Map<Integer, Page> mPages;
	SlideoutList mNavList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
	
	public void setSlideoutNavList(int listLayoutResId) {
//		LayoutInflater inflater = getLayoutInflater();
		mNavList = (SlideoutList) new SlideoutList(this, null);
		mNavList.setListLayout(listLayoutResId);
		mNavList.setHeaderLayout(R.layout.default_slideout_header);
		mSlideoutFrame.setSlideoutContent(mNavList);
	}

}
