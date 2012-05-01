package edu.duq.mehok;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DuqTourActivity extends Activity {
    /** Called when the activity is first created. */
	
	TextView welcomeText;
	Button startButton;
	Button aboutButton;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        
        
        welcomeText = (TextView) findViewById(R.id.welcome_text);
        welcomeText.setText("Welcome to the Duquesne University Virtual Tour!");
        
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setText("Start Tour!");
        startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DuqTourActivity.this, DuqTourMap.class);
        		startActivity(i);
        		finish();
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