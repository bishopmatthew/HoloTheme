package com.airlocksoftware.holo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * A class that can be used to wrap content that should be replaced with a
 * progress bar while the data is loading, and then shown once it is complete.
 **/
public class ProgressFrame extends FrameLayout {

	// VIEWS
	private ProgressBar mProgress;
	private View mContent;

	// STATE
	private boolean mContentVisible;

	// LISTENERS
	private OnProgressChangedListener mListener;
	
	// CONSTANTS
	private static final int PROGRESS_LAYOUT = 1; //TODO

	// CONSTRUCTORS
	public ProgressFrame(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ProgressFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ProgressFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	// PUBLIC METHODS
	public void toggleProgress() {
		if (mContentVisible) showProgress();
		else showContent();
	}

	public void showProgress() {
		// TODO
		notifyListener();
	}

	public void showContent() {
		// TODO
		notifyListener();
	}

	public void setProgressChangedListener(OnProgressChangedListener listener) {
		mListener = listener;
	}

	// PRIVATE METHODS
	private void notifyListener() {
		if (mListener != null) mListener.onProgressChange(mContentVisible);
	}

	// INNER CLASSES
	public abstract static class OnProgressChangedListener {
		public abstract void onProgressChange(boolean contentVisible);
	}

}
