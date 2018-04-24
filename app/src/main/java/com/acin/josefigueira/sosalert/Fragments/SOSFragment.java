package com.acin.josefigueira.sosalert.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAssignedNumbers;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
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
import android.telephony.SmsManager;
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
import android.preference.PreferenceManager;
import java.util.ArrayList;

import com.acin.josefigueira.sosalert.Classes.Languages;
import com.acin.josefigueira.sosalert.Classes.MyLocation;
import com.acin.josefigueira.sosalert.Classes.ServiceLocationListener;
import com.acin.josefigueira.sosalert.Controller.GPSController;
import com.acin.josefigueira.sosalert.Controller.UserController;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.R;
import com.acin.josefigueira.sosalert.View.MainActivity;
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
    TextView textView;
    public Location location;
    LocationManager locationManager;
    LocationListener listener;

    private SMSController smsController;
    private UserController userController;
    private GPSController gpsController;
    SharedPreferences SPreferences;
    ArrayList<String> userData;
    ArrayList<String> locData;

    private String fname,lname,country,description,phone;

    public float longitude;
    public float latitude;

    String numcountdown;
    private Button cancelBtn;
    private TextView txtCountDown;
    private TextView txtLongitude;
    private TextView txtLatitude;

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
        getBtnData(view);
        return view;
    }

    /*public void setContext(Context context){
        mContext = context;
    }*/

    @SuppressLint("ClickableViewAccessibility")
    public void getBtnData(View view){

        //view = (LinearLayout) view.findViewById(R.id.linear_layout_tags);

        imageButton = view.findViewById(R.id.sos_img_btn);
        button_sos = view.findViewById(R.id.cancel_btn);
        button_sos.setVisibility(View.INVISIBLE);
        txtCountDown = (TextView) view.findViewById(R.id.txt_count_down);
        txtLongitude = (TextView) view.findViewById(R.id.txtLongitude);
        txtLatitude = (TextView) view.findViewById(R.id.txtLatitude);
        layoutView = view;
        mContext = getActivity();
        userController = new UserController(mContext);
        gpsController = new GPSController(mContext);
        checkAndroidVersion();


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
                            /*MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                                @Override
                                public void gotLocation(Location location) {
                                    //Log.d( "Location: ","lon: "+location.getLongitude()+" ----- lat: "+location.getLatitude());
                                    //txtLongitude.setText("Longitude: " + longitude);
                                    //txtLatitude.setText("Latitude: " +latitude);
                                    try {
                                        latitude = (float) location.getLatitude();
                                        longitude = (float) location.getLongitude();
                                        userController.putLocation(getActivity().getApplicationContext(), latitude, longitude);
                                    } catch (NullPointerException ex) {

                                    }
                                }
                            };
                            MyLocation myLocation = new MyLocation();
                            myLocation.getLocation(mContext, locationResult);*/
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
                            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
                                MyLocation myLocation = new MyLocation();
                                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                                    @Override
                                    public void gotLocation(Location location) {
                                        //Log.d( "Location: ","lon: "+location.getLongitude()+" ----- lat: "+location.getLatitude());
                                        //txtLongitude.setText("Longitude: " + longitude);
                                        //txtLatitude.setText("Latitude: " +latitude);
                                        try {
                                            latitude = (float) location.getLatitude();
                                            longitude = (float) location.getLongitude();
                                            userController.putLocation(getActivity().getApplicationContext(), latitude, longitude);
                                            smsController = new SMSController();
                                            smsController.SMSController(mContext);
                                            smsController.sendTextMessage();
                                            smsController.unregisterSentReceiver();
                                            smsController.unregisterDeliveredReceiver();
                                        } catch(NullPointerException ex ){
                                        }
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



    }

    /*private class CustomTask extends AsyncTask<void,void,void> {
        @Override
        protected void doInBackground(void... voids) {
        }
    }*/

    public void setContext(Context context){

        smsContext = context;

    }

    public void checkIfEnabled(){


    }

    /*public void sendTextMessage(){
        //Toast.makeText(getActivity().getBaseContext(), "Sent.", Toast.LENGTH_LONG).show();
        SPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userData = userController.getData();
        //locData = userController.getLocation();
        for (int i=0; i <= 4; i++){
            System.out.println(userData.get(i));
        }
        fname = userData.get(0);
        lname = userData.get(1);
        country = userData.get(2);
        description = userData.get(3);
        phone = userData.get(4);
        latitude = SPreferences.getFloat("latitude",0);
        longitude = SPreferences.getFloat("longitude",0);
        String strPhone = "+351965639423";
        String strPhone2 =  phone;
        String strMessage = fname + " " + lname + " from " + country + " is located at http://maps.google.com/?q="+latitude+","+longitude;
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";
            PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,new Intent(DELIVERED), 0);
// ---when the SMS has been sent---
        BroadcastReceiver sendSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context,Intent sentPI)
            {
                switch(getResultCode())
                {
                case Activity.RESULT_OK:
                    Toast.makeText(getActivity(),"Message Sent",Toast.LENGTH_LONG).show();
                    toneBeep.startTone(ToneGenerator.TONE_CDMA_CONFIRM,300);
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getActivity(), "Generic failure",Toast.LENGTH_SHORT).show();
                        break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getActivity(),"     No Service\nMessage NOT Sent",Toast.LENGTH_LONG).show();
                        break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getActivity(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getActivity(),"        Radio Off\nMessage NOT Sent",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        // ---when the SMS has been delivered---
        BroadcastReceiver deliverSMS = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context arg0,Intent arg1)
                {
                    switch(getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(getActivity(),"Message delivered",Toast.LENGTH_LONG).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getActivity(),"Message NOT delivered",Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };
        //Toast.makeText(getActivity(),"Message Sent",Toast.LENGTH_LONG).show();
            // ---Notify when the SMS has been sent---
            getActivity().registerReceiver(sendSMS, new IntentFilter(SENT));
            // ---Notify when the SMS has been delivered---
            getActivity().registerReceiver(deliverSMS, new IntentFilter(DELIVERED));
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> messageParts = sms.divideMessage(strMessage);
            sms.sendTextMessage(strPhone, null, strMessage, sentPI, deliveredPI);
            //Toast.makeText(getActivity(), "The SMS was sent successfully", Toast.LENGTH_LONG).show();
            if (phone != "") {
                sms.sendMultipartTextMessage(strPhone2, null, messageParts, null, null);
                //Toast.makeText(getActivity(), "SMS failed, please try again later!", Toast.LENGTH_LONG).show();
            }
        }catch(Exception E){
        }
    }*/


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
            locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (GpsStatus == false) {
                gpsController.showSettingsAlert();
            }else{
                MyLocation myLocation = new MyLocation();
                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        //Log.d( "Location: ","lon: "+location.getLongitude()+" ----- lat: "+location.getLatitude());
                        //txtLongitude.setText("Longitude: " + longitude);
                        //txtLatitude.setText("Latitude: " +latitude);
                        try {
                            latitude = (float) location.getLatitude();
                            longitude = (float) location.getLongitude();
                            userController.putLocation(getActivity().getApplicationContext(), latitude, longitude);
                        } catch(NullPointerException ex ){
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
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .SEND_SMS, Manifest.permission
                                                        .ACCESS_FINE_LOCATION},
                                                PERMISSIONS_MULTIPLE_REQUEST);
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
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onDestroy() {

        super.onDestroy();
        //permissionsSnackbar.dismiss();
    }

   /* public void onClickimgbtn(){
        Toast.makeText(getActivity().getApplicationContext(), "Button Selected", Toast.LENGTH_LONG).show();
    }*/
}