package edu.duq.mehok;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProximityIntentReceiver extends BroadcastReceiver {

	Dialog dialog;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		String title;
		String description;

		Boolean entering = intent.getBooleanExtra(key, false);
		
		String action = intent.getAction();
			
		title = intent.getBundleExtra(action).getString("title");
		description = intent.getBundleExtra(action).getString("description");
		
		
		
		if (entering) {
			Log.d(getClass().getSimpleName(), "entering");
			
			Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			
			vibrator.vibrate(300);
			
			dialog = new Dialog(context);
	        dialog.setContentView(R.layout.location_dialog);
	        dialog.setTitle(title);
	        dialog.setCancelable(true);
	        //there are a lot of settings, for dialog, check them all out!

	        //set up text
	        TextView text = (TextView) dialog.findViewById(R.id.TextView01);
	        text.setText(description);

	        //set up image view
	        ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
	        img.setImageResource(R.drawable.d_logo);

	        //set up button
	        Button button = (Button) dialog.findViewById(R.id.Button01);
	        button.setOnClickListener(new Button.OnClickListener() {
	        @Override
	            public void onClick(View v) {
	        		dialog.dismiss();
	            }
	        });
	        //now that the dialog is set up, it's time to show it    
	        dialog.show();
		}
		else {
			Log.d(getClass().getSimpleName(), "exiting");
			
			if(dialog.isShowing()) {
				dialog.dismiss();
			}
			
			Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			
			vibrator.vibrate(100);
			
			Toast.makeText(context, "You are leaving " + title, Toast.LENGTH_LONG).show();
		}			  
		
	}
}




