package com.acin.josefigueira.sosalert.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.util.ArrayList;

import com.acin.josefigueira.sosalert.Classes.MyLocation;
import com.acin.josefigueira.sosalert.Classes.ServiceLocationListener;
import com.acin.josefigueira.sosalert.Controller.GPSController;
import com.acin.josefigueira.sosalert.Controller.UserController;
import android.os.CountDownTimer;

import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.Model.LocationHandler;
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
    LinearLayout layout;
    LinearLayout.LayoutParams layoutParams;
    MainMenuActivity mainActivity;


    Button button_sos;
    ImageButton imageButton;
    TextView sendingsms, FindingYou;
    public Location location;
    LocationManager locationManager;
    LocationListener listener;

    private SMSController smsController;
    private UserController userController;
    private GPSController gpsController;
    SharedPreferences SPreferences;
    ArrayList<String> userData;
    ArrayList<String> locData;

    LocationHandler locationHandler;
    private String fname,lname,country,description,phone;

    public float longitude;
    public float latitude;
    public float precision;

    String numcountdown;
    private Button cancelBtn;
    private TextView txtCountDown;

    public ToneGenerator toneBeep;

    public static Snackbar permissionsSnackbar;
    private boolean GpsStatus;


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
        imageButton = view.findViewById(R.id.sos_img_btn);
        button_sos = view.findViewById(R.id.cancel_btn);
        button_sos.setVisibility(View.INVISIBLE);
        txtCountDown = (TextView) view.findViewById(R.id.txt_count_down);
        sendingsms = view.findViewById(R.id.tv_sending_message);
        FindingYou = view.findViewById(R.id.tv_are_you);
        layoutView = view;
        mContext = getActivity();
        userController = new UserController(mContext);
        gpsController = new GPSController(mContext);
        getBtnData(view);
        return view;
    }

    /*public void setContext(Context context){
        mContext = context;
    }*/

    @SuppressLint("ClickableViewAccessibility")
    public void getBtnData(View view){

        //view = (LinearLayout) view.findViewById(R.id.linear_layout_tags);


        checkAndroidVersion();
        view.performClick();
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
                    //checkAndroidVersion();
                    toneBeep = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                    new CountDownTimer(4000,1000){
                        public void onTick(long millisUntilFinished) {
                            //imageButton.setClickable(false);
                            button_sos.setVisibility(View.VISIBLE);
                            button_sos.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v)
                                {
                                    button_sos.setVisibility(View.INVISIBLE);
                                    txtCountDown.setText("");
                                    imageButton.setEnabled(true);
                                    imageButton.setImageResource(R.drawable.sos_btn);
                                    //toneBeep.stopTone();tranquis
                                    toneBeep.release();
                                    cancel();
                                }
                            });
                            imageButton.setEnabled(false);
                            txtCountDown.setText("");
                            numcountdown = String.valueOf(millisUntilFinished/1000);
                            toneBeep.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK,200);
                            if (Integer.parseInt(numcountdown) < 1){
                                txtCountDown.append("0");
                            }else {
                                txtCountDown.append(numcountdown);
                            }
                            txtCountDown.setShadowLayer(1.5f, 5, 5, Color.BLACK);
                            imageButton.setImageResource(R.drawable.circle);
                            //System.out.println(millisUntilFinished/1000);
                        }
                        public void onFinish(){
                            //toneBeep.stopTone();
                            toneBeep.release();
                            locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                            try {
                                GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            }catch(NullPointerException e){
                                e.printStackTrace();
                            }
                            if (GpsStatus == false) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(
                                        R.string.gps_disabled)
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.yes_btn,
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog,
                                                                        int id) {
                                                        // Sent user to GPS settings screen
                                                        //final ComponentName toLaunch = new ComponentName("com.android.settings","com.android.settings.SecuritySettings");
                                                        final Intent intent = new Intent(
                                                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                        //intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                                        //intent.setComponent(toLaunch);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivityForResult(intent, 0);
                                                        dialog.dismiss();
                                                        button_sos.setVisibility(View.INVISIBLE);
                                                        txtCountDown.setText("");
                                                        imageButton.setEnabled(true);
                                                        imageButton.setImageResource(R.drawable.sos_btn);
                                                    }
                                                })
                                        .setNegativeButton(R.string.no_btn,
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog,
                                                                        int id) {
                                                        button_sos.setVisibility(View.INVISIBLE);
                                                        txtCountDown.setText("");
                                                        imageButton.setEnabled(true);
                                                        imageButton.setImageResource(R.drawable.sos_btn);
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }else {
                                txtCountDown.setText("");
                                button_sos.setVisibility(View.INVISIBLE);
                                imageButton.setEnabled(true);
                                imageButton.setImageResource(R.drawable.sos_btn);
                                //sendingsms.append("Sending Message");
                                final LocationHandler locationHandler =  new LocationHandler();
                                final MyLocation myLocation = new MyLocation(locationHandler);
                                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                                    @Override
                                    public void gotLocation(Location location) {
                                        //locationHandler.addLocation(location);
                                        if (locationHandler.hasMinLocations()) {
                                            Log.d("LOCATION", " Send message");
                                            location = locationHandler.selectMostAccurateLocation();
                                            latitude = (float) location.getLatitude();
                                            longitude = (float) location.getLongitude();
                                            userController.setPlace(FindingYou.getText().toString());
                                            precision = (float) location.getAccuracy();
                                            System.out.println("latitude: " + latitude + " \nlongitude: " + longitude + " \nAccuracy: " + precision);
                                            userController.putLocation(getActivity().getApplicationContext(), latitude, longitude, precision);
                                            smsController = new SMSController();
                                            smsController.SMSController(mContext);
                                            smsController.sendTextMessage();
                                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                                public void run() {
                                                    //alterar aqui
                                                }
                                            });
                                        }

                                        if (locationHandler.hasMaxLocations()) {
                                            locationManager.removeUpdates(myLocation.getListener());
                                            locationHandler.clear();
                                        }
                                            /*latitude = (float) location.getLatitude();
                                            longitude = (float) location.getLongitude();
                                            userController.setPlace(FindingYou.getText().toString());
                                            precision = (float) location.getAccuracy();
                                            System.out.println("latitude: " + latitude + " \nlongitude: " + longitude + " \nAccuracy: " + precision);
                                            userController.putLocation(getActivity().getApplicationContext(), latitude, longitude,precision);
                                            smsController = new SMSController();
                                            smsController.SMSController(mContext);

                                            smsController.sendTextMessage();*/
                                            //smsController.unregisterSentReceiver();
                                            //smsController.unregisterDeliveredReceiver();
                                            //sendingsms.setText("");

                                        //OJO CON EL CONTROLADOR DE MENSAJES
                                        //sendTextMessage();
                                    }
                                };
                                myLocation.getLocation(mContext, locationResult);
                            }
                            //showRequestPermissionsInfoAlertDialog();
                        }
                    }.start();
                }
                return true;
            }

        });

        FindingYou.setText("");

    }


    public void setContext(Context context){

        smsContext = context;

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
        if (makeSystemRequest) {
            requestReadAndSendSmsPermission();
            //sendTextMessage();
        }
    }


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {
            // write your logic here
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) +
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.SEND_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                imageButton.setEnabled(false);
                permissionsSnackbar.make(getActivity().findViewById(R.id.main_menu),
                        R.string.grant_permissions,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.enable_btn,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    requestPermissions(
                                        new String[]{Manifest.permission
                                                .SEND_SMS, Manifest.permission
                                                .ACCESS_FINE_LOCATION},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                                }catch (IllegalStateException e){
                                    e.printStackTrace();
                                }
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

            locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!GpsStatus) {
                gpsController.showSettingsAlert();
            }else{

                    final LocationHandler locationHandler = new LocationHandler();
                    final MyLocation myLocation = new MyLocation(locationHandler);
                    final SMSController smsController = new SMSController();
                    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                        @Override
                        public void gotLocation(Location location) {
                            //locationHandler.addLocation(location);
                            if (locationHandler.hasMinLocations()) {
                                //Log.d("LOCATION", " Send message");
                                location = locationHandler.selectMostAccurateLocation();
                                latitude = (float) location.getLatitude();
                                longitude = (float) location.getLongitude();
                                userController.setPlace(FindingYou.getText().toString());
                                precision = (float) location.getAccuracy();
                                System.out.println("latitude: " + latitude + " \nlongitude: " + longitude + " \nAccuracy: " + precision);
                                try {
                                userController.putLocation(getContext(), latitude, longitude, precision);
                                }catch(NullPointerException e){
                                    e.printStackTrace();
                                }
                                Log.d("LOCATION", " Start sending message");
                                //smsController = new SMSController();
                                smsController.SMSController(mContext);
                                //smsController.sendTextMessage();
                                //locationHandler.clear();
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    public void run() {
                                        //alterar aqui
                                    }
                                });
                            }

                            if (locationHandler.hasMaxLocations()) {
                                locationManager.removeUpdates(myLocation.getListener());
                                locationHandler.clear();
                            }

                        }
                    };
                    myLocation.getLocation(mContext, locationResult);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean gpsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean sendSMS = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(gpsPermission && sendSMS)
                    {
                        imageButton.setEnabled(true);
                        //fetchLocation();
                        //sendTextMessage();
                        // write your logic here
                    } else {
                        imageButton.setEnabled(false);
                            permissionsSnackbar = Snackbar.make(getActivity().findViewById(R.id.main_menu),
                                    R.string.grant_permissions,
                                    Snackbar.LENGTH_INDEFINITE);
                            permissionsSnackbar.setAction(R.string.enable_btn,
                                new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                        requestPermissions(
                                            new String[]{Manifest.permission
                                                    .SEND_SMS, Manifest.permission
                                                    .ACCESS_FINE_LOCATION},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                                        } catch(IllegalStateException|NullPointerException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }).show();
                    }
                }
            break;
        }

    }

    public void onAttach(){
        super.onAttach(getActivity());

    }

    public void onPause() {

        super.onPause();


    }

    public void onResume() {

        super.onResume();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        }
        /*final LocationHandler locationHandler =  new LocationHandler();
        final MyLocation myLocation = new MyLocation(locationHandler);
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //locationHandler.addLocation(location);
                if (locationHandler.hasMinLocations()) {
                    Log.d("LOCATION", " Send message");
                    location = locationHandler.selectMostAccurateLocation();
                    latitude = (float) location.getLatitude();
                    longitude = (float) location.getLongitude();
                    userController.setPlace(FindingYou.getText().toString());
                    precision = (float) location.getAccuracy();
                    System.out.println("latitude: " + latitude + " \nlongitude: " + longitude + " \nAccuracy: " + precision);
                    userController.putLocation(getActivity().getApplicationContext(), latitude, longitude, precision);
                    //locationHandler.clear();
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            //alterar aqui
                        }
                    });
                }

                if (locationHandler.hasMaxLocations()) {
                    locationManager.removeUpdates(myLocation.getListener());
                }

            }
        };
        myLocation.getLocation(mContext, locationResult);*/
    }

    public void onDestroy() {

        super.onDestroy();
        //permissionsSnackbar.dismiss();
    }

   /* public void onClickimgbtn(){
        Toast.makeText(getActivity().getApplicationContext(), "Button Selected", Toast.LENGTH_LONG).show();
    }*/
}