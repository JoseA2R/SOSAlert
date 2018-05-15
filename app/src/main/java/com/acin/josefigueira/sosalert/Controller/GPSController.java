package com.acin.josefigueira.sosalert.Controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.acin.josefigueira.sosalert.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class GPSController{

    private static final int REQUEST_LOCATION = 1;

    private double longitude = 0.0;
    private double latitude = 0.0;
    private Location location;
    private LocationManager locationManager;
    private LocationListener listener;
    private final Context context;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // flag for GPS status
    private boolean canGetLocation = false;

    public GPSController(Context context) {
        this.context = context;
        //getLocation();
    }


    public Location getLocation() {


        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    } catch (SecurityException e) {

                    }
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        try {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        /*longitude = location.getLongitude();
                        latitude = location.getLatitude();*/
                        }catch(SecurityException e){

                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            //if (locationManager != null) {location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                            //}
                        } catch (SecurityException s) {

                        }
                    }
                }
            }

        }catch(SecurityException e){

        }

        return location;

          /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();*/

    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates((LocationListener) this);
            } catch (SecurityException e) {

            }

        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle(R.string.gps_settings);
        // Setting Dialog Message
        alertDialog.setMessage(R.string.gps_disabled);
        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.settings_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}
