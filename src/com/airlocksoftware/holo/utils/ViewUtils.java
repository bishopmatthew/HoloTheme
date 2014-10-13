package com.airlocksoftware.holo.utils;

import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** A collection of static methods useful when working with Views. **/
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
			if (child instanceof ViewGroup) {
				View[] childArr = ViewUtils.allChildViews((ViewGroup) child);
				arr.addAll(Arrays.asList(childArr));
			} else {
				arr.add(child);
			}
		}
		return (View[]) arr.toArray();
	}

	/** Does a depth first search and returns an array with itself and all child views & viewgroups. **/
	public static View[] allChildViewsAndViewGroups(ViewGroup parent) {
		List<View> arr = new ArrayList<View>();
		arr.add(parent);

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ViewGroup) {
				View[] childArr = ViewUtils.allChildViewsAndViewGroups((ViewGroup) child);
				arr.addAll(Arrays.asList(childArr));
			} else {
				arr.add(child);
			}
		}
		View[] views = arr.toArray(new View[1]);
//		return (View[]) arr.toArray();
		return views;
	}

	/** Mutates the state of background BitmapDrawables so that TileMode.REPEAT works properly **/
	public static void fixBackgroundRepeat(View view) {
		Drawable bg = view.getBackground();
		fixDrawableRepeat(bg);
	}

	/** Dispatches Drawable repeat fixes based on the type of the Drawable **/
	public static void fixDrawableRepeat(Drawable d) {
		if (d != null) {
			if (d instanceof BitmapDrawable) fixBitmapRepeat((BitmapDrawable) d);
			else if (d instanceof LayerDrawable) fixLayerRepeat((LayerDrawable) d);
			else if (d instanceof StateListDrawable) fixStateListRepeat((StateListDrawable) d);
		}
	}

	/** There's a limitation where we can only get the current Drawable, so that's the only one that can be mutated **/
	private static void fixStateListRepeat(StateListDrawable d) {
		fixDrawableRepeat(d.getCurrent());
	}

	/** Mutate the bitmap and reset tilemode **/
	public static void fixBitmapRepeat(BitmapDrawable bmp) {
		bmp.mutate(); // make sure that we aren't sharing state anymore
		bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
	}

	/** Go through layers and dispatch them via fixDrawableRepeat **/
	public static void fixLayerRepeat(LayerDrawable lyr) {
		for (int i = 0; i < lyr.getNumberOfLayers(); i++) {
			fixDrawableRepeat(lyr.getDrawable(i));
		}
	}

	/** Converts a boolean for visibility into View.VISIBLE or View.GONE **/
	public static int boolToVis(boolean visible) {
		return visible ? View.VISIBLE : View.GONE;
	}

  public static int boolToVisOrInvis(boolean visible) {
    return visible ? View.VISIBLE : View.INVISIBLE;
  }


  public static boolean visToBool(int visibility) {
    return visibility == View.VISIBLE;
  }


  /** Gets the view's LayoutParams and sets it's width & height. Accepts either
   * LayoutParams.MATCH_PARENT / WRAP_CONTENT or pixel values*/
  public static void setViewDimens(View view, int width, int height) {
    android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
    params.width = width;
    params.height = height;
    view.setLayoutParams(params);
  }

  /** Gets the view's LayoutParams and sets it's width & height. Accepts either
   * LayoutParams.MATCH_PARENT / WRAP_CONTENT or pixel values*/
  public static void setSquareViewDimens(View view, int edgeLength) {
    setViewDimens(view, edgeLength, edgeLength);
  }

  /** Convenience version of setViewMargins(View view, int left, int top, int right, int bottom) */
  public static void setViewMargins(View view, int margin) {
    setViewMargins(view, margin, margin, margin, margin);
  }

  /** Convenience version of setViewMargins(View view, int left, int top, int right, int bottom) */
  public static void setViewMargins(View view, int vertical, int horizontal) {
    setViewMargins(view, horizontal, vertical, horizontal, vertical);
  }

  /** Sets the margins for a view in a ViewGroup of type LinearLayout, RelativeLayout, or FrameLayout */
  public static void setViewMargins(View view, int left, int top, int right, int bottom) {
    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    params.setMargins(left, top, right, bottom);
  }

  public static RectF getViewBounds(View view) {
    int location[] = new int[2];
    view.getLocationOnScreen(location);
    int viewX = location[0];
    int viewY = location[1];

    return new RectF(viewX, viewY, viewX + view.getWidth(), viewY + view.getHeight());
  }

  public static boolean isPointInsideRect(float x, float y, RectF rect) {
    return x > rect.left && x < rect.right && y > rect.top && y < rect.bottom;
  }

  public static boolean isPointInsideView(float x, float y, View view){
    RectF bounds = getViewBounds(view);
    return isPointInsideRect(x, y, bounds);
  }
}
