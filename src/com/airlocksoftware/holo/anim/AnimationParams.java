package com.airlocksoftware.holo.anim;


public class AnimationParams {
	
	// STATE
	private FillType mFillType = FillType.FILL_SCREEN;
	private int mZIndex = 0;
	private Exclusivity mExclusivity = Exclusivity.EXCLUDE_ALL;
	
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
	
	public Exclusivity exclusivity() {
		return mExclusivity;
	}

	public FillType fillType() {
		return mFillType;
	}
}