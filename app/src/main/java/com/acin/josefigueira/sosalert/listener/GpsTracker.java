package com.acin.josefigueira.sosalert.listener;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jose.figueira on 12-04-2018.
 */

public class GpsTracker extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
