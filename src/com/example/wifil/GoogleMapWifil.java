package com.example.wifil;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
	private HashMap<String, Integer> markerHashMap = new HashMap<String, Integer>();

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] mMapTypes;


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
		mMapTypes = getResources().getStringArray(R.array.map_types);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mMapTypes));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.common_signin_btn_icon_normal_light,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				);

		mDrawerLayout.setDrawerListener(mDrawerToggle); 
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);




		if (savedInstanceState == null) {
			selectItem(0);
		}

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
					final LatLng myCoords = new LatLng(loc.getLatitude(), loc.getLongitude());
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCoords, 13));

					if (extras != null) {
						final String ssid = extras.getString("SSID");
						final String bssid = extras.getString("BSSID");  
						final boolean isPublicBool = extras.getBoolean("ISPUBLIC");  
						final int isPublic = isPublicBool?1:0;

						// Post the selected hotspot to the server
						new Thread(new Runnable() {
							public void run() {
								Hotspot hs = new Hotspot(ssid, bssid, myCoords.latitude, myCoords.longitude, 0, isPublic);
								ServerCommunication.submitData("1234", "5678", ServerCommunication.toJSON(hs));
							}
						}).start();

						Toast.makeText(getBaseContext(), "Wi-Fi hotspot: "+ssid+" has been pinned.", Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
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
										// Don't pin markers more than once
										if(!markerHashMap.containsKey(hs.getMac())) {
											markerHashMap.put(hs.getMac(), 1);
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

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		if(position == 0)
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		else if(position==1)
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		else if(position ==2)
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		else if(position == 3)
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
	}



}