package com.airlocksoftware.holo.actionbar;

import java.util.ArrayList;
import java.util.List;

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
import com.airlocksoftware.holo.adapters.OverflowAdapter;
import com.airlocksoftware.holo.adapters.OverflowAdapter.OverflowItem;
import com.airlocksoftware.holo.anim.AnimationParams;
import com.airlocksoftware.holo.anim.AnimationParams.Exclusivity;
import com.airlocksoftware.holo.anim.AnimationParams.FillType;
import com.airlocksoftware.holo.anim.OverlayManager;
import com.airlocksoftware.holo.type.FontText;

public class ActionBar extends Fragment {

	// CONTEXT
	// ActionBarActivity mActivity;
	Context mContext;

	// VIEWS
	RelativeLayout mFrame;
	ListView mOverflowList;
	ImageButton mOverflowButton;
	ImageView mAppButtonNavIcon;

	OverflowAdapter mAdapter;
	OverlayManager mOverlayManager;

	// RESOURCE IDS
	int mAppUpIconResId;
	int mAppTopIconResId;

	// LISTENERS
	private View.OnClickListener mTopListener;
	private View.OnClickListener mUpListener;

	// QUEUE of stuff done before onCreateView() is called
	private List<OverflowItem> mOverflowQueue;

	// CONSTANTS
	private static final String TITLE_REPLACEMENT_TAG = "ActionBar.titleReplacement";
	private static final int TITLE_TEXT_ID = R.id.action_bar_title;
	private static final int DEFAULT_LAYOUT_RES_ID = R.layout.action_bar;

	// PUBLIC METHODS
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = inflater.getContext();

		mFrame = (RelativeLayout) inflater.inflate(DEFAULT_LAYOUT_RES_ID, container, false);
		mOverflowButton = (ImageButton) findViewById(R.id.overflow_button);
		mOverflowList = (ListView) findViewById(R.id.overflow_list);
		mAppButtonNavIcon = (ImageView) findViewById(R.id.app_nav_icon);

		TypedValue tv = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.actionBarNavUpIcon, tv, true);
		mAppUpIconResId = tv.resourceId;

		tv = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.actionBarNavTopIcon, tv, true);
		mAppTopIconResId = tv.resourceId;

		return mFrame;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		// GET CONTEXT
		// Activity activity = getActivity();
		// if (activity instanceof ActionBarActivity) {
		// mActivity = (ActionBarActivity) activity;
		// } else {
		// throw new RuntimeException("Can't use an ActionBar outside of an ActionBarActivity");
		// }

		mContext = getActivity();

		// SETUP OVERFLOW LIST
		((FrameLayout) mOverflowList.getParent()).removeView(mOverflowList);
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
				if (mOverlayManager != null) {
					mOverlayManager.toggleViewById(mOverflowList.getId());
				} else {
					throw new NullPointerException(
							"You have to call ActionBar.overlayManager(OverlayManager manager) if you want to display an Overflow list");
				}
			}
		});

		// DEAL WITH STUFF FROM QUEUE
		// OverflowItems
		if (mOverflowQueue != null) {
			for (OverflowItem item : mOverflowQueue) {
				mAdapter.add(item);
			}
			showOverflowButton();
		}

	}

	public ActionBar overlayManager(OverlayManager om) {
		mOverlayManager = om;
		om.addView(mOverflowList,
				new AnimationParams(FillType.CLIP_CONTENT).exclusivity(Exclusivity.EXCLUDE_ALL));
		return this;
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

		FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER);
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

		int buttonEdgeLength = (int) getActivity().getResources().getDimension(
				R.dimen.action_bar_height);
		actionBarButton.setLayoutParams(new LinearLayout.LayoutParams(buttonEdgeLength,
				buttonEdgeLength));

		actionBarButton.setScaleType(ScaleType.CENTER);
		actionBarButton.setBackgroundResource(R.drawable.btn_ab);
		actionBarButton.setImageResource(iconResourceId);
		LinearLayout buttonContainer = (LinearLayout) mFrame.findViewById(R.id.button_container);
		buttonContainer.addView(actionBarButton);
	}

	/** Adds a button to the right side of the ActionBar with a specified icon, text, and callback **/
	public void addActionBarButton(int iconResourceId, String text, OnClickListener clickListener) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout buttonContainer = (LinearLayout) mFrame.findViewById(R.id.button_container);
		RelativeLayout button = (RelativeLayout) inflater.inflate(R.layout.button_ab_text_plus_icon,
				buttonContainer, false);
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
		if (mAdapter == null) {
			// add it to a queue
			if (mOverflowQueue == null) mOverflowQueue = new ArrayList<OverflowItem>();
			mOverflowQueue.add(item);
		} else {
			mAdapter.add(item);
			showOverflowButton();
		}
	}

	public void removeOverflowItem(OverflowItem item) {
		mAdapter.remove(item);
		if (mAdapter.getCount() < 1) hideOverflowButton();
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

	private void refreshAppButton() {
		View appButton = appButton();

		if (mUpListener != null) {
			appButton.setClickable(true);
			appButton.setOnClickListener(mUpListener);
			mAppButtonNavIcon.setImageResource(mAppUpIconResId);
			mAppButtonNavIcon.setVisibility(View.VISIBLE);
		} else if (mTopListener != null) {
			appButton.setClickable(false);
			appButton.setOnClickListener(mTopListener);
			mAppButtonNavIcon.setImageResource(mAppTopIconResId);
			mAppButtonNavIcon.setVisibility(View.VISIBLE);
		} else {
			appButton.setClickable(false);
			appButton.setOnClickListener(null);
			mAppButtonNavIcon.setVisibility(View.GONE);
		}
	}

	/** Get the view that contains the app button (up / list) **/
	public View appButton() {
		return mFrame.findViewById(R.id.app_button);
	}

	public void setUpListener(OnClickListener listener) {
		mUpListener = listener;
		refreshAppButton();
	}

	public void setTopListener(OnClickListener listener) {
		mTopListener = listener;
		refreshAppButton();
	}

}
