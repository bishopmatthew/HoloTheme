package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.airlocksoftware.holo.R;

public class FontText extends TextView {

	Context mContext;

	// Text shadow with a color state list
	private ColorStateList mShadowColors;
	private float mShadowDx;
	private float mShadowDy;
	private float mShadowRadius;

	/**
	 * A text view that allows you to use a custom typeface, set either via xml
	 * or by calling setFont()
	 **/
	public FontText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		getAttributes(attrs);
	}

	/**
	 * A text view that allows you to use a custom typeface, set either via xml
	 * or by calling setFont()
	 **/
	public FontText(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		this.mContext = context;
		getAttributes(attrs);
	}

	private void getAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FontText);

		int font = a.getInt(R.styleable.FontText_font, 0);
		// fix so that I can use the graphical layout editor
		if (!isInEditMode()) {
			setFont(font);
		}

		mShadowColors = a.getColorStateList(R.styleable.FontText_shadowColors);
		mShadowDx = a.getFloat(R.styleable.FontText_android_shadowDx, 0);
		mShadowDy = a.getFloat(R.styleable.FontText_android_shadowDy, 0);
		mShadowRadius = a.getFloat(R.styleable.FontText_android_shadowRadius, 0);
		updateShadowColor();
		
		a.recycle();
	}

	public void setFont(int font) {
		setTypeface(FontFactory.getTypeface(mContext, font));
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}

	/** fixes a bug, I think? **/
	public void changeBackgroundResource(int backgroundResourceId) {
		int paddingTop = getPaddingTop();
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		setBackgroundResource(backgroundResourceId);
		setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}

	private void updateShadowColor() {
		if (mShadowColors != null) {
			setShadowLayer(mShadowRadius, mShadowDx, mShadowDy,
					mShadowColors.getColorForState(getDrawableState(), 0));
			invalidate();
		}
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		updateShadowColor();
	}

}
