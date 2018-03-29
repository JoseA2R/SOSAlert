package com.acin.josefigueira.sosalert.Model;

import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jose.figueira on 27-03-2018.
 */

public class UserData {

    @SerializedName("_FName")
    private String _FirstName;
    @SerializedName("_LName")
    private String _LastName;
    @SerializedName("_Country")
    private String _Country;
    @SerializedName("_Description")
    private String _Description;
    @SerializedName("_Phone")
    private String _Phone;


    public UserData() {
        super();
    }

    public UserData(String firstName, String lastName, String country, String description, String phone) {

        _FirstName = firstName;
        _LastName = lastName;
        _Country = country;
        _Description = description;
        _Phone = phone;

    }

    public void storeData(){

        SharedPreferences userSharedPreferences;
        SharedPreferences.Editor editor;


    }

}
