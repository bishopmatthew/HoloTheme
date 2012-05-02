package com.airlocksoftware.holo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBar;
import com.airlocksoftware.holo.anim.AnimationOverlayView;
import com.airlocksoftware.holo.interfaces.AnimationOverlay;

/** An activity that has hooks for getting the action bar, and drop-down menus**/
public class ActionBarActivity extends FragmentActivity implements AnimationOverlay {

	ActionBar mActionBar;
	AnimationOverlayView mOverlayAnimFrame;
	FrameLayout mFrame;

	private static final int DEFAULT_LAYOUT = R.layout.action_bar_activity;
	protected static final int FRAME_ID = R.id.action_bar_activity_frame;
	protected static final int ACTION_BAR_ID = R.id.action_bar;
	protected static final int OVERLAY_ANIM_FRAME = R.id.action_bar_overlay_anim_frame;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		super.setContentView(DEFAULT_LAYOUT);

		FragmentManager fm = getSupportFragmentManager();
		mActionBar = (ActionBar) fm.findFragmentById(ACTION_BAR_ID);
		mOverlayAnimFrame = (AnimationOverlayView) findViewById(OVERLAY_ANIM_FRAME);
		mFrame = (FrameLayout) findViewById(FRAME_ID);
	}

	@Override
	/** Layout **/
	public void setContentView(int layoutResID) {
		getLayoutInflater().inflate(layoutResID, mFrame);
	}

	public ActionBar getAB() {
		return mActionBar;
	}

	public FrameLayout getFrame() {
		return mFrame;
	}
	
	public AnimationOverlayView getAnimationOverlayView() {
		return mOverlayAnimFrame;
	}
	
}
