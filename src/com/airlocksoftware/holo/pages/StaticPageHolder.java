package com.airlocksoftware.holo.pages;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.activities.ActionBarActivity;

/** Analagous to ViewPager, but doesn't do the sideways scrolling. It just switches between pages & fragments **/
public class StaticPageHolder extends FrameLayout {

	Context mContext;
	ActionBarActivity mActivity;
	FragmentManager mFm;

//	 Maps from Page.mId to the actual page
//	Map<Integer, Page> mPages = new HashMap<Integer, Page>();
//	PageAdapter mAdapter;
	
	private FrameLayout mFragmentContainer;
	Page mPage;
	
	private static final int FRAG_CONTAINER_ID = R.id.static_fragment_holder_container;
	private static final String TAG = StaticPageHolder.class.getSimpleName();

	public StaticPageHolder(Context context) {
		this(context, null);
	}

	public StaticPageHolder(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (!(context instanceof ActionBarActivity)) throw new RuntimeException(
				"The context provided to PageHolder must be a ActionBarActivity");

		mContext = context;
		mActivity = (ActionBarActivity) context;
		mFm = mActivity.getSupportFragmentManager();
		
		// SETUP FRAGMENT CONTAINER
		mFragmentContainer = new FrameLayout(mContext, null);
		mFragmentContainer.setId(FRAG_CONTAINER_ID);
		this.addView(mFragmentContainer);
		
	}

	/**
	 * Replaces the currently shown fragment with a new one.
	 * 
	 * @param fragment
	 */
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = mFm.beginTransaction();
		Log.d(TAG, "Showing fragment " + fragment.toString());

		// no animation
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		ft.replace(FRAG_CONTAINER_ID, fragment);

		// try not adding to back stack
//		ft.addToBackStack(null);
		ft.commit();
	}

	public void showPage(Page page) {
		if(mPage != null) mPage.cleanupActionBar(mActivity.actionBar());
		
		Log.d(TAG, "Showing page " + page.toString());
		
		page.setupActionBar(mActivity.actionBar());
		showFragment(page.getVisibleFragment());
		
		mPage = page;
	}
	
	public Page page() {
		return mPage;
	}
}
