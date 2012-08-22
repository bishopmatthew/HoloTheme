package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.airlocksoftware.holo.R;

public class FontButton extends Button {

	Context context;
	
	private static final int DEFAULT_STYLE = R.style.FontButton;

	public FontButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getAttributes(attrs);
	}

	public FontButton(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		this.context = context;
		getAttributes(attrs);
	}

	private void getAttributes(AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontText);

		int font = a.getInt(R.styleable.FontText_font, 0);
	// fix so that I can use the graphical layout editor
			if (!isInEditMode()) {
				setFont(font);
			}

		a.recycle();
	}

	public void setFont(int font) {
		setTypeface(FontFactory.getTypeface(context, font));
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}

}
