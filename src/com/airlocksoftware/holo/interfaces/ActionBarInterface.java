package com.airlocksoftware.holo.interfaces;

import android.content.Context;

import com.airlocksoftware.holo.actionbar.ActionBarView;

/**
 * Fragments, especially in a ViewPager, should implement this to receive notifications when they have received control
 * of the ActionBarView.
 **/
public interface ActionBarInterface {

	public void setupActionBar(Context context, ActionBarView ab);

	public void cleanupActionBar(Context context, ActionBarView ab);

}
