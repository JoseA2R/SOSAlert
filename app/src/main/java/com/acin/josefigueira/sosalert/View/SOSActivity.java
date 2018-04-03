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
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Controller.GPSController;
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
    static final int REQUEST_LOCATION = 1;

    double longitude = 0.0;
    double latitude = 0.0;

    private TextView txtLongitude;
    private TextView txtLatitude;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_button);

        button = (Button) findViewById(R.id.btnprueba);
        textView = (TextView) findViewById(R.id.tvprueba);

        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);

        getLocation();

        txtLongitude.setText("Longitude: " + longitude);
        txtLatitude.setText("Latitude: " +latitude);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextMessage();
            }
        });

    }

    public void getLocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else{


            locationManager = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);

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
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        txtLongitude.setText("Longitude: " + longitude);
                        txtLatitude.setText("Latitude: " +latitude);
                        return;
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            txtLongitude.setText("Longitude: " + longitude);
                            txtLatitude.setText("Latitude: " +latitude);
                            return;
                        }
                    }
                }
            }


          /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/

        }

        if (location != null){

            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }

    }

    public void sendTextMessage(){
        String strPhone = "+351965639423";

        String strMessage = "Lorem\nIpsum";

        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(strPhone, null, strMessage, null, null);

        Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
    }

}


