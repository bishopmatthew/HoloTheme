package com.airlocksoftware.holo.grid;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public abstract class FixedGridAdapter<T> extends ArrayAdapter<T> {

	// CONSTANTS
	private static final int DEFAULT_TEXT_VIEW_RES_ID = 0;

	// LISTENERS
	private OnItemLongClickListener mOnItemLongClickListener;
	private OnItemClickListener mOnItemClickListener;

	// CONSTRUCTORS
	public FixedGridAdapter(Context context) {
		super(context, DEFAULT_TEXT_VIEW_RES_ID);
	}

	public FixedGridAdapter(Context context, int resource) {
		super(context, resource, DEFAULT_TEXT_VIEW_RES_ID);
	}

	public FixedGridAdapter(Context context, T[] objects) {
		super(context, DEFAULT_TEXT_VIEW_RES_ID, objects);
	}

	public FixedGridAdapter(Context context, List<T> objects) {
		super(context, DEFAULT_TEXT_VIEW_RES_ID, objects);
	}

	public FixedGridAdapter(Context context, int resource, T[] objects) {
		super(context, resource, DEFAULT_TEXT_VIEW_RES_ID, objects);
	}

	public FixedGridAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, DEFAULT_TEXT_VIEW_RES_ID, objects);
	}

	// PUBLIC METHODS
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	/** Children shouldn't call or override this method **/
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = getView(position, convertView);
		if (convertView != null && isEnabled(position)) {
			convertView.setTag(getItem(position));
			convertView.setOnClickListener(childClickListener);
			convertView.setOnLongClickListener(childLongClickListener);
		}
		return convertView;
	}

	public LinearLayout.LayoutParams getGridLayoutParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, 1.0f);
		return params;
	}

	// PUBLIC ABSTRACT METHODS
	public abstract View getView(int position, View convertView);

	// LISTENERS
	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * AdapterView has been clicked.
	 */
	public interface OnItemClickListener {

		/**
		 * Callback method to be invoked when an item in this AdapterView has
		 * been clicked.
		 * <p>
		 * Implementers can call getItemAtPosition(position) if they need to
		 * access the data associated with the selected item.
		 * 
		 * @param fixedGridView The AdapterView where the click happened.
		 * @param view The view within the AdapterView that was clicked (this
		 *            will be a view provided by the adapter)
		 * @param position The position of the view in the adapter.
		 * @param id The row id of the item that was clicked.
		 */
		void onItemClick(View view, int position, long id);
	}

	/**
	 * Register a callback to be invoked when an item in this AdapterView has
	 * been clicked.
	 * 
	 * @param listener The callback that will be invoked.
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	/**
	 * @return The callback to be invoked with an item in this AdapterView has
	 *         been clicked, or null id no callback has been set.
	 */
	public final OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * view has been clicked and held.
	 */
	public interface OnItemLongClickListener {
		/**
		 * Callback method to be invoked when an item in this view has been
		 * clicked and held.
		 * 
		 * Implementers can call getItemAtPosition(position) if they need to
		 * access
		 * the data associated with the selected item.
		 * 
		 * @param parent The AbsListView where the click happened
		 * @param view The view within the AbsListView that was clicked
		 * @param position The position of the view in the list
		 * @param id The row id of the item that was clicked
		 * 
		 * @return true if the callback consumed the long click, false otherwise
		 */
		boolean onItemLongClick(View view, int position, long id);
	}

	/**
	 * Register a callback to be invoked when an item in this AdapterView has
	 * been clicked and held
	 * 
	 * @param listener The callback that will run
	 */
	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mOnItemLongClickListener = listener;
	}

	/**
	 * @return The callback to be invoked with an item in this AdapterView has
	 *         been clicked and held, or null id no callback as been set.
	 */
	public final OnItemLongClickListener getOnItemLongClickListener() {
		return mOnItemLongClickListener;
	}

	public View.OnClickListener childClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				int position = getPosition((T) v.getTag());
				mOnItemClickListener.onItemClick(v, position, getItemId(position));
			}
		}
	};

	public View.OnLongClickListener childLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			if (mOnItemLongClickListener != null) {
				int position = getPosition((T) v.getTag());
				return mOnItemLongClickListener.onItemLongClick(v, position, getItemId(position));
			}
			return false;
		}
	};

}
