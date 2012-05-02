package com.airlocksoftware.holo.slideout;

import java.util.HashMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.checkable.CheckableView;
import com.airlocksoftware.holo.interfaces.PageHolder;
import com.airlocksoftware.holo.pages.Page;
import com.airlocksoftware.holo.type.FontText;

public class SlideoutListItem extends CheckableView implements PageHolder {

	private Context mContext;
	private String mLabel;
	private Drawable mIcon;
	private Page mPage;

	private String mClassname;

	private HashMap<String, Class<?>> mClassMap = new HashMap<String, Class<?>>();
	

	// CONSTRUCTORS
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

	public Page getPage() {
		if (mPage == null) {
			mPage = instantiate(mClassname);
		}
		return mPage;
	}

	public void setPage(Page page) {
		this.mPage = page;
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

		mClassname = a.getString(R.styleable.SlideoutListItem_page_classname);

		a.recycle();
	}

	private Page instantiate(String pageClassName) {
		try {
			Class<?> clazz = mClassMap.get(pageClassName);
			if (clazz == null) {
				// Class not found in the cache, see if it's real, and try to add it
				clazz = mContext.getClassLoader().loadClass(pageClassName);
				mClassMap.put(pageClassName, clazz);
			}
			Page p = (Page) clazz.newInstance();
			return p;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to instantiate page " + pageClassName
					+ ": make sure class name exists, is public, and has an" + " empty constructor that is public", e);
		} catch (java.lang.InstantiationException e) {
			throw new RuntimeException("Unable to instantiate page " + pageClassName
					+ ": make sure class name exists, is public, and has an" + " empty constructor that is public", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to instantiate page " + pageClassName
					+ ": make sure class name exists, is public, and has an" + " empty constructor that is public", e);
		}
	}

}
