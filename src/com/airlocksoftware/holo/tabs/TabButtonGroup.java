package com.airlocksoftware.holo.tabs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airlocksoftware.holo.R;

/** Displays a vertical or horizontal group of TabButtons **/
public class TabButtonGroup extends LinearLayout {

	// CONTEXT
	private Context mContext;
	// tracks children radio buttons checked state
	private TabButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
	// when true, mOnCheckedChangeListener discards events
	private boolean mProtectFromCheckedChange = false;
	private OnTabButtonChangedListener mOnTabChangeListener;
	private PassThroughHierarchyChangeListener mPassThroughListener;

	// STATE
	private int mCheckedId = -1;
	private int mLastCheckedId = -1;

	// CONSTANTS
	private static final float DEFAULT_WEIGHT = 1.0f;

	// CONSTRUCTORS
	public TabButtonGroup(Context context) {
		this(context, null);
	}

	public TabButtonGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		retrieveAttrs(attrs);

		mChildOnCheckedChangeListener = new CheckedStateTracker();

		mPassThroughListener = new PassThroughHierarchyChangeListener();
		super.setOnHierarchyChangeListener(mPassThroughListener);
	}

	private void retrieveAttrs(AttributeSet attrs) {
		TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.TabButtonGroup, 0, 0);

		int value = attributes.getResourceId(R.styleable.TabButtonGroup_tabgroup_checkButton, View.NO_ID);
		if (value != View.NO_ID) {
			mCheckedId = value;
		}

		attributes.recycle();
	}

	public void addTabButton(TabButton tab) {
		this.addView(tab, generateDefaultLayoutParams());
	}

	public void addTab(TabButton tab, ViewGroup.LayoutParams params) {
		super.addView(tab, params);
	}

	@Override
	protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
		if (child instanceof TabButton) {
			this.addTab((TabButton) child, params);
		} else {
			throw new IllegalArgumentException("Can't add something that isn't a TabButton to TabButtonGroup");
		}
		return true;
	}

	@Override
	public void addView(View v) {
		if (v instanceof TabButton) {
			this.addTabButton((TabButton) v);
		} else {
			throw new IllegalArgumentException("Can't add something that isn't a TabButton to TabButtonGroup");
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		// checks the appropriate radio button as requested in the XML file,
		// otherwise it checks the first item
		if (mCheckedId == View.NO_ID) {
			if (getChildCount() > 0) mCheckedId = getChildAt(0).getId();
		}

		if (mCheckedId != View.NO_ID) {
			mProtectFromCheckedChange = true;
			setCheckedStateForView(mCheckedId, true);
			mProtectFromCheckedChange = false;
			setCheckedId(mCheckedId);
		}
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (child instanceof TabButton) {
			final TabButton button = (TabButton) child;
			if (button.isChecked()) {
				mProtectFromCheckedChange = true;
				if (mCheckedId != -1) {
					setCheckedStateForView(mCheckedId, false);
				}
				mProtectFromCheckedChange = false;
				setCheckedId(button.getId());
			}
			super.addView(child, index, params);
		} else {
			throw new IllegalArgumentException("Can't add something that isn't a TabButton to TabButtonGroup");
		}

	}

	/**
	 * <p>
	 * Sets the selection to the radio button whose identifier is passed in
	 * parameter. Using -1 as the selection identifier clears the selection;
	 * such an operation is equivalent to invoking {@link #clearCheck()}.
	 * </p>
	 * 
	 * @param id the unique id of the radio button to select in this group
	 * 
	 * @see #getCheckedRadioButtonId()
	 * @see #clearCheck()
	 */
	public void check(int id) {
		// don't even bother
		if (id != -1 && (id == mCheckedId)) {
			return;
		}

		if (mCheckedId != -1) {
			setCheckedStateForView(mCheckedId, false);
		}

		if (id != -1) {
			setCheckedStateForView(id, true);
		}

		setCheckedId(id);
	}

	private void setCheckedId(int id) {
		mLastCheckedId = mCheckedId;
		mCheckedId = id;
		if (mOnTabChangeListener != null) {
			mOnTabChangeListener.onTabButtonChanged(this, this.indexOfChildById(mCheckedId), this.indexOfChildById(mLastCheckedId));
		}
	}

	private void setCheckedStateForView(int viewId, boolean checked) {
		View checkedView = findViewById(viewId);
		if (checkedView != null && checkedView instanceof TabButton) {
			((TabButton) checkedView).setChecked(checked);
		}
	}

	/**
	 * <p>
	 * Returns the identifier of the selected tab button in this group. Upon
	 * empty selection, the returned value is -1.
	 * </p>
	 * 
	 * @return the unique id of the selected tab button in this group
	 * 
	 * @see #check(int)
	 * @see #clearCheck()
	 */
	public int getCheckedTabButtonId() {
		return mCheckedId;
	}

	public void setOnTabChangedListener(OnTabButtonChangedListener listener) {
		mOnTabChangeListener = listener;
	}

	/**
	 * <p>
	 * Clears the selection. When the selection is cleared, no radio button in
	 * this group is selected and {@link #getCheckedRadioButtonId()} returns
	 * null.
	 * </p>
	 * 
	 * @see #check(int)
	 * @see #getCheckedRadioButtonId()
	 */
	public void clearCheck() {
		check(-1);
	}
	
	private int indexOfChildById(int id) {
		return indexOfChild(findViewById(id));
	}

	// USING CUSTOM LAYOUT PARAMS
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		LayoutParams params = new TabButtonGroup.LayoutParams(getContext(), attrs);
		if (params.weight == 0.0f) params.weight = DEFAULT_WEIGHT;
		return params;
	}

	/**
	 * Returns a set of layout parameters with a width of
	 * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT},
	 * a height of {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT} and
	 * no spanning.
	 */
	@Override
	protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof TabButtonGroup.LayoutParams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	// INTERFACES
	/**
	 * Interface definition for a callback to be invoked when the selected tab
	 * changes.
	 */
	public interface OnTabButtonChangedListener {
		/**
		 * Called when the current tab has changed. When the selection
		 * is cleared, newId is -1.
		 * 
		 * @param group the group in which the checked radio button has changed
		 * @param checkedId the unique identifier of the newly checked radio
		 *            button
		 */
		public void onTabButtonChanged(TabButtonGroup group, int newPosition, int oldPosition);
	}

	// CLASSES

	private class CheckedStateTracker implements TabButton.OnCheckedChangeListener {
		public void onCheckedChanged(TabButton buttonView, boolean isChecked) {
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

	public static class LayoutParams extends LinearLayout.LayoutParams {

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		/**
		 * <p>
		 * Sets the child width and the child height.
		 * </p>
		 * 
		 * @param w the desired width
		 * @param h the desired height
		 */
		public LayoutParams(int w, int h) {
			super(w, h);
		}

		/**
		 * <p>
		 * Sets the child width to {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
		 * and the child height to
		 * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}.
		 * </p>
		 */
		public LayoutParams() {
			super(MATCH_PARENT, MATCH_PARENT, DEFAULT_WEIGHT);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(ViewGroup.LayoutParams p) {
			super(p);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		@Override
		protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
			// We don't want to force users to specify a layout_width
			if (a.hasValue(widthAttr)) {
				width = a.getLayoutDimension(widthAttr, "layout_width");
			} else {
				width = MATCH_PARENT;
			}

			// We don't want to force users to specify a layout_height
			if (a.hasValue(heightAttr)) {
				height = a.getLayoutDimension(heightAttr, "layout_height");
			} else {
				height = MATCH_PARENT;
			}
		}
	}

	/**
	 * <p>
	 * A pass-through listener acts upon the events and dispatches them to
	 * another listener. This allows the table layout to set its own internal
	 * hierarchy change listener without preventing the user to setup his.
	 * </p>
	 */
	private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
		private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

		/**
		 * {@inheritDoc}
		 */
		public void onChildViewAdded(View parent, View child) {
			if (parent == TabButtonGroup.this && child instanceof TabButton) {
				int id = child.getId();
				// generates an id if it's missing
				if (id == View.NO_ID) {
					id = child.hashCode();
					child.setId(id);
				}
				((TabButton) child).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
			}

			if (mOnHierarchyChangeListener != null) {
				mOnHierarchyChangeListener.onChildViewAdded(parent, child);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void onChildViewRemoved(View parent, View child) {
			if (parent == TabButtonGroup.this && child instanceof TabButton) {
				((TabButton) child).setOnCheckedChangeListener(null);
			}

			if (mOnHierarchyChangeListener != null) {
				mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
			}
		}
	}

}
