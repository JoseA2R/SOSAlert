package com.acin.josefigueira.sosalert.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.R;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by jose.figueira on 26-03-2018.
 */

public class FormActivity extends AppCompatActivity {


    Spinner spcountry;
    ArrayAdapter<String> adapter;
    int numberOfRows  = 0;
    private LinearLayout parentLinearLayout;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);
        spcountry = findViewById(R.id.spinnerCountry);

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);

        setCountriesSpinner();

        spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void setCountriesSpinner(){

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcountry.setAdapter(adapter);

    }

    public void onAddField(View v){
       /* if (numberOfRows == 0) {
            LinearLayout TextLayout = (LinearLayout) findViewById(R.id.phone);
            LayoutInflater inflating = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowPhoneView = inflating.inflate(R.layout.phone_number_text,null);
            parentLinearLayout.addView(rowPhoneView,parentLinearLayout.getChildCount() -1);

            numberOfRows++;

        }else {*/

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.phone_field, null);
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);

        //}
    }

    public void onDelete(View v) {



        parentLinearLayout.removeView((View) v.getParent());
    }

    public void btnNextClicked(View v){

        EditText etFName = (EditText) findViewById(R.id.etFName);
        EditText etLName = (EditText) findViewById(R.id.etSName);
        spcountry = findViewById(R.id.spinnerCountry);
        EditText etDescription = (EditText) findViewById(R.id.etDescription);
        EditText etPhone = (EditText) findViewById(R.id.etPhone);

        String FName = etFName.getText().toString();
        String LName = etLName.getText().toString();
        String Country = spcountry.getSelectedItem().toString();
        String Description = etDescription.getText().toString();
        String Phone = etFName.getText().toString();


    }

}

