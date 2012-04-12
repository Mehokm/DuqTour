package edu.duq.mehok;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class DuqTourMap extends MapActivity {

	public MapView mapView;
	public MyLocationOverlay meOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duq_tour_map);
		
		mapView = (MapView)findViewById(R.id.map_view);
		
		final MapController mc = mapView.getController();
       	
		meOverlay = new MyLocationOverlay(this, mapView);
		meOverlay.enableCompass();
		meOverlay.enableMyLocation();
		
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
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
