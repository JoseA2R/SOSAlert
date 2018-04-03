package com.acin.josefigueira.sosalert.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Controller.GPSController;
import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.R;

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
    private static final int SMS_PERMISSION_CODE = 123;
    private SMSController controller_sms = null;

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
        controller_sms = new SMSController(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRequestPermissionsInfoAlertDialog();
            }
        });

    }

    public void getLocation(){




        // GPSData objeto { Float latitude, Float longitude, }
        // SMSModel retorna GPSData
        // SMSController retorna GPSData
        // GPSData obj
        // latitude.setText(objeto.latitude)
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

        String strMessage = "Testing";


            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(strPhone, null, strMessage, null, null);
            Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
        }


    /**
     * Check if we have SMS permission
     */
        public boolean isSmsPermissionGranted() {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        }

    /**
     * Request runtime SMS permission
     * @param onClickListener
     */
        private void requestReadAndSendSmsPermission(DialogInterface.OnClickListener onClickListener) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                // You may display a non-blocking explanation here, read more in the documentation:
                // https://developer.android.com/training/permissions/requesting.html

            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
        }

    public void showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true);
    }

    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        builder.setMessage(R.string.permission_dialog_message); // Your own message

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request?
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission(this);
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sendTextMessage();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}


