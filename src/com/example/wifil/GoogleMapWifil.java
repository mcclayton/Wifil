package com.example.wifil;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
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

		mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
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

		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				// TODO Request nearby hotspots and pin them to the map.
				final LatLng cameraCoords = position.target;
				//float zoomLevel = position.zoom;
				
				// Get the hotspots on a different thread so the UI doesn't slow down
				new Thread(new Runnable() {
			        public void run() {					
			        	
						final Hotspot[] hotspots = ServerCommunication.getHotspots("1234", "5678", cameraCoords.latitude+"", cameraCoords.longitude+"", "5");
						if (hotspots != null) {
							Handler handler = new Handler(Looper.getMainLooper());
							handler.post(new Runnable(){
								@Override
								public void run() {
									for (Hotspot hs : hotspots) {
										LatLng latlng = new LatLng(hs.getLat(), hs.getLon());
										mMap.addCircle(new CircleOptions().center(latlng).radius(hs.getRadius()).strokeColor(Color.BLUE).strokeWidth(2).fillColor(Color.TRANSPARENT));
										// Add a green marker if hotspot is public, or red otherwise
										if (hs.getIsPublic() == 1)
											mMap.addMarker(new MarkerOptions().position(latlng).draggable(false).title(hs.getSSID()).snippet("["+hs.getMac()+"]")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
										else {
											mMap.addMarker(new MarkerOptions().position(latlng).draggable(false).title(hs.getSSID()).snippet("["+hs.getMac()+"]"));
										}
									}
								} 
							});
							
						}
			        }
			    }).start();
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