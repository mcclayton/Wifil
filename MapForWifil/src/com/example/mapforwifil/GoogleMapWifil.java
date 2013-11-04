package com.example.mapforwifil;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.Menu;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
public class GoogleMapWifil extends FragmentActivity implements OnMapLongClickListener {
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
        LatLng latlng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
       /////////////just for now
       if(latlng == null){
        	latlng = new LatLng(-33, 44);
        }///////////////////////need to be removed
        //move camera to my location
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        
        //Enable compass, rotate gestures and tilt gestures
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(true);
        

    }

	@Override
	public void onMapLongClick(final LatLng point) {
//
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Wifi Info");
		alert.setMessage("Enter the name of Wifi hotspot");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		 Editable value = input.getText();
		  // Do something with value!
		  map.addMarker(new MarkerOptions().position(point).title(value.toString()));
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
		//
		
		
	}
}