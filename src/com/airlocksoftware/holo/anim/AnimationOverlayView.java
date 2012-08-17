package com.airlocksoftware.holo.anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;

/**
 * A FrameLayout for holding one or more views that can display on top of the rest of the layout,
 * and who's visibility can be animated.
 **/
public class AnimationOverlayView extends FrameLayout {

	// CONTEXT
	Context mContext;
	
	private AnimationFillType mFillType;
	RelativeLayout.LayoutParams mClipActionBarParams;
	RelativeLayout.LayoutParams mFillScreenParams;

	// STATE
	private boolean mVisible = false;
	View mCurrentView;

	// DEFAULT ANIMATIONS (for views that don't specify their own)
	private int mInAnimResId;
	private int mOutAnimResId;
	private Animation mInAnim;
	private Animation mOutAnim;
	
	// if a view has a custom outAnim it's stored here to be used in the event of hideAll()
	private Animation mTempOutAnim;

	// CONSTANTS
	private static final int DEFAULT_IN_ANIM = R.anim.scale_in;
	private static final int DEFAULT_OUT_ANIM = R.anim.scale_out;

	// CONSTRUCTOR
	public AnimationOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		getAttributes(attrs);
		
		animationFillType(AnimationFillType.CLIP_ACTION_BAR); // default
	}

	// PUBLIC METHODS
	
	/** Set animation fill type**/
	public void animationFillType(AnimationFillType fill) {
		if(true) return;
		mFillType = fill;
		
		if(mClipActionBarParams != null && mFillScreenParams != null) {
			switch(fill) {
			case CLIP_ACTION_BAR:
				setLayoutParams(mClipActionBarParams);
				break;
			case FILL_SCREEN:
				setLayoutParams(mFillScreenParams);
				break;
			default:
				throw new RuntimeException("Couldn't find a matching AnimationFillType for " + fill.name());
			} 
			ViewParent parent = getParent();
			parent.requestLayout();
		}
	}
	
	public void showViewById(int id) {
		showViewById(id, mInAnim);
	}
	
	public void showViewById(int id, int inAnim) {
		showViewById(id, AnimationUtils.loadAnimation(mContext, inAnim), null);
	}


	public void showViewById(int id, int inAnim, OpenListener listener) {
		showViewById(id, AnimationUtils.loadAnimation(mContext, inAnim), listener);
	}
	
	public void showViewById(int id, Animation inAnim) {
		showViewById(id, inAnim, null);
	}

	public void showViewById(int id, Animation inAnim, OpenListener listener) {
		View toShow = findViewById(id);
		if(toShow == null) return;
		
		hideAll();
		
		toShow.setAnimation(inAnim);
		toShow.getAnimation().start();
		toShow.setVisibility(VISIBLE);
		
		mCurrentView = toShow;
		mVisible = true;
	}

	public void hideViewById(int id) {
		hideViewById(id, mOutAnim);
	}

	public void hideViewById(int id, int outAnim) {
		hideViewById(id, AnimationUtils.loadAnimation(mContext, outAnim));
	}
	
	public void hideViewById(int id, int outAnim, CloseListener listener) {
		hideViewById(id, AnimationUtils.loadAnimation(mContext, outAnim), listener);

	}

	public void hideViewById(int id, Animation outAnim) {
		hideViewById(id, outAnim, null);
	}
	
	public void hideViewById(int id, Animation outAnim, CloseListener listener) {
		final View toHide = findViewById(id);
		if (toHide == null) return;

		final CloseListener hideListener = listener;
		
		outAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				toHide.setVisibility(GONE);
				mCurrentView = null;
				mVisible = false;
				if(hideListener != null) hideListener.onCloseFinished(AnimationOverlayView.this);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}
		});

		toHide.setAnimation(outAnim);
		
		toHide.getAnimation().start();		
	}

	public void toggleViewById(int id) {
		toggleViewById(id, mInAnim, mOutAnim);
	}

	public void toggleViewById(int id, int inAnim, int outAnim) {
		if (mVisible) {
			hideViewById(id, outAnim);
		} else {
			showViewById(id, inAnim);
		}
	}

	public void toggleViewById(int id, Animation inAnim, Animation outAnim) {
		if (mVisible) {
			hideViewById(id, outAnim);
		} else {
			showViewById(id, inAnim);
			mTempOutAnim = outAnim;
		}
	}

	public void removeViewById(int id) {
		removeView(findViewById(id));
	}

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
	
	public void hideAll() {
		for(int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if(child.getVisibility() == VISIBLE) {
				if(mTempOutAnim != null) {
					hideViewById(child.getId(), mTempOutAnim);
				} else {
					hideViewById(child.getId());
				}
			}
		}
	}

	// OVERRIDES
	@Override
	public void addView(View child) {
		child.setVisibility(GONE);
		super.addView(child);
	}

	@Override
	public void addView(View child, int index) {
		child.setVisibility(GONE);
		super.addView(child, index);
	}

	@Override
	public void addView(View child, int width, int height) {
		child.setVisibility(GONE);
		super.addView(child, width, height);
	}
	
	@Override
	public void addView(View child, ViewGroup.LayoutParams params) {
		child.setVisibility(GONE);
		super.addView(child, params);
	}
	
	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		child.setVisibility(GONE);
		super.addView(child, index, params);
	}

	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		setupLayoutParams();
	}

	// PRIVATE METHODS

	private void setupLayoutParams() {
		mClipActionBarParams = (RelativeLayout.LayoutParams) getLayoutParams();
		mFillScreenParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.animationFillType(mFillType);
	}

	private void getAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AnimatedView);

		setDefaultInAnimation(a.getResourceId(R.styleable.AnimatedView_inAnim, DEFAULT_IN_ANIM));
		setDefaultOutAnimation(a.getResourceId(R.styleable.AnimatedView_outAnim, DEFAULT_OUT_ANIM));

		a.recycle();
	}
	
	// ENUMS
	public enum AnimationFillType {
		FILL_SCREEN, CLIP_ACTION_BAR;
	}
	
	// INNER CLASSES
	public interface CloseListener {
		public void onCloseFinished(AnimationOverlayView overlay);
	}
	
	public interface OpenListener {
		public void onOpenFinished(AnimationOverlayView overlay);
	}

	

}
