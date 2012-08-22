package com.airlocksoftware.holo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

	/** Returns an array with the direct children of the parent ViewGroup **/
	public static View[] directChildViews(ViewGroup parent) {
		int childCount = parent.getChildCount();
		View[] arr = new View[childCount];
		for (int i = 0; i < childCount; i++) {
			arr[i] = parent.getChildAt(i);
		}
		return arr;
	}

	/** Does a depth-first search for all children that aren't ViewGroups **/
	public static View[] allChildViews(ViewGroup parent) {
		List<View> arr = new ArrayList<View>();
		
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);
			if(child instanceof ViewGroup) {
				View[] childArr = ViewUtils.allChildViews((ViewGroup) child);
				arr.addAll(Arrays.asList(childArr));
			} else {
				arr.add(child);
			}
		}
		return (View[]) arr.toArray();
	}

}
