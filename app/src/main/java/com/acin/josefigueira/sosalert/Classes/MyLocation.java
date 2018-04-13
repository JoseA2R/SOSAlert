package com.acin.josefigueira.sosalert.Classes;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jose.figueira on 13-04-2018.
 */

public class MyLocation {

    Timer timer1;
    LocationManager locationManager;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    public boolean getLocation(Context context, LocationResult result) {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (SecurityException ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException ex) {
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false;

        if (gps_enabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        else if (network_enabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 10000);
        return true;
    }

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            //locationManager.removeUpdates(locationListener);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            locationManager.removeUpdates(locationListener);

            Location networkLocation = null, gpsLocation = null;
            if (gps_enabled)
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //if there are both values use the latest one
            if (gpsLocation != null && networkLocation != null) {
                if (gpsLocation.getTime() > networkLocation.getTime())
                    locationResult.gotLocation(gpsLocation);
                else
                    locationResult.gotLocation(networkLocation);
                return;
            }

            if (gpsLocation != null) {
                locationResult.gotLocation(gpsLocation);
                return;
            }
            else if (networkLocation != null) {
                locationResult.gotLocation(networkLocation);
                return;
            }
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);

    }
}
