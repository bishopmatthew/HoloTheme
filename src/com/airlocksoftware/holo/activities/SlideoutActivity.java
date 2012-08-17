package com.airlocksoftware.holo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.interfaces.OnActivityResultListener;
import com.airlocksoftware.holo.pages.StaticPageHolder;
import com.airlocksoftware.holo.slideout.SlideoutFrame;

public class SlideoutActivity extends ActionBarActivity {

	private SlideoutFrame mSlideoutFrame;

	private StaticPageHolder mHolder;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		setContentView(R.layout.slideout_list_activity);
		mHolder = (StaticPageHolder) findViewById(R.id.page_holder);

		actionBar().setTopListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SlideoutActivity.this.toggleSlideout();
			}
		});

		mSlideoutFrame = new SlideoutFrame(this, this, overlayManager(), actionBar().appButton()
				.getWidth(), ROOT_VIEW_ID);
	}

	protected void toggleSlideout() {
		mSlideoutFrame.setSlideWidth(actionBar().appButton().getWidth());
		mSlideoutFrame.toggle();
	}

	public SlideoutFrame slideoutFrame() {
		return mSlideoutFrame;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (mHolder.page().getVisibleFragment() instanceof OnActivityResultListener) {
			((OnActivityResultListener) mHolder.page().getVisibleFragment()).onActivityResult(
					requestCode, resultCode, intent);
		}
	}

	@Override
	public void onBackPressed() {
		if (mSlideoutFrame.isOpen()) mSlideoutFrame.close();
		else if (!mHolder.page().onBackPressed()) super.onBackPressed();
	}

	public StaticPageHolder holder() {
		return mHolder;
	}

}
