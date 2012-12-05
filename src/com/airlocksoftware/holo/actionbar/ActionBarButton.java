package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.image.IconView;
import com.airlocksoftware.holo.type.FontText;

public class ActionBarButton extends FrameLayout {

	// STATE
	private Context mContext;

	private String mText;
	private Bitmap mIcon;
	private FontText mFontText;
	private IconView mIconView;
	private Priority mPriority = Priority.LOW;
	private DrawMode mDrawMode;

	private boolean mLayoutFinished = false;
	private boolean mShowProgress = false;

	// CONSTANTS
	private int ACTIONBAR_HEIGHT;
	private int ACTIONBAR_WIDTH;
	private LinearLayout.LayoutParams ACTIONBAR_PARAMS;
	private LinearLayout.LayoutParams OVERFLOW_PARAMS;
	private static final int ACTIONBAR_LAYOUT = R.layout.vw_actionbar_btn;
	private static final int OVERFLOW_LAYOUT = R.layout.vw_actionbar_overflow_btn;
	private static final int SPIN_LAYOUT = R.layout.vw_actionbar_progressbar_spin;

	// CONSTRUCTORS
	public ActionBarButton(Context context) {
		this(context, null);
	}

	public ActionBarButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		ACTIONBAR_HEIGHT = mContext.getResources()
																.getDimensionPixelSize(R.dimen.actionbar_underline_offset);
		ACTIONBAR_WIDTH = mContext.getResources()
															.getDimensionPixelSize(R.dimen.actionbar_height);
		ACTIONBAR_PARAMS = new LinearLayout.LayoutParams(ACTIONBAR_HEIGHT, ACTIONBAR_HEIGHT);
		OVERFLOW_PARAMS = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		this.setClickable(true);
	}

	// PUBLIC API
	/** Set the mode of this button. **/
	public ActionBarButton priority(Priority priority) {
		mPriority = priority;
		return this;
	}

	/** Get the mode of this button. **/
	public Priority priority() {
		return mPriority;
	}

	public DrawMode drawMode() {
		return mDrawMode;
	}

	public ActionBarButton drawMode(DrawMode mode) {
		if (mDrawMode == null || mDrawMode != mode) {
			mLayoutFinished = false;
			this.removeAllViews();
			LayoutInflater inflater = LayoutInflater.from(mContext);

			switch (mode) {
			case ICON_ONLY:
				if (mShowProgress) inflater.inflate(SPIN_LAYOUT, this);
				else inflater.inflate(ACTIONBAR_LAYOUT, this);
				setLayoutParams(ACTIONBAR_PARAMS);
				break;
			case OVERFLOW:
				inflater.inflate(OVERFLOW_LAYOUT, this);
				setLayoutParams(OVERFLOW_PARAMS);
				break;
			}
			mIconView = (IconView) findViewById(R.id.icv);
			mFontText = (FontText) findViewById(R.id.txt);
			icon(mIcon);
			text(mText);

			mLayoutFinished = true;
		}
		mDrawMode = mode;
		return this;
	}

	public ActionBarButton text(String text) {
		mText = text;
		if (mText != null && mFontText != null) mFontText.setText(text);
		return this;
	}

	public String text() {
		return mText;
	}

	public ActionBarButton icon(Bitmap bmp) {
		mIcon = bmp;
		if (mIcon != null && mIconView != null) mIconView.iconSource(mIcon);
		return this;
	}

	public ActionBarButton icon(int iconResId) {
		icon(BitmapFactory.decodeResource(getResources(), iconResId));
		return this;
	}

	public Bitmap icon() {
		return mIcon;
	}

	public Object tag() {
		return getTag();
	}

	public ActionBarButton tag(Object tag) {
		setTag(tag);
		return this;
	}

	public int id() {
		return getId();
	}

	public ActionBarButton id(int id) {
		setId(id);
		return this;
	}

	public ActionBarButton onClick(OnClickListener click) {
		setOnClickListener(click);
		return this;
	}

	public boolean showProgress() {
		return mShowProgress;
	}

	public ActionBarButton showProgress(boolean showProgress) {
		mShowProgress = showProgress;

		// refresh mDrawMode
		if (mDrawMode == DrawMode.ICON_ONLY) {
			mDrawMode = null;
			drawMode(DrawMode.ICON_ONLY);
		}

		return this;
	}

	// PRIVATE METHODS

	// ENUMS & INNER CLASSES
	public enum Priority {
		HIGH, LOW;
	}

	public enum DrawMode {
		ICON_ONLY, OVERFLOW;
	}

	// OVERRIDE THIS addView SINCE ALL OTHERS ARE ROUTED THROUGH IT
	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (!mLayoutFinished) super.addView(child, index, params);
		else throw new RuntimeException("Can't add views to an ActionBarButton after initial layout");
	}

	@Override
	public String toString() {
		return ActionBarButton.class.getSimpleName() + ": \"" + ((mText != null) ? mText : "NULL") + "\"";
	}

}
