package com.example.wifil;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapWifil extends Activity {
	/**
	 * Note that this may be null if the Google Play services APK is not available.
	 */
	private GoogleMap mMap;
	private boolean myLocationFound = false;
	private Bundle extras = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map_wifil);
		extras = getIntent().getExtras();
		if (extras != null) {
			ProgressBar pb = (ProgressBar) findViewById(R.id.pinningProgressBar);
			pb.setVisibility(ProgressBar.VISIBLE);
		}
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
	 * installed) and the map has not already been instantiated.. This will ensure that we only ever
	 * call setUpMap() once when mMap is not null.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the camera.
	 * This should only be called once and when we are sure that mMap is not null.
	 */
	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setAllGesturesEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);

		mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
			//[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]
			//TODO Ensure that this is only done once. Also, if extras != null, then add a loading bar and if onMyLocationChange is not called within some epsilon time, display an error!!!!!!!!!!!!!!!!!
			//******************************************************************************************************************************************************************************
			@Override
			public void onMyLocationChange(Location loc) {
				if (!myLocationFound) {
					ProgressBar pb = (ProgressBar) findViewById(R.id.pinningProgressBar);
					pb.setVisibility(ProgressBar.INVISIBLE);
					myLocationFound = true;
					LatLng myCoords = new LatLng(loc.getLatitude(), loc.getLongitude());
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCoords, 13));

					if (extras != null) {
						String ssid = extras.getString("SSID");
						String bssid = extras.getString("BSSID");       
						// Place a marker at the current location with the selected Wi-Fi information in the info window
						mMap.addMarker(new MarkerOptions().position(myCoords).draggable(true).title(ssid).snippet("["+bssid+"]"));
						Toast.makeText(getBaseContext(), "Wi-Fi hotspot: "+ssid+" has been pinned.", Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		// Uncomment if I decide I want to delete pinned hotspots onInfoWindowClick
		/*
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {
				marker.remove();
			}
		});
		*/
		 
	}

}