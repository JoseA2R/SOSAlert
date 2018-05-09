package com.acin.josefigueira.sosalert.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.acin.josefigueira.sosalert.R;

/**
 * Created by jose.figueira on 24-04-2018.
 */

public class SplashActivity extends AppCompatActivity {
    private boolean splashLoaded = false;
    private Intent goToMainActivity;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //setContentView(R.layout.splash_activity);
        if (!splashLoaded){

            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    startActivity(new Intent(SplashActivity.this,MainMenuActivity.class));
                    finish();
                }
            },secondsDelayed * 1500);
            splashLoaded = true;
        }else{
            goToMainActivity = new Intent(SplashActivity.this, MainMenuActivity.class);
            //goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}
