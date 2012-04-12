package edu.duq.mehok;

import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DuqTourActivity extends Activity {
    /** Called when the activity is first created. */
	
	String PROXY_INTENT = "edu.duq.mehok.duqtouractivity.PROXIMITY_ALERT";
	int requestCode = 0;
	PointLocation[] locations;
	
	InputStream tis;
	InputStream dis;
	InputStream latis;
	InputStream lonis;
	
	String t;
	String d;
	String lat;
	String lon;
	String[] titles;
	String[] descriptions;
	String[] latitudes;
	String[] longitudes;
	
	TextView welcomeText;
	Button startButton;
	Button aboutButton;
	LocationManager locationManager;
	Location location;
	
	public void initResources() {
		try {
    		tis = getResources().openRawResource(R.raw.titles);
    		dis = getResources().openRawResource(R.raw.descriptions);
    		latis = getResources().openRawResource(R.raw.latitudes);
    		lonis = getResources().openRawResource(R.raw.longitudes);
    		t = IOUtils.getFileContents(tis);
    		d = IOUtils.getFileContents(dis);
    		lat = IOUtils.getFileContents(latis);
    		lon = IOUtils.getFileContents(lonis);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	titles = t.split("\n\n");
    	descriptions = d.split("\n\n");
    	latitudes = lat.split("\n\n");
    	longitudes = lon.split("\n\n");
	}
	
	public void initLocations() {
		locations = new PointLocation[titles.length];
		for(int i = 0; i < locations.length; i++) {
			locations[i] = new PointLocation(Double.parseDouble(latitudes[i]), Double.parseDouble(longitudes[i]), titles[i], descriptions[i]);
		}
	}
	
	public void initProxies() {
		for(int i = 0; i < locations.length; i++) {
			addAlert(locations[i]);
		}
	}
	
	public void addAlert(PointLocation pl) {
		//String action = PROXY_INTENT + (requestCode + 1);
		String action = PROXY_INTENT;
		
		Bundle extras = new Bundle();
		extras.putString("title", pl.getTitle());
		extras.putString("description", pl.getDescription());
		extras.putInt("id", requestCode + 1);
		
		Intent intent = new Intent(action);
		intent.putExtra(action, extras);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		locationManager.addProximityAlert(
				pl.getLatitude(),
				pl.getLongitude(),
				10,
				-1,
				proximityIntent
		);
		requestCode++;
	}
    
    private class MyLocationListener implements LocationListener {

        @Override
		public void onLocationChanged(Location location) {
        	
        }

        @Override
		public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(DuqTourActivity.this, "Provider status changed",
//                    Toast.LENGTH_LONG).show();
        }

        @Override
		public void onProviderDisabled(String s) {
            Toast.makeText(DuqTourActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        @Override
		public void onProviderEnabled(String s) {
            Toast.makeText(DuqTourActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        Criteria crt = new Criteria();
        crt.setAccuracy(Criteria.ACCURACY_FINE);
        
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = locationManager.getBestProvider(crt, true);
        locationManager.requestLocationUpdates(
        		bestProvider,
        		1000,
        		1,
        		new MyLocationListener());
        
        IntentFilter filter = new IntentFilter(PROXY_INTENT); 
	    registerReceiver(new ProximityIntentReceiver(), filter);
        
        initResources();
        initLocations();
        initProxies();
        
        welcomeText = (TextView) findViewById(R.id.welcome_text);
        welcomeText.setText("Welcome to the Duquesne University Virtual Tour!");
        
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setText("Start Tour!");
        startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DuqTourActivity.this, DuqTourMap.class);
        		startActivity(i);
			}
        	
        });
        
        aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setText("About");
        aboutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(DuqTourActivity.this);
				dialog.setContentView(R.layout.about_dialog);
				dialog.setTitle("About");
				dialog.setCancelable(true);
				
				Button close = (Button) dialog.findViewById(R.id.about_button);
				close.setText("Close");
				close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				dialog.show();
			}
        });  
    }
}