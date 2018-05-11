package com.acin.josefigueira.sosalert.Model;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jose.figueira on 11-05-2018.
 */

public class LocationHandler
{
    List<Location> locations;
    public static final int MIN_LOCATIONS = 3;
    public static final int MAX_LOCATIONS = 3;

    public LocationHandler() {
        locations = new ArrayList<>();
    }

    public void addLocation(Location loc)
    {
        locations.add(loc);
    }

    public boolean hasMinLocations ()
    {
        Log.d("LOCATION", "GOT" + locations.size());
        return locations.size() >= MIN_LOCATIONS;
    }

    public boolean hasMaxLocations()
    {
        Log.d("LOCATION", "GOT" + locations.size());
        return locations.size() >= MAX_LOCATIONS;
    }
    public Location selectMostAccurateLocation()
    {
        if (locations.size() < MIN_LOCATIONS) return null;

        Collections.sort(locations, new Comparator<Location>(){
            @Override
            public int compare(Location location, Location t1) {

                int acc1 = location != null && location.hasAccuracy() ? (int) location.getAccuracy() : 0;
                int acc2 = t1 != null && t1.hasAccuracy() ? (int) t1.getAccuracy() : 0;

                return  acc1 == acc2 ? 0 : acc1 > acc2 ? 1 : -1;
            }
        });

        for(Location loc : locations) {
                if(loc != null)
                    return loc;
        }

        return null;
    }

    public void clear()
    {
        locations.clear();
    }
}
