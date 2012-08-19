package com.airlocksoftware.holo.slideout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.anim.AnimationParams;
import com.airlocksoftware.holo.anim.AnimationParams.Exclusivity;
import com.airlocksoftware.holo.anim.AnimationParams.FillType;
import com.airlocksoftware.holo.anim.OverlayManager;
import com.airlocksoftware.holo.anim.OverlayManager.AnimationFinishedListener;

/**
 * The frame the SlideoutView lives in. Consists of the ImageView that holds a screenshot of the
 * screen we're sliding over, and a FrameLayout to hold the content.
 **/
public class SlideoutFrame extends FrameLayout {

	private Bitmap mBitmap;
	private FrameLayout mSlideoutContent;
	private ImageView mScreenshotView;
	
	private View mShadowView;
	GradientDrawable mShadowGradient;

	private Context mContext;
	Activity mActivity;

	OverlayManager mOverlayManager;
	int mSlideWidth = 72; // default value

	private Animation mInAnimation;
	private Animation mOutAnimation;

	Screenshot mScreenshot;

	private boolean mOpen;
	private boolean mIsAnimating;

	// CONSTANTS
	private static final int DURATION_MS = 150;
	private static final int ID = R.id.slideout_frame;
	private static final int DEFAULT_LAYOUT = R.layout.slideout_frame;

	// CONSTRUCTOR
	public SlideoutFrame(Context context, Activity activity, OverlayManager overlay, int rootId) {
		super(context, null);
		mContext = context;
		mOverlayManager = overlay;
		mActivity = activity;

		setId(ID);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(DEFAULT_LAYOUT, this);
		mSlideoutContent = (FrameLayout) findViewById(R.id.slideout_content);
		mScreenshotView = (ImageView) findViewById(R.id.screenshot);
		mScreenshotView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsAnimating) close();
			}
		});

		mOverlayManager.addView(this,
				new AnimationParams(FillType.FILL_SCREEN).exclusivity(Exclusivity.EXCLUDE_ALL));
		mScreenshot = new Screenshot(mActivity, rootId);
	}

	// PUBLIC METHODS

	public void open() {
		mOpen = true;
		mIsAnimating = true;

		int screenWidth = getScreenWidth();
		int frameWidth = (2 * screenWidth) - mSlideWidth;

		LayoutParams params = (LayoutParams) this.getLayoutParams();
		params.width = frameWidth;
		params.gravity = Gravity.LEFT;
		this.setLayoutParams(params);

		mBitmap = mScreenshot.snap();
		mScreenshotView.setImageBitmap(mBitmap);

		mInAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
				-(screenWidth - mSlideWidth), TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE,
				0, TranslateAnimation.ABSOLUTE, 0);
		mInAnimation.setDuration(DURATION_MS);
		mInAnimation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
		mInAnimation.setFillAfter(true);

		mOverlayManager.showViewById(ID, mInAnimation, new AnimationFinishedListener() {

			@Override
			public void onFinished(View overlay) {
				mIsAnimating = false;
			}
		});
	}

	public void close() {
		mOpen = false;
		mIsAnimating = true;

		int screenWidth = getScreenWidth();

		mOutAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -(screenWidth - mSlideWidth), TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0);
		mOutAnimation.setDuration(DURATION_MS);
		mOutAnimation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);

		mOverlayManager.hideViewById(ID, mOutAnimation, new AnimationFinishedListener() {
			@Override
			public void onFinished(View overlay) {
				mIsAnimating = false;
			}
		});
	}

	public void toggle() {
		if (isOpen()) close();
		else open();
	}

	public void setSlideWidth(int slideWidth) {
		mSlideWidth = slideWidth;
	}
	
	public void showShadowEdge(int width) {
		mShadowView = new View(mContext, null);
		mShadowView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0));
	}
	
	public void hideShadowEdge() {
		
	}

	// PRIVATE METHODS

	@SuppressWarnings("deprecation")
	private int getScreenWidth() {
		Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		return display.getWidth();
	}

	public void setContentView(View child) {
		mSlideoutContent.addView(child);
	}

	public boolean isOpen() {
		return mOpen;
	}

}
