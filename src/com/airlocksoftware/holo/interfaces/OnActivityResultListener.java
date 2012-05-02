package com.airlocksoftware.holo.interfaces;

import android.content.Intent;

public abstract interface OnActivityResultListener {
	
	public abstract void onActivityResult(int requestCode, int resultCode, Intent intent);

}
