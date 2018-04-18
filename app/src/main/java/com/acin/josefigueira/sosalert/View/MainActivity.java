package com.acin.josefigueira.sosalert.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.acin.josefigueira.sosalert.R;

import java.util.Locale;

/**
 * Created by jose.figueira on 09-04-2018.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .getBoolean("isfirstrun",true);

        if (!isFirstRun) {
            startActivity(new Intent(MainActivity.this,MainMenuActivity.class));
        }
        setContentView(R.layout.initial_mainpage);

        spinner = (Spinner) findViewById(R.id.language_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.languages_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //((TextView) parent.getChildAt(pos)).setTextColor(Color.WHITE);
        item = spinner.getSelectedItem().toString();
        switch(pos){
            case 1:

            case 2:
        }
        if (item.equals("English")) {
            setLocales("en");
            //recreate();
            startActivity(new Intent(MainActivity.this, WelcomeMainActivity.class));
        }
        else if (item.equals("Portuguese")){
            setLocales("pt");
            //recreate();
            startActivity(new Intent(MainActivity.this, WelcomeMainActivity.class));
        }
    }

    public void onNothingSelected(AdapterView<?> parent){

    }

    private void setLocales(String lang){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("lang",lang);
        editor.apply();

    }

    public void loadLocales(){
        SharedPreferences SPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = SPreferences.getString("lang","");
        setLocales(language);
    }


}


