package com.airlocksoftware.holo.pages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airlocksoftware.holo.activities.ActionBarActivity;
import com.airlocksoftware.holo.interfaces.ActionBarInterface;

/** A fragment that is meant to be held inside of a Page. **/
public abstract class PageFragment extends Fragment implements ActionBarInterface {

	private Context mContext;
	private ActionBarActivity mActivity;
	protected View mFrame;
	protected Page mParent;
	
	private int mFragmentId;

	// CONSTRUCTOR
	public PageFragment(ActionBarActivity activity, Page parent) {
		mContext = activity;
		mActivity = activity;
		mParent = parent;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		this.mContext = (Context) getActivity();
	}
	
	@Override
	public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	public View findViewById(int resourceId) {
		return (mFrame != null) ? mFrame.findViewById(resourceId) : null;
	}
	
	/** Getter for fragmentId**/
	public int fragmentId(){
		return mFragmentId;
	}
	
	/** Setter for fragmentId **/
	public void fragmentId(int id) {
		mFragmentId = id;
	}
	
	/** Getter for mContext**/
	public Context context() {
		return mContext;
	}
	
	/** Getter for mActivity**/
	public ActionBarActivity activity() {
		return mActivity;
	}

}
