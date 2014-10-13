package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.interfaces.ActionBarController;
import com.airlocksoftware.holo.anim.AnimationParams;
import com.airlocksoftware.holo.anim.AnimationParams.Exclusivity;
import com.airlocksoftware.holo.anim.AnimationParams.FillType;
import com.airlocksoftware.holo.anim.OverlayManager;
import com.airlocksoftware.holo.image.IconView;
import com.airlocksoftware.holo.utils.ViewUtils;

/**
 * A 2.1+ compatible replacement for the ActionBar in Android 3.0+. As of now (Jan 2013) I would recommend that you
 * use ActionBarSherlock from Jake Wharton instead. This implementation does have some interesting properties though:
 * - the content is set by ActionBarController, which allows you to create different types of layouts (e.g. a split-pane
 * ActionBar)
 * - uses OverlayManager so you can use custom animation on the Overflow menu
 * - customize the icon that the UpButton displays
 * - use custom fonts in the title
 **/
public class ActionBarView extends RelativeLayout {

	private Context mContext;
	ActionBarController mController;

	ViewGroup mControllerContainer;
	View mUpContainer, mUpIcon;
	IconView mUpIndicator;

	ActionBarOverflow mOverflow;
	OverlayManager mOverlayManager;

	private boolean mLayoutFinished = false;
	private boolean mNeedsLayout = false;

	// CONSTANTS
	private static final String TAG = ActionBarView.class.getSimpleName();
	private static final int ACTIONBAR_FRAME = R.layout.vw_actionbar_frame;

	public ActionBarView(Context context) {
		this(context, null);
	}

	@SuppressWarnings("deprecation")
	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		mOverflow = new ActionBarOverflow(mContext);
		mOverflow.setId(R.id.root_overflow_menu);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(ACTIONBAR_FRAME, this);

		mControllerContainer = (ViewGroup) findViewById(R.id.cnt_actionbar_controller);
		mUpContainer = findViewById(R.id.cnt_up);
		mUpIndicator = (IconView) findViewById(R.id.icv_up_indicator);
		mUpIcon = findViewById(R.id.img_up_icon);

		// get the background drawable & make sure it repeats properly
		TypedValue typedValue = new TypedValue();
		mContext.getTheme()
						.resolveAttribute(R.attr.actionBarBg, typedValue, true);
		Drawable d = getResources().getDrawable(typedValue.resourceId);
		ViewUtils.fixDrawableRepeat(d);
		setBackgroundDrawable(d);

		mLayoutFinished = true;
	}

	// UP BUTTON
	public View getUpButton() {
		return mUpContainer;
	}

	public IconView getUpIndicator() {
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
	public ActionBarView setOverlayManager(OverlayManager om) {
		mOverlayManager = om;
		om.addView(mOverflow, new AnimationParams(FillType.CLIP_CONTENT).exclusivity(Exclusivity.EXCLUDE_ALL));
		return this;
	}

	public OverlayManager getOverlayManager() {
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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mLayoutFinished && mNeedsLayout) {
			mController.onMeasure(mControllerContainer.getMeasuredWidth(), mControllerContainer.getMeasuredHeight());
			mNeedsLayout = false;
		}
	}

	public void requestNeedsLayout() {
		mNeedsLayout = true;
		requestLayout();
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
