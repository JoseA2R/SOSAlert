package com.acin.josefigueira.sosalert.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.acin.josefigueira.sosalert.View.FormActivity;
import com.acin.josefigueira.sosalert.View.SOSActivity;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class FormFragment extends Fragment{

    private View view;

    Spinner spcountry;
    ArrayAdapter<String> adapter;
    int numberOfRows  = 0;
    Button btnNext;
    private LinearLayout parentLinearLayout;
    EditText etFName, etLName, etDescription, etPhone;
    User user;
    UserController controller;


    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment}
        view = inflater.inflate(R.layout.activity_userdata,container,false);
        insertData(view);
        return view;
    }

    public void insertData(View view){

        controller = new UserController(getActivity().getBaseContext());

        spcountry = view.findViewById(R.id.spinnerCountry);
        etFName = (EditText) view.findViewById(R.id.etFName);
        etLName = (EditText) view.findViewById(R.id.etSName);
        spcountry = view.findViewById(R.id.spinnerCountry);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        btnNext  = view.findViewById(R.id.btnNext);

        fillData();

        parentLinearLayout = (LinearLayout) view.findViewById(R.id.parent_linear_layout);

        setCountriesSpinner();

        spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getActivity().getBaseContext(),adapterView.getItemAtPosition(i)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnNextClicked();
                controller.SaveData(user);
                Toast.makeText(getActivity().getApplicationContext(),controller.viewData(), Toast.LENGTH_LONG).show();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new SOSFragment();
                //Revisar lo del id del contenedor para ser llamado luego
                //fragmentTransaction.replace(R.id.);
                fragmentTransaction.commit();

                //startActivity(new Intent(FormFragment.this,SOSActivity.class));

            }
        });

    }

    public void setCountriesSpinner(){

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item, controller.getLocales());
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

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.phone_field, null);
        parentLinearLayout.addView(v, parentLinearLayout.getChildCount() - 1);

        //}
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    public void btnNextClicked(){

        /*SharedPreferences SPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editorPreferences = SPreferences.edit();*/

        user.setFirstName(etFName.getText().toString());
        user.setLastname(etLName.getText().toString());
        user.setCountry(spcountry.getSelectedItem().toString());
        user.setDescription(etDescription.getText().toString());
        user.setPhone(etPhone.getText().toString());

        controller.putStringData(getActivity().getApplicationContext());

        /*editorPreferences.putString("First Name: ",user.getFirstName());
        editorPreferences.putString("Last Name: ",user.getLastName());
        editorPreferences.putString("Country : ",user.getCountry());
        editorPreferences.putString("Description : ",user.getDescription());
        editorPreferences.putString("Phone Number : ",user.getPhone());
        editorPreferences.apply();*/

    }

    public void fillData(){

        etFName.setText(controller.getFName());
        etLName.setText(controller.getLName());
        etDescription.setText(controller.getDescription());
        etPhone.setText(controller.getPhone());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
