package com.airlocksoftware.holo.utils;

import java.util.Date;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;

public class Utils {

	public static String getLocalizedDate(Context context, Date transactionDate) {
		java.text.DateFormat dateForm = android.text.format.DateFormat.getDateFormat(context);
		String localizedDate = dateForm.format(transactionDate);
		return localizedDate.replace("/", ".");
	}

	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
					+ "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public static Point getScreenSize(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return new Point(metrics.widthPixels, metrics.heightPixels);
	}

	public static Point getWindowLocation(Context context, View v) {
		int[] locInWindow = { 0, 0 };
		v.getLocationInWindow(locInWindow);
		return new Point(locInWindow[0], locInWindow[1]);
	}

	public static int getStatusBarHeight(Context context) {
		return (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
	}

	public static int dpToPixels(Context context, int dp) {
		return (int) Math.ceil(dp * context.getResources().getDisplayMetrics().density);
	}

}
