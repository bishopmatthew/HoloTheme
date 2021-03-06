package com.airlocksoftware.holo.actionbar.interfaces;

import android.view.View;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.actionbar.ActionBarButton;

/**
 * The interface exposed to fragments that need access to the ActionBar, exposed to Fragments
 * through the ActionBarClient interface.
 **/
public interface ActionBarController {

	// /** Common method for initializing the controller once the ActionBar has been created. **/
	// public void initialize(Context context, ActionBarView actionBar);

	/** Add an ActionBarButton to either the overflow or the HIGH_PRIORITY button area. **/
	public void addButton(ActionBarButton button);

	/** Removes the button from the ActionBar, if found. **/
	public void removeButton(ActionBarButton button);

	/** Adds a generic View to the OverflowMenu **/
	public void addOverflowView(View toAdd);

	/** Removes a view from the OverflowMenu, if found. **/
	public void removeOverflowView(View toRemove);

	/** Sets the text of the title. Set to null to hide the title TextView. Set to null when cleaning up ActionBar. **/
	public void setTitleText(String text);

	/** Get the ViewGroup that can be used for custom titles. Empty when cleaning up ActionBar. **/
	public RelativeLayout getTitleGroup();

	/** Returns the title group to the default state (i.e. an empty textview being the only child) **/
	public void clearTitleGroup();

	/**
	 * Called by ActionBarView when it's own onMeasure method is called. Don't forget to set the draw mode of the
	 * ActionBarButtons!
	 **/
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec);

}
