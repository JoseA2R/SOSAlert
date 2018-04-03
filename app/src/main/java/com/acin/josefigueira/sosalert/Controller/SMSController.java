package com.acin.josefigueira.sosalert.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.R;
import com.acin.josefigueira.sosalert.View.SOSActivity;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class SMSController {

    private static final int SMS_PERMISSION_CODE = 123;
    private Context context;
    private Activity activity;

    public SMSController(Context context){

        context = this.context;
        showRequestPermissionsInfoAlertDialog();

    }

    public void sendTextMessage(){

        String strPhone = "+351965639423";

        String strMessage = "Lorem\nIpsum";


        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(strPhone, null, strMessage, null, null);
        Toast.makeText(this.context, "Sent", Toast.LENGTH_SHORT).show();
    }


    /**
     * Check if we have SMS permission
     */
    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this.context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime SMS permission
     * @param onClickListener
     */
    private void requestReadAndSendSmsPermission(DialogInterface.OnClickListener onClickListener) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html

        }
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    public void showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true);
    }

    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        builder.setMessage(R.string.permission_dialog_message); // Your own message

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request?
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission(this);
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
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
        }
    }

}
