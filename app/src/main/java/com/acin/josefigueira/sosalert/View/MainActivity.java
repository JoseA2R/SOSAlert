package com.acin.josefigueira.sosalert.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.acin.josefigueira.sosalert.Classes.Languages;
import com.acin.josefigueira.sosalert.R;

import java.util.Locale;

/**
 * Created by jose.figueira on 09-04-2018.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String item;
    Button _btn;
    Languages languages;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .getBoolean("isfirstrun",true);

        if (!isFirstRun) {
            if(android.os.Build.VERSION.SDK_INT > 23) {
                intent = new Intent(MainActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else{
                intent = new Intent(MainActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setContentView(R.layout.initial_mainpage);
            spinner = (Spinner) findViewById(R.id.language_spinner);
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages_array, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }else{
            setContentView(R.layout.initial_mainpage_19);
        }

        _btn = findViewById(R.id.select_language_btn);

        languages = new Languages();
        languages.Languages(this);

        _btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fragmentManager.beginTransaction().replace(R.id.content_frame, new FormFragment()).commit();
                //Iniciar actividad con el FormActivity

                startActivity(new Intent(MainActivity.this,FormActivity.class));
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //((TextView) parent.getChildAt(pos)).setTextColor(Color.WHITE);
        //((TextView)parent.getChildAt(pos)).setTextColor(Color.parseColor("#FFFFFF"));
        item = spinner.getSelectedItem().toString();
        if (item.contains("English") || item.contains("Inglês")) {
            languages.setLocales("en");
            //recreate();
            //startActivity(new Intent(MainActivity.this, WelcomeMainActivity.class));
        }
        else if (item.contains("Portuguese") || item.contains("Português")){
            languages.setLocales("pt");
            //recreate();
            //startActivity(new Intent(MainActivity.this, WelcomeMainActivity.class));
        }
    }

    public void onNothingSelected(AdapterView<?> parent){

    }




}


