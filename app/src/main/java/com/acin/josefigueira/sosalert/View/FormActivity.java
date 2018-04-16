package com.acin.josefigueira.sosalert.View;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Controller.UserController;
import com.acin.josefigueira.sosalert.POJO.User;
import com.acin.josefigueira.sosalert.R;

/**
 * Created by jose.figueira on 26-03-2018.
 */

public class FormActivity extends AppCompatActivity {


    Spinner spcountry;
    ArrayAdapter<String> adapter;
    int numberOfRows  = 0;
    Button btnNext;
    private LinearLayout parentLinearLayout;
    EditText etFName, etLName, etDescription, etPhone;
    String fname,lname,country,description,phone;
    User user;
    UserController controller;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_userdata);

        user = new User();
        controller = new UserController(this);

        spcountry = findViewById(R.id.spinnerCountry);
        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etSName);
        spcountry = findViewById(R.id.spinnerCountry);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etPhone = (EditText) findViewById(R.id.etPhone);
        btnNext  = findViewById(R.id.btnNext);

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

        btnNext.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnNextClicked();
                //controller.SaveData(user);
                Toast.makeText(getApplicationContext(),controller.viewData(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(FormActivity.this,MainMenuActivity.class));

            }
        });


    }

    public void setCountriesSpinner(){

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, controller.getLocales());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcountry.setAdapter(adapter);

    }

    public void onAddField(View v){
        /*if (numberOfRows == 0) {
            LinearLayout TextLayout = (LinearLayout) findViewById(R.id.phone);
            LayoutInflater inflating = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowPhoneView = inflating.inflate(R.layout.phone_number_text,null);
            parentLinearLayout.addView(rowPhoneView,parentLinearLayout.getChildCount() -1);

        }else {*/
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.phone_field, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        numberOfRows++;
       // }
    }

    public void onDelete(View v) {

        parentLinearLayout.removeView((View) v.getParent());
        numberOfRows--;
    }

    public void btnNextClicked(){

        /*SharedPreferences SPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editorPreferences = SPreferences.edit();*/

        fname = etFName.getText().toString();
        lname = etLName.getText().toString();
        country = spcountry.getSelectedItem().toString();
        description = etDescription.getText().toString();
        phone = etPhone.getText().toString();

        controller.setData(fname,lname,country,description,phone);

        controller.putStringData(this);

        /*editorPreferences.putString("First Name: ",user.getFirstName());
        editorPreferences.putString("Last Name: ",user.getLastName());
        editorPreferences.putString("Country : ",user.getCountry());
        editorPreferences.putString("Description : ",user.getDescription());
        editorPreferences.putString("Phone Number : ",user.getPhone());
        editorPreferences.apply();*/

    }

}

