android-LocaitonDemo
====================
This application shows how to program you application to show the current location of the mobile. This generally uses the longitude and the latitude of accesses from the GPS or from the Network. You need to add few permissions in the manifest file and write a class to use the functions of LocationListener class.

There are few steps to do this :
Step 1 : Give the permission in the android manifest file.
Step 2 : Create the main class to handle the operation for button , when clicked.
Step 3 : Create a class extending from the Service class and implement LocationListener.

____________________________________________________________________________________________________________________________________________
Step 1 : Give the permission in the android manifest file.

    <uses-permission android:name="android.permission.INTERNET"  />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION"/>
____________________________________________________________________________________________________________________________________________
Step 2 : Create the main class to handle the operation for button , when clicked.

	GpsTracker gpsTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
...

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

____________________________________________________________________________________________________________________________________________
Step 3 : Create a class extending from the Service class and implement LocationListener.

public class GpsTracker extends Service implements LocationListener {

...
	private static final long MIN_DISTANCE_CHANGE_UPDATE = 10;
	private static final long MIN_TIME_BW_UPDATE = 1000 * 60 * 1;

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
  ....
	
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
____________________________________________________________________________________________________________________________________________
