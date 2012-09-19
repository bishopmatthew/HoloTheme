package com.airlocksoftware.holo.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBarViewGroup;
import com.airlocksoftware.holo.anim.OverlayManager;
import com.airlocksoftware.holo.interfaces.OnActivityResultListener;
import com.airlocksoftware.holo.interfaces.OnBackPressedListener;
import com.airlocksoftware.holo.interfaces.OnStopListener;

/** An activity that has hooks for getting the action bar, and drop-down menus **/
public class ActionBarViewGroupActivity extends FragmentActivity {

	// STATE
	ActionBarViewGroup mActionBar;
	FrameLayout mFrame;
	OverlayManager mOverlayManager;

	// private List<OnBackPressedListener> mOnBackPressedListeners;
	// private List<OnActivityResultListener> mOnActivityResultListeners;
	private List<OnStopListener> mOnStopListeners;
	private boolean mInitialized = false;

	// CONSTANTS

	private static final int DEFAULT_LAYOUT = R.layout.act_actionbar;
	protected static final int FRAME_ID = R.id.actionbar_activity_frame;
	protected static final int ACTIONBAR_ID = R.id.actionbar;
	protected static final int ROOT_VIEW_ID = R.id.actionbar_activity_root;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
	}

	protected void initialize() {
		super.setContentView(DEFAULT_LAYOUT);

		mActionBar = (ActionBarViewGroup) findViewById(ACTIONBAR_ID);
		mFrame = (FrameLayout) findViewById(FRAME_ID);

		mOverlayManager = new OverlayManager(this, this.getWindow());
		mActionBar.overlayManager(mOverlayManager);

		mInitialized = true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mActionBar.toggleOverflow();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO go through mOnActivityResultListeners to see if any accept the result
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public void onBackPressed() {
		// TODO go through mOnBackPressedListeners to see if any accept the back press
		super.onBackPressed();
	}

	@Override
	public void onStop() {
		if (mOnStopListeners != null) {
			for (OnStopListener listener : mOnStopListeners) {
				listener.onStop();
			}
		}
		super.onStop();
	}

	@Override
	/** Layout is set inside of mFrame (the content below the ActionBar) **/
	public void setContentView(int layoutResID) {
		if (!mInitialized) throw new RuntimeException(
				"You must call ActionBarActivity.initialize() in your Activities onCreate() before calling setContentView().");
		getLayoutInflater().inflate(layoutResID, mFrame);
	}

	/** Get the ActionBar **/
	public ActionBarViewGroup actionBar() {
		return mActionBar;
	}

	/** Get the frame where the content goes **/
	public FrameLayout frame() {
		return mFrame;
	}

	public OverlayManager overlayManager() {
		return mOverlayManager;
	}

	public void addOnStopListener(OnStopListener listener) {
		if (mOnStopListeners == null) mOnStopListeners = new ArrayList<OnStopListener>();
		mOnStopListeners.add(listener);
	}
}
