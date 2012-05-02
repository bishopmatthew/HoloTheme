package com.airlocksoftware.holo.anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
	RelativeLayout.LayoutParams mDefaultParams;
	RelativeLayout.LayoutParams mFillParams;
	boolean mIsFillParent = false;

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
		setupLayoutParams();
	}

	// PUBLIC METHODS
	
	public void setFillParent(boolean fill) {
		if(mDefaultParams == null || mFillParams == null) {
			setupLayoutParams();
		}
		this.setLayoutParams((fill) ? mFillParams : mDefaultParams);
	}
	
	public void showViewById(int id) {
		showViewById(id, mInAnim);
	}

	public void showViewById(int id, int inAnim) {
		showViewById(id, AnimationUtils.loadAnimation(mContext, inAnim));
	}

	public void showViewById(int id, Animation inAnim) {
		View toShow = findViewById(id);
		if(toShow == null) return;
		
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

	public void hideViewById(int id, Animation outAnim) {
		View toHide = findViewById(id);
		if (toHide == null) return;

		toHide.setAnimation(outAnim);
		toHide.getAnimation().start();
		toHide.setVisibility(GONE);

		mCurrentView = null;
		mVisible = false;
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
	
//	@Override
	

	// PRIVATE METHODS
	private void getAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AnimatedView);

		setDefaultInAnimation(a.getResourceId(R.styleable.AnimatedView_inAnim, DEFAULT_IN_ANIM));
		setDefaultOutAnimation(a.getResourceId(R.styleable.AnimatedView_outAnim, DEFAULT_OUT_ANIM));

		a.recycle();
	}
	
	private void setupLayoutParams() {
		mDefaultParams = (RelativeLayout.LayoutParams) getLayoutParams();
		mFillParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	

}
