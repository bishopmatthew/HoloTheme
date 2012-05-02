package com.airlocksoftware.holo.tabs;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.activities.MapActivity;
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
	RadioGroup mTabGroup;

	// DATA
	private PageAdapter mAdapter;

	// TABS
	ArrayList<RadioButton> mTabs = new ArrayList<RadioButton>();
	int visibleTabIndex;
	int checkedTabIndex;

	// TAB CHANGE LISTENER
	private OnTabChangeListener tabChangeListener;

	public TabPager(Context context) {
		this(context, null);
	}

	public TabPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.mActivity = (MapActivity) context;
		this.mFm = mActivity.getSupportFragmentManager();

		inflateLayout();

		mAdapter = new PageAdapter(mFm);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);

		mTabGroup = (RadioGroup) findViewById(R.id.tab_group);
	}

	private void inflateLayout() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.tab_pager, this);
	}

	public void addTab(Page tab, int backgroundResourceId) {
		if (mAdapter.getCount() < 1) tab.setupActionBar(mActivity.getAB());
		mAdapter.addItem(tab);
		addImageButton(backgroundResourceId);
	}

	private void addImageButton(int backgroundResourceId) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		RadioButton button = (RadioButton) inflater.inflate(R.layout.tab_portrait_image, mTabGroup, false);
		button.setBackgroundResource(backgroundResourceId);
		if (mTabs.size() < 1) {
			// the first tab, so we set the button to be checked.
			mTabGroup.addView(button);
			button.setChecked(true);
			checkedTabIndex = 0;
			visibleTabIndex = 0;
			mTabGroup.setOnCheckedChangeListener(tabListener);
		} else {
			mTabGroup.addView(button);
		}
		mTabs.add(button);
	}

	private void onTabChange(int oldTab, int newTab) {
		if (tabChangeListener != null) {
			tabChangeListener.onTabChange(mAdapter.getPages().get(oldTab), mAdapter.getPages().get(newTab));
		}
	}

	public Fragment getCurrentItem() {
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

	// public boolean getKeepTouchEvents() {
	// return mPager.getKeepTouchEvents();
	// }
	//
	// public void setKeepTouchEvents(boolean keepTouchEvents) {
	// mPager.setKeepTouchEvents(keepTouchEvents);
	// }

	// @Override
	// public boolean onBackPressed() {
	// return mAdapter.popBackImmediate(mPager);
	// }

	private OnCheckedChangeListener tabListener = new OnCheckedChangeListener() {

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			RadioButton checkedTab = null;
			for (RadioButton tab : mTabs) {
				if (tab.getId() == checkedId) {
					checkedTab = tab;
					checkedTabIndex = mTabs.indexOf(checkedTab);
				}
			}
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
