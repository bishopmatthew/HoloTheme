package com.airlocksoftware.holo.tab;


import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.activities.MapActivity;
import com.airlocksoftware.holo.checkable.CheckableViewGroup;
import com.airlocksoftware.holo.checkable.CheckableViewGroup.OnCheckedViewChangedListener;
import com.airlocksoftware.holo.pages.Page;
import com.airlocksoftware.holo.pages.PageAdapter;

/**
 * A ViewPager of Tabs (and then Tabs manage their own TabFragments).
 * 
 * @author matthewbbishop
 * 
 */

public class TabPager extends RelativeLayout implements ViewPager.OnPageChangeListener {

	// CONTEXT
	Context mContext;
	MapActivity mActivity;
	FragmentManager mFm;

	// VIEWS
	ViewPager mPager;
	TabButtonGroup mTabGroup;

	// DATA
	private PageAdapter mAdapter;

	// TABS
	ArrayList<TabButton> mTabs = new ArrayList<TabButton>();
	int visibleTabIndex;
	int checkedTabIndex;

	// TAB CHANGE LISTENER
	private OnTabChangeListener tabChangeListener;

	// CONSTANTS
	private static final int DEFAULT_LAYOUT = R.layout.tab_pager;

	// CONSTRUCTORS
	public TabPager(Context context) {
		this(context, null);
	}

	public TabPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.mActivity = (MapActivity) context;
		this.mFm = mActivity.getSupportFragmentManager();

		inflateLayout(DEFAULT_LAYOUT);

		mAdapter = new PageAdapter(mFm);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);

		mTabGroup = (TabButtonGroup) findViewById(R.id.tab_group);
	}

	private void inflateLayout(int layoutResId) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(layoutResId, this);
	}

	public void addTab(Page tab, int iconResId) {
		if (mAdapter.getCount() < 1) tab.setupActionBar(mActivity.actionBar());
		mAdapter.addPage(tab);
		addTabButton(iconResId);
	}

	public void addTab(Page tab, String tabName) {
		if (mAdapter.getCount() < 1) tab.setupActionBar(mActivity.actionBar());
		mAdapter.addPage(tab);
		addTabButton(tabName);
	}

	private void addTabButton(String tabName) {
		TabButton button = new TabButton(mContext);
		button.setTabText(tabName);
		if (mTabs.size() < 1) {
			initialTabSetup(button);
		} else {
			mTabGroup.addTabButton(button);
		}
		mTabs.add(button);
	}

	private void addTabButton(int iconResId) {
		TabButton button = new TabButton(mContext);
		button.setTabIcon(iconResId);
		if (mTabs.size() < 1) {
			initialTabSetup(button);
		} else {
			mTabGroup.addTabButton(button);
		}
		mTabs.add(button);
	}
	
	/** The first tab, so we set the button to be checked. **/
	private void initialTabSetup(TabButton button) {
		mTabGroup.addTabButton(button);
		button.setChecked(true);
		checkedTabIndex = 0;
		visibleTabIndex = 0;
		mTabGroup.setOnCheckedChangedListener(tabListener);
	}

	private void onTabChange(int oldTab, int newTab) {
		if (tabChangeListener != null) {
			tabChangeListener.onTabChange(mAdapter.getPages().get(oldTab), mAdapter.getPages().get(newTab));
		}
	}

	public Fragment getCurrentFragment() {
		return mAdapter.getItem(visibleTabIndex);
	}

	public Page getCurrentTab() {
		return mAdapter.getPages().get(visibleTabIndex);
	}

	public void setOnTabChangeListener(OnTabChangeListener listener) {
		this.tabChangeListener = listener;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		int oldTabIndex = visibleTabIndex;
		visibleTabIndex = position;
		if (visibleTabIndex != checkedTabIndex) {
			mTabs.get(position).setChecked(true);
		}
		onTabChange(oldTabIndex, visibleTabIndex);
	}

	public PageAdapter getAdapter() {
		return mAdapter;
	}

	public ViewPager getViewPager() {
		return mPager;
	}

	private OnCheckedViewChangedListener tabListener = new OnCheckedViewChangedListener() {

		public void onCheckedViewChanged(CheckableViewGroup group, int newPosition, int oldPosition) {
			checkedTabIndex = newPosition;
			if (checkedTabIndex != visibleTabIndex) {
				mPager.setCurrentItem(checkedTabIndex);
			}
		}

	};

	public abstract static class OnTabChangeListener {
		public int curTab;
		public int prevTab;

		public abstract void onTabChange(Page oldTab, Page newTab);
	}

}
