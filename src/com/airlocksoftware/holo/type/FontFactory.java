package com.airlocksoftware.holo.type;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

/** Loads & caches typefaces used by the various Font_____ classes. **/
public class FontFactory {

	static Typeface robotoRg;
	static Typeface robotoBdCd;
	static Typeface robotoCd;
	static Typeface robotoBd;
	static Typeface robotoTh;
	static Typeface robotoLt;

	public static final int ROBOTO_RG = 0;
	public static final int ROBOTO_BD_CD = 1;
	public static final int ROBOTO_CD = 2;
	public static final int ROBOTO_BD = 3;
	public static final int ROBOTO_TH = 4;
	public static final int ROBOTO_LT = 5;

	// CONSTANTS
	public static final String PREFS_NAME = "holobootstrap_fontprefs";
	public static final String TEXT_SCALING_FACTOR = "FontFactory.textScalingFactor";

	/**
	 * Creates and caches the typefaces used by the application
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static Typeface getTypeface(Context context, int fontEnum) {
		Typeface font;

		switch (fontEnum) {
		case ROBOTO_RG:
			if (robotoRg == null) {
				robotoRg = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
			}
			font = robotoRg;
			break;
		case ROBOTO_BD_CD:
			if (robotoBdCd == null) {
				robotoBdCd = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldCondensed.ttf");
			}
			font = robotoBdCd;
			break;
		case ROBOTO_CD:
			if (robotoCd == null) {
				robotoCd = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Condensed.ttf");
			}
			font = robotoCd;
			break;
		case ROBOTO_BD:
			if (robotoBd == null) {
				robotoBd = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
			}
			font = robotoBd;
			break;
		case ROBOTO_TH:
			if (robotoTh == null) {
				robotoTh = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
			}
			font = robotoTh;
			break;
		case ROBOTO_LT:
			if (robotoLt == null) {
				robotoLt = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
			}
			font = robotoLt;
			break;
		default: // default is roboto-rg
			if (robotoRg == null) {
				robotoRg = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
			}
			font = robotoRg;
			break;
		}

		if (font == null) throw new RuntimeException("Error: you must copy any fonts used to the assets/fonts directory");
		return font;
	}

	public static void saveTextScaleFactor(Context context, float scaleFactor) {
		SharedPreferences mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPrefs.edit();
		mEditor.putFloat(TEXT_SCALING_FACTOR, scaleFactor);
		mEditor.commit();
	}

	public static float getTextScaleFactor(Context context) {
		SharedPreferences mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return mPrefs.getFloat(TEXT_SCALING_FACTOR, 1.0f);
	}
}
