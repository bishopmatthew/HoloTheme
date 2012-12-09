package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.interfaces.ActionBarController;
import com.airlocksoftware.holo.anim.AnimationParams;
import com.airlocksoftware.holo.anim.AnimationParams.Exclusivity;
import com.airlocksoftware.holo.anim.AnimationParams.FillType;
import com.airlocksoftware.holo.anim.OverlayManager;
import com.airlocksoftware.holo.image.IconView;

public class ActionBarView extends RelativeLayout {

	private Context mContext;

	// CONTROLLER UPDATE
	ActionBarController mController;
	
	ViewGroup mControllerContainer;
	private static final int ACTIONBAR_FRAME = R.layout.vw_actionbar_frame;

	// VIEWS
	RelativeLayout mUpContainer;
	IconView mUpIndicator;
	ImageView mUpIcon;

	ActionBarOverflow mOverflow;
	OverlayManager mOverlayManager;

	private boolean mLayoutFinished = false;
	private boolean mNeedsLayout = false;

	public static final int ONE_PANE_LAYOUT = 0;
	public static final int TWO_PANE_LAYOUT = 1;

	// CONSTANTS
	private static final String TAG = ActionBarView.class.getSimpleName();
	private int ACTIONBAR_HEIGHT;

	public ActionBarView(Context context) {
		this(context, null);
	}

	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		ACTIONBAR_HEIGHT = mContext.getResources()
																.getDimensionPixelSize(R.dimen.actionbar_height);

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ActionBarView, 1, 0);

		mOverflow = new ActionBarOverflow(mContext);
		mOverflow.setId(R.id.root_overflow_menu);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(ACTIONBAR_FRAME, this);
		mControllerContainer = (ViewGroup) findViewById(R.id.cnt_actionbar_controller);

		int layoutType = a.getInt(R.styleable.ActionBarView_ab_layout_mode, ONE_PANE_LAYOUT);
		switch (layoutType) {
		case ONE_PANE_LAYOUT:
			// inflateOnePaneLayout(inflater);
			break;
		case TWO_PANE_LAYOUT:
			// inflateTwoPaneLayout(inflater);
			break;
		}

		a.recycle();

		mLayoutFinished = true;
	}

	// UP BUTTON
	public RelativeLayout upButton() {
		return mUpContainer;
	}

	public IconView upIndicator() {
		return mUpIndicator;
	}

	public ActionBarOverflow getOverflow() {
		return mOverflow;
	}

	// OVERFLOW
	public void toggleOverflow() {
		if (mOverlayManager == null) {
			throw new RuntimeException("You have to set the OverlayManager to use the ActionBarOverflow");
		} else if (mOverflow.hasCustomViews() || mOverflow.hasActionBarButtons()) {
			mOverlayManager.toggleViewById(R.id.root_overflow_menu, R.anim.scale_in, R.anim.scale_out);
		}
	}

	public void hideOverflow() {
		if (mOverlayManager == null) {
			throw new RuntimeException("You have to set the OverlayManager to use the ActionBarOverflow");
		}
		View overflowRoot = mOverlayManager.findViewById(R.id.root_overflow_menu);
		if (overflowRoot.getVisibility() == View.VISIBLE) mOverlayManager.hideView(overflowRoot, R.anim.scale_out);
	}

	public void showOverflow() {
		if (mOverlayManager == null) {
			throw new RuntimeException("You have to set the OverlayManager to use the ActionBarOverflow");
		} else if (mOverflow.hasCustomViews() || mOverflow.hasActionBarButtons()) {
			mOverlayManager.showViewById(R.id.root_overflow_menu, R.anim.scale_out);
		}
	}

	// OVERLAY MANAGER
	public ActionBarView overlayManager(OverlayManager om) {
		mOverlayManager = om;
		om.addView(mOverflow, new AnimationParams(FillType.CLIP_CONTENT).exclusivity(Exclusivity.EXCLUDE_ALL));
		return this;
	}

	public OverlayManager overlayManager() {
		return mOverlayManager;
	}
	
	// MISC
	/** Clients should use this method instead of findViewById() in order to find views in the Overflow **/
	public View findView(int id) {
		View v = findViewById(id);
		if (v == null && mOverflow != null) return mOverflow.findViewById(id);
		else return v;
	}

	@Override
	/** Determines where in the ActionBar various views should go. Gives priority to HIGH Priority buttons,
	 * then the title, then LOW Priority buttons. If the title won't fit, force it to by cutting off excess.
	 * If any buttons won't fit, move them to the OverflowMenu. **/
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mLayoutFinished) {
			// TODO get width and height of mControllerContainer instead
			 mController.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public ViewGroup getControllerContainer() {
		return mControllerContainer;
	}
	
	public ActionBarController getController() {
		return mController;
	}
	
	public void setController(ActionBarController controller) {
		mController = controller;
	}
}
