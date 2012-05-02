package com.airlocksoftware.holo.adapters;

import java.util.ArrayList;

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

@SuppressWarnings("rawtypes")
public abstract class RadioListAdapter extends BaseAdapter implements SpinnerAdapter, ListAdapter {

	protected transient Context context;
	
	protected ArrayList objects;
	int selectedPosition;

	private String prompt;
	
	private static final String DEFAULT_PROMPT = "Tap to select";

	public RadioListAdapter(Context context, ArrayList objects) {
		super();
		this.context = context;
		setData(objects);
		selectedPosition = -1;
	}

	public void setData(ArrayList data) {
		this.objects = data;
	}

	@Override
	public int getCount() {
		if (objects != null) {
			return objects.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (objects != null) {
			return objects.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** Gets the position of an object within the array **/
	public int getPosition(Object object) {
		if (objects != null) {
			return objects.indexOf(object);
		} else {
			return -1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.spinner_dropdown_view, parent, false);
		}
		
		convertView.setTag(getItem(position));
		((FontText) convertView.findViewById(R.id.text)).setText(getItemText(position));
		
		RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.radio_button);
		if(position == selectedPosition) {
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

	public View getSpinnerView(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View spinnerView = inflater.inflate(R.layout.spinner_view, parent, false);

		int position = getSelectedPosition();
		FontText text = (FontText) spinnerView.findViewById(R.id.text);
		if (position != -1) {
			text.setText(getItemText(position));
		} else if (prompt != null) {
			text.setText(prompt);
		} else {
			text.setText(DEFAULT_PROMPT);
		}
		return spinnerView;
	}

	public abstract String getItemText(int position);

	public abstract void onItemClick(int position);
	
	public void setSelectedPosition(int position) {
		this.selectedPosition = position;
	}

	public Object getSelected() {
		try {
			return objects.get(selectedPosition);
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * Returns the index of the currently selected item in the radio list, or -1
	 * otherwise
	 **/
	int getSelectedPosition() {
		return selectedPosition;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

}
