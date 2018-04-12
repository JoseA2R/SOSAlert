package com.acin.josefigueira.sosalert.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;

import com.acin.josefigueira.sosalert.Classes.ServiceLocationListener;
import com.acin.josefigueira.sosalert.Controller.UserController;
import android.os.CountDownTimer;

import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.R;
import com.acin.josefigueira.sosalert.View.MainMenuActivity;

/**
 * Created by jose.figueira on 04-04-2018.
 */

public class SOSFragment extends Fragment {

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    static final int REQUEST_LOCATION = 1;
    private static final int SMS_PERMISSION_CODE = 123;
    private static final int PERMISSIONS_MULTIPLE_REQUEST = 112;

    private Location currentBestLocation = null;
    private ServiceLocationListener gpsLocationListener;
    private ServiceLocationListener networkLocationListener;
    private Handler handler = new Handler();

    View view;
    View layoutView;
    Context mContext;
    Context smsContext;
    MainMenuActivity mainActivity;

    Button button_sos;
    ImageButton imageButton;
    TextView textView;
    public Location location;
    LocationManager locationManager;
    LocationListener listener;

    private SMSController controller_sms = null;
    private UserController userController;
    SharedPreferences SPreferences;

    private String fname,lname,country,description;

    public float longitude;
    public float latitude;

    String numcountdown;
    private Button cancelBtn;
    private TextView txtCountDown;
    private TextView txtLongitude;
    private TextView txtLatitude;

    public void SOSFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment}
        view = inflater.inflate(R.layout.fragment_alert_button,container,false);
        //imageButton.performClick();
        getBtnData(view);
        return view;
    }

    /*public void setContext(Context context){
        mContext = context;
    }*/

    @SuppressLint("ClickableViewAccessibility")
    public void getBtnData(View view){

        imageButton = view.findViewById(R.id.sos_img_btn);
        // button_sos = view.findViewById(R.id.btnpruebasos);
        txtCountDown = (TextView) view.findViewById(R.id.txt_count_down);

        txtLongitude = (TextView) view.findViewById(R.id.txtLongitude);
        txtLatitude = (TextView) view.findViewById(R.id.txtLatitude);
        layoutView = view;
        mContext = getActivity();

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction()  == MotionEvent.ACTION_DOWN){
                    layoutView.setBackgroundResource(R.color.colorAccent);
                    view.setBackgroundResource(R.color.colorAccent);
                    imageButton.setImageResource(R.drawable.sos_btn_pressed);
                }
                else if(event.getAction()  == MotionEvent.ACTION_UP){
                    layoutView.setBackgroundResource(R.color.colorPrimary);
                    view.setBackgroundResource(R.color.colorPrimary);
                    new CountDownTimer(4000,1000){
                        public void onTick(long millisUntilFinished) {
                            //imageButton.setClickable(false);
                            imageButton.setEnabled(false);
                            txtCountDown.setText("");
                            numcountdown = String.valueOf(millisUntilFinished/1000);
                            txtCountDown.append(numcountdown);
                            imageButton.setImageResource(R.drawable.circle);
                            System.out.println(millisUntilFinished/1000);
                        }
                        public void onFinish(){
                            txtCountDown.setText("");
                            imageButton.setEnabled(true);
                            imageButton.setImageResource(R.drawable.sos_btn);
                            checkAndroidVersion();
                            userController = new UserController(mContext);
                            userController.putLocation(getActivity().getApplicationContext(),latitude,longitude);
                            //showRequestPermissionsInfoAlertDialog();
                        }
                    }.start();
                    txtLongitude.setText("Longitude: " + longitude);
                    txtLatitude.setText("Latitude: " +latitude);

                }
                return true;


            }

        });


    }


    /*    button_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mainActivity.showRequestPermissionsInfoAlertDialog();
            }
        });*/

    public void checkPermissions() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

            fetchLocation();
            //getlocation();
            /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
        }
        if (location != null) {
            longitude = (float) location.getLongitude();
            latitude = (float) location.getLatitude();
        }
    }


    public void fetchLocation() {

        try {

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            LocationProvider gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
            LocationProvider networkProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

            //Figure out if we have a location somewhere that we can use as a current best location
            if( gpsProvider != null ) {
                Location lastKnownGPSLocation = locationManager.getLastKnownLocation(gpsProvider.getName());
                if( isBetterLocation(lastKnownGPSLocation, currentBestLocation) )
                    currentBestLocation = lastKnownGPSLocation;
            }else if( networkProvider != null ) {
                Location lastKnownNetworkLocation = locationManager.getLastKnownLocation(networkProvider.getName());
                if( isBetterLocation(lastKnownNetworkLocation, currentBestLocation) )
                    currentBestLocation = lastKnownNetworkLocation;
            }


            gpsLocationListener = new ServiceLocationListener();
            networkLocationListener = new ServiceLocationListener();


            if(gpsProvider != null) {
                locationManager.requestLocationUpdates(gpsProvider.getName(), 0l, 0.0f, gpsLocationListener);
            }else if(networkProvider != null) {
                locationManager.requestLocationUpdates(networkProvider.getName(), 0l, 0.0f, networkLocationListener);
            }


            if(gpsProvider != null || networkProvider != null) {
                handler.postDelayed(timerRunnable, 2 * 60 * 1000);
            } else {
                handler.post(timerRunnable);
            }
        } catch (SecurityException se) {
            finish();
        }
    }

    /*public void getlocation(){

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
               // synchronized ( this ) {
                    if(isBetterLocation(newLocation, currentBestLocation)) {
                        currentBestLocation = newLocation;

                        if(currentBestLocation.hasAccuracy() && currentBestLocation.getAccuracy() <= 100) {
                            finish();
                        }
                    }
              //  }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
                if (locationManager != null) {
                    try {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        //isBetterLocation(newLocation, currentLocation);

                        longitude = (float) location.getLongitude();
                        latitude = (float) location.getLatitude();
                        /*txtLongitude.setText("Longitude: " + longitude);
                            txtLatitude.setText("Latitude: " + latitude);
                        return;

                    }catch(SecurityException e){

                    }
                }
            }

            public void onProviderDisabled(String provider) {
                if (locationManager != null) {
                    try{
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = (float) location.getLongitude();
                        latitude = (float) location.getLatitude();
                        /*txtLongitude.setText("Longitude: " + longitude);
                        txtLatitude.setText("Latitude: " + latitude);
                        return;
                    }catch(SecurityException e){

                    }
                }
            }
        };
    }*/

    public synchronized void finish() {
        ServiceLocationListener sll = new ServiceLocationListener();

        longitude = (float) location.getLatitude();
        latitude = (float)  location.getLongitude();
        handler.removeCallbacks(timerRunnable);
        handler.post(timerRunnable);
    }
    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    public boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {

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

    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            //Intent intent = new Intent(LocationService.this.getPackageName() + ".action.LOCATION_FOUND");

            if(currentBestLocation != null) {
                //intent.putExtra(LocationManager.KEY_LOCATION_CHANGED, currentBestLocation);
                locationManager.removeUpdates(gpsLocationListener);
                locationManager.removeUpdates(networkLocationListener);

            }
        }
    };

    public void setContext(Context context){

        smsContext = context;

    }

    public void sendTextMessage(){

        //Toast.makeText(getActivity().getBaseContext(), "Sent.", Toast.LENGTH_LONG).show();


        SPreferences = PreferenceManager.getDefaultSharedPreferences(smsContext);

        fname = SPreferences.getString("firstname","");
        lname = SPreferences.getString("lastname","");
        country = SPreferences.getString("country","");
        description = SPreferences.getString("description","");
        latitude = SPreferences.getFloat("latitude",0);
        longitude = SPreferences.getFloat("longitude",0);

        String strPhone = "123";
        String strMessage = fname + " " + lname + " from " + country + " is located at http://maps.google.com/?q="+latitude+","+longitude;
        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> messageParts = sms.divideMessage(strMessage);
        //Toast.makeText(getActivity(),"Message Sent",Toast.LENGTH_LONG).show();
        sms.sendMultipartTextMessage(strPhone, null, messageParts, null, null);

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
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        //builder.setMessage(R.string.permission_dialog_message); // Your own message

        //builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

            //public void onClick(DialogInterface dialog, int which) {
               // dialog.dismiss();
                // Display system runtime permission request?
        if (makeSystemRequest) {

                requestReadAndSendSmsPermission();
                    //sendTextMessage();
        }
    }
        //});

       // builder.setCancelable(false);
       // builder.show();
    //}


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {
            // write your logic here
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS) + ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.SEND_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Please Grant Permissions to get your location and send messages",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .SEND_SMS, Manifest.permission
                                                .ACCESS_FINE_LOCATION},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                this.requestPermissions(
                        new String[]{Manifest.permission
                                .SEND_SMS, Manifest.permission
                                .ACCESS_FINE_LOCATION},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            //fetchLocation();
            //sendTextMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
       /* switch (requestCode) {
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
        }*/

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean gpsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean sendSMS = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(gpsPermission && sendSMS)
                    {
                        fetchLocation();
                        sendTextMessage();
                        // write your logic here
                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }


   /* public void onClickimgbtn(){
        Toast.makeText(getActivity().getApplicationContext(), "Button Selected", Toast.LENGTH_LONG).show();
    }*/




}
