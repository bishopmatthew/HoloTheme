package com.airlocksoftware.holo.grid;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.airlocksoftware.holo.R;

/**
 * A grid of mNumRows by mNumCols that doesn't scroll (unless wrapped in
 * ScrollView) and expands to fill whatever space it is given.
 * 
 * @author matthewbbishop
 * @param <T>
 * 
 */
public class FixedGridView extends LinearLayout {

	// CONTEXT
	Context mContext;

	// INSTANCE VARIABLES
	private int mNumRows;
	private int mNumCols;
	private int mDefStyle;

	private FixedGridAdapter mAdapter;
	ArrayList<View> mScrapViews;
	private ArrayList<LinearLayout> mRows;

	// CONSTANTS
	private static final int DEFAULT_STYLE = 0;
	private static final float DEFAULT_LAYOUT_WEIGHT = 1.0f;

	// CONSTRUCTORS
	public FixedGridView(Context context, int numCols, int numRows) {
		super(context);
		mContext = context;
		mNumRows = numRows;
		mNumCols = numCols;

		init();
	}

	public FixedGridView(Context context, AttributeSet attrs) {
		this(context, attrs, DEFAULT_STYLE);
	}

	public FixedGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mContext = context;
		mDefStyle = defStyle;
		getAttrs(attrs);

		init();
	}

	// PUBLIC METHODS
	public FixedGridAdapter getAdapter() {
		return mAdapter;
	}

	public void setAdapter(FixedGridAdapter adapter) {
		mAdapter = adapter;
		mAdapter.registerDataSetObserver(mObserver);
		populate();
	}

	// PRIVATE METHODS
	private void getAttrs(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FixedGridView);

		mNumRows = a.getInt(R.styleable.FixedGridView_grid_rows, 1);
		mNumCols = a.getInt(R.styleable.FixedGridView_grid_cols, 1);

		a.recycle();
	}

	private void init() {
		setOrientation(VERTICAL);
		setClickable(true);

		mScrapViews = new ArrayList<View>();

		mRows = new ArrayList<LinearLayout>();
		for (int i = 0; i < mNumRows; i++) {
			LinearLayout row = new LinearLayout(mContext, null);
			row.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,
					DEFAULT_LAYOUT_WEIGHT));
			addView(row);
			mRows.add(row);
		}
	}

	/** Populates the FixedGridView from mAdapter **/
	private void populate() {
		for (int rowPos = 0; rowPos < mNumRows; rowPos++) {
			LinearLayout row = mRows.get(rowPos);
			for (int colPos = 0; colPos < mNumCols; colPos++) {
				int position = (rowPos * mNumCols) + colPos;
				View scrapView = getScrapView(position);
				View item = mAdapter.getView(position, scrapView, row);
				// only add it if we didn't get a scrapView (because otherwise
				// it's already in position)
				if (scrapView == null) {
					row.addView(item);
					mScrapViews.add(item);
				} else {
					row.removeViewAt(colPos);
					row.addView(item, colPos);
				}
			}
		}
	}

	private View getScrapView(int position) {
		if (mScrapViews.size() > position) {
			return mScrapViews.get(position);
		}
		return null;
	}

	private DataSetObserver mObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			populate();
		}
		
		@Override
		/** Currently unsupported **/
		public void onInvalidated() {
			throw new UnsupportedOperationException();
		}

	};

}
