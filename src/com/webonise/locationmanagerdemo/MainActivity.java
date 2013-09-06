package com.webonise.locationmanagerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.locationmanagerdemo.R;

public class MainActivity extends Activity {

	private static final String TAG="MainActivity";
	Button btnShowLocation;

	GpsTracker gpsTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i(TAG,"reached");
		btnShowLocation = (Button) findViewById(R.id.buttonMain);

		btnShowLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				gpsTracker = new GpsTracker(MainActivity.this);

				if (gpsTracker.canGetLocation()) {

					double latitude = gpsTracker.getLatitude();
					double longitude = gpsTracker.getLongitude();
					double altitude=gpsTracker.getAltitude();
					long time=gpsTracker.getTime();
					String provider=gpsTracker.getProvider();

					Toast.makeText(
							getApplicationContext(),
							"Your Location is - \n" +
							"Lat: " +	latitude +
						    "\nLong: " + longitude+
						    "\nAltitude : " +altitude +
						    "\nService Provider : "+ provider +
						    "\nTime : "+time, Toast.LENGTH_LONG)
							.show();
				} else {

					gpsTracker.showSettingsAlert();
				}

			}
		});
	}

}
