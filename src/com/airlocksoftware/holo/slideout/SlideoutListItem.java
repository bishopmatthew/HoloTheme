package com.airlocksoftware.holo.slideout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.checkable.CheckableView;
import com.airlocksoftware.holo.type.FontText;

public class SlideoutListItem extends CheckableView {

	private Context mContext;
	private String mLabel;
	private Drawable mIcon;

	// CONSTRUCTORS
	/** Context passed must be an ActionBarActivity **/
	public SlideoutListItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;

		retrieveAttrs(attrs);
		inflateLayout();
	}

	// PUBLIC METHODS

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		mLabel = label;
	}

	public Drawable getIcon() {
		return mIcon;
	}

	public void setIcon(Drawable icon) {
		this.mIcon = icon;
	}

	// PRIVATE METHOD

	private void inflateLayout() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.default_slideout_list_item, this);

		// find views
		((FontText) findViewById(R.id.text)).setText(mLabel);
		((ImageView) findViewById(R.id.icon)).setImageDrawable(mIcon);

		// set style
		setClickable(true);
		this.setBackgroundResource(R.drawable.btn_slideout_list_holo_dark);
	}

	private void retrieveAttrs(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SlideoutListItem, 0, 0);

		mIcon = a.getDrawable(R.styleable.SlideoutListItem_android_src);

		mLabel = a.getString(R.styleable.SlideoutListItem_android_text);

		// mClassname = a.getString(R.styleable.SlideoutListItem_page_classname);
		//
		// mFragmentId = a.getInt(R.styleable.SlideoutListItem_page_fragmentId, -1);

		a.recycle();
	}
}
