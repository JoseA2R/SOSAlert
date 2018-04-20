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

import com.acin.josefigueira.sosalert.Classes.Languages;
import com.acin.josefigueira.sosalert.R;

import java.util.Locale;

/**
 * Created by jose.figueira on 09-04-2018.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String item;
    Languages languages;

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
        languages = new Languages();
        languages.Languages(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //((TextView) parent.getChildAt(pos)).setTextColor(Color.WHITE);
        item = spinner.getSelectedItem().toString();
        if (item.equals("English")) {
            languages.setLocales("en");
            //recreate();
            startActivity(new Intent(MainActivity.this, WelcomeMainActivity.class));
        }
        else if (item.equals("Portuguese")){
            languages.setLocales("pt");
            //recreate();
            startActivity(new Intent(MainActivity.this, WelcomeMainActivity.class));
        }
    }

    public void onNothingSelected(AdapterView<?> parent){

    }




}


