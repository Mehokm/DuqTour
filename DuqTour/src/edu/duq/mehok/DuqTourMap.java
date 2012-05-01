package edu.duq.mehok;

import java.io.InputStream;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;


public class DuqTourMap extends MapActivity {
	
	String PROXY_INTENT = "edu.duq.mehok.duqtouractivity.PROXIMITY_ALERT";

	public MapView mapView;
	public MyLocationOverlay meOverlay;
	public MapController mc;
	public Criteria crt;
	String bestProvider;

	ProgressDialog progDiag;
	
	int requestCode = 0;
	PointLocation[] locations;
	
	InputStream tis;
	InputStream dis;
	InputStream latis;
	InputStream lonis;
	InputStream ris;
	
	String t;
	String d;
	String lat;
	String lon;
	String r;
	String[] titles;
	String[] descriptions;
	String[] latitudes;
	String[] longitudes;
	String[] radii;
	
	LocationManager locationManager;
	Location location;
	
	public void initResources() {
		try {
    		tis = getResources().openRawResource(R.raw.titles);
    		dis = getResources().openRawResource(R.raw.descriptions);
    		latis = getResources().openRawResource(R.raw.latitudes);
    		lonis = getResources().openRawResource(R.raw.longitudes);
    		ris = getResources().openRawResource(R.raw.radii);
    		t = IOUtils.getFileContents(tis);
    		d = IOUtils.getFileContents(dis);
    		lat = IOUtils.getFileContents(latis);
    		lon = IOUtils.getFileContents(lonis);
    		r = IOUtils.getFileContents(ris);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	titles = t.split("\n\n");
    	descriptions = d.split("\n\n");
    	latitudes = lat.split("\n\n");
    	longitudes = lon.split("\n\n");
    	radii = r.split("\n\n");
	}
	
	public void initLocations() {
		locations = new PointLocation[titles.length];
		for(int i = 0; i < locations.length; i++) {
			locations[i] = new PointLocation(Double.parseDouble(latitudes[i]), Double.parseDouble(longitudes[i]), 
					Integer.parseInt(radii[i]), titles[i], descriptions[i]);
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
				pl.getRadius(),
				-1,
				proximityIntent
		);
		requestCode++;
	}
    
    private class MyLocationListener implements LocationListener {

        @Override
		public void onLocationChanged(Location location) {
        	if(location != null && location.getAccuracy() < 30.0) {
        		progDiag.dismiss();
        		if(meOverlay.getMyLocation() != null) {
        			mc.animateTo(meOverlay.getMyLocation());
        		}
        	}
        	else if(location.getAccuracy() > 50.0) {
        		progDiag = ProgressDialog.show(DuqTourMap.this, "Finding GPS", "Sorry, GPS signal is bad.");
        	}
        	if(meOverlay.getMyLocation() != null) {
        		mc.animateTo(meOverlay.getMyLocation());
        	}
        }

        @Override
		public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(DuqTourActivity.this, "Provider status changed",
//                    Toast.LENGTH_LONG).show();
        }

        @Override
		public void onProviderDisabled(String s) {
            Toast.makeText(DuqTourMap.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        @Override
		public void onProviderEnabled(String s) {
            Toast.makeText(DuqTourMap.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duq_tour_map);
		
		crt = new Criteria();
        crt.setAccuracy(Criteria.ACCURACY_FINE);
        
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        bestProvider = locationManager.getBestProvider(crt, true);
        
        progDiag = ProgressDialog.show(DuqTourMap.this, "Finding GPS", "Searching for GPS...");
        //progDiag.setCancelable(true);
        
        IntentFilter filter = new IntentFilter(PROXY_INTENT); 
	    registerReceiver(new ProximityIntentReceiver(), filter);

	    initResources();
	    initLocations();
	    initProxies();
		
		mapView = (MapView)findViewById(R.id.map_view);
		
		mc = mapView.getController();
       	
		meOverlay = new MyLocationOverlay(this, mapView);
		
		meOverlay.runOnFirstFix(
				new Runnable() {
					public void run() {
						mc.animateTo(meOverlay.getMyLocation());
						mc.setZoom(20);
					}
				});
		
		mapView.getOverlays().add(meOverlay);
		
		mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setSatellite(true);
        
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder adb = new AlertDialog.Builder(DuqTourMap.this);
		adb.setTitle("Exit DuqTour");
		adb.setMessage("Are you sure you want to exit?");
		adb.setNegativeButton("No", null);
		adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				System.exit(0);
			}	
		});
		adb.show();   	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		locationManager.removeUpdates(new MyLocationListener());
		meOverlay.disableCompass();
		meOverlay.disableMyLocation();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		locationManager.requestLocationUpdates(
        		bestProvider,
        		1000,
        		1,
        		new MyLocationListener());
		meOverlay.enableCompass();
		meOverlay.enableMyLocation();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
