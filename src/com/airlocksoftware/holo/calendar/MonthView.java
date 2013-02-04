package com.airlocksoftware.holo.calendar;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.calendar.MonthAdapter.OnMonthChangedListener;
import com.airlocksoftware.holo.grid.FixedGridAdapter;
import com.airlocksoftware.holo.grid.FixedGridView;
import com.airlocksoftware.holo.type.FontButton;
import com.airlocksoftware.holo.type.FontText;

/** Displays a monthly calendar with controls at the top for changing the month. **/
public class MonthView extends FrameLayout {

	// CONTEXT
	protected Context mContext;

	// VIEWS
	// NAV
	private FontButton mNavTitleButton;
	private ImageButton mNavRightButton;
	private ImageButton mNavLeftButton;

	// CALENDAR
	ArrayList<LinearLayout> rows;
	FixedGridView mGrid;

	// DATA
	private MonthAdapter mAdapter;
	
	// LISTENERS
	

	// CONSTANTS
	private static final int DEFAULT_STYLE = 0;
	private static final int DEFAULT_LAYOUT = R.layout.vw_month;
//	private static final int NUM_ROWS = 6;
//	private static final int NUM_COLS = 7;
	private static SimpleDateFormat NAV_TITLE_FORMATTER = new SimpleDateFormat("MMMM yyyy");
	
	// CONSTRUCTORS
	public MonthView(Context context) {
		this(context, null);
	}

	public MonthView(Context context, AttributeSet attrs) {
		this(context, attrs, DEFAULT_STYLE);
	}

	public MonthView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		// TODO do stuff with my style?
		mContext = context;
		inflateLayout();

		// TESTING
		mNavTitleButton.setText("March 2013");
	}

	// PUBLIC METHODS
	public void setAdapter(MonthAdapter adapter) {
		mAdapter = adapter;
		mGrid.setAdapter(mAdapter);
		refreshViews();
	}
	
	public MonthAdapter getAdapter() {
		return mAdapter;
	}

	// PRIVATE METHODS
	private void inflateLayout() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(DEFAULT_LAYOUT, this);

		// NAV
		mNavTitleButton = (FontButton) findViewById(R.id.nav_title_button);
		mNavRightButton = (ImageButton) findViewById(R.id.nav_right_button);
		mNavLeftButton = (ImageButton) findViewById(R.id.nav_left_button);
		
		mNavRightButton.setOnClickListener(navButtonListener);
		mNavLeftButton.setOnClickListener(navButtonListener);
		
		// HEADER
		String[] dayNames = new DateFormatSymbols().getShortWeekdays();
		ViewGroup calHeader = (ViewGroup) findViewById(R.id.month_view_header);
		for (int i = 0; i < calHeader.getChildCount(); i++) {
			((FontText) calHeader.getChildAt(i)).setText(dayNames[i + 1].toUpperCase());
		}
		
		// CALENDAR
		mGrid = (FixedGridView) findViewById(R.id.month_view_grid);		
	}

	/** Populates the calendar from the adapter **/
	private void refreshViews() {
		String navTitle = NAV_TITLE_FORMATTER.format(mAdapter.getDate());
		mNavTitleButton.setText(navTitle);
	}
	
//	private OnMonthChangedListener monthChangeListener;
	
	private View.OnClickListener navButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int delta = 1;
			if(v.getId() == R.id.nav_left_button) delta = -1;
			mAdapter.changeMonth(delta);
			refreshViews();
		}
	};
	
}
