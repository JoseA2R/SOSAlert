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

                getLocation();
                txtLongitude.setText("Longitude: " + longitude);
                txtLatitude.setText("Latitude: " +latitude);

            }
        });

    /*    button_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //mainActivity.showRequestPermissionsInfoAlertDialog();
            }
        });*/

    }

    public void getLocation() {


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
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            /*txtLongitude.setText("Longitude: " + longitude);
                            txtLatitude.setText("Latitude: " + latitude);*/
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
                                /*txtLongitude.setText("Longitude: " + longitude);
                                txtLatitude.setText("Latitude: " + latitude);*/
                                return;
                            }
                        }
                    }
                }


          /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
            }

            if (location != null) {

                longitude = location.getLongitude();
                latitude = location.getLatitude();

            }


    }


   /* public void onClickimgbtn(){
        Toast.makeText(getActivity().getApplicationContext(), "Button Selected", Toast.LENGTH_LONG).show();
    }*/

}
