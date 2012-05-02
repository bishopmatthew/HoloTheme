package com.airlocksoftware.holo.anim;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.airlocksoftware.holo.R;

public class Flip3dView extends FrameLayout {

	// CONTEXT
	Context mContext;

	// VIEWS
	View mFront;
	View mBack;

	// STATE
	boolean mFrontVisible;

	// CONSTANTS
	private static final int FRONT_ID = R.id.flip_3d_front;
	private static final int BACK_ID = R.id.flip_3d_back;

	// CONSTRUCTORS
	public Flip3dView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mContext = context;
		mFrontVisible = true;
		getAttrs(attrs);
	}

	private void getAttrs(AttributeSet attrs) {
		// TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.);
		//
		// Drawable bg = a.getDrawable(R.styleable.ClearableFontEdit_clearable_editBackground);
		//
		// a.recycle();
	}

	// PUBLIC METHODS
	public void setFrontView(View front) {
		mFront = front;
		mFront.setVisibility(mFrontVisible ? VISIBLE : GONE);
	}

	public void setBackView(View back) {
		mBack = back;
		mBack.setVisibility(mFrontVisible ? GONE : VISIBLE);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// try to see if one of the children has a front or back side id
		if (mFront == null) setFrontView(findViewById(FRONT_ID));
		if (mBack == null) setBackView(findViewById(BACK_ID));
	}

	public void toggle() {
		if (mFrontVisible) {
			applyRotation(0, 90);

		} else {
			applyRotation(0, -90);
		}
		mFrontVisible = !mFrontVisible;
	}

	// PRIVATE METHODS

	private void applyRotation(float start, float end) {
		// Find the center of view
		final float centerX = mFront.getWidth() / 2.0f;
		final float centerY = mFront.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(mFrontVisible, mFront, mBack));

		if (mFrontVisible) {
			mFront.startAnimation(rotation);
		} else {
			mBack.startAnimation(rotation);
		}

	}

	// INNER CLASSES
	public final class DisplayNextView implements Animation.AnimationListener {
		private boolean mCurrentView;
		View frontSide;
		View backSide;

		public DisplayNextView(boolean currentView, View mFront, View mBack) {
			mCurrentView = currentView;
			this.frontSide = mFront;
			this.backSide = mBack;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			frontSide.post(new SwapViews(mCurrentView, frontSide, backSide));
		}

		public void onAnimationRepeat(Animation animation) {
		}

	}

	public class Flip3dAnimation extends Animation {
		private final float mFromDegrees;
		private final float mToDegrees;
		private final float mCenterX;
		private final float mCenterY;
		private Camera mCamera;

		public Flip3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY) {
			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;
			mCenterX = centerX;
			mCenterY = centerY;
		}

		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			mCamera = new Camera();
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			final float fromDegrees = mFromDegrees;
			float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

			final float centerX = mCenterX;
			final float centerY = mCenterY;
			final Camera camera = mCamera;

			final Matrix matrix = t.getMatrix();

			camera.save();

			camera.rotateY(degrees);

			camera.getMatrix(matrix);
			camera.restore();

			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);

		}

	}

	public final class SwapViews implements Runnable {
		private boolean mIsFirstView;
		View frontSide;
		View backSide;

		public SwapViews(boolean isFirstView, View frontSide, View backSide) {
			mIsFirstView = isFirstView;
			this.frontSide = frontSide;
			this.backSide = backSide;
		}

		public void run() {
			final float centerX = frontSide.getWidth() / 2.0f;
			final float centerY = frontSide.getHeight() / 2.0f;
			Flip3dAnimation rotation;

			if (mIsFirstView) {
				frontSide.setVisibility(View.GONE);
				backSide.setVisibility(View.VISIBLE);
				backSide.requestFocus();

				rotation = new Flip3dAnimation(-90, 0, centerX, centerY);
			} else {
				backSide.setVisibility(View.GONE);
				frontSide.setVisibility(View.VISIBLE);
				frontSide.requestFocus();

				rotation = new Flip3dAnimation(90, 0, centerX, centerY);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			if (mIsFirstView) {
				backSide.startAnimation(rotation);
			} else {
				frontSide.startAnimation(rotation);
			}
		}
	}
}