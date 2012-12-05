package com.airlocksoftware.holo.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;

/**
 * Useful extensions to ImageView for icons;
 * - Change color of icon using ColorStateList / Color
 * - Add a drop shadow from ColorStateList / Color
 * **/
public class IconView extends ImageView {

	private Context mContext;

	private Bitmap mSourceBitmap;
	private ColorStateList mColors, mShadowColors;
	private int mColor = Color.TRANSPARENT, 
							mShadowColor = Color.TRANSPARENT;
	private float mShadowRadius = 0.0f, mShadowDx = 0.0f, mShadowDy = 0.0f;

	private boolean mWaitForBuild = false;

	private static final boolean IS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= 11;

	// CONSTANTS
	private static final int[][] COLOR_STATES = { new int[] { -android.R.attr.state_enabled },
			new int[] { android.R.attr.state_checked, android.R.attr.state_enabled },
			new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled },
			new int[] { android.R.attr.state_focused, android.R.attr.state_enabled },
			new int[] { android.R.attr.state_selected, android.R.attr.state_enabled },
			new int[] { android.R.attr.state_enabled } };

	public IconView(Context context) {
		this(context, null);
	}

	@SuppressLint("NewApi")
	public IconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.IconView);

		// disable hardware acceleration (since it is unsupported for setShadowLayer() that isn't text)
		if (IS_HONEYCOMB) setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.IconView_icon_colors:
				mColors = a.getColorStateList(attr);
				break;
			case R.styleable.IconView_icon_shadowColors:
				mShadowColors = a.getColorStateList(attr);
				break;
			case R.styleable.IconView_icon_shadowDx:
				mShadowDx = a.getDimension(attr, 0.0f);
				break;
			case R.styleable.IconView_icon_shadowDy:
				mShadowDy = a.getDimension(attr, 0.0f);
				break;
			case R.styleable.IconView_icon_shadowRadius:
				mShadowRadius = a.getDimension(attr, 0.0f);
				break;
			case R.styleable.IconView_icon_src:
				mSourceBitmap = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			}
		}

		a.recycle();
		generateDrawables();
	}

	public IconView shadowColor(int color) {
		mShadowColor = color;
		mShadowColors = null;
		generateDrawables();
		return this;
	}
	
	public int shadowColor() {
		return mShadowColor;
	}

	public IconView shadowColors(ColorStateList colors) {
		mShadowColors = colors;
		generateDrawables();
		return this;
	}
	
	public ColorStateList shadowColors() {
		return mShadowColors;
	}

	public IconView shadow(float radius, float dx, float dy) {
		mShadowRadius = radius;
		mShadowDx = dx;
		mShadowDy = dy;
		generateDrawables();
		return this;
	}

	public IconView iconColor(int color) {
		mColor = color;
		mColors = null;
		generateDrawables();
		return this;
	}
	
	public int iconColor() {
		return mColor;
	}

	public IconView iconColors(ColorStateList colors) {
		mColors = colors;
		generateDrawables();
		return this;
	}
	
	public ColorStateList iconColors() {
		return mColors;
	}

	public IconView iconSource(Bitmap b) {
		mSourceBitmap = b;
		generateDrawables();
		return this;
	}

	public IconView iconSource(int iconResId) {
		mSourceBitmap = BitmapFactory.decodeResource(getResources(), iconResId);
		generateDrawables();
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
			StateListDrawable icons = new StateListDrawable();

			for (int[] stateSet : COLOR_STATES) {
				int color, shadowColor;

				if (mColors != null) {
					color = mColors.getColorForState(stateSet, mColors.getDefaultColor());
				} else {
					color = mColor;
				}

				if (mShadowColors != null) {
					shadowColor = mShadowColors.getColorForState(stateSet, mShadowColors.getDefaultColor());
				} else {
					shadowColor = mShadowColor;
				}

				Bitmap b = generateBitmap(mSourceBitmap, color, shadowColor, mShadowRadius, mShadowDx, mShadowDy);
				BitmapDrawable bd = new BitmapDrawable(getResources(), b);
				icons.addState(stateSet, bd);
			}
			this.setImageDrawable(icons);
		}
	}

	public static Bitmap generateBitmap(Bitmap source, int iconColor) {
		return generateBitmap(source, iconColor, Color.TRANSPARENT, 0.0f, 0.0f, 0.0f);
	}

	public static Bitmap generateBitmap(Bitmap source, int shadowColor, float radius, float dx, float dy) {
		return generateBitmap(source, Color.TRANSPARENT, shadowColor, radius, dx, dy);
	}

	public static Bitmap generateBitmap(Bitmap source, int iconColor, int shadowColor, float radius, float dx, float dy) {

		int paddingX = (int) (radius + dx);
		int paddingY = (int) (radius + dy);

		Bitmap result = Bitmap.createBitmap(source.getWidth() + paddingX, source.getHeight() + paddingY,
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
