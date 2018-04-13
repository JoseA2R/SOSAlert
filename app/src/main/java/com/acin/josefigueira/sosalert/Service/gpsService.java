package com.acin.josefigueira.sosalert.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jose.figueira on 13-04-2018.
 */

public class gpsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

}
