package com.airlocksoftware.holo.utils;

/** A collection static methods useful for working with Fragments. **/
public class FragmentUtils {

	/** Creates the tag a fragment will have inside of a ViewPager. **/
	public static String makeFragmentPagerTag(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

}
