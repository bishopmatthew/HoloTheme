package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.actionbar.interfaces.ActionBarController;
import com.airlocksoftware.holo.image.IconView;
import com.airlocksoftware.holo.type.FontText;
import com.airlocksoftware.holo.utils.ViewUtils;

public class TwoPaneController implements ActionBarController {
	
	private Context mContext;
	private ActionBarView mActionBar;
	private ViewGroup mControllerContainer;

	// TWO PANE
	private boolean mIsTwoPane;
	private static final int TWO_PANE_LAYOUT_RES = R.layout.vw_actionbar_twopane;
	public static final int TWOPANE_LEFT_TAG = 0;
	public static final int TWOPANE_RIGHT_TAG = 1;

	View mTitleLeft;
	TextView mTitleTextLeft;
	View mButtonsLeft;
	View mTitleRight;
	TextView mTitleTextRight;
	View mButtonsRight;
	
	IconView mOverflowIcon;

	private static final String TAG = ActionBarView.class.getSimpleName();
	private int ACTIONBAR_HEIGHT;

	public TwoPaneController(Context context, ActionBarView ab) {
		mContext = context;
		mActionBar = ab;
		mControllerContainer = mActionBar.getControllerContainer();
		
	}

	@Override
	public void addButton(ActionBarButton button) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeButton(ActionBarButton button) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addOverflowView(View toAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTitleText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeOverflowView(View toRemove) {
		// TODO Auto-generated method stub
	}

	@Override
	public ViewGroup getTitleGroup() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void clearTitleGroup() {
//		for (View v : ViewUtils.directChildViews(mTitleContainer)) {
//			if (v != mTitleText) mTitleContainer.removeView(v);
//		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

	}

	private void inflateLayout(LayoutInflater inflater) {
		inflater.inflate(TWO_PANE_LAYOUT_RES, mControllerContainer);


		mTitleLeft = (RelativeLayout) mControllerContainer.findViewById(R.id.cnt_title_left);
		mTitleTextLeft = (FontText) mControllerContainer.findViewById(R.id.txt_title_left);
		mTitleRight = (RelativeLayout) mControllerContainer.findViewById(R.id.cnt_title_right);
		mTitleTextRight = (FontText) mControllerContainer.findViewById(R.id.txt_title_left);
		mButtonsLeft = (LinearLayout) mControllerContainer.findViewById(R.id.cnt_btns_left);
		mButtonsRight = (LinearLayout) mControllerContainer.findViewById(R.id.cnt_btns_right);

		mOverflowIcon = (IconView) mControllerContainer.findViewById(R.id.icv_overflow);

		mOverflowIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActionBar.toggleOverflow();
			}
		});
	}

}
