package com.airlocksoftware.holo.anim;

import android.view.animation.Animation;

/**
 * Used by OverlayManager to control how a view is animated. Includes parameters for enter / exit animations, which part
 * of the screen to cover, and whether to hide other overlay views when displaying it.
 **/
public class AnimationParams {

	// STATE
	private FillType mFillType = FillType.FILL_SCREEN;
	private int mZIndex = 0;
	private Exclusivity mExclusivity = Exclusivity.EXCLUDE_ALL;
	private boolean mIsVisible = false;
	private Animation mInAnim;
	private Animation mOutAnim;

	// CONSTRUCTORS
	public AnimationParams() {
		this(FillType.FILL_SCREEN);
	}

	public AnimationParams(FillType fillType) {
		mFillType = fillType;
	}

	// ENUMS
	public enum FillType {
		// fill the whole screen (minus the status bar)
		FILL_SCREEN,
		// fill the action bar
		CLIP_ACTION_BAR,
		// fill the area below the action bar
		CLIP_CONTENT;
	}

	public enum Exclusivity {
		// hide all other views
		EXCLUDE_ALL,
		// don't hide any other views
		EXCLUDE_NONE;
	}

	/** Getter for mFillType **/
	public FillType fillType() {
		return mFillType;
	}

	/** Setter for mFillType **/
	public AnimationParams fillType(FillType type) {
		mFillType = type;
		return this;
	}

	/** Getter for mExclusivity **/
	public Exclusivity exclusivity() {
		return mExclusivity;
	}

	/** Setter for mExclusivity **/
	public AnimationParams exclusivity(Exclusivity exclusivity) {
		mExclusivity = exclusivity;
		return this;
	}

	public boolean visible() {
		return mIsVisible;
	}

	public AnimationParams visible(boolean isVisible) {
		mIsVisible = isVisible;
		return this;
	}

	public Animation inAnimation() {
		return mInAnim;
	}

	public AnimationParams inAnimation(Animation in) {
		mInAnim = in;
		return this;
	}

	public Animation outAnimation() {
		return mOutAnim;
	}

	public AnimationParams outAnimation(Animation out) {
		mOutAnim = out;
		return this;
	}
}