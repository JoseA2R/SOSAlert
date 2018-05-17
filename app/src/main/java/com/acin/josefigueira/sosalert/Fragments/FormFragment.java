package com.acin.josefigueira.sosalert.Fragments;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.acin.josefigueira.sosalert.Classes.KeyboardUtil;
import com.acin.josefigueira.sosalert.Classes.Languages;
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
    String getTVPersonalData,getHintFName,getHintLName,getHintDescription,getHintPhone,getTextNext;
    User user;
    int selectedCountry;
    UserController controller;
    KeyboardUtil keyboardUtil;
    Languages languages;
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

        user = new User();
        controller = new UserController(getActivity().getBaseContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            view = inflater.inflate(R.layout.activity_userdata, container, false);


            etFName = view.findViewById(R.id.etFName);
            etLName = view.findViewById(R.id.etSName);
            spcountry = view.findViewById(R.id.spinnerCountry);
            etDescription = view.findViewById(R.id.etDescription);
            etPhone = view.findViewById(R.id.etPhone);
            btnNext = view.findViewById(R.id.btnNext);

            final View activityRootView = view.findViewById(R.id.coordinatorlayout);
            activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    //r will be populated with the coordinates of your view that area still visible.
                    activityRootView.getWindowVisibleDisplayFrame(r);

                    int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                    if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                        btnNext.setPadding(0, 0, 0, 10);
                    } else {
                        btnNext.setPadding(0, 10, 0, 0);
                    }
                }
            });

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);


            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout);
            appBarLayout.setPadding(0, (int) px, 0, 0);

        /*if(((AppCompatActivity)getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        /*etFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });*/

        }else{

            getTVPersonalData = getResources().getString(R.string.tv_personal_data);
            getHintFName = getResources().getString(R.string.first_names);
            getHintLName = getResources().getString(R.string.last_names);
            getHintDescription = getResources().getString(R.string.edit_comments);
            getHintPhone = getResources().getString(R.string.phone_number);
            getTextNext = getResources().getString(R.string.next_btn);

            languages = new Languages();
            languages.Languages(getActivity());
            languages.loadLocales();

            view = inflater.inflate(R.layout.activity_userdata_19, container, false);

            etFName = view.findViewById(R.id.etFName_19);
            etLName = view.findViewById(R.id.etSName_19);
            spcountry = view.findViewById(R.id.spinnerCountry_19);
            etDescription = view.findViewById(R.id.etDescription_19);
            etPhone = view.findViewById(R.id.etPhone_19);
            btnNext = view.findViewById(R.id.btnNext_19);

            /*etFName.setHint(getHintFName);
            etLName.setHint(getHintLName);
            etDescription.setHint(getHintDescription);
            etPhone.setHint(getHintPhone);
            btnNext.setHint(getTextNext);*/

        }

        insertData();
        return view;
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
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
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.spinner_selected_black, controller.getLocales());
        adapter.setDropDownViewResource(R.layout.spinner_item_black);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void btnNextClicked(){

        register();

    }
    public void register(){
        //initialize();
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
        fname = etFName.getText().toString().trim();
        lname = etLName.getText().toString().trim();
        country = spcountry.getSelectedItem().toString();
        description = etDescription.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
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
