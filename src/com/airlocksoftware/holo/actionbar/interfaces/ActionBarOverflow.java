package com.airlocksoftware.holo.actionbar.interfaces;

import android.view.View;

public interface ActionBarOverflow {

	public void addView(View v, ViewType type);

	public void removeView(View v);

	public void findViewById(int id);
	
	public enum ViewType {
		ACTION_BAR_BUTTON, CUSTOM;
	}

}
