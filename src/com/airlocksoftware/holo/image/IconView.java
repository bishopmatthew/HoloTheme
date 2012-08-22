package com.airlocksoftware.holo.image;

import java.util.Random;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;

public class IconView extends ImageView {

	private Context mContext;

	private Drawable mIcons; // will be a state list drawable if supported

	private Bitmap mSourceBitmap;
	private ColorStateList mColors, mShadowColors;
	private Color mColor, mShadowColor;
	private float mShadowRadius = 0.0f, mShadowDx = 0.0f, mShadowDy = 0.0f;

	private boolean mWaitForBuild = false;

	// CONSTANTS
	private static final int[][] SUPPORTED_COLOR_STATES = {
			new int[] {- android.R.attr.state_enabled},
			new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled },
			new int[] { android.R.attr.state_focused, android.R.attr.state_enabled },
			new int[] { android.R.attr.state_selected, android.R.attr.state_enabled },
			new int[] { android.R.attr.state_enabled } };

	public IconView(Context context) {
		this(context, null);
	}

	public IconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// if (attrs != null) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.IconView);

		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.IconView_icon_color:
				break;
			case R.styleable.IconView_icon_shadowColors:
				break;
			case R.styleable.IconView_icon_shadowDx:
				break;
			case R.styleable.IconView_icon_shadowDy:
				break;
			case R.styleable.IconView_icon_shadowRadius:
				break;
			case R.styleable.IconView_icon_src:
				break;
			}
		}

		a.recycle();
		// }
	}

	public IconView shadowColor(Color color) {
		mShadowColor = color;
		return this;
	}

	public IconView shadowColors(ColorStateList colors) {
		mShadowColors = colors;
		return this;
	}

	public IconView shadow(int radius, int dx, int dy) {
		mShadowRadius = radius;
		mShadowDx = dx;
		mShadowDy = dy;
		return this;
	}

	public IconView iconColor(Color color) {
		mColor = color;
		return this;
	}

	public IconView iconColors(ColorStateList colors) {
		mColors = colors;
		return this;
	}

	public IconView iconSource(Bitmap b) {
		mSourceBitmap = b;
		return this;
	}

	public IconView waitForBuild() {
		mWaitForBuild = true;
		return this;
	}

	public IconView build() {
		mWaitForBuild = false;
		generateDrawables();
		return this;
	}

	// PRIVATE METHODS
	private void generateDrawables() {
		if (mSourceBitmap != null && !mWaitForBuild) {
			if (mColors != null) {
				StateListDrawable icons = new StateListDrawable();

				for (int[] stateSet : SUPPORTED_COLOR_STATES) {
					int color = mColors.getColorForState(stateSet, mColors.getDefaultColor());
					icons.addState(stateSet,
							new BitmapDrawable(getResources(), generateBitmap(mSourceBitmap, color)));
				}
				this.setImageDrawable(icons);
			}

		}
	}

	public static Bitmap generateBitmap(Bitmap source, int iconColor) {
		return generateBitmap(source, iconColor, Color.TRANSPARENT, 0.0f, 0.0f, 0.0f);
	}

	public static Bitmap generateBitmap(Bitmap source, int shadowColor, float radius, float dx,
			float dy) {
		return generateBitmap(source, Color.TRANSPARENT, shadowColor, radius, dx, dy);
	}

	public static Bitmap generateBitmap(Bitmap source, int iconColor, int shadowColor, float radius,
			float dx, float dy) {

		Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);

		if (shadowColor != Color.TRANSPARENT) {
			Paint shadow = new Paint();
			shadow.setShadowLayer(radius, dx, dy, shadowColor);
			ColorFilter filter = new PorterDuffColorFilter(shadowColor, PorterDuff.Mode.MULTIPLY);
			shadow.setColorFilter(filter);
			canvas.drawBitmap(source, 0, 0, shadow);
		}

		Paint icon = new Paint();
		if (iconColor != Color.TRANSPARENT) {
			ColorFilter filter = new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);
			icon.setColorFilter(filter);
		}

		canvas.drawBitmap(source, 0, 0, icon);

		return result;
	}

}
