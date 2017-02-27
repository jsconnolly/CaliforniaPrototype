package com.hotb.pgmacdesign.californiaprototype.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * This class will likely change once back-end has working code for me to match data model to
 * Created by pmacdowell on 2017-02-23.
 */
public class AlertBeacon {

    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    @SerializedName("alertInformation")
    private String alertInformation;
    @SerializedName("circleRadius")
    private double circleRadius;
    @SerializedName("circleId")
    private String circleId;

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAlertInformation() {
        return alertInformation;
    }

    public void setAlertInformation(String alertInformation) {
        this.alertInformation = alertInformation;
    }

    public double getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(double circleRadius) {
        this.circleRadius = circleRadius;
    }
}
