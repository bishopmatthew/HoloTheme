package com.airlocksoftware.holo.activities;

import java.util.List;

import android.os.Bundle;

import com.airlocksoftware.holo.R;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapActivity extends ActionBarActivity {

	MapView mMapView;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		
		this.mMapView = new MapView(this, getString(R.string.airlock_software_maps_key));

		mMapView.setClickable(true);
		mMapView.setFocusable(true);
	}

	@Override
	public void onPause() {
		super.onPause();

		List<Overlay> overlays = mMapView.getOverlays();
		
		if(overlays == null || overlays.size() < 1) return;

		for (Overlay overlay : overlays) {
			if (overlay instanceof MyLocationOverlay) ((MyLocationOverlay) overlay).disableMyLocation();
		}
	}

	public MapView getMapView() {
		return mMapView;
	}
}
