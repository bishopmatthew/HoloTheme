package com.airlocksoftware.holo.slideout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Debug;
import android.util.Log;
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
import com.airlocksoftware.holo.interfaces.OnStopListener;
import com.airlocksoftware.holo.utils.Utils;

/**
 * An old implementation of SlidingMenu I wrote. Take a screenshot of the content, then displays an animation of the new
 * content and the screenshot sliding over the content.
 * 
 * The frame the SlideoutView lives in. Consists of the ImageView that holds a screenshot of the
 * screen we're sliding over, and a FrameLayout to hold the content.
 **/
public class SlideoutFrame extends FrameLayout implements OnStopListener {

	private FrameLayout mSlideoutContent;
	private ImageView mScreenshotView;

	private View mShadowView;
	GradientDrawable mShadowGradient;
	Bitmap mBitmap;

	private Context mContext;
	Activity mActivity;

	OverlayManager mOverlayManager;
	private int mSlideWidth = 72; // default value
	private int mRootId;
	private View mRootView;

	private Animation mInAnimation;
	private Animation mOutAnimation;

	private boolean mOpen;
	private boolean mIsAnimating;
	private int mDuration = 4000; // default

	// CONSTANTS
	private static final int ID = R.id.slideout_frame;
	private static final int DEFAULT_LAYOUT = R.layout.vw_slideoutframe;
	private static final String TAG = SlideoutFrame.class.getSimpleName();

	// CONSTRUCTOR
	public SlideoutFrame(Context context, Activity activity, OverlayManager overlay, int rootId) {
		super(context, null);
		mContext = context;
		mOverlayManager = overlay;
		mActivity = activity;
		mRootId = rootId;

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

		// we pre-create the Bitmap to speed up the opening animation
		mRootView = mActivity.findViewById(mRootId);
		mBitmap = createFullscreenBitmap(mContext);

		mOverlayManager.addView(this, new AnimationParams(FillType.FILL_SCREEN).exclusivity(Exclusivity.EXCLUDE_ALL));
	}

	// PUBLIC METHODS

	public void open() {
		// TODO DEBUG
		// android.os.Debug.startMethodTracing("slideout", 1000 * 1000 * 60);

		mOpen = true;
		mIsAnimating = true;

		int screenWidth = getScreenWidth();
		int frameWidth = (2 * screenWidth) - mSlideWidth;

		LayoutParams params = (LayoutParams) this.getLayoutParams();
		params.width = frameWidth;
		params.gravity = Gravity.LEFT;
		this.setLayoutParams(params);

		mScreenshotView.setImageBitmap(SlideoutFrame.loadBitmapFromView(mBitmap, mRootView));

		this.setDrawingCacheEnabled(true);

		mInAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, -(screenWidth - mSlideWidth),
				TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);
		mInAnimation.setDuration(mDuration);
		mInAnimation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
		mInAnimation.setFillAfter(true);

		mOverlayManager.showViewById(ID, mInAnimation, new AnimationFinishedListener() {

			@Override
			public void onFinished(View overlay) {
				mIsAnimating = false;
				SlideoutFrame.this.setDrawingCacheEnabled(false);
				// TODO DEBUG
				// android.os.Debug.stopMethodTracing();
			}
		});
	}

	public void close() {
		mOpen = false;
		mIsAnimating = true;

		int screenWidth = getScreenWidth();

		mOutAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE,
				-(screenWidth - mSlideWidth), TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);
		mOutAnimation.setDuration(mDuration);
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

	/** Set the width of the Screenshot in pixels **/
	public SlideoutFrame slideWidth(int slideWidth) {
		mSlideWidth = slideWidth;
		return this;
	}

	public void showShadowEdge(int width) {
		mShadowView = new View(mContext, null);
		mShadowView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0));
	}

	public void hideShadowEdge() {

	}

	/** Get duration animation in millis **/
	public int duration() {
		return mDuration;
	}

	/** Set duration of animation in millis **/
	public SlideoutFrame duration(int duration) {
		mDuration = duration;
		return this;
	}

	public void setContentView(View child) {
		mSlideoutContent.addView(child);
	}

	public boolean isOpen() {
		return mOpen;
	}

	// LIFECYCLE METHODS
	@Override
	public void onStop() {

		mBitmap.recycle();
		mBitmap = null;

		if (mScreenshotView != null && mScreenshotView.getDrawable() != null) {
			((BitmapDrawable) mScreenshotView.getDrawable()).getBitmap()
																											.recycle();
			mScreenshotView.setImageBitmap(null);
			mScreenshotView.setImageDrawable(null);
		}

		// Log.d(TAG, "Stopped - Recycled Bitmap");
		// int usedMegs = (int) (Debug.getNativeHeapAllocatedSize() / 1048576L);
		// String usedMegsString = String.format(" - Memory Used: %d MB", usedMegs);
		// Log.d(TAG, usedMegsString);
	}

	// PRIVATE METHODS

	@SuppressWarnings("deprecation")
	private int getScreenWidth() {
		Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display.getWidth();
	}

	public static Bitmap createFullscreenBitmap(Context context) {
		Point size = Utils.getScreenSize(context);
		int width = size.x;
		int height = size.y - Utils.getStatusBarHeight(context);
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		return bm;
	}

	public static Bitmap createBitmapForView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
		return b;
	}

	public static Bitmap loadBitmapFromView(Bitmap bm, View v) {
		Canvas c = new Canvas(bm);
		v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
		v.draw(c);
		c = null;
		return bm;
	}

}
