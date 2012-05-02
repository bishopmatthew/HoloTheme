package com.airlocksoftware.holo.pages;

import java.util.ArrayList;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

public class PageAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Page> mPages = new ArrayList<Page>();
	private ArrayList<String> mIds = new ArrayList<String>();

	public PageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		if (index >= 0 && index < mPages.size()) {
			return mPages.get(index).getVisibleFragment();
		} else {
			return null;
		}
	}

	@Override
	public int getCount() {
		return mPages.size();
	}

	/**
	 * If the object isn't one of the visible fragments in the array, return
	 * POSITION_NONE
	 **/
	@Override
	public int getItemPosition(Object object) {
		for (Page tab : mPages) {
			if (tab.getVisibleFragment().equals(object)) {
				return POSITION_UNCHANGED;
			}
		}
		return POSITION_NONE;
	}

	public void addItem(Page fragment) {
		mPages.add(fragment);
		mIds.add(fragment.getStringId());
	}

	@Override
	protected int getFragmentPosition(Fragment fragment) {
		if (fragment instanceof PageFragment) {
			return mIds.indexOf(((PageFragment) fragment).getStringId());
		} else {
			return -1;
		}
	}

	@Override
	protected void handleGetItemInvalidated(View container, Fragment oldFragment, Fragment newFragment) {
		if (oldFragment instanceof PageFragment && newFragment instanceof PageFragment) {
			int oldFragmentIndex = mIds.indexOf(((PageFragment) oldFragment).getStringId());
			if (oldFragmentIndex != -1) {
				mIds.set(oldFragmentIndex, ((PageFragment) newFragment).getStringId());
			}
		}
	}

	public ArrayList<Page> getPages() {
		return mPages;
	}
}