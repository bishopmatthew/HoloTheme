package com.airlocksoftware.holo.pages;

import java.util.Stack;

import android.content.Context;
import android.view.View;

import com.airlocksoftware.holo.actionbar.ActionBar;
import com.airlocksoftware.holo.activities.ActionBarActivity;
import com.airlocksoftware.holo.interfaces.ActionBarInterface;
import com.airlocksoftware.holo.interfaces.BackPressedListener;

/**
 * Represents a tab in the TabPager. Implements a BackStack for TabFragments, handles 
 * BackPressed callbacks, setting up and cleaning up the ActionBar, etc.
 * 
 * @author matthewbishop
 * 
 */
public abstract class Page implements BackPressedListener, ActionBarInterface {

	private Context mContext;
	private ActionBarActivity mActivity;
	
	private StaticPageHolder mHolder;
	private PageFragment mVisibleFragment;
	
	private Stack<PageFragment> mStack = new Stack<PageFragment>();
	private int mId;
	
	// CONSTANTS
	private static final int DEFAULT_ID = 1;

	// CONSTRUCTORS
	public Page(ActionBarActivity context, StaticPageHolder holder) {
		this(context, holder, DEFAULT_ID);
	}

	public Page(ActionBarActivity context, StaticPageHolder holder, int id) {
		mContext = context;
		mActivity = context;
		mHolder = holder;
		setId(id);
	}
	
	// PUBLIC METHODS
	
	public Context context() {
		return mContext;
	}
	
	public ActionBarActivity activity() {
		return mActivity;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public void setVisibleFragment(PageFragment fragment) {
		setVisibleFragment(fragment, false);
	}

	private void setVisibleFragment(PageFragment fragment, boolean isBackStack) {
		if (mVisibleFragment != null) {
			if (!isBackStack) mStack.add(mVisibleFragment);

//			TabPager tabPager = mContext.getPager();
//			tabPager.getAdapter().replaceFragments(tabPager.getViewPager(), mVisibleFragment, fragment);
			
			mHolder.showFragment(mVisibleFragment);
			
			mVisibleFragment.cleanupActionBar(mActivity.actionBar());
			mVisibleFragment = fragment;
			setupActionBar(mActivity.actionBar());
		} else {
			mVisibleFragment = fragment;
		}
	}

	public PageFragment getVisibleFragment() {
		if (mVisibleFragment == null) {
			throw new NullPointerException("Error: setVisibleFragment() not called with a valid TabFragment");
		}
		return mVisibleFragment;
	}

	public int getPageId() {
		return getVisibleFragment().fragmentId();
	}

	@Override
	/** Pops the back stack **/
	public boolean onBackPressed() {
		if (!mStack.empty()) {
			setVisibleFragment(mStack.pop(), true);
			return true;
		} else if (mVisibleFragment instanceof BackPressedListener) {
			return ((BackPressedListener) mVisibleFragment).onBackPressed();
		} else {
			return false;
		}
	}
	
	public void clearBackStack() {
		mStack.clear();
	}

	@Override
	public void setupActionBar(ActionBar actionBar) {
		if (!mStack.empty()) {
			actionBar.setUpListener(mBackListener);
		} else {
			actionBar.setUpListener(null);
		}
		getVisibleFragment().setupActionBar(actionBar);
	}

	@Override
	public void cleanupActionBar(ActionBar actionBar) {
		actionBar.setUpListener(null);
		getVisibleFragment().cleanupActionBar(actionBar);
	}

	private View.OnClickListener mBackListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};
}
