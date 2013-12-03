package com.example.wifil;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainWifilActivity extends Activity {
	
    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;

    private final Handler handler = new Handler();
    
	private final ArrayList<ScanResult> hotspotList = new ArrayList<ScanResult>();
	CustomArrayAdapter adapter = null;
	ProgressBar progBar = null;
	
	ScanResult selectedHotspot = null;
	
    private AlertDialog.Builder builder = null;
    private DialogInterface.OnClickListener dialogClickListener = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_wifil);		
		
	    builder = new AlertDialog.Builder(this);
		
		final ListView listview = (ListView) findViewById(R.id.hotspotListView);
	    progBar = (ProgressBar) findViewById(R.id.progressBar2);
		progBar.setVisibility(ProgressBar.INVISIBLE);
		
		adapter = new CustomArrayAdapter(getBaseContext(), hotspotList);
		listview.setAdapter(adapter);
		
	    mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

	    //receiverWifi = new WifiReceiver();
	    //registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
        final Button button = (Button) findViewById(R.id.hotspotButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Scan for hotspots if the WiFi is enabled
    	        if(mainWifi.isWifiEnabled()==false)
    	        {
    	            //mainWifi.setWifiEnabled(true);
    	        	Toast.makeText(getBaseContext(), "ERROR: Wi-Fi is disabled! Please enable.", Toast.LENGTH_LONG).show();
    	        } else {
    	        	startScan();
    	        	progBar.setVisibility(ProgressBar.VISIBLE);
    	        }
            }
        });
        
        final Button viewMapButton = (Button) findViewById(R.id.viewMapButton);
        viewMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switch the activity to the map activity
            	Intent mapIntent = new Intent(getBaseContext(), GoogleMapWifil.class);
            	startActivity(mapIntent);
            }
        });
        
		dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
	            	Intent mapIntent = new Intent(getBaseContext(), GoogleMapWifil.class);
	            	mapIntent.putExtra("SSID", selectedHotspot.SSID);
	            	mapIntent.putExtra("BSSID", selectedHotspot.BSSID);
	            	mapIntent.putExtra("ISPUBLIC", hotspotIsPublic(selectedHotspot));
	            	startActivity(mapIntent);
		            break;
		        }
		    }
		};
        
		listview.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				  selectedHotspot = ((ScanResult)parent.getItemAtPosition(position));
				  builder.setMessage("Are you sure you want to place a pin for:\n"+((ScanResult)parent.getItemAtPosition(position)).SSID).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
			  }
		}); 
        
        // Scan for hotspots if the WiFi is enabled
        if(mainWifi.isWifiEnabled()==false)
        {
            //mainWifi.setWifiEnabled(true);
        	Toast.makeText(getBaseContext(), "ERROR: WiFi is disabled!", Toast.LENGTH_LONG).show();
        } else {
        	startScan();
        	progBar.setVisibility(ProgressBar.VISIBLE);
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_wifi_l, menu);
		return true;
	}
	
	
    public void startScan()
    {
        handler.post(new Runnable() {

            @Override
            public void run()
            {
                // Implement the run method of the Runnable interface
                mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                receiverWifi = new WifiReceiver();
                registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
            }
        });

    }

    @Override
    protected void onPause()
    {
    	// Unregister the WiFi receiver when the user leaves (pauses) the application. 
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
    	// Re-register the WiFi receiver after the user resumes the application.
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {
        	HashMap<String, ScanResult> scansHashMap = new HashMap<String, ScanResult>();

        	for(ScanResult sr: mainWifi.getScanResults())
            {
        		// Don't store hotspots with duplicate SSIDS. Only store the one with a higher signal strength.
            	if(!scansHashMap.containsKey(sr.SSID)) {
            		scansHashMap.put(sr.SSID, sr);
            	} else if (sr.level < scansHashMap.get(sr.SSID).level) {
            		scansHashMap.remove(sr.SSID);
            		scansHashMap.put(sr.SSID, sr);
            	}
            }
            
            hotspotList.clear();                        
            for(ScanResult sr : scansHashMap.values()) {
                hotspotList.add(sr);
            }
            
			adapter.notifyDataSetChanged();
        }
    }
  
    public class CustomArrayAdapter extends ArrayAdapter<ScanResult> {
    	  private final Context context;
    	  private final ArrayList<ScanResult> values;

    	  public CustomArrayAdapter(Context context, ArrayList<ScanResult> values) {
    	    super(context, R.layout.rowlayout, values);
    	    this.context = context;
    	    this.values = values;
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
    	    TextView textView = (TextView) rowView.findViewById(R.id.label);
    	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    	    textView.setText(values.get(position).SSID);
    	    
    	    // Change the Wi-Fi icon based on signal strength and security of the hotspot
    	    ScanResult sr = values.get(position);
    	    boolean isPublic = hotspotIsPublic(sr);
    	    int signal = sr.level;
    	    if (signal >= -76) {
    	    	if (isPublic) {
    	    		imageView.setImageResource(R.drawable.wifi_signal_4);
    	    	} else {
    	    		imageView.setImageResource(R.drawable.wifi_signal_locked_4);
    	    	}
    	    } else if (signal < -76 && signal >= -87 ) {
    	    	if (isPublic) {
    	    		imageView.setImageResource(R.drawable.wifi_signal_3);
    	    	} else {
    	    		imageView.setImageResource(R.drawable.wifi_signal_locked_3);
    	    	}
    	    } else if (signal < -87 && signal >= -95 ) {
    	    	if (isPublic) {
    	    		imageView.setImageResource(R.drawable.wifi_signal_2);
    	    	} else {
    	    		imageView.setImageResource(R.drawable.wifi_signal_locked_2);
    	    	}
      	    } else if (signal < -95) {
    	    	if (isPublic) {
    	    		imageView.setImageResource(R.drawable.wifi_signal_1);
    	    	} else {
    	    		imageView.setImageResource(R.drawable.wifi_signal_locked_1);
    	    	}
      	    }

    	    progBar.setVisibility(ProgressBar.INVISIBLE);
    	    return rowView;
    	  }
    	}
    
    private boolean hotspotIsPublic(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] securityModes = { "WEP", "PSK", "EAP" };
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                return false;
            }
        }  
        return true;
    }

}
