package com.airlocksoftware.holo.anim;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.anim.AnimationParams.Exclusivity;
import com.airlocksoftware.holo.anim.AnimationParams.FillType;
import com.airlocksoftware.holo.interfaces.OnBackPressedListener;

public class OverlayManager implements OnBackPressedListener {

	// STATE
	Map<AnimationParams.FillType, FrameLayout> mFrames = new HashMap<AnimationParams.FillType, FrameLayout>();
	Map<View, AnimationParams> mAnimationParams = new HashMap<View, AnimationParams>();

	private Context mContext;
	private Window mWindow;
	FrameLayout mRoot;

	// animations
	private int mInAnimResId;
	private int mOutAnimResId;
	private Animation mInAnim;
	private Animation mOutAnim;
	
	private boolean mHasOpenViews = false;

	private OnClickListener hideListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			hideAllViews();
		}
	};

	// CONSTANTS
	private static final int DEF_IN_ANIM = R.anim.scale_in;
	private static final int DEF_OUT_ANIM = R.anim.scale_out;

	// CONSTRUCTOR
	public OverlayManager(Context context, Window window) {
		mContext = context;
		mWindow = window;
		mRoot = new FrameLayout(context, null);
		mRoot.setId(R.id.overlay_root);
		mWindow.addContentView(mRoot, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// TODO this is temporary
		setDefaultInAnimation(DEF_IN_ANIM);
		setDefaultOutAnimation(DEF_OUT_ANIM);
	}

	// PUBLIC METHODS

	// Adding and removing
	/** Adds the view, and initializes a new AnimationParams object **/
	public void addView(View v) {
		addView(v, new AnimationParams());
	}

	public void addView(View v, AnimationParams params) {
		mAnimationParams.put(v, params);
		FrameLayout frame = mFrames.get(params.fillType());
		if (frame == null) {
			frame = createFrame(params.fillType());
			mFrames.put(params.fillType(), frame);
		}

		frame.addView(v);
		v.setVisibility(View.GONE);
	}

	public void removeView(View v) {
		AnimationParams params = mAnimationParams.remove(v);
		FrameLayout frame = mFrames.get(params.fillType());
		frame.removeView(v);

	}

	public View findViewById(int id) {
		return mRoot.findViewById(id);
	}

	// showing and hiding
	/** Show view using the default Animations **/
	public void showViewById(int id) {
		showViewById(id, mInAnim);
	}

	/** Show view using an animation loaded from resource id **/
	public void showViewById(int id, int animResId) {
		showViewById(id, AnimationUtils.loadAnimation(mContext, animResId), null);
	}

	/**
	 * Show view using an animation loaded from resource id with an AnimationFinishedListener called at the end of the
	 * animation
	 **/
	public void showViewById(int id, int animResId, AnimationFinishedListener listener) {
		showViewById(id, AnimationUtils.loadAnimation(mContext, animResId), listener);
	}

	/** Show view using the provided animation **/
	public void showViewById(int id, Animation inAnim) {
		showViewById(id, inAnim, null);
	}

	/** Show view using the provided animation and AnimationFinishedListener. This is where all the work is done **/
	public void showViewById(int id, Animation inAnim, AnimationFinishedListener listener) {
		showView(findViewById(id), inAnim, listener);
	}

	private void showView(View view, Animation inAnim, AnimationFinishedListener listener) {
		final View toShow = view;
		AnimationParams params = mAnimationParams.get(toShow);
		if (toShow == null || params == null) return;
		if (params.inAnimation() != null) {
			inAnim = params.inAnimation();
		}

		if (params.exclusivity() == Exclusivity.EXCLUDE_ALL) {
			hideAllViews();
		}

		// makes it so clicking outside overlay view hides all views
		mRoot.setOnClickListener(hideListener);
		mHasOpenViews = true;

		final AnimationFinishedListener showListener = listener;

		inAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				if (showListener != null) showListener.onFinished(toShow);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		toShow.setVisibility(View.VISIBLE);
		toShow.startAnimation(inAnim);
		params.visible(true);
	}

	/** Hide view using the default Animations **/
	public void hideViewById(int id) {
		hideViewById(id, mOutAnim);
	}

	/** Hide view using animation loaded by resource id **/
	public void hideViewById(int id, int outAnim) {
		hideViewById(id, AnimationUtils.loadAnimation(mContext, outAnim));
	}

	/** Hide view using animation loaded by resource id with AnimationFinishedListener **/
	public void hideViewById(int id, int outAnim, AnimationFinishedListener listener) {
		hideViewById(id, AnimationUtils.loadAnimation(mContext, outAnim), listener);
	}

	/** Hide view using the provided animation **/
	public void hideViewById(int id, Animation outAnim) {
		hideViewById(id, outAnim, null);
	}

	/** Hide view using the provided animation with AnimationFinishedListener **/
	public void hideViewById(int id, Animation outAnim, AnimationFinishedListener listener) {
		hideView(findViewById(id), outAnim, listener);
	}

	public void hideView(View view, int outAnim) {
		hideView(view, AnimationUtils.loadAnimation(mContext, outAnim), null);
	}

	public void hideView(View view, Animation outAnim, AnimationFinishedListener listener) {
		final View toHide = view;
		AnimationParams params = mAnimationParams.get(toHide);
		if (toHide == null || params == null) return;

		final AnimationFinishedListener hideListener = listener;
		if (params.outAnimation() != null) {
			outAnim = params.outAnimation();
		}
		if (outAnim == null) {
			outAnim = mOutAnim;
		}

		outAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				toHide.setVisibility(View.GONE);
				mAnimationParams.get(toHide).visible(false);
				if (hideListener != null) hideListener.onFinished(toHide);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		toHide.startAnimation(outAnim);
	}

	public void toggleViewById(int id) {
		toggleViewById(id, mInAnim, mOutAnim);
	}

	public void toggleViewById(int id, int inAnim, int outAnim) {
		toggleViewById(id, AnimationUtils.loadAnimation(mContext, inAnim), AnimationUtils.loadAnimation(mContext, outAnim));
	}

	public void toggleViewById(int id, Animation inAnim, Animation outAnim) {
		View toToggle = findViewById(id);
		AnimationParams params = mAnimationParams.get(toToggle);

		if (params.visible()) {
			hideViewById(id, outAnim);
		} else {
			showViewById(id, inAnim);
		}
	}

	public void hideAllViews() {
		for (int i = 0; i < mRoot.getChildCount(); i++) {
			FrameLayout frame = (FrameLayout) mRoot.getChildAt(i);
			for (int j = 0; j < frame.getChildCount(); j++) {
				View child = frame.getChildAt(j);
				if (child.getVisibility() == View.VISIBLE) {
					hideViewById(child.getId());
				}
			}
		}
		mRoot.setOnClickListener(null);
		mRoot.setClickable(false);
		mHasOpenViews = false;
	}

	// setting animations
	public void setDefaultInAnimation(Animation inAnim) {
		mInAnim = inAnim;
		mInAnim = AnimationUtils.loadAnimation(mContext, mInAnimResId);
	}

	public void setDefaultInAnimation(int inAnim) {
		mInAnimResId = inAnim;
		mInAnim = AnimationUtils.loadAnimation(mContext, mInAnimResId);
	}

	public void setDefaultOutAnimation(Animation outAnim) {
		mOutAnim = outAnim;
		mOutAnim = AnimationUtils.loadAnimation(mContext, mOutAnimResId);
	}

	public void setDefaultOutAnimation(int outAnim) {
		mOutAnimResId = outAnim;
		mOutAnim = AnimationUtils.loadAnimation(mContext, mOutAnimResId);
	}

	// PRIVATE METHODS

	private FrameLayout createFrame(FillType fillType) {
		FrameLayout frame = new FrameLayout(mContext, null);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		Display display = mWindow.getWindowManager().getDefaultDisplay();
		int statusBarHeight = (int) Math.ceil(25 * mContext.getResources().getDisplayMetrics().density);
		int screenHeight = display.getHeight(); // deprecated

		int abHeight = (int) mContext.getResources().getDimension(R.dimen.actionbar_height);
		int contentHeight = screenHeight - statusBarHeight;

		switch (fillType) {
		case FILL_SCREEN:
			break;
		case CLIP_ACTION_BAR:
			lp.height = abHeight;
			lp.gravity = Gravity.TOP;
			break;
		case CLIP_CONTENT:
			lp.height = contentHeight - abHeight;
			lp.gravity = Gravity.BOTTOM;
			break;
		}

		frame.setLayoutParams(lp);
		mRoot.addView(frame);
		return frame;
	}

	// INNER CLASSES
	public interface AnimationFinishedListener {
		public void onFinished(View overlay);
	}

	@Override
	public boolean onBackPressed() {
		if(mHasOpenViews) {
			hideAllViews();
			return true;
		} else {
			return false;
		}
	}

}
