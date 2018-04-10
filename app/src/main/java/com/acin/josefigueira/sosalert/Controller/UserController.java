package com.acin.josefigueira.sosalert.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.acin.josefigueira.sosalert.Model.UserModel;
import com.acin.josefigueira.sosalert.POJO.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by jose.figueira on 28-03-2018.
 */

public class UserController {

    private User user;
    UserModel model;
    SharedPreferences SPreferences;
    SharedPreferences.Editor editorPreferences;
    Context mContext;

    public UserController(Context context){

        this.user = new User();
        mContext = context;

    }

    public void SaveData(User user){
        this.user = user;
    }

    public void ClearPreferences(){


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

    public void setData(String fname,String lname, String country, String description, String phone){

        user.setFirstName(fname);
        user.setLastname(lname);
        user.setCountry(country);
        user.setDescription(description);
        user.setPhone(phone);

    }

   /* public void setLocationData(double latitude, double longitude){
        user.setLatitude(latitude);
        user.setLongitude(longitude);

    }*/

    public void putStringData(Context context){

        SPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editorPreferences = SPreferences.edit();

        editorPreferences.putString("firstname",this.user.getFirstName());
        editorPreferences.putString("lastname",this.user.getLastName());
        editorPreferences.putString("country",this.user.getCountry());
        editorPreferences.putString("description",this.user.getDescription());
        editorPreferences.putString("phonenumber",this.user.getPhone());
        editorPreferences.apply();

    }

    public void putLocation(Context context,float lat, float lon){
        SPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editorPreferences = SPreferences.edit();

        editorPreferences.putFloat("latitude",lat);
        editorPreferences.putFloat("longitude",lon);
        editorPreferences.apply();
    }

   public String getUserFName(){
        return user.getFirstName();
    }

    public String getUserLName(){
        return user.getLastName();
    }

    public String getUserCountry(){
        return user.getCountry();
    }

    public String getUserDescription(){
        return user.getDescription();
    }

    public String getUserPhone(){
        return user.getPhone();
    }

    public String getFName(){
        SPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return SPreferences.getString("firstname","");
    }

    public String getLName(){
        SPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return SPreferences.getString("lastname","");
    }
    public String getCountry(){
        SPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return SPreferences.getString("country","");
    }
    public String getDescription(){
        SPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return SPreferences.getString("description","");
    }
    public String getPhone(){
        SPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return SPreferences.getString("phonenumber","");
    }


}
