package com.acin.josefigueira.sosalert.View;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.acin.josefigueira.sosalert.R;

/**
 * Created by jose.figueira on 09-04-2018.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.initial_mainpage);

        spinner = (Spinner) findViewById(R.id.language_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.planets_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //((TextView) parent.getChildAt(pos)).setTextColor(Color.WHITE);
        startActivity(new Intent(MainActivity.this,WelcomeMainActivity.class));
    }

    public void onNothingSelected(AdapterView<?> parent){

    }

}
