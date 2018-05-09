package com.acin.josefigueira.sosalert.Classes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.ToneGenerator;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.Fragments.SOSFragment;

/**
 * Created by jose.figueira on 08-05-2018.
 */

public class SmsReceiver extends BroadcastReceiver {
    SMSController smsController = new SMSController();
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(smsController.mContext, "Message Sent", Toast.LENGTH_SHORT).show();
                //toneBeep.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 300);
                smsController.unregisterSentReceiver();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(smsController.mContext, "Generic failure", Toast.LENGTH_SHORT).show();
                smsController.unregisterSentReceiver();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(smsController.mContext, "     No Service\n" +
                        "Message NOT Sent", Toast.LENGTH_SHORT).show();
                smsController.unregisterSentReceiver();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(smsController.mContext, "Null PDU", Toast.LENGTH_SHORT).show();
                smsController.unregisterSentReceiver();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(smsController.mContext, "        Radio Off\n" +
                        "Message NOT Sent", Toast.LENGTH_SHORT).show();
                smsController.unregisterSentReceiver();
                break;
        }
    }
}
