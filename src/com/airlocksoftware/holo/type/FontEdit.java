package com.airlocksoftware.holo.type;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.airlocksoftware.holo.R;


public class FontEdit extends EditText {
	
	Context mContext;
	
	private static final int DEFAULT_STYLE = R.style.FontEdit;

	public FontEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public FontEdit(Context context, AttributeSet attrs, int style) {
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

	public String getString() {
		return getText().toString();
	}

}
