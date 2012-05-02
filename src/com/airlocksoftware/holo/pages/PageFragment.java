package com.airlocksoftware.holo.pages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airlocksoftware.holo.interfaces.ActionBarInterface;

public abstract class PageFragment extends Fragment implements ActionBarInterface {

	protected Context mContext;
	protected View mFrame;
	
	public abstract String getStringId();

	public PageFragment(Context context) {
		this.mContext = context;
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

}
