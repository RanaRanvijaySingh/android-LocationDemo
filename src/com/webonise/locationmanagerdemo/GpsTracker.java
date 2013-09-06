package com.webonise.locationmanagerdemo;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GpsTracker extends Service implements LocationListener {

	private final Context mContext;
	boolean isGpsEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;

	Location location;
	double latitude;
	double longitude;
	double altitude;
	long time;
	String provider;
	

	private static final long MIN_DISTANCE_CHANGE_UPDATE = 10;
	private static final long MIN_TIME_BW_UPDATE = 1000 * 60 * 1;

	private static final String TAG = "GpsTracker";

	LocationManager locationManager;

	public GpsTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	private Location getLocation() {
		locationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);
		isGpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = locationManager
				.isProviderEnabled(locationManager.NETWORK_PROVIDER);

		if (!isGpsEnabled && !isNetworkEnabled) {
			Log.e(TAG, "No Network Provider is available.");
		} else {
			this.canGetLocation = true;
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATE,
						MIN_DISTANCE_CHANGE_UPDATE, this);
				Log.i(TAG, "Network");
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
						altitude=location.getAltitude();
						time=location.getTime();
						provider=location.getProvider();
					}
				}
			}
			if (isGpsEnabled) {
				if (location == null) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATE,
							MIN_DISTANCE_CHANGE_UPDATE, this);
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							latitude = location.getLongitude();
							altitude=location.getAltitude();
							time=location.getTime();
							provider=location.getProvider();
						}
					}
				}
			}
		}
		return location;

	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v(TAG,""+location.getLatitude()+" "+location.getLongitude());

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void stopUsing() {

		if (locationManager != null) {
			locationManager.removeUpdates(GpsTracker.this);
		}
	}

	public double getAltitude() {
		return altitude;
	}


	public long getTime() {
		return time;
	}


	public String getProvider() {
		return provider;
	}

	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
			Log.v(TAG,""+location.getLatitude());
		}
		return latitude;
	}

	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}
		return longitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setTitle("GPS is Setting");
		alertDialog.setMessage("GPS is not enabled. Do you want to enable?");
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}
}
