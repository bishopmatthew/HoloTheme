package com.airlocksoftware.holo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBar;
import com.airlocksoftware.holo.anim.OverlayManager;

/** An activity that has hooks for getting the action bar, and drop-down menus**/
public class ActionBarActivity extends FragmentActivity {

	ActionBar mActionBar;
	FrameLayout mFrame;
	
	OverlayManager mOverlayManager;

	private static final int DEFAULT_LAYOUT = R.layout.action_bar_activity;
	protected static final int FRAME_ID = R.id.action_bar_activity_frame;
	protected static final int ACTION_BAR_ID = R.id.action_bar;
	protected static final int ROOT_VIEW_ID = R.id.action_bar_activity_root;
	
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		super.setContentView(DEFAULT_LAYOUT);

		FragmentManager fm = getSupportFragmentManager();
		mActionBar = (ActionBar) fm.findFragmentById(ACTION_BAR_ID);
		mFrame = (FrameLayout) findViewById(FRAME_ID);
		
		mOverlayManager = new OverlayManager(this, this.getWindow());
	}

	@Override
	/** Layout is set inside of mFrame (the content below the ActionBar) **/
	public void setContentView(int layoutResID) {
		getLayoutInflater().inflate(layoutResID, mFrame);
	}

	/** Get the ActionBar **/
	public ActionBar actionBar() {
		return mActionBar;
	}

	/** Get the frame where the content goes **/
	public FrameLayout frame() {
		return mFrame;
	}
	
	public OverlayManager overlayManager() {
		return mOverlayManager;
	}
}
