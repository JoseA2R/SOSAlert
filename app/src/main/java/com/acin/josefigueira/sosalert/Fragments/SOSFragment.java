package com.acin.josefigueira.sosalert.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAssignedNumbers;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;

import com.acin.josefigueira.sosalert.Classes.MyLocation;
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
    LinearLayout layout;
    LinearLayout.LayoutParams layoutParams;
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

    Snackbar permissionsSnackbar;

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
                                    cancel();
                                }
                            });
                            imageButton.setEnabled(false);
                            txtCountDown.setText("");
                            numcountdown = String.valueOf(millisUntilFinished/1000);
                            txtCountDown.append(numcountdown);
                            txtCountDown.setShadowLayer(1.5f, 5, 5, Color.BLACK);
                            imageButton.setImageResource(R.drawable.circle);
                            //System.out.println(millisUntilFinished/1000);
                        }
                        public void onFinish(){
                            button_sos.setVisibility(View.INVISIBLE);
                            txtCountDown.setText("");
                            imageButton.setEnabled(true);
                            imageButton.setImageResource(R.drawable.sos_btn);
                            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                                @Override
                                public void gotLocation(Location location) {
                                    //Log.d( "Location: ","lon: "+location.getLongitude()+" ----- lat: "+location.getLatitude());
                                    //txtLongitude.setText("Longitude: " + longitude);
                                    //txtLatitude.setText("Latitude: " +latitude);
                                    latitude = (float) location.getLatitude();
                                    longitude = (float) location.getLongitude();
                                    userController.putLocation(getActivity().getApplicationContext(), latitude, longitude);
                                    sendTextMessage();
                                }
                            };
                            MyLocation myLocation = new MyLocation();
                            myLocation.getLocation(mContext, locationResult);


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

    public void sendTextMessage(){

        //Toast.makeText(getActivity().getBaseContext(), "Sent.", Toast.LENGTH_LONG).show();


        SPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        fname = SPreferences.getString("firstname","");
        lname = SPreferences.getString("lastname","");
        country = SPreferences.getString("country","");
        description = SPreferences.getString("description","");
        latitude = SPreferences.getFloat("latitude",0);
        longitude = SPreferences.getFloat("longitude",0);

        String strPhone = "+351965639423";
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
                        //fetchLocation();
                        //sendTextMessage();
                        // write your logic here
                    } else {
                        permissionsSnackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Please Grant Permissions to use GPS and send Messages",
                                Snackbar.LENGTH_INDEFINITE);
                        permissionsSnackbar.setAction("ENABLE",
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
    }public void onResume() {

        super.onResume();
        permissionsSnackbar.getView();
    }

    public void onDestroy() {

        super.onDestroy();
        permissionsSnackbar.dismiss();
    }

   /* public void onClickimgbtn(){
        Toast.makeText(getActivity().getApplicationContext(), "Button Selected", Toast.LENGTH_LONG).show();
    }*/
}
