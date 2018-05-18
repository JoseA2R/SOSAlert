package com.acin.josefigueira.sosalert.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.acin.josefigueira.sosalert.Controller.MyLocation;
import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.Controller.UserController;
import com.acin.josefigueira.sosalert.Model.LocationHandler;

/**
 * Created by jose.figueira on 18-05-2018.
 */

public class MessageHandler {

    Context mContext;
    private float longitude;
    private float latitude;
    private float precision;
    SMSController smsController;
    UserController userController;
    LocationManager locationManager;
    Activity activity;

    public MessageHandler(Context context, Activity activity) {
        this.mContext = context;
        this.activity = activity;
    }

    public void gettingGPSSendMessage(){
        final LocationHandler locationHandler =  new LocationHandler();
        final MyLocation myLocation = new MyLocation(locationHandler);
        final MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //locationHandler.addLocation(location);
                if (locationHandler.hasMinLocations()) {
                    Log.d("LOCATION", " Send message");
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                                                    /*sendingsms.setText("Sending SMS");
                                                    imageButton.setEnabled(false);*/
                        }
                    });
                    try {
                        location = locationHandler.selectMostAccurateLocation();
                        latitude = (float) location.getLatitude();
                        longitude = (float) location.getLongitude();
                        //userController.setPlace(FindingYou.getText().toString());
                        precision = (float) location.getAccuracy();
                        System.out.println("latitude: " + latitude + " \nlongitude: " + longitude + " \nAccuracy: " + precision);
                        userController.putLocation(activity, latitude, longitude, precision);
                        smsController = new SMSController();
                        smsController.SMSController(mContext);
                        smsController.sendTextMessage();
                        if (locationHandler.hasMaxLocations()) {
                            locationManager.removeUpdates(myLocation.getListener());
                            locationHandler.clear();
                        }
                    }catch(NullPointerException e) {
                        activity.runOnUiThread(new Thread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                                // Setting Dialog Title
                                alertDialog.setTitle("Mesage not Sent");
                                // Setting Dialog Message
                                alertDialog.setMessage("The message was not sent due to a GPS error. Try to send the message again");
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //locationManager.removeUpdates(myLocation.getListener());
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();
                            }
                        }));
                        e.printStackTrace();
                    }

                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    // Setting Dialog Title
                    alertDialog.setTitle("Mesage not Sent");
                    // Setting Dialog Message
                    alertDialog.setMessage("We couldn't get your exact location.\n Try Again");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            locationManager.removeUpdates(myLocation.getListener());
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }
        };
        myLocation.getLocation(mContext, locationResult);
    }

}
