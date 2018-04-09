package com.acin.josefigueira.sosalert.View;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.acin.josefigueira.sosalert.R;

public class WelcomeMainActivity extends AppCompatActivity {

    Button _btn;
    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_mainpage);

        _btn = findViewById(R.id.app_start_btn);

       _btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fragmentManager.beginTransaction().replace(R.id.content_frame, new FormFragment()).commit();
                //Iniciar actividad con el FormActivity
                startActivity(new Intent(WelcomeMainActivity.this,FormActivity.class));
            }
        });

    }

    public void onClick(View view) {



    }



}
