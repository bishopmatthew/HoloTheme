package com.airlocksoftware.holo.actionbar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBarButton.DrawMode;
import com.airlocksoftware.holo.actionbar.ActionBarButton.Priority;
import com.airlocksoftware.holo.actionbar.interfaces.ActionBarController;
import com.airlocksoftware.holo.image.IconView;
import com.airlocksoftware.holo.type.FontText;
import com.airlocksoftware.holo.utils.ViewUtils;

public class TwoPaneController implements ActionBarController {

	// CONTEXT & CONTAINERS
	private Context mContext;
	private ActionBarView mActionBar;
	private ViewGroup mControllerContainer;
	private ActionBarOverflow mOverflow;

	// STATE
	/** Controls which half of the ActionBar Buttons / Titles get added to. **/
	private boolean mAddToLeftSide = false;
	private int mLeftPaneWidth;

	// VIEWS
	ViewGroup mTitleLeft;
	TextView mTitleTextLeft;
	ViewGroup mButtonsLeft;
	ViewGroup mTitleRight;
	TextView mTitleTextRight;
	ViewGroup mButtonsRight;
	IconView mOverflowIcon;

	// TODO keep a separate reference for whichever is active (or just switch them all out when

	private List<ActionBarButton> mLowRightButtons = new ArrayList<ActionBarButton>();
	private List<ActionBarButton> mHighRightButtons = new ArrayList<ActionBarButton>();
	private List<ActionBarButton> mLowLeftButtons = new ArrayList<ActionBarButton>();
	private List<ActionBarButton> mHighLeftButtons = new ArrayList<ActionBarButton>();

	// CONSTANTS
	private static final int TWO_PANE_LAYOUT_RES = R.layout.vw_actionbar_twopane;
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

		inflateLayout(LayoutInflater.from(mContext));
	}

	// ACTIONBAR CONTROLER INTERFACE
	@Override
	public void addButton(ActionBarButton button) {
		if (button.priority() == Priority.HIGH) {
			if (mAddToLeftSide) mHighLeftButtons.add(button);
			else mHighRightButtons.add(button);
		} else {
			if (mAddToLeftSide) mLowLeftButtons.add(button);
			else mLowRightButtons.add(button);
		}
		mActionBar.requestLayout();
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
		if (mAddToLeftSide) {
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
		mActionBar.requestLayout();
	}

	@Override
	public ViewGroup getTitleGroup() {
		ViewGroup container = null;
		if (mAddToLeftSide) {
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
		if (mAddToLeftSide) {
			for (View v : ViewUtils.directChildViews(mTitleLeft)) {
				if (v != mTitleTextLeft) mTitleLeft.removeView(v);
			}
		} else {
			for (View v : ViewUtils.directChildViews(mTitleRight)) {
				if (v != mTitleTextRight) mTitleRight.removeView(v);
			}
		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mButtonsLeft.removeAllViews();
		mButtonsRight.removeAllViews();
		mOverflow.removeActionBarButtons();

		// screen width - width of up button
		int totalWidth = mControllerContainer.getMeasuredWidth();
		int upButtonWidth = mActionBar.getUpButton()
																	.getMeasuredWidth();

		// setup LEFT SIDE
		int availableLeftWidth = mLeftPaneWidth - upButtonWidth;
		int titleLeftWidth = mTitleLeft.getMeasuredWidth();
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
		RelativeLayout.LayoutParams params = (LayoutParams) mTitleLeft.getLayoutParams();
		if (availableLeftWidth < titleLeftWidth) {
			params.width = availableLeftWidth;
			availableLeftWidth = 0;
		} else {
			params.width = LayoutParams.WRAP_CONTENT;
			availableLeftWidth -= titleLeftWidth;
		}
		mTitleLeft.setLayoutParams(params);
		
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
		
		// setup right side

		// // should add all views in one go, because the next onMeasure isn't called synchronously
		//
		// mButtonContainer.removeAllViews();
		// mOverflow.removeActionBarButtons();
		//
		// // setup variables for calculations
		// int available = mControllerContainer.getMeasuredWidth(); // available space
		//
		// int title = mTitleContainer.getMeasuredWidth(); // width the title container would like to have
		// boolean highOverflow = available - mHighButtons.size() * ACTIONBAR_HEIGHT < 0; // do high pri. buttons overflow
		// boolean lowOverflow = (available - title - (mHighButtons.size() + mLowButtons.size()) * ACTIONBAR_HEIGHT < 0 &&
		// mLowButtons.size() > 0);
		// boolean overflow = mOverflow.hasCustomViews() || highOverflow || lowOverflow; // does anything belong in Overflow
		//
		// // show or hide overflow button
		// if (overflow) {
		// available -= ACTIONBAR_HEIGHT;
		// mOverflowIcon.setVisibility(View.VISIBLE);
		// } else mOverflowIcon.setVisibility(View.GONE);
		//
		// // high priority
		// for (ActionBarButton h : mHighButtons) {
		// if (available > ACTIONBAR_HEIGHT) {
		// mButtonContainer.addView(h.drawMode(DrawMode.ICON_ONLY));
		// available -= ACTIONBAR_HEIGHT;
		// } else {
		// mOverflow.addButton(h.drawMode(DrawMode.OVERFLOW));
		// }
		// }
		//
		// // then title
		// RelativeLayout.LayoutParams params = (LayoutParams) mTitleContainer.getLayoutParams();
		// if (available < title) {
		// params.width = available;
		// available = 0;
		// } else {
		// params.width = LayoutParams.WRAP_CONTENT;
		// available -= title;
		// }
		// mTitleContainer.setLayoutParams(params);
		//
		// // then low btns
		// for (ActionBarButton l : mLowButtons) {
		// if (available > ACTIONBAR_HEIGHT) {
		// mButtonContainer.addView(l.drawMode(DrawMode.ICON_ONLY));
		// available -= ACTIONBAR_HEIGHT;
		// } else {
		// mOverflow.addButton(l.drawMode(DrawMode.OVERFLOW));
		// }
		// }
	}

	// PUBLIC METHODS
	/** Controls which half of the ActionBar we are adding Buttons & titles to. **/
	public void setAddToLeftSide(boolean addToLeft) {
		mAddToLeftSide = addToLeft;
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
		mTitleTextRight = (FontText) mControllerContainer.findViewById(R.id.txt_title_left);
		mButtonsLeft = (LinearLayout) mControllerContainer.findViewById(R.id.cnt_btns_left);
		mButtonsRight = (LinearLayout) mControllerContainer.findViewById(R.id.cnt_btns_right);

		mOverflowIcon = (IconView) mControllerContainer.findViewById(R.id.icv_overflow);

		mOverflowIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActionBar.toggleOverflow();
			}
		});
	}

}
