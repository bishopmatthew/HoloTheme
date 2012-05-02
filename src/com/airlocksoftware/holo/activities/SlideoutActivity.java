package com.airlocksoftware.holo.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.anim.AnimationOverlayView;
import com.airlocksoftware.holo.slideout.SlideoutFrame;
import com.korovyansk.android.slideout.utils.Screenshot;

public class SlideoutActivity extends ActionBarActivity {

	protected SlideoutFrame mSlideoutFrame;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		getAB().setListListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SlideoutActivity.this.toggleSlideout();
			}
		});

		// GET WIDTH OF BUTTON
		View backButton = findViewById(R.id.back_button);
		int buttonWidth = backButton.getWidth();
		mSlideoutFrame = new SlideoutFrame(this, this, this, buttonWidth);
	}

	protected void toggleSlideout() {
		View backButton = findViewById(R.id.back_button);
		int buttonWidth = backButton.getWidth();
		mSlideoutFrame.setSlideWidth(buttonWidth);
		mSlideoutFrame.showSlideout();
	}
	
	public SlideoutFrame getSlideoutFrame() {
		return mSlideoutFrame;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && mSlideoutFrame.isOpen()){
			mSlideoutFrame.closeSlideout();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
