package com.airlocksoftware.holo.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View.BaseSavedState;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.checkable.CheckableView;
import com.airlocksoftware.holo.type.FontText;

public class TabButton extends CheckableView {

	// OBJECTS
	private Context mContext;
	private Drawable mButtonDrawable;

	// STATE
	private int mButtonResource;
	private int mBackgroundResource;
	private String mTabText;
	private boolean mTabTextAllCaps;
	private boolean mIsTopTab;

	// VIEWS
	private FontText mTabTextView;
	private ImageView mTabImageView;

	// CONSTANTS
	private static final int DEFAULT_STYLE = R.style.TabButton;

	private static final int DEFAULT_BACKGROUND = R.drawable.btn_tab;
	private static final int DEFAULT_TEXT_LAYOUT = R.layout.text_tab;
	private static final int DEFAULT_IMAGE_LAYOUT = R.layout.image_tab;

	// CONSTRUCTORS
	public TabButton(Context context) {
		this(context, null);
	}

	public TabButton(Context context, AttributeSet attrs) {
		this(context, attrs, DEFAULT_STYLE);
	}

	public TabButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mContext = context;

		retrieveAttrs(attrs, defStyle);
	}

	private void retrieveAttrs(AttributeSet attrs, int defStyle) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TabButton, defStyle, 0);

		Drawable icon = a.getDrawable(R.styleable.TabButton_tab_icon);
		if (icon != null) {
			setButtonDrawable(icon);
		}

		setTextAllCaps(a.getBoolean(R.styleable.TabButton_tab_textAllCaps, true));
		
		Drawable bg = a.getDrawable(R.styleable.TabButton_tab_background);
		if (bg != null) {
			setBackgroundDrawable(bg);
		} else {
			setBackgroundDrawable(mContext.getResources().getDrawable(DEFAULT_BACKGROUND));
		}

		String txt = a.getString(R.styleable.TabButton_tab_text);
		if (bg != null && txt != null) throw new IllegalArgumentException(
				"Can't add both text and an icon to a tab at this time");
		if (txt != null) setTabText(txt);

		boolean checked = a.getBoolean(R.styleable.TabButton_tab_checked, false);
		setChecked(checked);
		
		setIsTopTab(a.getBoolean(R.styleable.TabButton_tab_checked, false));
		
		// things that I might want to make attributes in the future
		this.setClickable(true);

		a.recycle();
	}
	
	// TEXT, IMAGES, ETC.
	/**
	 * Set the background to a given Drawable
	 * 
	 * @param d The Drawable to use as the background
	 */
	public void setButtonDrawable(Drawable d) {
		if (d != null) {
			
			if (mTabImageView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				mTabImageView = (ImageView) inflater.inflate(DEFAULT_IMAGE_LAYOUT, this, false);
				this.addView(mTabImageView);
			}
			
			if (mButtonDrawable != null) {
				mButtonDrawable.setCallback(null);
				unscheduleDrawable(mButtonDrawable);
			}
			d.setCallback(this);
			d.setState(getDrawableState());
			d.setVisible(getVisibility() == VISIBLE, false);
			mButtonDrawable = d;
			mButtonDrawable.setState(null);
			setMinimumHeight(mButtonDrawable.getIntrinsicHeight());
			
			mTabImageView.setImageDrawable(d);
			
		}

		refreshDrawableState();
	}
	
	public void setTabIcon(int iconResourceId) {
		setButtonDrawable(mContext.getResources().getDrawable(iconResourceId));
	}

	public void setTabText(String text) {
		this.mTabText = text;
		if (mTabTextView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			mTabTextView = (FontText) inflater.inflate(DEFAULT_TEXT_LAYOUT, this, false);
			this.addView(mTabTextView);
		}
		if (mTabTextAllCaps) {
			mTabTextView.setText(mTabText.toUpperCase());
		} else {
			mTabTextView.setText(mTabText);
		}
	}

	private void setTextAllCaps(boolean tabTextAllCaps) {
		mTabTextAllCaps = tabTextAllCaps;
		// TODO
	}

	private void setIsTopTab(boolean isTopTab) {
		mIsTopTab = isTopTab;
		// TODO
	}
}
