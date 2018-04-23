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
import android.view.WindowManager;
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

import static com.acin.josefigueira.sosalert.Fragments.SOSFragment.permissionsSnackbar;

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
    String fname,lname,country,description,phone;
    User user;
    int selectedCountry;
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
        etFName = view.findViewById(R.id.etFName);
        etLName = view.findViewById(R.id.etSName);
        spcountry = view.findViewById(R.id.spinnerCountry);
        etDescription = view.findViewById(R.id.etDescription);
        etPhone = view.findViewById(R.id.etPhone);
        btnNext  = view.findViewById(R.id.btnNext);
        user = new User();
        controller = new UserController(getActivity().getBaseContext());
        insertData();
        return view;
    }

    public void insertData(){

        setCountriesSpinner();
        fillData();
        //parentLinearLayout = (LinearLayout) view.findViewById(R.id.parent_linear_layout);
    /*    spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });*/
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnNextClicked();

                //Toast.makeText(getActivity().getBaseContext(),controller.viewData(), Toast.LENGTH_LONG).show();
                //fragmentManager.beginTransaction().replace(R.id.content_frame, new SOSFragment()).commit();
                //startActivity(new Intent(FormFragment.this,SOSActivity.class));
            }
        });
    }

    public void setCountriesSpinner(){

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item, controller.getLocales());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcountry.setAdapter(adapter);

    }

    /*public void onAddField(View v){
       /* if (numberOfRows == 0) {
            LinearLayout TextLayout = (LinearLayout) findViewById(R.id.phone);
            LayoutInflater inflating = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowPhoneView = inflating.inflate(R.layout.phone_number_text,null);
            parentLinearLayout.addView(rowPhoneView,parentLinearLayout.getChildCount() -1);

            numberOfRows++;

        }else {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.phone_field, null);
        parentLinearLayout.addView(v, parentLinearLayout.getChildCount() - 1);

        //}
    }*/

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }


    public void btnNextClicked(){

        register();

    }

    public void register(){
        initialize();
        if (!validate()){
            Toast.makeText(getActivity(),"Error Data",Toast.LENGTH_SHORT).show();
        }
        else{
            onDataInputSuccess();
        }
    }

    public void onDataInputSuccess(){

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragment = new SOSFragment();
        Toast.makeText(getActivity().getBaseContext(),"Profile Modified Successfully", Toast.LENGTH_LONG).show();
        controller.SaveData(user);
        controller.setData(fname,lname,country,description,phone);
        controller.putStringData(getActivity().getApplicationContext());
        //Revisar lo del id del contenedor para ser llamado luego
        fragmentTransaction.replace(R.id.content_frame,fragment);
        fragmentTransaction.detach(this);
        fragmentTransaction.commit();
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
        if(fname.isEmpty() || fname.length()>40 || !fname.matches("[a-zA-Zç\\p{L} ]+$")){
            System.out.println("\\w+");
            etFName.setError(getString(R.string.valid_fname));
            valid = false;
        }
        if(lname.isEmpty() || lname.length()>40 || !lname.matches("[a-zA-Zç\\p{L} ]+$")){
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
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        /*if (SOSFragment.permissionsSnackbar.isShown()) {
            SOSFragment.permissionsSnackbar.dismiss();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
