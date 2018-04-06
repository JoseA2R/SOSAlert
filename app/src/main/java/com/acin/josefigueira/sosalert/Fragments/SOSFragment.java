package com.acin.josefigueira.sosalert.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.R;
import com.acin.josefigueira.sosalert.View.MainMenuActivity;

/**
 * Created by jose.figueira on 04-04-2018.
 */

public class SOSFragment extends Fragment {

    View view;
    Context mContext;
    MainMenuActivity mainActivity;

    Button button_sos;
    ImageButton imageButton;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment}
        view = inflater.inflate(R.layout.fragment_alert_button,container,false);

        getBtnData(view);

        return view;
    }

    public void getBtnData(View view){

        imageButton = view.findViewById(R.id.sos_img_btn);
        // button_sos = view.findViewById(R.id.btnpruebasos);
        txtLongitude = (TextView) view.findViewById(R.id.txtLongitude);
        txtLatitude = (TextView) view.findViewById(R.id.txtLatitude);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermissions();
                txtLongitude.setText("Longitude: " + longitude);
                txtLatitude.setText("Latitude: " +latitude);
                showRequestPermissionsInfoAlertDialog();


            }
        });

    /*    button_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //mainActivity.showRequestPermissionsInfoAlertDialog();
            }
        });*/

    }

    public void checkPermissions() {


        // GPSData objeto { Float latitude, Float longitude, }
        // SMSModel retorna GPSData
        // SMSController retorna GPSData
        // GPSData obj
        // latitude.setText(objeto.latitude)
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // GPSData objeto { Float latitude, Float longitude, }
            // SMSModel retorna GPSData
            // SMSController retorna GPSData
            // GPSData obj
            // latitude.setText(objeto.latitude)

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {


               getlocation();


          /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
            }

            if (location != null) {

                longitude = location.getLongitude();
                latitude = location.getLatitude();

            }


    }

    public void getlocation(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
                    try {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                            /*txtLongitude.setText("Longitude: " + longitude);
                            txtLatitude.setText("Latitude: " + latitude);*/
                        return;
                    }catch(SecurityException e){

                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    if (locationManager != null) {
                        try{
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                                /*txtLongitude.setText("Longitude: " + longitude);
                                txtLatitude.setText("Latitude: " + latitude);*/
                        return;
                    }catch(SecurityException e){

                    }
                    }
                }
            }
        }
    }

    public void sendTextMessage(){

        Toast.makeText(getActivity().getApplicationContext(), "Sent.", Toast.LENGTH_LONG).show();
        String strPhone = "+351965639423";
        String strMessage = "Testing";

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(strPhone, null, strMessage, null, null);

    }


    /**
     * Check if we have SMS permission
     */
    public boolean isSmsPermissionGranted() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request runtime SMS permission
     */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html

        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    public void showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true);
    }

    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        builder.setMessage(R.string.permission_dialog_message); // Your own message

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request?
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission();
                    //sendTextMessage();
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


   /* public void onClickimgbtn(){
        Toast.makeText(getActivity().getApplicationContext(), "Button Selected", Toast.LENGTH_LONG).show();
    }*/

}
