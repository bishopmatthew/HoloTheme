package com.airlocksoftware.holo.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.slideout.SlideoutListItem;

/** Analagous to ViewPager, but doesn't do the sideways scrolling. It just switches between pages & fragments **/
public class PageHolder extends FrameLayout {
	
	Context mContext;
	FragmentActivity mActivity;
	FragmentManager mFm;

	// Maps from Page.mId to the actual page
	Map<Integer, Page> mPages;
	PageAdapter mAdapter;
	
	public PageHolder(Context context) {
		this(context, null);
	}
	
	public PageHolder(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		if(!(context instanceof FragmentActivity)) {
			throw new RuntimeException("The context provided to PageHolder must be a FragmentActivity");
		}
		
		mContext = context;
		mActivity = (FragmentActivity) context;
		mFm = mActivity.getSupportFragmentManager();
		
		mAdapter = new PageAdapter(mFm);
	}
	
//	public void setAdapter(PageAdapter adapter) {
//		mAdapter = adapter;
//	}

	public void addPages(ArrayList<Page> pages) {
		mAdapter.addPages(pages);
	}
	
	/**
	 * Replaces the currently shown fragment with a new one.
	 * @param fragment
	 */
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = mFm.beginTransaction();

		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		ft.

//		transaction.replace(R.id.content_fragment, fragment);
//		currentFragment = fragment;

//		ft.addToBackStack(null);
		ft.commit();
	}

}
