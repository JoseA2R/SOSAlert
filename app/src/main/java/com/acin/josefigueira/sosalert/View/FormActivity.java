package com.acin.josefigueira.sosalert.View;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.acin.josefigueira.sosalert.Fragments.SOSFragment;
import com.acin.josefigueira.sosalert.POJO.User;
import com.acin.josefigueira.sosalert.R;

/**
 * Created by jose.figueira on 26-03-2018.
 */

public class FormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Spinner spcountry;
    ArrayAdapter<String> adapter;
    int numberOfRows  = 0;
    Button btnNext;
    private LinearLayout parentLinearLayout;
    EditText etFName, etLName, etDescription, etPhone;
    Toolbar mToolbar;
    String fname,lname,country,description,phone,item;
    MainActivity main = new MainActivity();
    User user;
    UserController controller;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //main.loadLocales();
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

        /*CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Personal Data");
        collapsingToolbarLayout.setTitleEnabled(true);*/
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);

        /*spcountry.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countries));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcountry.setAdapter(adapter);*/

        setCountriesSpinner();

        spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i)+" selected",Toast.LENGTH_LONG).show();
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
                //Toast.makeText(getApplicationContext(),controller.viewData(), Toast.LENGTH_LONG).show();
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

        register();

    }

    public void register(){
        initialize();
        if (!validate()){
            Toast.makeText(this,"Error Data",Toast.LENGTH_SHORT).show();
        }
        else{
            onDataInputSuccess();
        }
    }

    public void onDataInputSuccess(){

        Toast.makeText(this,"Profile Modified Successfully", Toast.LENGTH_LONG).show();
        controller.SaveData(user);
        controller.setData(fname,lname,country,description,phone);
        controller.putStringData(this.getApplicationContext());
        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isfirstrun",false).apply();
        startActivity(new Intent(FormActivity.this,MainMenuActivity.class));

    }

    public void initialize(){

        fname = etFName.getText().toString();
        lname = etLName.getText().toString();
        country = spcountry.getSelectedItem().toString();
        description = etDescription.getText().toString();
        phone = etPhone.getText().toString();
    }

    public boolean validate(){
        boolean valid = true;
        if(fname.isEmpty() || fname.length()>40 || !fname.matches("[a-zA-Zç\\p{L} ]+$") || fname.trim() == ""){
            System.out.println("\\w+");
            etFName.setError(getString(R.string.valid_fname));
            valid = false;
        }
        if(lname.isEmpty() || lname.length()>40 || !lname.matches("[a-zA-Zç\\p{L} ]+$") || lname.trim() == ""){
            etLName.setError(getString(R.string.valid_lname));
            valid = false;
        }
        if(description.length()>350 || !description.matches("[- a-zA-Z0-9ç.',:¡!¿?()+\n\\p{L}]*")) {
            etDescription.setError(getString(R.string.valid_description));
            valid = false;
        }
        if(phone.length()>15 || !phone.matches("[0-9+]*")){
            etPhone.setError(getString(R.string.valid_phonenumber));
            valid = false;
        }

        return valid;
    }


    public void fillData(){

        etFName.setText(controller.getFName());
        etLName.setText(controller.getLName());
        country = controller.getCountry();
        spcountry.setSelection(adapter.getPosition(country));
        etDescription.setText(controller.getDescription());
        etPhone.setText(controller.getPhone());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = spcountry.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

