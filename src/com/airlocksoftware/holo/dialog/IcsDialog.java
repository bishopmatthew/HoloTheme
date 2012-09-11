package com.airlocksoftware.holo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airlocksoftware.holo.R;
import com.airlocksoftware.holo.type.FontButton;
import com.airlocksoftware.holo.type.FontText;

public class IcsDialog extends Dialog {

	protected Context mContext;

	protected FrameLayout content;
	private RelativeLayout titleContainer;
	private TextView title;
	private LinearLayout buttonContainer;
	
	private int btnLayoutResId;

	public static final int DEFAULT_THEME = R.style.Dialog;
	public static final int DEFAULT_LAYOUT = R.layout.ics_dialog_holo_dark;
	public static final int DEFAULT_BUTTON = R.layout.dialog_button_holo_dark;
	

	public IcsDialog(Context context) {
		this(context, DEFAULT_LAYOUT);
	}

	/**
	 * Allows you to specify a custom layout The custom layout must include ids
	 * title_text, list, and button_container
	 **/
	public IcsDialog(Context context, int layoutResourceId) {
		this(context, layoutResourceId, DEFAULT_THEME);

	}

	/** **/
	public IcsDialog(Context context, int layoutResourceId, int theme) {
		super(context, theme);
		this.mContext = context;
		this.setContentView(layoutResourceId);
		this.setCancelable(true);

		titleContainer = (RelativeLayout) findViewById(R.id.title_container);
		content = (FrameLayout) findViewById(R.id.dialog_content);
		title = (TextView) findViewById(R.id.title_text);
		buttonContainer = (LinearLayout) findViewById(R.id.button_container);
		
		btnLayoutResId = DEFAULT_BUTTON;
	}

	/** Displays the title above the list content using the default theme **/
	public void setTitle(String titleText) {
		titleContainer.setVisibility(View.VISIBLE);
		title.setText(titleText);
	}

	/** Removes any arbitrary views and hides the default title text **/
	public void clearTitle() {
		titleContainer.setVisibility(View.GONE);
	}

	/** Adds a text button to the bottom of the screen (ordered left to right) **/
	public void addButton(String text, View.OnClickListener listener) {
		buttonContainer.setVisibility(View.VISIBLE);
		FontButton button = (FontButton) LayoutInflater.from(mContext).inflate(btnLayoutResId, buttonContainer,
				false);
		button.setText(text);
		button.setOnClickListener(listener);
		buttonContainer.addView(button);

		View divider = new View(mContext, null);
		LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(1,
				LinearLayout.LayoutParams.FILL_PARENT);
		divider.setLayoutParams(dividerParams);
		divider.setBackgroundResource(R.color.grey_40);
		buttonContainer.addView(divider);
	}
	
	public void displayText(String toDisplay) {
		FontText display = (FontText) LayoutInflater.from(mContext).inflate(R.layout.dialog_text, content, false);
		display.setText(toDisplay);
		setContentView(display);
	}
	
	public void clearButtons() {
		buttonContainer.removeAllViews();
		buttonContainer.setVisibility(View.GONE);
	}
	
	public void setButtonLayoutResId(int layoutResId) {
		btnLayoutResId = layoutResId;
	}
	
	public void setContentView(View contentView) {
		content.addView(contentView);
	}
	
	public void clearContentView() {
		content.removeAllViews();
	}

	@Override
	public void show() {
		super.show();
	}

}
