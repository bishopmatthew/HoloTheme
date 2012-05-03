package com.airlocksoftware.holo.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.airlocksoftware.holo.slideout.SlideoutListItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

public class PageAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Page> mPages = new ArrayList<Page>();
	Map<Integer, Page> mIdMap = new HashMap<Integer, Page>();


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

	public void addItem(Page page) {
		mPages.add(page);
		mIdMap.put(page.getPageId(), page);
	}

	@Override
	protected int getFragmentPosition(Fragment fragment) {
		if (fragment instanceof PageFragment) {
			Page page = mIdMap.get(((PageFragment) fragment).getPageId());
			return mPages.indexOf(page);
		} else {
			return -1;
		}
	}

	@Override
	protected void handleGetItemInvalidated(View container, Fragment oldFragment, Fragment newFragment) {
		if (oldFragment instanceof PageFragment && newFragment instanceof PageFragment) {
			int oldId = ((PageFragment) oldFragment).getPageId();
			Page page = mIdMap.get(oldId);
			int oldIndex = mPages.indexOf(page);
			if(oldIndex != -1) {
				mIdMap.remove(oldId);
				mIdMap.put(((PageFragment) newFragment).getPageId(), page);
			}
		}
	}

	public ArrayList<Page> getPages() {
		return mPages;
	}

	public void addPages(ArrayList<Page> pages) {
		if(mPages == null) {
			mPages = pages;
		} else {
			mPages.addAll(pages);
		}
	}
}