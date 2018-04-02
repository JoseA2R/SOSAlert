package com.acin.josefigueira.sosalert.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.acin.josefigueira.sosalert.POJO.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by jose.figueira on 28-03-2018.
 */

public class UserController {

    private User user;

    public UserController(){

        this.user = new User();

    }

    public void SaveData(User user){
        this.user = user;
    }

    public void ClearUser(){

        this.user.setFirstName("");
        this.user.setLastname("");
        this.user.setCountry("");
        this.user.setDescription("");
        this.user.setPhone("");

    }



    public String viewData(){

        String userdata = "";
        userdata += "First Name = " + this.user.getFirstName() + "\n" +
                "Last Name = " + this.user.getLastName() + "\n" +
                "Country = " + this.user.getCountry() + "\n" +
                "Description = " + this.user.getDescription() + "\n" +
                "Phone = " + this.user.getPhone() + "\n";

        return userdata;

    }

    public ArrayList<String> getLocales(){

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country)){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        return countries;

    }

    public void putStringData(Context context){

        SharedPreferences SPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editorPreferences = SPreferences.edit();

        editorPreferences.putString("First Name: ",this.user.getFirstName());
        editorPreferences.putString("Last Name: ",this.user.getLastName());
        editorPreferences.putString("Country : ",this.user.getCountry());
        editorPreferences.putString("Description : ",this.user.getDescription());
        editorPreferences.putString("Phone Number : ",this.user.getPhone());
        editorPreferences.apply();

    }


}
