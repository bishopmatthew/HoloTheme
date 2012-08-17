package com.airlocksoftware.holo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBar;
import com.airlocksoftware.holo.anim.AnimationOverlay;
import com.airlocksoftware.holo.anim.AnimationOverlayView;

/** An activity that has hooks for getting the action bar, and drop-down menus**/
public class ActionBarActivity extends FragmentActivity {

	ActionBar mActionBar;
	AnimationOverlayView mFillScreenAnimFrame;
	AnimationOverlayView mClipActionBarAnimFrame;
	FrameLayout mFrame;
	
	AnimationOverlay mTest;

	private static final int DEFAULT_LAYOUT = R.layout.action_bar_activity;
	protected static final int FRAME_ID = R.id.action_bar_activity_frame;
	protected static final int ACTION_BAR_ID = R.id.action_bar;
	
	protected static final int FILL_SCREEN_ANIM_FRAME = R.id.action_bar_fill_screen_anim_frame;
	protected static final int CLIP_ACTION_BAR_ANIM_FRAME = R.id.action_bar_clip_anim_frame;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		super.setContentView(DEFAULT_LAYOUT);

		FragmentManager fm = getSupportFragmentManager();
		mActionBar = (ActionBar) fm.findFragmentById(ACTION_BAR_ID);
		mFillScreenAnimFrame = (AnimationOverlayView) findViewById(FILL_SCREEN_ANIM_FRAME);
		mClipActionBarAnimFrame = (AnimationOverlayView) findViewById(CLIP_ACTION_BAR_ANIM_FRAME);
		mFrame = (FrameLayout) findViewById(FRAME_ID);
		
		mTest = new AnimationOverlay(this, this.getWindow());
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
	
	/** Get the AnimationOverlayView **/
	public AnimationOverlayView fillScreenAnimation() {
		return mFillScreenAnimFrame;
	}
	
	public AnimationOverlayView clipActionBarAnimation() {
		return mClipActionBarAnimFrame;
	}
	
}
