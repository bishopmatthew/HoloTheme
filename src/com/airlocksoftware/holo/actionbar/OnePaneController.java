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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBarButton.DrawMode;
import com.airlocksoftware.holo.actionbar.ActionBarButton.Priority;
import com.airlocksoftware.holo.actionbar.interfaces.ActionBarController;
import com.airlocksoftware.holo.image.IconView;
import com.airlocksoftware.holo.type.FontText;
import com.airlocksoftware.holo.utils.ViewUtils;

/**
 * Inflates the layout of the ActionBar, finds it's views,
 * and acts as a controller for a one-pane ActionBarView.
 **/
public class OnePaneController implements ActionBarController {

	private ActionBarView mActionBar;
	private Context mContext;

	// VIEWS
	private ViewGroup mControllerContainer;

	private ViewGroup mTitleContainer, mButtonContainer;
	private IconView mOverflowIcon;
	private TextView mTitleText;

	private ActionBarOverflow mOverflow;

	private List<ActionBarButton> mHighButtons = new ArrayList<ActionBarButton>();
	private List<ActionBarButton> mLowButtons = new ArrayList<ActionBarButton>();

	// STATE

	// CONSTANTS
	private static final int ONE_PANE_LAYOUT_RES = R.layout.vw_actionbar_onepane;
	@SuppressWarnings("unused")
	private static final String TAG = OnePaneController.class.getSimpleName();
	private int ACTIONBAR_HEIGHT; // constant but has to be looked up at runtime

	// CONSTRUCTOR
	public OnePaneController(Context context, ActionBarView actionBar) {
		mContext = context;
		mActionBar = actionBar;
		mControllerContainer = actionBar.getControllerContainer();
		mOverflow = actionBar.getOverflow();
		actionBar.setController(this);

		ACTIONBAR_HEIGHT = mContext.getResources()
																.getDimensionPixelSize(R.dimen.actionbar_height);

		inflateLayout();
	}

	// ACTIONBAR CONTROLLER INTERFACE

	@Override
	public void addButton(ActionBarButton button) {
		if (button.priority() == Priority.HIGH) mHighButtons.add(button);
		else mLowButtons.add(button);
		mActionBar.requestNeedsLayout();
	}

	@Override
	public void removeButton(ActionBarButton button) {
		switch (button.priority()) {
		case HIGH:
			mHighButtons.remove(button);
			break;
		case LOW:
			mLowButtons.remove(button);
			break;
		}
		int index = mButtonContainer.indexOfChild(button);

		if (index != -1) mButtonContainer.removeViewAt(index);
		else mOverflow.removeView(button);
	}

	@Override
	public void addOverflowView(View toAdd) {
		mOverflow.addCustomView(toAdd);
	}

	@Override
	public void removeOverflowView(View toRemove) {
		mOverflow.removeView(toRemove);
		mActionBar.requestNeedsLayout();
	}

	@Override
	public void setTitleText(String text) {
		if (text == null) mTitleText.setVisibility(View.GONE);
		else {
			mTitleText.setVisibility(View.VISIBLE);
			mTitleText.setText(text);
			clearTitleGroup();
		}
	}

	@Override
	public ViewGroup getTitleGroup() {
		mTitleText.setVisibility(View.GONE);
		return mTitleContainer;
	}

	@Override
	public void clearTitleGroup() {
		for (View v : ViewUtils.directChildViews(mTitleContainer)) {
			if (v != mTitleText) mTitleContainer.removeView(v);
		}
	}

	@Override
	/** Determines where in the ActionBar various views should go. Gives priority to HIGH Priority buttons,
	 * then the title, then LOW Priority buttons. If the title won't fit, force it to by cutting off excess.
	 * If any buttons won't fit, move them to the OverflowMenu. **/
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// should add all views in one go, because the next onMeasure isn't called synchronously

		mButtonContainer.removeAllViews();
		mOverflow.removeActionBarButtons();

		// setup variables for calculations
		int available = mControllerContainer.getMeasuredWidth(); // available space

		int title = mTitleContainer.getMeasuredWidth(); // width the title container would like to have
		boolean highOverflow = available - mHighButtons.size() * ACTIONBAR_HEIGHT < 0; // do high pri. buttons overflow
		boolean lowOverflow = (available - title - (mHighButtons.size() + mLowButtons.size()) * ACTIONBAR_HEIGHT < 0 && mLowButtons.size() > 0);
		boolean overflow = mOverflow.hasCustomViews() || highOverflow || lowOverflow; // does anything belong in Overflow

		// show or hide overflow button
		if (overflow) {
			available -= ACTIONBAR_HEIGHT;
			mOverflowIcon.setVisibility(View.VISIBLE);
		} else mOverflowIcon.setVisibility(View.GONE);

		// high priority
		for (ActionBarButton h : mHighButtons) {
			if (available > ACTIONBAR_HEIGHT) {
				mButtonContainer.addView(h.drawMode(DrawMode.ICON_ONLY));
				available -= ACTIONBAR_HEIGHT;
			} else {
				mOverflow.addButton(h.drawMode(DrawMode.OVERFLOW));
			}
		}

		// then title
		RelativeLayout.LayoutParams params = (LayoutParams) mTitleContainer.getLayoutParams();
		if (available < title) {
			params.width = available;
			available = 0;
		} else {
			params.width = LayoutParams.WRAP_CONTENT;
			available -= title;
		}
		mTitleContainer.setLayoutParams(params);

		// then low btns
		for (ActionBarButton l : mLowButtons) {
			if (available > ACTIONBAR_HEIGHT) {
				mButtonContainer.addView(l.drawMode(DrawMode.ICON_ONLY));
				available -= ACTIONBAR_HEIGHT;
			} else {
				mOverflow.addButton(l.drawMode(DrawMode.OVERFLOW));
			}
		}
	}

	// PRIVATE METHODS
	private void inflateLayout() {
		LayoutInflater.from(mContext)
									.inflate(ONE_PANE_LAYOUT_RES, mControllerContainer);

		mTitleContainer = (RelativeLayout) mActionBar.findViewById(R.id.cnt_title);
		mTitleText = (FontText) mActionBar.findViewById(R.id.txt_actionbar_title);
		mButtonContainer = (LinearLayout) mActionBar.findViewById(R.id.cnt_buttons);
		mOverflowIcon = (IconView) mActionBar.findViewById(R.id.icv_overflow);

		mOverflowIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActionBar.toggleOverflow();
			}
		});
	}

}
