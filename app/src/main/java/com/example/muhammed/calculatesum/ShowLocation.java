package com.example.muhammed.calculatesum;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class ShowLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

  //  private final String TAG = "ravimaps";
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    TextView lats;
    TextView longs;
    private Location mLastLocation;

    LocationListener mlocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation=location;

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



         lats = (TextView) findViewById(R.id.textView8);
         longs = (TextView) findViewById(R.id.textView9);
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            float LOCATION_REFRESH_DISTANCE = 0;
            long LOCATION_REFRESH_TIME = 5;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE,  mlocationListener);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation!=null){
               // Toast.makeText(getApplicationContext(), "YES! mLastLocation!=null", Toast.LENGTH_SHORT).show();
              //  Log.i(TAG, mLastLocation.toString());

                lats.setText("Latitude:"+Double.toString(mLastLocation.getLatitude()));
                longs.setText("Longitude"+Double.toString(mLastLocation.getLongitude()));

                final Handler h = new Handler();
                final int delay = 10000; //milliseconds

                h.postDelayed(new Runnable(){
                    public void run(){

                        lats.setText("Latitude:"+Double.toString(mLastLocation.getLatitude()));
                        longs.setText("Longitude"+Double.toString(mLastLocation.getLongitude()));
                        h.postDelayed(this, delay);
                    }
                }, delay);
                double latitude = mLastLocation.getLatitude();  //Save latitude in a double variable
                double longitude = mLastLocation.getLongitude(); //Save longitude in a double variable
                //Toast to display Coordinates
              //  Toast.makeText(getApplicationContext(), "Latitude = " + latitude + "\nLongitude = " + longitude, Toast.LENGTH_SHORT).show();

            }
        }

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
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
