package com.acin.josefigueira.sosalert.Classes;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.acin.josefigueira.sosalert.Fragments.SOSFragment;

/**
 * Created by jose.figueira on 11-04-2018.
 */

public class ServiceLocationListener implements LocationListener {

    public Location currentBestLocation;
    SOSFragment sosFragment = new SOSFragment();

    @Override
    public void onLocationChanged(Location newLocation) {
        synchronized ( this ) {
            if(sosFragment.isBetterLocation(newLocation, currentBestLocation)) {
                currentBestLocation = newLocation;

                if(currentBestLocation.hasAccuracy() && currentBestLocation.getAccuracy() <= 100) {
                    sosFragment.location = currentBestLocation;
                    sosFragment.finish();
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

}
