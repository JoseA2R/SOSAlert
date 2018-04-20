package com.acin.josefigueira.sosalert.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Model.UserModel;
import com.acin.josefigueira.sosalert.POJO.User;
import com.acin.josefigueira.sosalert.R;
import com.acin.josefigueira.sosalert.View.SOSActivity;

import java.util.ArrayList;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class SMSController {

    private User user;
    UserModel model;
    SharedPreferences SPreferences;
    SharedPreferences.Editor editorPreferences;
    Context mContext;
    private String fname,lname,country,description,phone;
    public float longitude;
    public float latitude;

    ArrayList<String> userData;

    final ToneGenerator toneBeep = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

    public void SMSController(Context context){
        mContext = context;
    }

    public void sendTextMessage(){

        //Toast.makeText(getActivity().getBaseContext(), "Sent.", Toast.LENGTH_LONG).show();

        UserController userController = new UserController(mContext);
        SPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
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
                            Toast.makeText(mContext,"Message Sent",Toast.LENGTH_LONG).show();
                            toneBeep.startTone(ToneGenerator.TONE_CDMA_CONFIRM,300);
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(mContext, "Generic failure",Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(mContext,"     No Service\nMessage NOT Sent",Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(mContext, "Null PDU", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(mContext,"        Radio Off\nMessage NOT Sent",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(mContext,"Message delivered",Toast.LENGTH_LONG).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(mContext,"Message NOT delivered",Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };

            //Toast.makeText(getActivity(),"Message Sent",Toast.LENGTH_LONG).show();
            // ---Notify when the SMS has been sent---
            mContext.registerReceiver(sendSMS, new IntentFilter(SENT));

            // ---Notify when the SMS has been delivered---
            mContext.registerReceiver(deliverSMS, new IntentFilter(DELIVERED));

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
    }
    
}
