package com.airlocksoftware.holo.interfaces;

import android.content.Context;

import com.airlocksoftware.holo.actionbar.ActionBarViewGroup;

public interface ActionBarInterface {
	
	public void setupActionBar(Context context, ActionBarViewGroup ab);
	
	public void cleanupActionBar(Context context, ActionBarViewGroup ab);

}
