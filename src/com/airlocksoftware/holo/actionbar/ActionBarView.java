package com.airlocksoftware.holo.actionbar;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.image.IconView;

public class ActionBarView extends View {

	private Context mContext;
	RelativeLayout mTitleContainer, mUpContainer;
	LinearLayout mRootView, mHighButtons, mLowButtons;
	IconView mOverflowIcon, mUpIndicator;
	ImageView mUpIcon;

	// private int mWidth, mHeight;

	private static final int DEFAULT_LAYOUT = R.layout.vw_actionbar;

	public ActionBarView(Context context) {
		this(context, null);
	}

	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ActionBarView);

		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			// TODO
			}
		}

		a.recycle();

		// this.setClickable(true);
		// this.setFocusable(true);
		this.setEnabled(true);
		this.setClickable(true);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		mRootView = (LinearLayout) inflater.inflate(DEFAULT_LAYOUT, null);
		mUpContainer = (RelativeLayout) mRootView.findViewById(R.id.cnt_up);
		mOverflowIcon = (IconView) mRootView.findViewById(R.id.icv_overflow);
		mUpIcon = (ImageView) mRootView.findViewById(R.id.img_up_icon);
		mUpIndicator = (IconView) mRootView.findViewById(R.id.icv_up_indicator);

	}
//	
//	@Override
//	public View findViewById(int id) {
//		return mRootView.findViewById(id);
//	}

	// ViewGroup responsibilities

	@Override
	protected void onDraw(Canvas canvas) {
		mRootView.draw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// mRootView.measure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY),
		// MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
		mRootView.measure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(mRootView.getMeasuredWidth(), mRootView.getMeasuredHeight());
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		mRootView.layout(left, top, right, bottom);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mRootView.invalidate();
	}

	// TOUCHES
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		boolean handled = mRootView.onTouchEvent(event);
		boolean superHandled = super.onTouchEvent(event);
		// return handled;
		// return super.onTouchEvent(event);
		return handled;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean handled = mRootView.dispatchTouchEvent(ev);
		// boolean superHandled = super.dispatchTouchEvent(ev);
		// return handled;
		return handled;
	}

	@Override
	public void addTouchables(ArrayList<View> views) {
		super.addTouchables(views);
		mRootView.addTouchables(views);
	}

}
