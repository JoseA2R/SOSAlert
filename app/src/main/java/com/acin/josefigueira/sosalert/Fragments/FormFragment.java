package com.acin.josefigueira.sosalert.Fragments;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
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
import android.support.v7.widget.Toolbar;

import com.acin.josefigueira.sosalert.Classes.KeyboardUtil;
import com.acin.josefigueira.sosalert.Controller.UserController;
import com.acin.josefigueira.sosalert.POJO.User;
import com.acin.josefigueira.sosalert.R;
import com.acin.josefigueira.sosalert.View.FormActivity;

import static com.acin.josefigueira.sosalert.Fragments.SOSFragment.permissionsSnackbar;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class FormFragment extends Fragment implements AdapterView.OnItemSelectedListener {

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
    KeyboardUtil keyboardUtil;

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

        /*ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams( 0, 0 );
        layoutParams.setMargins(0,25,0,0);*/
        view = inflater.inflate(R.layout.activity_userdata,container,false);
        etFName = view.findViewById(R.id.etFName);
        etLName = view.findViewById(R.id.etSName);
        spcountry = view.findViewById(R.id.spinnerCountry);
        etDescription = view.findViewById(R.id.etDescription);
        etPhone = view.findViewById(R.id.etPhone);
        btnNext  = view.findViewById(R.id.btnNext);
        user = new User();

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, r.getDisplayMetrics());
        AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout);
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        appBarLayout.setPadding(0,(int)px,0,0);

        /*if(((AppCompatActivity)getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        controller = new UserController(getActivity().getBaseContext());
        insertData();
        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }*/


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        keyboardUtil = new KeyboardUtil(getActivity(),
                ((ViewGroup) view.findViewById(android.R.id.content)).getChildAt(0));

        if ((newConfig.keyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO))
            Toast.makeText(getActivity(), "soft keyboard visible", Toast.LENGTH_SHORT).show();
        else if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
            Toast.makeText(getActivity(), "soft keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertData(){

        setCountriesSpinner();
        fillData();
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnNextClicked();
            }
        });
    }

    public void setCountriesSpinner(){

        /*spcountry.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countries));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcountry.setAdapter(adapter);*/
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.spinner_item_black, controller.getLocales());
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
