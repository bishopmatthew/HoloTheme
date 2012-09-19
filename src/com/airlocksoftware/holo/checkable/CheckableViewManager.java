package com.airlocksoftware.holo.checkable;

import java.util.ArrayList;
import java.util.List;

import com.airlocksoftware.holo.checkable.CheckableViewGroup.OnCheckedViewChangedListener;

import android.view.View;

public class CheckableViewManager {

	// STATE
	private List<CheckableView> mChildren = new ArrayList<CheckableView>();
	private CheckableView.OnCheckedChangeListener mChildOnCheckedChangeListener = new CheckedStateTracker();
	private OnCheckedViewChangedListener mOnCheckedChangedListener;
	public boolean mProtectFromCheckedChange;
	public int mCheckedId;
	private int mLastCheckedId;

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
	}

	public void deregisterAll() {
		for (CheckableView child : mChildren) {
			child.setOnCheckedChangeListener(null);
		}
		mChildren.clear();
	}

	/** Checks the view that matches id, if found. **/
	public void check(int id) {
		// don't even bother
		if (id != -1 && (id == mCheckedId)) {
			return;
		}

		if (mCheckedId != -1) setCheckedStateForView(mCheckedId, false);

		if (id != -1) setCheckedStateForView(id, true);
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
		protectedCheck(-1);
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
		mLastCheckedId = mCheckedId;
		mCheckedId = id;
		if (mOnCheckedChangedListener != null && !mProtectFromCheckedChange) {
			mOnCheckedChangedListener.onCheckedViewChanged(this, this.indexOfChildById(mCheckedId),
					this.indexOfChildById(mLastCheckedId));
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
		 * is cleared, newId is -1.
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
			if (mCheckedId != -1) {
				setCheckedStateForView(mCheckedId, false);
			}
			mProtectFromCheckedChange = false;

			int id = buttonView.getId();
			setCheckedId(id);
		}
	}

}
