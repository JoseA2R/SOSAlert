package com.acin.josefigueira.sosalert.View;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.acin.josefigueira.sosalert.R;

/**
 * Created by jose.figueira on 02-04-2018.
 */

public class GPSPruebActivity extends AppCompatActivity {

    private TextView txtLongitude;
    private TextView txtLatitude;

    static final int REQUEST_LOCATION = 1;

    Location location;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_gps);

        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);

        double longitude = 0.0;
        double latitude = 0.0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else{

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
        if (location != null){

            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }

        txtLongitude.setText("Longitude: " + longitude);
        txtLatitude.setText("Latitude: " +latitude);

    }


}
