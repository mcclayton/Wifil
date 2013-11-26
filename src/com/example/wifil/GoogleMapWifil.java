package com.example.wifil;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class GoogleMapWifil extends Activity implements OnMapLongClickListener {
	GoogleMap map;
	LocationManager locationManager;
	Location myLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map_wifil);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMapLongClickListener(this);
        
        //Open the map on my current location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        myLocation = locationManager.getLastKnownLocation(provider);
        //Get latitude and longitude
        LatLng latlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        //move camera to my location
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        
        //Enable compass, rotate gestures and tilt gestures
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(true);
     
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String ssid = extras.getString("SSID");
            String bssid = extras.getString("BSSID");
            
    		// Place a marker at the current location with the selected Wi-Fi information in the info window
            map.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())).draggable(false).title(ssid).snippet("["+bssid+"]"));
            Toast.makeText(getBaseContext(), "Wi-Fi hotspot: "+ssid+" has been pinned.", Toast.LENGTH_LONG).show();
        }
    }
    
	@Override
	public void onMapLongClick(final LatLng point) {
		map.addMarker(new MarkerOptions().position(point).draggable(true));		
	}
}