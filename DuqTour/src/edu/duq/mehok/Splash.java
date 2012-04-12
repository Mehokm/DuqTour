package edu.duq.mehok;

import android.app.*;
import android.content.Intent;
import android.os.*;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Thread timer = new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
					Intent i = new Intent(Splash.this, DuqTourActivity.class);
					startActivity(i);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
}
