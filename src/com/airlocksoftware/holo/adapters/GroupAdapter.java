package com.airlocksoftware.holo.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An adapter for a group of items that have a header and footer. Works well with MergeAdapter to provide a list of
 * groups. If there aren't any items in the mObjects array, then the footer and header won't show up.
 **/
public abstract class GroupAdapter<T> extends BaseAdapter {

	private View mHeaderView;
	private View mFooterView;
	private List<T> mObjects = new ArrayList<T>();

	private static final int HEADER_VIEW_TYPE = 0;
	private static final int LIST_VIEW_TYPE = 1;
	private static final int FOOTER_VIEW_TYPE = 2;

	@Override
	public int getCount() {
		int count = mObjects.size();
		if (count > 0) {
			if (mHeaderView != null) count++;
			if (mFooterView != null) count++;
		}
		return count;
	}

	@Override
	public T getItem(int position) {
		int index = position;
		if (mHeaderView != null) position -= 1;
		if (index >= mObjects.size()) return null;
		else return mObjects.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int viewType = getItemViewType(position);

		if (viewType == HEADER_VIEW_TYPE) return mHeaderView;
		else if (viewType == FOOTER_VIEW_TYPE) return mFooterView;
		else return getView(getItem(position), convertView, parent);
	}

	public abstract View getView(T item, View convertView, ViewGroup parent);

	@Override
	public int getItemViewType(int position) {
		int count = getCount();
		if (position == 0) {
			if (mHeaderView != null) return HEADER_VIEW_TYPE;
			else if (mObjects.size() > 0) return LIST_VIEW_TYPE;
			else return FOOTER_VIEW_TYPE;
		} else if (position == count - 1 && mFooterView != null) {
			return FOOTER_VIEW_TYPE;
		} else {
			return LIST_VIEW_TYPE;
		}
	}

	@Override
	public int getViewTypeCount() {
		int count = 0;
		if (mHeaderView != null) count++;
		if (mFooterView != null) count++;
		if (mObjects.size() > 0) count++;
		return count;
	}
	
	public void addHeader(View header) {
		mHeaderView = header;
		notifyDataSetChanged();
	}
	
	public void addFooter(View footer) {
		mHeaderView = footer;
		notifyDataSetChanged();
	}
	
	public void add(T object) {
		mObjects.add(object);
	}
	
	public void addAll(Collection<? extends T> coll) {
		mObjects.addAll(coll);
	}
	
	public void clear() {
		mObjects.clear();
	}

}
