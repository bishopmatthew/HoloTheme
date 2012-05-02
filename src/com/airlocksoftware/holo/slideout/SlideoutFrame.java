package com.airlocksoftware.holo.slideout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.anim.AnimationOverlayView;
import com.airlocksoftware.holo.interfaces.AnimationOverlay;
import com.korovyansk.android.slideout.utils.Screenshot;

public class SlideoutFrame extends FrameLayout {

	private Bitmap mBitmap;
	private FrameLayout mSlideoutContent;
	private ImageView mScreenshotView;

	AnimationOverlayView mOverlayView;
	AnimationOverlay mOverlay;
	private Context mContext;
	Activity mActivity;
	int mSlideWidth;

	private Animation mInAnimation;
	private Animation mOutAnimation;

	Screenshot mScreenshot;

	private boolean mOpen;
	private boolean mIsAnimating;

	// CONSTANTS
	private static final int DURATION_MS = 150;

	// CONSTRUCTOR
	public SlideoutFrame(Context context, Activity activity, AnimationOverlay overlay, int slideWidth) {
		super(context, null);
		mContext = context;
		mOverlay = overlay;
		mOverlayView = mOverlay.getAnimationOverlayView();
		mActivity = activity;
		mSlideWidth = slideWidth;

		inflateLayout();

		this.setId(R.id.slideout_frame);
		mOverlayView.addView(this);

		mScreenshot = new Screenshot(mActivity, R.id.action_bar_activity_root);
	}

	// PUBLIC METHODS
	
	public void showSlideout() {
		mOpen = true;

		int screenWidth = getScreenWidth();
		int frameWidth = (2 * screenWidth) - mSlideWidth;

		LayoutParams params = (LayoutParams) this.getLayoutParams();
		params.width = frameWidth;
		params.gravity = Gravity.LEFT;
		this.setLayoutParams(params);

		mBitmap = mScreenshot.snap();
		mScreenshotView.setImageBitmap(mBitmap);

		mInAnimation = new TranslateAnimation(	
				TranslateAnimation.ABSOLUTE, -(screenWidth - mSlideWidth),
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);
		mInAnimation.setDuration(DURATION_MS);
		mInAnimation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
		mInAnimation.setFillAfter(true);
		mInAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setAnimation(null);
				LayoutParams params = (LayoutParams) getLayoutParams();
				params.gravity = Gravity.LEFT;
				setLayoutParams(params);
				mIsAnimating = false;
			}
		});

		mOverlayView.setFillParent(true);
		mIsAnimating = true;
		mOverlayView.showViewById(R.id.slideout_frame, mInAnimation);
	}

	public void setSlideWidth(int slideWidth) {
		mSlideWidth = slideWidth;
	}

	public void toggle() {
		if (mOpen) closeSlideout();
		else showSlideout();
	}

	public void closeSlideout() {
		mOpen = false;

		int screenWidth = getScreenWidth();

		mOutAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -(screenWidth - mSlideWidth),
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0);
		mOutAnimation.setDuration(DURATION_MS);
		mOutAnimation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
		mOutAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mOverlayView.setFillParent(false);
				mIsAnimating = false;
			}
		});

		mIsAnimating = true;
		mOverlayView.hideViewById(R.id.slideout_frame, mOutAnimation);
	}
	
	// PRIVATE METHODS
	
	private void inflateLayout() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.slideout_frame, this);
		mSlideoutContent = (FrameLayout) findViewById(R.id.slideout_content);
		mScreenshotView = (ImageView) findViewById(R.id.screenshot);
		mScreenshotView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsAnimating) closeSlideout();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private int getScreenWidth() {
		Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		// Point size = new Point();
		// display.getSize(size);
		// return size.x;
		return display.getWidth();
	}

	public void setSlideoutContent(View child) {
		mSlideoutContent.addView(child);
	}

	public boolean isOpen() {
		return mOpen;
	}

}
