package com.airlocksoftware.holo.actionbar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.activities.ActionBarActivity;
import com.airlocksoftware.holo.activities.SlideoutNavActivity;
import com.airlocksoftware.holo.adapters.OverflowAdapter;
import com.airlocksoftware.holo.adapters.OverflowAdapter.OverflowItem;
import com.airlocksoftware.holo.type.FontText;

public class ActionBar extends Fragment {

	// CONTEXT
	ActionBarActivity mContext;

	// VIEWS
	RelativeLayout mFrame;
	ListView mOverflowList;
	OverflowAdapter mAdapter;
	ImageButton mOverflowButton;
	ImageView mNavIcon;
	
	// RESOURCE IDS
	int mNavBackIconResId;
	int mNavListIconResId;
	
	// LISTENERS
	private View.OnClickListener mListListener;
	private View.OnClickListener mBackListener;


	// CONSTANTS
	private static final String TITLE_REPLACEMENT_TAG = "title_replacement";
	private static final int TITLE_TEXT_ID = R.id.action_bar_title;
	private static final int DEFAULT_LAYOUT_RES_ID = R.layout.action_bar;
	
	// PUBLIC METHODS
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = inflater.getContext();
		
		mFrame = (RelativeLayout) inflater.inflate(DEFAULT_LAYOUT_RES_ID, container, false);
		mOverflowButton = (ImageButton) findViewById(R.id.overflow_button);
		mOverflowList = (ListView) findViewById(R.id.overflow_list);
		mNavIcon = (ImageView) findViewById(R.id.nav_icon);
		
		TypedValue tv = new TypedValue();
	    context.getTheme().resolveAttribute(R.attr.actionBarNavBackIcon, tv, true);
	    mNavBackIconResId = tv.resourceId;
	    		
	    tv = new TypedValue();
	    context.getTheme().resolveAttribute(R.attr.actionBarNavListIcon, tv, true);
	    mNavListIconResId = tv.resourceId;
		
		return mFrame;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		// GET CONTEXT
		mContext = (ActionBarActivity) getActivity();
		
		// SETUP OVERFLOW LIST
		((FrameLayout) mOverflowList.getParent()).removeView(mOverflowList);
		mContext.getAnimationOverlayView().addView(mOverflowList);
		mAdapter = new OverflowAdapter(mContext);
		mOverflowList.setAdapter(mAdapter);
		mOverflowList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OverflowItem item = mAdapter.getItem(position);
				if (item != null && item.mListener != null) item.mListener.onClick(view);
			}
		});
		
		// SETUP OVERFLOW BUTTON
		mOverflowButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.getAnimationOverlayView().toggleViewById(mOverflowList.getId());
			}
		});
		
	}

	// TITLE
	public void setTitle(String text) {
		FontText title = (FontText) findViewById(R.id.action_bar_title);
		FrameLayout titleContainer = (FrameLayout) findViewById(R.id.title_container);
		View toRemove = titleContainer.findViewWithTag(TITLE_REPLACEMENT_TAG);
		if (toRemove != null) titleContainer.removeView(toRemove);

		title.setVisibility(View.VISIBLE);
		title.setText(text);
	}

	public void replaceTitle(View view) {
		findViewById(R.id.action_bar_title).setVisibility(View.GONE);
		view.setTag(TITLE_REPLACEMENT_TAG);

		FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);
		view.setLayoutParams(params);

		FrameLayout titleContainer = (FrameLayout) findViewById(R.id.title_container);
		titleContainer.addView(view);
	}

	public FrameLayout.LayoutParams getTitleLayoutParams() {
		return (LayoutParams) findViewById(R.id.action_bar_title).getLayoutParams();
	}

	public FrameLayout getTitleContainer() {
		return (FrameLayout) findViewById(R.id.title_container);
	}

	// ACTION BAR BUTTONS

	/**
	 * Adds a button to the right side of the action bar, with the specified
	 * icon and callback
	 **/
	public void addActionBarButton(int iconResourceId, OnClickListener clickListener) {
		ImageButton actionBarButton = new ImageButton(getActivity(), null);
		actionBarButton.setOnClickListener(clickListener);
		actionBarButton.setTag(iconResourceId);

		int buttonEdgeLength = (int) getActivity().getResources().getDimension(R.dimen.action_bar_height);
		actionBarButton.setLayoutParams(new LinearLayout.LayoutParams(buttonEdgeLength, buttonEdgeLength));

		actionBarButton.setScaleType(ScaleType.CENTER);
		actionBarButton.setBackgroundResource(R.drawable.btn_ab);
		actionBarButton.setImageResource(iconResourceId);
		LinearLayout buttonContainer = (LinearLayout) mFrame.findViewById(R.id.button_container);
		buttonContainer.addView(actionBarButton);
	}

	public void addActionBarButton(int iconResourceId, String text, OnClickListener clickListener) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout buttonContainer = (LinearLayout) mFrame.findViewById(R.id.button_container);
		RelativeLayout button = (RelativeLayout) inflater.inflate(R.layout.button_ab_text_plus_icon, buttonContainer,
				false);
		button.setOnClickListener(clickListener);
		button.setTag(iconResourceId);
		((FontText) button.findViewById(R.id.text)).setText(text);
		((ImageView) button.findViewById(R.id.icon)).setImageResource(iconResourceId);
		buttonContainer.addView(button);
	}

	public void removeActionBarButtonByIcon(int iconResId) {
		LinearLayout buttonContainer = (LinearLayout) mFrame.findViewById(R.id.button_container);
		View button = buttonContainer.findViewWithTag(iconResId);
		if (button != null) {
			buttonContainer.removeView(button);
		}
	}

	public void clearActionBarButtons() {
		LinearLayout buttonContainer = (LinearLayout) mFrame.findViewById(R.id.button_container);
		buttonContainer.removeAllViews();
	}

	// OVERFLOW MENU
	public void addOverflowItem(OverflowItem item) {
		mAdapter.add(item);
		showOverflowButton();
	}
	
	public void removeOverflowItemIcon(OverflowItem item) {
		mAdapter.remove(item);
		if(mAdapter.getCount() < 1) hideOverflowButton();
	}
	
	public void showOverflowButton() {
		findViewById(R.id.overflow_button).setVisibility(View.VISIBLE);
	}
	
	public void hideOverflowButton() {
		findViewById(R.id.overflow_button).setVisibility(View.GONE);
	}

	// MANAGING VIEWS
	public View findViewById(int id) {
		return (mFrame != null) ? mFrame.findViewById(id) : null;
	}

	public View findViewWithTag(Object tag) {
		return (mFrame != null) ? mFrame.findViewWithTag(tag) : null;
	}

	public View findViewByIcon(int iconResId) {
		return findViewWithTag(iconResId);
	}

	// UP / LIST / BACK BUTTON
	
		private void refreshBackButton() {
			View backButton = mFrame.findViewById(R.id.back_button);
			
			if (mBackListener != null) {
				backButton.setClickable(true);
				backButton.setOnClickListener(mBackListener);
				mNavIcon.setImageResource(mNavBackIconResId);
				mNavIcon.setVisibility(View.VISIBLE);
			} else if(mListListener != null){
				backButton.setClickable(false);
				backButton.setOnClickListener(mListListener);
				mNavIcon.setImageResource(mNavListIconResId);
				mNavIcon.setVisibility(View.VISIBLE);
			} else {
				backButton.setClickable(false);
				backButton.setOnClickListener(null);
				mNavIcon.setVisibility(View.GONE);
			}
		}
		
		public void setBackListener(OnClickListener listener) {
			mBackListener = listener;
			refreshBackButton();
		}
		
		public void setListListener(OnClickListener listener) {
			mListListener = listener;
			refreshBackButton();
		}

}
