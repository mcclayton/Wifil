package com.example.wifil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainWifilActivity extends Activity {
	
    WifiManager mainWifi;
    WifiReceiver receiverWifi;

    StringBuilder sb = new StringBuilder();

    private final Handler handler = new Handler();
    
    TextView statusText;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_wifil);
		
		statusText = (TextView) findViewById(R.id.textStatus);
		
	    mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

	    receiverWifi = new WifiReceiver();
	    registerReceiver(receiverWifi, new IntentFilter(
	    WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
		
        final Button button = (Button) findViewById(R.id.hotspotButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Scan for hotspots if the WiFi is enabled
    	        if(mainWifi.isWifiEnabled()==false)
    	        {
    	            //mainWifi.setWifiEnabled(true);
    	        	statusText.setText("ERROR: WiFi is disabled!");
    	        } else {
    	        	statusText.setText("HOTSPOTS:\n\nLoading...");
    	        	startScan();
    	        }
            }
        });

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
            ArrayList<String> connections=new ArrayList<String>();

            sb = new StringBuilder();
            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            
            sb.append("HOTSPOTS:\n\n");
            for(int i = 0; i < wifiList.size(); i++)
            {
                connections.add(wifiList.get(i).SSID);
                sb.append((i+1)+". \t"+wifiList.get(i).SSID+" [SIGNAL: "+wifiList.get(i).level+"]\n");
            }
            statusText.setText(sb.toString());

        }
    }

}
