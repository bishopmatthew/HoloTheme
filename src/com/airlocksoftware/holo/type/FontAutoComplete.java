package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.airlocksoftware.holo.R;

public class FontAutoComplete extends AutoCompleteTextView {

	Context mContext;
	
	private static final int DEFAULT_STYLE = R.style.FontAutoComplete;


	public FontAutoComplete(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FontAutoComplete(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		this.mContext = context;
		getAttributes(attrs);
	}

	private void getAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FontText);

		int font = a.getInt(R.styleable.FontText_font, 0);
		setFont(font);

		a.recycle();
	}

	public void setFont(int font) {
		setTypeface(FontFactory.getTypeface(mContext, font));
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}

}
