package com.acin.josefigueira.sosalert.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jose.figueira on 05-04-2018.
 */

public class GpsData {

    @SerializedName("latitude")
    @Expose
    private float latitude;

    @SerializedName("longitude")
    @Expose
    private float longitude;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
