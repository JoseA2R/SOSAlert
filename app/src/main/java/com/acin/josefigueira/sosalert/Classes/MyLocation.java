package com.acin.josefigueira.sosalert.Classes;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.acin.josefigueira.sosalert.Fragments.SOSFragment;
import com.acin.josefigueira.sosalert.Model.LocationHandler;
import com.acin.josefigueira.sosalert.View.MainActivity;
import com.acin.josefigueira.sosalert.View.MainMenuActivity;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by jose.figueira on 13-04-2018.
 */

public class MyLocation {

    private Timer timer1;
    private LocationManager locationManager;
    private LocationResult locationResult;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private Criteria criteria;
    private Location gpsLocation, networkLocation;
    MainMenuActivity mainMenu = new MainMenuActivity();

    public MyLocation(LocationHandler locationHandler) {
        this.locationHandler = locationHandler;

    }
    LocationHandler locationHandler;

    public boolean getLocation(Context context, LocationResult result) {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false;

        try {
            if (gps_enabled)
                criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                criteria.setAltitudeRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);
                criteria.setBearingRequired(false);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
                LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                try {
                    locationManager.requestLocationUpdates(0, 0, criteria, locationListener, null);
                }catch(NullPointerException e){
                    e.printStackTrace();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 900, 0, locationListener);
                }

            if (network_enabled)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 900, 0, locationListener);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }
        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 10000);
        return true;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (gps_enabled) {
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationHandler.addLocation(gpsLocation);
                Log.d("LOCATION", " got coordinate");
            }
            if (network_enabled) {
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                locationHandler.addLocation(networkLocation);
            }
            //if there are both values use the latest one
            if (gpsLocation != null && networkLocation != null) {
                if (gpsLocation.getTime() > networkLocation.getTime()) {
                    locationResult.gotLocation(gpsLocation);
                    locationHandler.addLocation(gpsLocation);
                    Log.d("LOCATION", " got coordinate");
                }else {
                    locationResult.gotLocation(networkLocation);
                    locationHandler.addLocation(networkLocation);
                }
                return;
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void setLocationResult(LocationResult res)
    {
        locationResult = res;
    }
    public LocationListener getListener()
    {
        return locationListener;
    }

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {

            //locationManager.removeUpdates(locationListener);
            //locationManager.removeUpdates(locationListener);

            Location networkLocation = null, gpsLocation = null;
            if (gps_enabled) {
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationHandler.addLocation(gpsLocation);
            }
            if (network_enabled)
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                locationHandler.addLocation(networkLocation);
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

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);

    }
}
