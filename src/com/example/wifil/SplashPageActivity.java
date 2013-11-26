package com.example.wifil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class SplashPageActivity extends Activity {			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);		
		
		final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

		Runnable startActivity = new Runnable() {
		    public void run() {
            	Intent mapIntent = new Intent(getBaseContext(), MainWifilActivity.class);
            	startActivity(mapIntent);
		    }
		};
		// Show the splash screen for 4 seconds before switching the activity
		worker.schedule(startActivity, 4, TimeUnit.SECONDS);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_wifi_l, menu);
		return true;
	}
	
}

