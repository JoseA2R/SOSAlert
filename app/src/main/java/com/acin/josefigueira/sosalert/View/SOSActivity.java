package com.acin.josefigueira.sosalert.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.R;

import static com.acin.josefigueira.sosalert.View.GPSPruebActivity.REQUEST_LOCATION;

/**
 * Created by jose.figueira on 27-03-2018.
 */

public class SOSActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    Location location;
    LocationManager locationManager;
    LocationListener listener;

    private TextView txtLongitude;
    private TextView txtLatitude;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_button);

        button = (Button) findViewById(R.id.btnprueba);
        textView = (TextView) findViewById(R.id.tvprueba);

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

       /* locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                textView.append("\n " + location.getLatitude() + " "
                        + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        };

        configure_button();*/

    }


   /*public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:

                configure_button();
                return;

            default:
                break;
        }

    }


    void configure_button() {



        button.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET}, 10);
                    }
                    return;
                }
                locationManager.requestLocationUpdates("gps", 10000, 0, listener);

                }
            });

        }*/
}


