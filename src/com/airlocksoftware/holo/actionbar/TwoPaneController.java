package com.airlocksoftware.holo.actionbar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBarButton.DrawMode;
import com.airlocksoftware.holo.actionbar.ActionBarButton.Priority;
import com.airlocksoftware.holo.actionbar.interfaces.ActionBarController;
import com.airlocksoftware.holo.image.IconView;
import com.airlocksoftware.holo.type.FontText;
import com.airlocksoftware.holo.utils.Utils;
import com.airlocksoftware.holo.utils.ViewUtils;

/**
 * An ActionBar that is split it half (for tablets). The width of the left side is controlled by LEFT_PANE_DEFAULT_SIZE,
 * and should match the width in vw_actionbar_twopane (TODO move to R.dimen). Which side the title and buttons will be
 * added to is controlled by mActiveSide.
 **/
public class TwoPaneController implements ActionBarController {

	// CONTEXT & CONTAINERS
	private Context mContext;
	private ActionBarView mActionBar;
	private ViewGroup mControllerContainer;
	private ActionBarOverflow mOverflow;

	// STATE
	/** Controls which half of the ActionBar Buttons / Titles get added to. Default is right side. **/
	public Side mActiveSide = Side.RIGHT;
	private int mLeftPaneWidth;

	// VIEWS
	RelativeLayout mTitleLeft, mTitleRight;
	ViewGroup mButtonsLeft, mButtonsRight;
	TextView mTitleTextLeft, mTitleTextRight;
	IconView mOverflowIcon;

	private List<ActionBarButton> mLowRightButtons = new ArrayList<ActionBarButton>();
	private List<ActionBarButton> mHighRightButtons = new ArrayList<ActionBarButton>();
	private List<ActionBarButton> mLowLeftButtons = new ArrayList<ActionBarButton>();
	private List<ActionBarButton> mHighLeftButtons = new ArrayList<ActionBarButton>();

	// CONSTANTS
	private static final int TWO_PANE_LAYOUT_RES = R.layout.vw_actionbar_twopane;
	private static final int LEFT_PANE_DEFAULT_SIZE = 320;// in dp, matches value in vw_actionbar_twopane
	public static final int TWOPANE_LEFT_TAG = 0;
	public static final int TWOPANE_RIGHT_TAG = 1;

	private static final String TAG = ActionBarView.class.getSimpleName();

	private int ACTIONBAR_HEIGHT;

	// CONSTRUCTOR
	public TwoPaneController(Context context, ActionBarView actionBar) {
		mContext = context;
		mActionBar = actionBar;
		mControllerContainer = actionBar.getControllerContainer();
		mOverflow = actionBar.getOverflow();
		actionBar.setController(this);

		ACTIONBAR_HEIGHT = mContext.getResources()
																.getDimensionPixelSize(R.dimen.actionbar_height);
		this.setLeftPaneWidth(Utils.dpToPixels(context, LEFT_PANE_DEFAULT_SIZE));

		inflateLayout(LayoutInflater.from(mContext));
	}

	// ACTIONBAR CONTROLER INTERFACE
	@Override
	public void addButton(ActionBarButton button) {
		if (button.priority() == Priority.HIGH) {
			if (mActiveSide == Side.LEFT) mHighLeftButtons.add(button);
			else mHighRightButtons.add(button);
		} else {
			if (mActiveSide == Side.LEFT) mLowLeftButtons.add(button);
			else mLowRightButtons.add(button);
		}
		mActionBar.requestNeedsLayout();
	}

	@Override
	public void removeButton(ActionBarButton button) {
		switch (button.priority()) {
		case HIGH:
			mHighLeftButtons.remove(button);
			mHighRightButtons.remove(button);
			break;
		case LOW:
			mLowLeftButtons.remove(button);
			mLowRightButtons.remove(button);
			break;
		}
		int index = mButtonsLeft.indexOfChild(button);
		if (index != -1) {
			mButtonsLeft.removeViewAt(index);
		} else {
			index = mButtonsRight.indexOfChild(button);
			if (index != -1) mButtonsRight.removeViewAt(index);
			else mOverflow.removeView(button);
		}

	}

	@Override
	public void addOverflowView(View toAdd) {
		mOverflow.addCustomView(toAdd);
	}

	@Override
	public void setTitleText(String text) {
		if (mActiveSide == Side.LEFT) {
			if (text == null) mTitleTextLeft.setVisibility(View.GONE);
			else {
				mTitleTextLeft.setVisibility(View.VISIBLE);
				mTitleTextLeft.setText(text);
				clearTitleGroup();
			}
		} else {
			if (text == null) mTitleTextRight.setVisibility(View.GONE);
			else {
				mTitleTextRight.setVisibility(View.VISIBLE);
				mTitleTextRight.setText(text);
				clearTitleGroup();
			}

		}
	}

	@Override
	public void removeOverflowView(View toRemove) {
		mOverflow.removeView(toRemove);
		mActionBar.requestNeedsLayout();
	}

	@Override
	public RelativeLayout getTitleGroup() {
		RelativeLayout container = null;
		if (mActiveSide == Side.LEFT) {
			mTitleTextLeft.setVisibility(View.GONE);
			container = mTitleLeft;
		} else {
			mTitleTextRight.setVisibility(View.GONE);
			container = mTitleRight;
		}
		return container;
	}

	@Override
	public void clearTitleGroup() {
		if (mActiveSide == Side.LEFT) {
			for (View v : ViewUtils.directChildViews(mTitleLeft)) {
				if (v != mTitleTextLeft) mTitleLeft.removeView(v);
			}
		} else {
			for (View v : ViewUtils.directChildViews(mTitleRight)) {
				if (v != mTitleTextRight) mTitleRight.removeView(v);
			}
		}
	}

	@SuppressWarnings("unused")
	@Override
	/** Layout the ActionBar according to the following rules:
	 * 
	 * 1) Everything in the left pane gets laid out first
	 * 2) Place Priority.HIGH buttons first, then the title container, then Priority.LOW buttons.
	 * 3) If a button won't fit in available space with respect to the previous rules, move it to the Overflow menu 
	 * 
	 * TODO This could probably be done better: do math upfront (i.e. number of HIGH and LOW buttons + 
	 * expected title width, then layout according to the results.
	 * **/
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// remove any views currently in the containers
		mButtonsLeft.removeAllViews();
		mButtonsRight.removeAllViews();
		mOverflow.removeActionBarButtons();

		// screen width - width of up button
		final int totalWidth = mControllerContainer.getMeasuredWidth();
		final int upButtonWidth = mActionBar.getUpButton()
																				.getMeasuredWidth();

		// --- SETUP LEFT SIDE ---
		final int totalLeftWidth = mLeftPaneWidth - upButtonWidth;
		int availableLeftWidth = mLeftPaneWidth - upButtonWidth;
		final int titleLeftWidth = mTitleLeft.getMeasuredWidth();
		boolean leftHasOverflowed = false;

		// high buttons
		for (ActionBarButton btn : mHighLeftButtons) {
			if (availableLeftWidth > ACTIONBAR_HEIGHT) {
				mButtonsLeft.addView(btn.drawMode(DrawMode.ICON_ONLY));
				availableLeftWidth -= ACTIONBAR_HEIGHT;
			} else {
				mOverflow.addButton(btn.drawMode(DrawMode.OVERFLOW));
				leftHasOverflowed = true;
			}
		}

		// title
		RelativeLayout.LayoutParams leftTitleParams = (LayoutParams) mTitleLeft.getLayoutParams();
		if (availableLeftWidth < titleLeftWidth) {
			leftTitleParams.width = availableLeftWidth;
			availableLeftWidth = 0;
		} else {
			leftTitleParams.width = titleLeftWidth;
			availableLeftWidth -= titleLeftWidth;
		}

		// low buttons
		for (ActionBarButton btn : mLowLeftButtons) {
			if (availableLeftWidth > ACTIONBAR_HEIGHT) {
				mButtonsLeft.addView(btn.drawMode(DrawMode.ICON_ONLY));
				availableLeftWidth -= ACTIONBAR_HEIGHT;
			} else {
				mOverflow.addButton(btn.drawMode(DrawMode.OVERFLOW));
				leftHasOverflowed = true;
			}
		}

		// finish by giving title any extra width to finish padding out the space
		if (availableLeftWidth > 0) {
			// TODO there's error introduced in here somewhere (possibly due to rounding?) and the -8 is a manual correction
			// and will probably need to be adjusted
			leftTitleParams.width += availableLeftWidth - 8;
			availableLeftWidth = 0;
		}
		mTitleLeft.setLayoutParams(leftTitleParams);

		// ---- SETUP RIGHT SIDE ---

		// setup variables for calculations
		final int totalRightWidth = totalWidth - totalLeftWidth;
		int availableRightWidth = totalRightWidth;
		final int titleRightWidth = mTitleRight.getMeasuredWidth();

		// do high priority buttons overflow?
		boolean highRightOverflow = totalRightWidth - mHighRightButtons.size() * ACTIONBAR_HEIGHT < 0;

		// do low priority buttons overflow?
		boolean lowRightOverflow = (totalRightWidth - titleRightWidth
				- (mHighRightButtons.size() + mLowRightButtons.size()) * ACTIONBAR_HEIGHT < 0 && mHighRightButtons.size() > 0);

		// does anything on the right side belong in Overflow
		boolean rightHasOverflowed = mOverflow.hasCustomViews() || highRightOverflow || lowRightOverflow;

		// show or hide overflow button
		if (rightHasOverflowed || leftHasOverflowed) {
			availableRightWidth -= ACTIONBAR_HEIGHT;
			mOverflowIcon.setVisibility(View.VISIBLE);
		} else mOverflowIcon.setVisibility(View.GONE);

		// high priority
		for (ActionBarButton h : mHighRightButtons) {
			if (availableRightWidth > ACTIONBAR_HEIGHT) {
				mButtonsRight.addView(h.drawMode(DrawMode.ICON_ONLY));
				availableRightWidth -= ACTIONBAR_HEIGHT;
			} else {
				mOverflow.addButton(h.drawMode(DrawMode.OVERFLOW));
			}
		}

		// then title
		RelativeLayout.LayoutParams rightTitleParams = (LayoutParams) mTitleRight.getLayoutParams();
		if (availableRightWidth < titleRightWidth) {
			rightTitleParams.width = availableRightWidth;
			availableRightWidth = 0;
		} else {
			rightTitleParams.width = LayoutParams.WRAP_CONTENT;
			availableRightWidth -= titleRightWidth;
		}
		mTitleRight.setLayoutParams(rightTitleParams);

		// then low btns
		for (ActionBarButton l : mLowRightButtons) {
			if (availableRightWidth > ACTIONBAR_HEIGHT) {
				mButtonsRight.addView(l.drawMode(DrawMode.ICON_ONLY));
				availableRightWidth -= ACTIONBAR_HEIGHT;
			} else {
				mOverflow.addButton(l.drawMode(DrawMode.OVERFLOW));
			}
		}
	}

	// PUBLIC METHODS
	/** Controls which half of the ActionBar we are adding Buttons & titles to. **/
	public void setActiveSide(Side currentSide) {
		mActiveSide = currentSide;
	}

	public void setLeftPaneWidth(int px) {
		mLeftPaneWidth = px;
		mActionBar.requestLayout();
	}

	// PRIVATE METHODS
	private void inflateLayout(LayoutInflater inflater) {
		inflater.inflate(TWO_PANE_LAYOUT_RES, mControllerContainer);

		mTitleLeft = (RelativeLayout) mControllerContainer.findViewById(R.id.cnt_title_left);
		mTitleTextLeft = (FontText) mControllerContainer.findViewById(R.id.txt_title_left);
		mTitleRight = (RelativeLayout) mControllerContainer.findViewById(R.id.cnt_title_right);
		mTitleTextRight = (FontText) mControllerContainer.findViewById(R.id.txt_title_right);
		mButtonsLeft = (ViewGroup) mControllerContainer.findViewById(R.id.cnt_btns_left);
		mButtonsRight = (ViewGroup) mControllerContainer.findViewById(R.id.cnt_btns_right);

		mOverflowIcon = (IconView) mControllerContainer.findViewById(R.id.icv_overflow);

		mOverflowIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActionBar.toggleOverflow();
			}
		});
	}

	// ENUMS
	public enum Side {
		LEFT, RIGHT;
	}

	/** Used to implement a HashMap that maps from a Side and Priority to a list of buttons. **/
	public class BtnKey {
		private Side mSide;
		private Priority mPriority;

		public BtnKey(Side side, Priority priority) {
			mSide = side;
			mPriority = priority;
		}

		@Override
		public int hashCode() {
			int code = 1;
			if (mSide != null) code += 2 * mSide.ordinal();
			if (mPriority != null) code += 3 * mPriority.ordinal();
			return code;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof BtnKey)) return false;
			BtnKey toCheck = (BtnKey) o;
			return toCheck.mPriority == this.mPriority && toCheck.mSide == this.mSide;
		}
	}
}
