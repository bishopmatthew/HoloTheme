package com.airlocksoftware.holo.picker.share;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.airlocksoftware.holo.R;

/** Displays a styleable list of **/
public class ShareList extends ListView {

	Context mContext;
	Intent mIntent;
	ArrayAdapter<ShareItem> mShareItems;
	
	 private static final int DEFAULT_LAYOUT = R.layout.vw_sharelist_item;

	public ShareList(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		
		setItemsCanFocus(true);
		setSelector(mContext.getResources().getDrawable(R.drawable.bg_actionbar_overflow));
		 
		mShareItems = new ArrayAdapter<ShareItem>(mContext, 0) {
			@Override
			public View getView (int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if(v == null) v = LayoutInflater.from(mContext).inflate(DEFAULT_LAYOUT, parent, false);
				
				ShareItem item = this.getItem(position);
				((TextView) v.findViewById(R.id.txt)).setText(item.label);
				((ImageView) v.findViewById(R.id.img)).setImageDrawable(item.icon);
				
				return v;
			}
		};

		setAdapter(mShareItems);
		setOnItemClickListener(mListener);
	}

	public void setIntent(Intent intent) {
		mIntent = intent;
		mShareItems.clear();
		for(ShareItem item : ShareItem.getShareItems(mContext, mIntent)) {
			mShareItems.add(item);
		}
	}

	private OnItemClickListener mListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ShareItem item = mShareItems.getItem(position);
			mIntent.setPackage(item.packageName);
			mContext.startActivity(mIntent);
		}

	};

}
