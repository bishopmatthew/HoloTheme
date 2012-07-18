package com.airlocksoftware.holo.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.adapters.OverflowAdapter.OverflowItem;

/** An adapter for an overflow menu in the ActionBar **/
public class OverflowAdapter extends ArrayAdapter<OverflowItem> {

	// CONTEXT
	Context mContext;
	
	//
	private int mTextResId;
	private int mImageResId;
	private int mLayoutResId;
	
	private static final int DEFAULT_TEXT_VIEW_RES_ID = 0;
	
	// CONSTRUCTORS
	public OverflowAdapter(Context context) {
		super(context, DEFAULT_TEXT_VIEW_RES_ID);
		mContext = context;
		getAttrs();
		// TODO Auto-generated constructor stub
	}

	public void setTextResId(int textResId) {
		mTextResId = textResId;
	}

	public void setImageResId(int imageResId) {
		mImageResId = imageResId;
	}

	public void setLayoutResId(int layoutResId) {
		mLayoutResId = layoutResId;
	}

	// PUBLIC METHODS
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		OverflowItem object = getItem(position);
		if(convertView == null) {
			view = LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
		} else {
			view = convertView;
		}
		
		((TextView) view.findViewById(mTextResId)).setText(object.mText);
		((ImageView) view.findViewById(mImageResId)).setImageResource(object.mIconResId);
		
		view.setTag(object.mIconResId);
		return view;
	}
	
	// PRIVATE METHODS
	private void getAttrs() {
		TypedValue tv = new TypedValue();
	    mContext.getTheme().resolveAttribute(R.attr.overflowMenuTextResId, tv, true);
	    setTextResId(tv.resourceId);
	    
	    tv = new TypedValue();
	    mContext.getTheme().resolveAttribute(R.attr.overflowMenuImageResId, tv, true);
	    setImageResId(tv.resourceId);
	    
	    tv = new TypedValue();
	    mContext.getTheme().resolveAttribute(R.attr.overflowMenuLayoutResId, tv, true);
	    setLayoutResId(tv.resourceId);
	}

	//INNER CLASSES
	public static class OverflowItem {
		public int mIconResId;
		public String mText;
		public OnClickListener mListener;

		public OverflowItem(int iconResId, String text, OnClickListener listener) {
			mIconResId = iconResId;
			mText = text;
			mListener = listener;
		}

		@Override
		public String toString() {
			return mText;
		}
	}
}
