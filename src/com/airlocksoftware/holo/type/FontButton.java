package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.utils.Utils;

/** An Button that allows you to use different fonts & apply an app-wide scaling factor. **/
public class FontButton extends Button {

	Context mContext;
	
	private boolean mTextScalingEnabled;
	private float mTextScalingFactor;

	private static final int DEFAULT_STYLE = R.style.FontButton;

	public FontButton(Context context, AttributeSet attrs) {
		this(context, attrs, DEFAULT_STYLE);
	}

	public FontButton(Context context, AttributeSet attrs, int style) {
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
		
		// text scaling
		mTextScalingEnabled = a.getBoolean(R.styleable.FontText_textScalingEnabled, true);
		if (mTextScalingEnabled) {
			mTextScalingFactor = FontFactory.getTextScaleFactor(mContext);
			refreshTextSize();
		}

		a.recycle();
	}

	public void setFont(int font) {
		setTypeface(FontFactory.getTypeface(mContext, font));
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}

	@Override
	public void setTextSize(float size) {
		if (mTextScalingEnabled) size *= mTextScalingFactor;
		super.setTextSize(size);
	}

	private void refreshTextSize() {
		float textSize = getTextSize();
		float converted = Utils.pixelsToSp(mContext, textSize);
		setTextSize(converted);
	}

}
