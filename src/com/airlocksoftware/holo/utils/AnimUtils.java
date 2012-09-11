package com.airlocksoftware.holo.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimUtils {

	public static void startInAnimation(View view, Animation inAnim) {
		view.setAnimation(inAnim);
		view.setVisibility(View.VISIBLE);
	}

	public static void startOutAnimation(View view, Animation outAnim) {
		final View hideView = view;
		outAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				hideView.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		view.startAnimation(outAnim);
	}

}
