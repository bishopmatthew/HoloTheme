package com.airlocksoftware.holo.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.View;

/** Extends FragmentStatePagerAdapter and allows you to add multiple Pages, whose data will be saved 
 * when you switch between them. **/
public class PageAdapter extends FragmentStatePagerAdapter {

	private List<Page> mPages = new ArrayList<Page>();
	private Map<Integer, Integer> mIdToPosition = new HashMap<Integer, Integer>();
	
	SparseArray<Page> mIdMap = new SparseArray<Page>();

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
	 * If the object isn't one of the visible fragments in the array, return POSITION_NONE
	 * Required for being able to replace the Fragments inside a Pager
	 **/
	@Override
	public int getItemPosition(Object object) {
		for (Page page : mPages) {
			if (page.getVisibleFragment().equals(object)) {
				return POSITION_UNCHANGED;
			}
		}
		return POSITION_NONE;
	}

	/** Required for being able to replace the Fragments inside a Pager **/
	@Override
	protected int getFragmentPosition(Fragment fragment) {
		if (fragment instanceof PageFragment) {
			Page page = mIdMap.get(((PageFragment) fragment).fragmentId());
			return mPages.indexOf(page);
		} else {
			return -1;
		}
	}
	
	/** Required for being able to replace the Fragments inside a Pager **/
	@Override
	protected void handleGetItemInvalidated(View container, Fragment old, Fragment replacement) {
		if (old instanceof PageFragment && replacement instanceof PageFragment) {
			int oldId = ((PageFragment) old).fragmentId();
			Page page = mIdMap.get(oldId);
			int oldIndex = mPages.indexOf(page);
			if (oldIndex != -1) {
				mIdMap.remove(oldId);
				mIdMap.put(((PageFragment) replacement).fragmentId(), page);
			}
		}
	}

	public void getItemByPageId(int id) {
		
	}

	public void addPage(Page page) {
		mPages.add(page);
		mIdMap.put(page.getPageId(), page);
	}

	public List<Page> getPages() {
		return mPages;
	}

	public void addPages(ArrayList<Page> pages) {
		if (mPages == null) {
			mPages = pages;
		} else {
			mPages.addAll(pages);
		}
		
		// add ids to mIdMap
		for(Page page : mPages) {
			mIdMap.put(page.getId(), page);
		}
	}
}