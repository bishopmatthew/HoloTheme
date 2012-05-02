package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.airlocksoftware.holo.R;

public class FontText extends TextView {

	Context mContext;
	boolean mAllCaps;

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
		boolean allCaps = a.getBoolean(R.styleable.FontText_allCaps, false);
		setAllCaps(allCaps);

		a.recycle();
	}

	public void setFont(int font) {
		setTypeface(FontFactory.getTypeface(mContext, font));
	}

	@Override
	public void setAllCaps(boolean allCaps) {
		mAllCaps = allCaps;
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (mAllCaps) text = text.toString().toUpperCase();
		super.setText(text, type);
	}

	public void changeBackgroundResource(int backgroundResourceId) {
		int paddingTop = getPaddingTop();
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		setBackgroundResource(backgroundResourceId);
		setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}

}
