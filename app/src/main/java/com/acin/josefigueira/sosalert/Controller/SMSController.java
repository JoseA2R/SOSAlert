package com.acin.josefigueira.sosalert.Controller;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Fragments.SOSFragment;
import com.acin.josefigueira.sosalert.Model.UserModel;
import com.acin.josefigueira.sosalert.POJO.User;

import java.util.ArrayList;

import static android.app.PendingIntent.getBroadcast;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class SMSController {

    private User user;
    SOSFragment sosFragment;
    UserModel model;
    SharedPreferences SPreferences;
    SharedPreferences.Editor editorPreferences;
    Context mContext;
    private String fname,lname,country,description,phone;
    private float longitude;
    private float latitude;

    BroadcastReceiver DeliveredReceiver;
    BroadcastReceiver SentReceiver;

    ArrayList<String> userData;

    private ToneGenerator toneBeep;

    public void SMSController(Context context){

        mContext = context;
    }

    public void sendTextMessage(){

        //Toast.makeText(getActivity().getBaseContext(), "Sent.", Toast.LENGTH_LONG).show();

        UserController userController = new UserController(mContext);
        SPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        toneBeep = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        userData = userController.getData();

        //locData = userController.getLocation();

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
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> messageParts = sms.divideMessage(strMessage);
            int partsCount = messageParts.size();
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";
            ArrayList<PendingIntent> sentPendings = new ArrayList<PendingIntent>(partsCount);
            ArrayList<PendingIntent> deliveredPendings = new ArrayList<PendingIntent>(partsCount);

            SentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(mContext, "Message Sent", Toast.LENGTH_SHORT).show();
                            //unregisterSentReceiver();
                            //toneBeep.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 300);
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(mContext, "Generic failure", Toast.LENGTH_SHORT).show();
                            //unregisterSentReceiver();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(mContext, "     No Service\n" +
                                    "Message NOT Sent", Toast.LENGTH_SHORT).show();
                            //unregisterSentReceiver();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(mContext, "Null PDU", Toast.LENGTH_SHORT).show();
                            //unregisterSentReceiver();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(mContext, "        Radio Off\n" +
                                    "Message NOT Sent", Toast.LENGTH_SHORT).show();
                            //unregisterSentReceiver();
                            break;
                    }
                }
            };

           DeliveredReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(mContext, "Message delivered", Toast.LENGTH_SHORT).show();
                            //unregisterDeliveredReceiver();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(mContext, "Message NOT delivered", Toast.LENGTH_SHORT).show();
                            //unregisterDeliveredReceiver();
                            break;
                    }
                }
            };

// ---when the SMS has been sent---
            for (int i = 0; i < partsCount; i++) {
                // ---when the SMS has been sent---
                PendingIntent sentPI = getBroadcast(mContext, 0, new Intent("SENT"), 0);
                mContext.registerReceiver(SentReceiver, new IntentFilter(SENT));
                sentPendings.add(sentPI);
                // ---when the SMS has been delivered---
                PendingIntent deliveredPI = getBroadcast(mContext, 0,new Intent("DELIVERED"), 0);
                mContext.registerReceiver(DeliveredReceiver, new IntentFilter(DELIVERED));
                deliveredPendings.add(deliveredPI);
            }

            //Toast.makeText(getActivity(),"Message Sent",Toast.LENGTH_LONG).show();
            // ---Notify when the SMS has been sent---
            //mContext.registerReceiver(sendSMS, new IntentFilter(SENT));

            // ---Notify when the SMS has been delivered---
            //mContext.registerReceiver(deliverSMS, new IntentFilter(DELIVERED));

            //ArrayList<String> messageParts = sms.divideMessage(strMessage);
            sms.sendMultipartTextMessage(strPhone, null, messageParts, sentPendings, deliveredPendings);
            //Toast.makeText(getActivity(), "The SMS was sent successfully", Toast.LENGTH_LONG).show();
            if (!phone.equals("")) {
                sms.sendMultipartTextMessage(strPhone2, null, messageParts, sentPendings, deliveredPendings);
                //Toast.makeText(getActivity(), "SMS failed, please try again later!", Toast.LENGTH_LONG).show();
            }
        }catch(Exception E){
        }
    }

    public void unregisterSentReceiver(){

        mContext.unregisterReceiver(SentReceiver);
    }

    public void unregisterDeliveredReceiver(){

        mContext.unregisterReceiver(DeliveredReceiver);
    }

}
