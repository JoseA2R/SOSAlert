package com.acin.josefigueira.sosalert.Classes;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.acin.josefigueira.sosalert.Fragments.SOSFragment;

/**
 * Created by jose.figueira on 11-04-2018.
 */

public class ServiceLocationListener implements LocationListener {

    public Location currentBestLocation;
    SOSFragment sosFragment = new SOSFragment();
    public Location getLoc;

    @Override
    public void onLocationChanged(Location newLocation) {

            //if(sosFragment.isBetterLocation(newLocation, currentBestLocation)) {
                currentBestLocation = newLocation;
                getLoc = currentBestLocation;
                sosFragment.location = currentBestLocation;
           // }
        //}
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

}
