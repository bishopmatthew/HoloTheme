package com.airlocksoftware.holo.actionbar.interfaces;

import android.content.Context;

/**
 * Fragments, especially in a ViewPager, should implement this to receive notifications when they have received control
 * of the ActionBarView.
 **/
public interface ActionBarClient {

	public void setupActionBar(Context context, ActionBarController ab);

	public void cleanupActionBar(Context context, ActionBarController ab);

}
