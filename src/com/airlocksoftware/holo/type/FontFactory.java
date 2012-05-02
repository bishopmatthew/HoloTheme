package com.airlocksoftware.holo.type;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Typeface;

import com.airlocksoftware.holo.R;

public class FontFactory {

	static Typeface robotoRg;
	static Typeface robotoBdCd;
	static Typeface robotoCd;
	static Typeface robotoBd;
	
	public static final int ROBOTO_RG = 0;
	public static final int ROBOTO_BD_CD = 1;
	public static final int ROBOTO_CD = 2;
	public static final int ROBOTO_BD = 3;

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
				font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
			} else {
				font = robotoRg;
			}
			break;
		case ROBOTO_BD_CD:
			if (robotoBdCd == null) {
				font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldCondensed.ttf");
			} else {
				font = robotoBdCd;
			}
			break;
		case ROBOTO_CD:
			if (robotoCd == null) {
				font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Condensed.ttf");
			} else {
				font = robotoCd;
			}
			break;
		case ROBOTO_BD:
			if (robotoBd == null) {
				font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
			} else {
				font = robotoBd;
			}
			break;
		default: // default is roboto-rg
			if (robotoRg == null) {
				font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
			} else {
				font = robotoRg;
			}
			break;

		}
		if(font == null) throw new RuntimeException("Error: you must copy any fonts used to the assets/fonts directory");
		return font;
	}

}
