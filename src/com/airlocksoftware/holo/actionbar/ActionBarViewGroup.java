package com.airlocksoftware.holo.actionbar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.ActionBarButton.DrawMode;
import com.airlocksoftware.holo.anim.AnimationParams;
import com.airlocksoftware.holo.anim.AnimationParams.Exclusivity;
import com.airlocksoftware.holo.anim.AnimationParams.FillType;
import com.airlocksoftware.holo.anim.OverlayManager;
import com.airlocksoftware.holo.image.IconView;
import com.airlocksoftware.holo.type.FontText;

public class ActionBarViewGroup extends RelativeLayout {

	private Context mContext;
	RelativeLayout mTitleContainer, mUpContainer;
	LinearLayout mButtonContainer;
	IconView mOverflowIcon, mUpIndicator;
	ImageView mUpIcon;
	FontText mTitleText;
	View mSpacer;

	// List<ActionBarButton> mHigh = new LinkedList<ActionBarButton>();
	// List<ActionBarButton> mLow = new LinkedList<ActionBarButton>();
	// List<ActionBarButton> mOverflowHigh = new LinkedList<ActionBarButton>();
	// List<ActionBarButton> mOverflowLow = new LinkedList<ActionBarButton>();
	// List<ActionBarButton> mNewHigh = new LinkedList<ActionBarButton>();
	List<ActionBarButton> mButtons = new ArrayList<ActionBarButton>();
	// private int mHighCount = 0;
	// private int mLowCount = 0;

	ActionBarOverflow mOverflow;
	OverlayManager mOverlayManager;

	private boolean mLayoutFinished = false;
	private boolean mNeedsLayout = false;

	// CONSTANTS
	private static final int DEFAULT_LAYOUT = R.layout.vw_actionbar;
	private static final String TAG = ActionBarViewGroup.class.getSimpleName();
	private int ACTIONBAR_HEIGHT;

	public ActionBarViewGroup(Context context) {
		this(context, null);
	}

	public ActionBarViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		ACTIONBAR_HEIGHT = mContext.getResources().getDimensionPixelSize(R.dimen.action_bar_height);

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ActionBarView, 1, 0);

		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			// TODO
			}
		}

		TypedValue tv = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.actionBarBg, tv, true);
		int background = tv.resourceId;
		setBackgroundResource(background);

		a.recycle();

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(DEFAULT_LAYOUT, this);
		mLayoutFinished = true;

		mUpContainer = (RelativeLayout) findViewById(R.id.cnt_up);
		mTitleContainer = (RelativeLayout) findViewById(R.id.cnt_title);
		mTitleText = (FontText) findViewById(R.id.txt_action_bar_title);
		mButtonContainer = (LinearLayout) findViewById(R.id.cnt_buttons);
		mOverflowIcon = (IconView) findViewById(R.id.icv_overflow);
		mUpIcon = (ImageView) findViewById(R.id.img_up_icon);
		mUpIndicator = (IconView) findViewById(R.id.icv_up_indicator);

		mOverflow = new ActionBarOverflow(mContext);
		mOverflow.setId(R.id.root_overflow_menu);
		mOverflowIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleOverflow();
			}
		});
	}
	
	public ActionBarViewGroup titleText(String text) {
		if(mTitleText != null) mTitleText.setText(text);
		return this;
	}
	
//	public ActionBarViewGroup upListener(OnClickListener l) {
//		if(mUpContainer != null) mUpContainer.setOnClickListener(l);
//		return this;
//	}
	
	public RelativeLayout upButton() {
		return mUpContainer;
	}

	public void toggleOverflow() {
		if (mOverlayManager == null) throw new RuntimeException(
				"You have to set the OverlayManager to use the ActionBarOverflow");
		mOverlayManager.toggleViewById(R.id.root_overflow_menu, R.anim.scale_in, R.anim.scale_out);
	}

	public ActionBarViewGroup overlayManager(OverlayManager om) {
		mOverlayManager = om;
		om.addView(mOverflow,
				new AnimationParams(FillType.CLIP_CONTENT).exclusivity(Exclusivity.EXCLUDE_ALL));
		return this;
	}

	public OverlayManager overlayManager() {
		return mOverlayManager;
	}

	/** Clients should use this method instead of findViewById() in order to find views in the Overflow **/
	public View findView(int id) {
		View v = findViewById(id);
		if (v == null && mOverflow != null) return mOverflow.findViewById(id);
		else return v;
	}

	@Override
	/** If called after layout, removes view from either one of the ActionBarButton containers, or from the overflow menu**/
	public void removeView(View v) {
		if (!mLayoutFinished) super.removeView(v);
		else {
			if (v instanceof ActionBarButton) {
				int index = mButtonContainer.indexOfChild(v);
				if (index != -1) {

					mButtonContainer.removeViewAt(index);
					// if(((ActionBarButton) v).priority() == Priority.HIGH) {
					//
					// } else {
					//
					// }
				} else mOverflow.removeView(v);
			}
		}
	}

	// OVERRIDE THIS addView SINCE ALL OTHERS ARE ROUTED THROUGH IT
	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (!mLayoutFinished) super.addView(child, index, params);
		else {
			if (child instanceof ActionBarButton) {
				ActionBarButton btn = (ActionBarButton) child;
				mButtons.add(btn);
				mNeedsLayout = true;
				// switch (btn.priority()) {
				// case HIGH:
				// mNewHigh.add((ActionBarButton) child);
				// break;
				// case LOW:
				// mNewLow.add((ActionBarButton) child);
				// break;
				// }
			} else {
				// custom view
				mOverflow.addCustomView(child);
			}
			// have to request layout & invalidate so onMeasure can be called and buttons can be added to the appropriate
			// location (including activating the overflow button if necessary
			requestLayout();
			invalidate();
		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mLayoutFinished && mNeedsLayout) {
			// should add all views in one go, because the next onMeasure isn't called synchronously

			mButtonContainer.removeAllViews();
			mOverflow.removeActionBarButtons();

			List<ActionBarButton> hButtons = new ArrayList<ActionBarButton>();
			List<ActionBarButton> lButtons = new ArrayList<ActionBarButton>();

			for (ActionBarButton btn : mButtons) {
				switch (btn.priority()) {
				case HIGH:
					hButtons.add(btn);
					break;
				case LOW:
					lButtons.add(btn);
					break;
				}
			}

			int width = getMeasuredWidth();
			int up = mUpContainer.getMeasuredWidth();
			int available = width - up;

			int title = mTitleContainer.getMeasuredWidth();
			boolean highOverflow = available - hButtons.size() * ACTIONBAR_HEIGHT < 0;
			boolean lowOverflow = (available - title - mButtons.size() * ACTIONBAR_HEIGHT < 0 && lButtons
					.size() > 0);
			boolean overflow = mOverflow.hasCustomViews() || highOverflow || lowOverflow;

			if (overflow) {
				available -= ACTIONBAR_HEIGHT;
				mOverflowIcon.setVisibility(VISIBLE);
			} else mOverflowIcon.setVisibility(GONE);

			// if(available - title - hButtons.size() * ACTIONBAR_HEIGHT <= 0) {
			// RelativeLayout.LayoutParams params = (LayoutParams) mTitleContainer.getLayoutParams();
			// params.width =
			//
			// }

			// high priority
			for (ActionBarButton h : hButtons) {
				if (available > ACTIONBAR_HEIGHT) {
					mButtonContainer.addView(h.drawMode(DrawMode.ICON_ONLY));
					available -= ACTIONBAR_HEIGHT;
				} else {
					mOverflow.addButton(h.drawMode(DrawMode.OVERFLOW));
				}
			}

			// then title
			// RelativeLayout.LayoutParams params = (LayoutParams) mTitleContainer.getLayoutParams();
			// if (available < title) {
			// params.width = available;
			// available = 0;
			// } else {
			// params.width = LayoutParams.WRAP_CONTENT;
			// available -= title;
			// }
			// mTitleContainer.setLayoutParams(params);
			available -= title;

			// the low btns
			for (ActionBarButton l : lButtons) {
				if (available > ACTIONBAR_HEIGHT) {
					mButtonContainer.addView(l.drawMode(DrawMode.ICON_ONLY));
					available -= ACTIONBAR_HEIGHT;
				} else {
					mOverflow.addButton(l.drawMode(DrawMode.OVERFLOW));
				}
			}

			mNeedsLayout = false;

			// int titleWidth = mTitleContainer.getMeasuredWidth();
			// int freeSpace = width - upWidth - titleWidth;
			// int buttonSpaces = freeSpace / buttonWidth;
			// int extraSpace = freeSpace % buttonWidth;
			//
			// int freespace = mSpacer.getMeasuredWidth();
			// int buttonWidth = ACTIONBAR_HEIGHT;
			// int numEmptySpaces = freespace / buttonWidth;
			//
			// boolean overflowIconMustDisplay = mOverflow.hasCustomViews();
			// if (overflowIconMustDisplay) {
			// mOverflowIcon.setVisibility(VISIBLE);
			// // buttonSpaces--;
			// }
			//
			// for (int i = 0; i < newButtons; i++) {
			// newHigh = mNewHigh.size();
			// newLow = mNewLow.size();
			// newButtons = newHigh + newLow;
			// ActionBarButton b;
			// if (numEmptySpaces > 0) {
			// if (newHigh > 0) {
			// b = mNewHigh.remove(0);
			// mHigh.add(b);
			// } else {
			// b = mNewLow.remove(0);
			// mLow.add(b);
			// }
			// mButtons.addView(b);
			// numEmptySpaces--;
			// } else {
			// if (newHigh > 0) {
			// b = mNewHigh.remove(0);
			// if (mLow.size() > 0) {
			// View old = mLow.remove(0);
			// mButtons.removeView(old);
			//
			// }
			// mHigh.add(b);
			// mButtons.addView(b);
			// } else {
			// b = mNewLow.remove(0);
			// mLow.add(b);
			// mOverflow.addView(b);
			// }
			// }
			// }

			// if (freespace > buttonWidth) {
			// if (high > 0) {
			// ActionBarButton b = mNewHigh.remove(0);
			// mHigh.add(b);
			// mButtons.addView(b);
			// } else {
			// ActionBarButton b = mNewLow.remove(0);
			// mLow.add(b);
			// mButtons.addView(b);
			// }
			// }

			// int width = this.getMeasuredWidth();
			// int upWidth = mUpContainer.getMeasuredWidth();
			// int titleWidth = mTitleContainer.getMeasuredWidth();
			// int titleWidth = mTitleContainer.getMeasuredWidth();
			// int freeSpace = width - upWidth - titleWidth;
			// int buttonSpaces = freeSpace / buttonWidth;
			// int extraSpace = freeSpace % buttonWidth;
			// Log.d(TAG, "Can add " + buttonSpaces + " to the ActionBar with " + extraSpace
			// + "px left over");
			// boolean overflowIconMustDisplay = mOverflow.hasCustomViews();
			// if (overflowIconMustDisplay) {
			// mOverflowIcon.setVisibility(VISIBLE);
			// buttonSpaces--;
			// }
			// int index = 0;
			// while (buttonSpaces > 0) {
			// ActionBarButton b = mButtons.get(index);
			// // b.getParent
			// }
		}
	}
}
