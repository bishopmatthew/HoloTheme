package com.airlocksoftware.holo.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/** Simple FragmentPagerAdapter that uses an ArrayList<> to hold the fragments. **/
public class FragmentPagerArrayAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments = new ArrayList<Fragment>();

	public FragmentPagerArrayAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if (mFragments.size() > position && position >= 0) {
			fragment = mFragments.get(position);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	public void addItem(Fragment fragment) {
		mFragments.add(fragment);
	}

}
