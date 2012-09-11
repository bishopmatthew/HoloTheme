package com.airlocksoftware.holo.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.SpinnerAdapter;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.type.FontText;

public abstract class SpinnerArrayAdapter<T> extends BaseAdapter implements SpinnerAdapter,
		ListAdapter {

	protected transient Context mContext;

	protected List<T> mObjects = new ArrayList<T>();
	int mSelectedPosition = -1;

	public SpinnerArrayAdapter(Context context) {
		this(context, null);
	}

	public SpinnerArrayAdapter(Context context, Collection<T> objects) {
		super();
		mContext = context;
		if (objects != null) addAll(objects);
	}

	@Override
	public int getCount() {
		if (mObjects != null) {
			return mObjects.size();
		} else {
			return 0;
		}
	}

	@Override
	public T getItem(int position) {
		if (mObjects != null) {
			return mObjects.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(T item) {
		mObjects.add(item);
	}

	public void addAll(Collection<T> items) {
		mObjects.addAll(items);
	}

	public void clear() {
		mObjects.clear();
	}

	/** Gets the position of an object within the array **/
	public int getPosition(Object object) {
		if (mObjects != null) {
			return mObjects.indexOf(object);
		} else {
			return -1;
		}
	}

	public T getSelected() {
		try {
			return mObjects.get(mSelectedPosition);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Returns the index of the currently selected item in the radio list, or -1
	 * otherwise
	 **/
	int getSelectedPosition() {
		return mSelectedPosition;
	}

	@Override
	/** Gets the view that will display in dropdown list or dialog **/
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.vw_spinner_dropdown, parent, false);
		}

		convertView.setTag(getItem(position));
		((FontText) convertView.findViewById(R.id.text)).setText(getItemText(position));

		RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.radio_button);
		if (position == mSelectedPosition) {
			radioButton.setChecked(true);
		} else {
			radioButton.setChecked(false);
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setSelectedPosition(getPosition(v.getTag()));

				// call onItemClick
				onItemClick(getSelectedPosition());
			}
		});
		return convertView;
	}

	/** Gets the view that will display within the spinner on the page (usually just a TextView) **/
	public View getSpinnerView(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View spinnerView = inflater.inflate(R.layout.vw_spinner, parent, false);

		int position = getSelectedPosition();
		FontText text = (FontText) spinnerView.findViewById(R.id.text);
		if (position != -1) {
			text.setText(getItemText(position));
		}
		return spinnerView;
	}

	public abstract String getItemText(int position);

	public abstract void onItemClick(int position);

	public void setSelectedPosition(int position) {
		this.mSelectedPosition = position;
	}

}
