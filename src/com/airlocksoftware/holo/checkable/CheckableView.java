package com.airlocksoftware.holo.checkable;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;

public class CheckableView extends FrameLayout implements Checkable {

	// STATE
	private Context mContext;

	private boolean mChecked;
	private boolean mBroadcasting;

	private OnCheckedChangeListener mOnCheckedChangeListener;

	// CONSTANTS
	private static final int[] STATE_CHECKED = { android.R.attr.state_checked };

	public CheckableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		retrieveAttrs(attrs, 0);
	}

	// PRIVATE METHODS
	private void retrieveAttrs(AttributeSet attrs, int defStyle) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.CheckableViewGroup, defStyle, 0);

		boolean checked = a.getBoolean(R.styleable.CheckableView_android_checked, false);
		setChecked(checked);

		// things that I might want to make attributes in the future
		this.setClickable(true);

		a.recycle();
	}

	// PUBLIC METHODS

	@Override
	public boolean performClick() {
		/* When clicked, toggle the state */
		toggle();
		return super.performClick();
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			refreshDrawableState();

			// Avoid infinite recursions if setChecked() is called from a
			// listener
			if (mBroadcasting) {
				return;
			}

			mBroadcasting = true;
			if (mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
			}

			mBroadcasting = false;
		}

	}

	@Override
	public void toggle() {
		if (!isChecked()) {
			setChecked(!mChecked);
		}
	}

	// CHECKED CHANGED LISTENER
	/**
	 * Register a callback to be invoked when the checked state of this button
	 * changes.
	 **/
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	/**
	 * Interface definition for a callback to be invoked when the checked state
	 * of a compound button changed.
	 */
	public static interface OnCheckedChangeListener {
		/**
		 * Called when the checked state of a compound button has changed.
		 */
		void onCheckedChanged(CheckableView checkableView, boolean isChecked);
	}

	// DRAWABLE STATE
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (mChecked) {
			mergeDrawableStates(drawableState, STATE_CHECKED);
		}
		return drawableState;
	}

}
