package com.airlocksoftware.holo.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.checkable.CheckableViewGroup;

/** Displays a vertical or horizontal group of TabButtons **/
public class TabButtonGroup extends CheckableViewGroup {

	// CONSTRUCTORS
	public TabButtonGroup(Context context) {
		this(context, null);
	}

	public TabButtonGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	public void addTabButton(TabButton tab) {
		this.addView(tab, generateDefaultLayoutParams());
	}

	public void addTab(TabButton tab, ViewGroup.LayoutParams params) {
		super.addView(tab, params);
	}

}
