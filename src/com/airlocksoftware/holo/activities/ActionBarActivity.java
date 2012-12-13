package com.airlocksoftware.holo.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBarView;
import com.airlocksoftware.holo.actionbar.OnePaneController;
import com.airlocksoftware.holo.actionbar.interfaces.ActionBarController;
import com.airlocksoftware.holo.anim.OverlayManager;
import com.airlocksoftware.holo.interfaces.OnStopListener;

/** An activity that has hooks for getting the action bar, and drop-down menus **/
public abstract class ActionBarActivity extends SlidingFragmentActivity {

	// VIEWS & CONTROLLERS
	ActionBarView mActionBar;
	ActionBarController mController;
	FrameLayout mFrame;
	OverlayManager mOverlayManager;

	// private List<OnBackPressedListener> mOnBackPressedListeners;
	// private List<OnActivityResultListener> mOnActivityResultListeners;
	private List<OnStopListener> mOnStopListeners;

	// STATE
	private boolean mInitialized = false;

	// CONSTANTS
	private static final int DEFAULT_LAYOUT = R.layout.act_actionbar;
	protected static final int FRAME_ID = R.id.actionbar_activity_frame;
	protected static final int ACTIONBAR_ID = R.id.actionbar;
	protected static final int ROOT_VIEW_ID = R.id.actionbar_activity_root;

	// PUBLIC METHODS
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
	}

	protected void initialize() {
		super.setContentView(DEFAULT_LAYOUT);

		mActionBar = (ActionBarView) findViewById(ACTIONBAR_ID);
		mFrame = (FrameLayout) findViewById(FRAME_ID);

		mOverlayManager = new OverlayManager(this, this.getWindow());
		mActionBar.setOverlayManager(mOverlayManager);

		createActionBarController();

		mInitialized = true;
	}

	/**
	 * Creates the ActionBarController and attaches it to the ActionBar. Override this method if the Activity
	 * needs to specify it's own Controller / Layout.
	 **/
	protected void createActionBarController() {
		mActionBar.setController(new OnePaneController(this, mActionBar));
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
		if (!mInitialized) throw new IllegalStateException(
				"You must call ActionBarActivity.initialize() in your Activity's onCreate() before calling setContentView().");
		getLayoutInflater().inflate(layoutResID, mFrame);
	}

	/** Get the ActionBar **/
	public ActionBarView getActionBarView() {
		return mActionBar;
	}

	/** Get the frame where the content goes **/
	public FrameLayout getContentFrame() {
		return mFrame;
	}

	/** Get the manager for views that overlay on top of the content. **/
	public OverlayManager getOverlayManager() {
		return mOverlayManager;
	}

	/** Add a callback when onStop is called in the Activity. **/
	public void addOnStopListener(OnStopListener listener) {
		if (mOnStopListeners == null) mOnStopListeners = new ArrayList<OnStopListener>();
		mOnStopListeners.add(listener);
	}

}
