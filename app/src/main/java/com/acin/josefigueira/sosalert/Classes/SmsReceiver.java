package com.acin.josefigueira.sosalert.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.Fragments.SOSFragment;

/**
 * Created by jose.figueira on 08-05-2018.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intents = new Intent(context, SMSController.class);
        context.startService(intents);
    }
}
