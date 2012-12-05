package com.airlocksoftware.holo.picker.share;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
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

	int mTextColor;
	int mItemBgDrawable;

	private static final int DEFAULT_LAYOUT = R.layout.vw_sharelist_item;

	public ShareList(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ShareList);
		mTextColor = a.getColor(R.styleable.ShareList_android_textColor, Color.BLACK);
		mItemBgDrawable = a.getResourceId(R.styleable.ShareList_sharelist_itemBg, 0);
		a.recycle();

		setItemsCanFocus(true);

		mShareItems = new ArrayAdapter<ShareItem>(mContext, 0) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if (v == null) v = LayoutInflater.from(mContext).inflate(DEFAULT_LAYOUT, parent, false);

				ShareItem item = this.getItem(position);
				TextView txt = (TextView) v.findViewById(R.id.txt);
				txt.setText(item.label);
				txt.setTextColor(mTextColor);
				((ImageView) v.findViewById(R.id.img)).setImageDrawable(item.icon);
				v.setBackgroundResource(mItemBgDrawable);

				return v;
			}
		};

		setAdapter(mShareItems);
		setOnItemClickListener(mListener);
	}

	public void setIntentType(Intent intent) {
		if (mIntent == null) setIntent(intent);
		mShareItems.clear();
		for (ShareItem item : ShareItem.getShareItems(mContext, mIntent)) {
			mShareItems.add(item);
		}
	}

	public void setIntent(Intent intent) {
		mIntent = intent;
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
