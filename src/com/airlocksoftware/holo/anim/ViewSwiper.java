package com.airlocksoftware.holo.anim;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewFlipper;

import com.airlocksoftware.holo.R;

public class ViewSwiper extends GestureOverlayView implements GestureOverlayView.OnGesturePerformedListener {
	private GestureLibrary gestureLibrary = null;
	protected ViewFlipper flipper = null;
	protected Context context;

	private boolean attachLongClickListener;

	public ViewSwiper(Context ctxt) {
		this(ctxt, null);
	}

	public ViewSwiper(Context ctxt, AttributeSet attrs) {
		this(ctxt, attrs, 0);
	}

	public ViewSwiper(Context ctxt, AttributeSet attrs, int defStyle) {
		super(ctxt, attrs, defStyle);

		this.context = ctxt;

		getAttributes(attrs);

		setGestureColor(0x00000000);
		setUncertainGestureColor(0x00000000);

		flipper = new ViewFlipper(getContext());
		flipper.setMeasureAllChildren(false);

		addOnGesturePerformedListener(this);
		addView(flipper);

		gestureLibrary = GestureLibraries.fromRawResource(ctxt, R.raw.gestures);
		gestureLibrary.load();
	}

	private void getAttributes(AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewSwiper);

		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = a.getIndex(i);
			if (attr == R.styleable.ViewSwiper_attachLongClickListener) {
				attachLongClickListener = a.getBoolean(attr, true);
			}
		}
		a.recycle();
	}

	@Override
	public void onFinishInflate() {
		int count = getChildCount();
		ArrayList<View> toMove = new ArrayList<View>();

		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);

			if (child != flipper) {
				toMove.add(child);
				if (attachLongClickListener) {
					child.setOnLongClickListener(longClickListener);
				}
			}
		}

		for (View child : toMove) {
			removeView(child);
			flipper.addView(child);
		}
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);

			if (prediction.score > 1.0) {
				if (prediction.name.startsWith("West")) {
					moveToNext();
				} else {
					moveToPrevious();
				}
			}
		}
	}

	public View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
		public boolean onLongClick(View v) {
			moveToNext();
			return true;
		}
	};

	public ViewFlipper getFlipper() {
		return (flipper);
	}

	protected void moveToNext() {
		flipper.setInAnimation(context, R.anim.fast_flip_in_previous);
		flipper.setOutAnimation(context, R.anim.fast_flip_out_previous);
		flipper.showNext();
	}

	protected void moveToPrevious() {
		flipper.setInAnimation(context, R.anim.fast_flip_in_next);
		flipper.setOutAnimation(context, R.anim.fast_flip_out_next);
		flipper.showPrevious();
	}
	
	protected void noAnimToNext() {
		flipper.setInAnimation(null);
		flipper.setOutAnimation(null);
		flipper.showNext();
	}
}

