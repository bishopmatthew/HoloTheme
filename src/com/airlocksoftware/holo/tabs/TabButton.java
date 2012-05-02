package com.airlocksoftware.holo.tabs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.type.FontText;

public class TabButton extends FrameLayout implements Checkable {

	// OBJECTS
	private Context mContext;
	private Drawable mButtonDrawable;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	// STATE
	private boolean mChecked;
	private int mButtonResource;
	private int mBackgroundResource;
	private String mTabText;
	private boolean mTabTextAllCaps;
	private boolean mBroadcasting;
	private boolean mIsTopTab;

	// VIEWS
	private FontText mTabTextView;
	private ImageView mTabImageView;

	// CONSTANTS
	private static final int DEFAULT_STYLE = R.style.TabButton;
	private static final int[] STATE_CHECKED = { android.R.attr.state_checked };

	private static final int DEFAULT_BACKGROUND = R.drawable.btn_tab_background;
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
		super(context, attrs, defStyle);
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
			setBackgroundDrawable(bg); // TODO don't use the default?
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

	// CHECKABLE INTERFACE

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			refreshDrawableState();

			// Avoid infinite recursions if setChecked() is called from a
			// listener
			if (mBroadcasting) {
				return;
			}

			mBroadcasting = true;
			if (mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
			}

			mBroadcasting = false;
		}

	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	@Override
    public boolean performClick() {
        /* When clicked, toggle the state */
        toggle();
        return super.performClick();
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

	// CHECKED CHANGED LISTENER
	/**
	 * Register a callback to be invoked when the checked state of this button
	 * changes.
	 * 
	 * @param listener the callback to call on checked state change
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	/**
	 * Interface definition for a callback to be invoked when the checked state
	 * of a compound button changed.
	 */
	public static interface OnCheckedChangeListener {
		/**
		 * Called when the checked state of a compound button has changed.
		 * 
		 * @param buttonView The compound button view whose state has changed.
		 * @param isChecked The new checked state of buttonView.
		 */
		void onCheckedChanged(TabButton buttonView, boolean isChecked);
	}

	// SAVING STATE

	static class SavedState extends BaseSavedState {
		boolean checked;

		/**
		 * Constructor called from {@link CompoundButton#onSaveInstanceState()}
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			checked = (Boolean) in.readValue(null);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeValue(checked);
		}

		@Override
		public String toString() {
			return "TabButton.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + checked
					+ "}";
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	@Override
	public Parcelable onSaveInstanceState() {
		// Force our ancestor class to save its state
		// setFreezesText(true);
		Parcelable superState = super.onSaveInstanceState();

		SavedState ss = new SavedState(superState);

		ss.checked = isChecked();
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;

		super.onRestoreInstanceState(ss.getSuperState());
		setChecked(ss.checked);
		requestLayout();
	}
	
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (mChecked) {
			mergeDrawableStates(drawableState, STATE_CHECKED);
		}
		return drawableState;
	}

}
