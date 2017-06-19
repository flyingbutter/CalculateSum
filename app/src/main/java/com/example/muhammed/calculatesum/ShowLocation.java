package com.example.muhammed.calculatesum;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static android.location.LocationManager.GPS_PROVIDER;


public class ShowLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //  private final String TAG = "ravimaps";
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    TextView lats;
    TextView longs;
    private Location mLastLocation;
    Handler h;
    Runnable myRunnable;

    LocationListener mlocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        lats = (TextView) findViewById(R.id.textView8);
        longs = (TextView) findViewById(R.id.textView9);
    }

    @Override
    public void onConnected(Bundle bundle) {
       gps();

    }




    public void gps(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            float LOCATION_REFRESH_DISTANCE = 0;
            long LOCATION_REFRESH_TIME = 0;
            locationManager.requestLocationUpdates(GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mlocationListener);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation == null) {
                return;
            }

            h = new Handler();
            final int delay = 10000; //milliseconds
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            lats.setText("Latitude:" + Double.toString(mLastLocation.getLatitude()));
            longs.setText("Longitude:" + Double.toString(mLastLocation.getLongitude()));
            h.postDelayed(myRunnable= new Runnable() {

                public void run() {
                    if (mLastLocation != null) {
                        // Toast.makeText(getApplicationContext(), "YES! mLastLocation!=null", Toast.LENGTH_SHORT).show();
                        //  Log.i(TAG, mLastLocation.toString());
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        lats.setText("Latitude:" + Double.toString(mLastLocation.getLatitude()));
                        longs.setText("Longitude:" + Double.toString(mLastLocation.getLongitude()));

                        if (ActivityCompat.checkSelfPermission(getWindow().findViewById(android.R.id.content).getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getWindow().findViewById(android.R.id.content).getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            //ActivityCompat.requestPermissions(,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                            return;
                        }


                        h.postDelayed(this, delay);

                        // double latitude = mLastLocation.getLatitude();  //Save latitude in a double variable
                        // double longitude = mLastLocation.getLongitude(); //Save longitude in a double variable
                        //Toast to display Coordinates
                        //  Toast.makeText(getApplicationContext(), "Latitude = " + latitude + "\nLongitude = " + longitude, Toast.LENGTH_SHORT).show();

                    }
                }
            }, delay);
        }

    }

    @Override
    public void onBackPressed() {
        h.removeCallbacksAndMessages(null);

        super.onBackPressed();
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onStop() {
      //  mGoogleApiClient.disconnect();
try {
    h.removeCallbacks(myRunnable);

}catch (Exception e)
{}
        super.onStop();

    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
