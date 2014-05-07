package com.airlocksoftware.holo.checkable;

import android.view.View;
import android.widget.Checkable;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows you to register CheckableViews as children, and then manage them like they were in a CheckableViewGroup even
 * if they are in different views.
 **/
public class CheckableViewManager {

	// STATE
	private List<CheckableView> mChildren = new ArrayList<CheckableView>();
	private CheckableView.OnCheckedChangeListener mChildOnCheckedChangeListener = new CheckedStateTracker();
	private OnCheckedViewChangedListener mOnCheckedChangedListener;
	public boolean mProtectFromCheckedChange;
	public int mCheckedId;

	private static final int NO_CHECKED_ID = -1;

	// CONSTRUCTOR
	public CheckableViewManager() {
		// default empty constructor
	}

	// PUBLIC METHODS
	public void register(CheckableView child) {
		mChildren.add(child);
		if (getChildCount() == 1) {
			protectedCheck(child.getId());
		}
		child.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
	}

	public void deregister(CheckableView child) {
		mChildren.remove(child);
		child.setOnCheckedChangeListener(null);

		if (child.getId() == mCheckedId) mCheckedId = NO_CHECKED_ID;
	}

	public void deregisterAll() {
		for (CheckableView child : mChildren) {
			child.setOnCheckedChangeListener(null);
		}
		mChildren.clear();
		mCheckedId = NO_CHECKED_ID;
	}

	/** Checks the view that matches id, if found. **/
	public void check(int id) {
		// don't even bother
		if (id != NO_CHECKED_ID && (id == mCheckedId)) {
			return;
		}

		if (mCheckedId != NO_CHECKED_ID) setCheckedStateForView(mCheckedId, false);

		if (id != NO_CHECKED_ID) setCheckedStateForView(id, true);
		else setCheckedStateForView(mCheckedId, false);

		setCheckedId(id);
	}

	/** Checks the view matching id, supressing any calls to listeners. **/
	public void protectedCheck(int id) {
		mProtectFromCheckedChange = true;
		check(id);
		mProtectFromCheckedChange = false;
	}

	public void clearCheck() {
		protectedCheck(NO_CHECKED_ID);
	}

	public CheckableView getChildAt(int location) {
		return mChildren.get(location);
	}

	public CheckableView findViewById(int id) {
		for (CheckableView v : mChildren) {
			if (v.getId() == id) return v;
		}
		return null;
	}

	// PRIVATE METHODS
	private void setCheckedId(int id) {
		int previousCheckedId = mCheckedId;
		mCheckedId = id;
		if (mOnCheckedChangedListener != null && !mProtectFromCheckedChange) {
			mOnCheckedChangedListener.onCheckedViewChanged(this, this.indexOfChildById(mCheckedId),
					this.indexOfChildById(previousCheckedId));
		}
	}

	private int getChildCount() {
		return mChildren.size();
	}

	private int indexOfChildById(int id) {
		return mChildren.indexOf(findViewById(id));
	}

	private void setCheckedStateForView(int viewId, boolean checked) {
		View checkedView = findViewById(viewId);
		if (checkedView != null && checkedView instanceof CheckableView) {
			((CheckableView) checkedView).setChecked(checked);
		}
	}

	public void setOnCheckedChangedListener(OnCheckedViewChangedListener listener) {
		mOnCheckedChangedListener = listener;
	}

	// INTERFACES
	/**
	 * Interface definition for a callback to be invoked when the selected tab
	 * changes.
	 */
	public interface OnCheckedViewChangedListener {
		/**
		 * Called when the checked view has changed. When the selection
		 * is cleared, newId is NO_CHECKED_ID.
		 * 
		 * @param group the group in which the checked radio button has changed
		 * @param newIndex the position of the newly selected CheckableView within this group
		 * @param oldIndex the position of the previously selected CheckableView within this group
		 */
		public void onCheckedViewChanged(CheckableViewManager manager, int newIndex, int oldIndex);
	}

	// INNER CLASSES
	private class CheckedStateTracker implements CheckableView.OnCheckedChangeListener {
		public void onCheckedChanged(CheckableView buttonView, boolean isChecked) {
			// prevents from infinite recursion
			if (mProtectFromCheckedChange) {
				return;
			}

			mProtectFromCheckedChange = true;
			if (mCheckedId != NO_CHECKED_ID) {
				setCheckedStateForView(mCheckedId, false);
			}
			mProtectFromCheckedChange = false;

			int id = buttonView.getId();
			setCheckedId(id);
		}
	}

}
