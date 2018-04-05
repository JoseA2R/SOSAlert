package com.acin.josefigueira.sosalert.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.acin.josefigueira.sosalert.POJO.User;
import com.acin.josefigueira.sosalert.View.FormActivity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by jose.figueira on 27-03-2018.
 */

public class UserModel {

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


    public UserModel() {
        super();
    }

    public UserModel(String firstName, String lastName, String country, String description, String phone) {

        _FirstName = firstName;
        _LastName = lastName;
        _Country = country;
        _Description = description;
        _Phone = phone;

    }

}
