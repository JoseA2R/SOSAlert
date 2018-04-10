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
    private String phone;
    @SerializedName("latitude")
    @Expose
    private float latitude;
    @SerializedName("longitude")
    @Expose
    private float longitude;

    public String getFirstName() {return first_name; }

    public void setFirstName(String first_name) { this.first_name = first_name;}

    public String getLastName() { return last_name; }

    public void setLastname(String last_name) { this.last_name = last_name; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getDescription(){return description; }

    public void setDescription(String description){ this.description = description;  }

    public String getPhone(){return phone; }

    public void setPhone(String phone){ this.phone = phone; }

    public float getLatitude(){return latitude; }

    public void setLatitude(float latitude){ this.latitude = latitude; }

    public float getLongitude(){return longitude; }

    public void setLongitude(float longitude){ this.longitude = longitude; }
}

