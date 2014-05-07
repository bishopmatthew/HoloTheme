package com.airlocksoftware.holo.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.Date;
import java.util.regex.Pattern;

/** A collection of static methods useful for working with Android applications. **/
public class Utils {

	/** Returns a String with the formatted, localized date and with slashes replaced by periods. **/
	public static String getLocalizedDate(Context context, Date date) {
		java.text.DateFormat dateForm = android.text.format.DateFormat.getDateFormat(context);
		String localizedDate = dateForm.format(date);
		return localizedDate.replace("/", ".");
	}

	/** A simple (ish) regex for checking valid emails **/
	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email)
																.matches();
	}

	/** Returns screen size as a point in pixels **/
	public static Point getScreenSize(Context context) {
		DisplayMetrics metrics = context.getResources()
																		.getDisplayMetrics();
		return new Point(metrics.widthPixels, metrics.heightPixels);
	}

  public static int getScreenWidth(Context context) {
    return getScreenSize(context).x;
  }

  public static int getScreenHeight(Context context) {
    return getScreenSize(context).y;
  }

	/** Returns a point representing the xy coordinates of the (I think it's top-left) corner of the view **/
	public static Point getWindowLocation(Context context, View v) {
		int[] locInWindow = { 0, 0 };
		v.getLocationInWindow(locInWindow);
		return new Point(locInWindow[0], locInWindow[1]);
	}

	/** Returns the pixel size of the status bar **/
	public static int getStatusBarHeight(Context context) {
		return (int) Math.ceil(25 * context.getResources()
																				.getDisplayMetrics().density);
	}

	public static int dpToPx(Context context, float dp) {
		return (int) Math.ceil(dp * context.getResources()
																				.getDisplayMetrics().density);
	}

	public static float pixelsToSp(Context context, Float px) {
		float scaledDensity = context.getResources()
																	.getDisplayMetrics().scaledDensity;
		return px / scaledDensity;
	}

	public static float spToPixels(Context context, Float sp) {
		float scaledDensity = context.getResources()
																	.getDisplayMetrics().scaledDensity;
		return sp * scaledDensity;
	}

	/** Converts a Point representing pixels into a PointF representing inches **/
	public static PointF pixelsToInches(Context context, Point point) {
		DisplayMetrics dm = context.getResources()
																.getDisplayMetrics();
		return new PointF(point.x / dm.xdpi, point.y / dm.ydpi);
	}

	/** Retrieves a single resourceId from the context and attribute id. **/
	public static int getThemedResourceId(Context context, int attrId) {
		TypedValue tv = new TypedValue();
		context.getTheme()
						.resolveAttribute(attrId, tv, true);
		return tv.resourceId;
	}

  public static int getThemedColor(Context context, int attrId) {
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(attrId, typedValue, true);
    int color = typedValue.data;
    return color;
  }

  public static ColorStateList getThemedColorStateList(Context context, int attrId) {
    int resId = getThemedResourceId(context, attrId);
    return context.getResources().getColorStateList(resId);
  }

}
