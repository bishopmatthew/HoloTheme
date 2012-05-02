package com.airlocksoftware.holo.slideout;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.checkable.CheckableViewGroup;
import com.airlocksoftware.holo.checkable.CheckableViewGroup.OnCheckedViewChangedListener;

/**
 * Displays the list in the Slideout View
 * 
 * @author Matthew Bishop
 **/
public class SlideoutList extends RelativeLayout {
	
	// INSTANCE VARIABLES

	private Context mContext;
	CheckableViewGroup mCheckableGroup;
	FrameLayout mHeader;
	private ArrayList<SlideoutListItem> mItems = new ArrayList<SlideoutListItem>();
	
	// CONSTANTS
	
	private static final int DEFAULT_LAYOUT = R.layout.default_slideout_list;

	// CONSTRUCTORS
	
	public SlideoutList(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		retrieveAttrs(attrs);
		innerInflateLayout();
	}

	// PUBLIC METHODS

	public void addItem(SlideoutListItem item) {
		mItems.add(item);
	}

	public void setListLayout(int listDataLayout) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(listDataLayout, mCheckableGroup);
	}
	
	public void setHeaderLayout(int headerLayoutResId) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(headerLayoutResId, mHeader);
	}
	
	public void setOnSelectedListener(OnCheckedViewChangedListener listener) {
		mCheckableGroup.setOnCheckedChangedListener(listener);
	}

	// PRIVATE METHODS

	private void innerInflateLayout() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(DEFAULT_LAYOUT, this);
		mCheckableGroup = (CheckableViewGroup) findViewById(R.id.checkable_group);
		mHeader = (FrameLayout) findViewById(R.id.header_frame);
	}

	private void retrieveAttrs(AttributeSet attrs) {
		// TODO Auto-generated method stub

	}

	

}
