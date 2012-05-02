package com.airlocksoftware.holo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.airlocksoftware.holo.R;

public class ImageDialog extends Dialog {

	protected Context mContext;

	// protected FrameLayout mContent;
	private ImageView mImageView;

	private int mBackgroundColorResId;

	public static final int DEFAULT_THEME = R.style.Dialog_Image;
	public static final int DEFAULT_LAYOUT = R.layout.image_dialog;

	public ImageDialog(Context context) {
		this(context, DEFAULT_LAYOUT);
	}

	/**
	 * Allows you to specify a custom layout. Must contain ImageView with id=image
	 **/
	public ImageDialog(Context context, int layoutResourceId) {
		this(context, layoutResourceId, DEFAULT_THEME);

	}

	public ImageDialog(Context context, int layoutResourceId, int theme) {
		super(context, theme);
		this.mContext = context;
		this.setContentView(layoutResourceId);
		
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		mImageView = (ImageView) findViewById(R.id.image);
		
		if (mBackgroundColorResId != 0) setBackgroundColor(mBackgroundColorResId);
	}

	public void setImageSource(int imageResourceId) {
		if (mImageView != null) mImageView.setImageResource(imageResourceId);
	}

	public void setImageSource(Bitmap bitmap) {
		if (mImageView != null) mImageView.setImageBitmap(bitmap);
	}

	public void setOnImageClickListener(View.OnClickListener listener) {
		if (mImageView != null) mImageView.setOnClickListener(listener);
	}

	public void setScaleType(ScaleType scaleType) {
		mImageView.setScaleType(scaleType);
	}

	public void setBackgroundColor(int colorResId) {
		mBackgroundColorResId = colorResId;
		if (mImageView != null) {
			mImageView.setBackgroundColor(mContext.getResources().getColor(mBackgroundColorResId));
		}
	}

	@Override
	public void show() {
		super.show();
	}

}
