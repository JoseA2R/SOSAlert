package com.acin.josefigueira.sosalert.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jose.figueira on 27-03-2018.
 */

public class User {

    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("phone")
    @Expose
    private ArrayList<String> phone;

    public String getFirstName() {return first_name; }

    public void setFirstName(String first_name) { this.first_name = first_name;}

    public String getLastName() { return last_name; }

    public void setLastname() { this.last_name = last_name; }

    public String getCountry() { return country; }

    public void setCountry() { this.country = country; }

    public String getDescription(){return description; }

    public void setDescription(){ this.description = description; }



}

