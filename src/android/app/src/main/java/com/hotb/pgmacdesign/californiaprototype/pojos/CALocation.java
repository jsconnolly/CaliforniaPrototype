package com.hotb.pgmacdesign.californiaprototype.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Custom location object mirroring the api
 * Created by pmacdowell on 2017-02-27.
 */
public class CALocation extends CAMasterObject{

    @SerializedName("displayName")
    private String displayName;
    @SerializedName("alertRadius")
    private String alertRadius;  // In Miles, as per meeting with Jason on: 2017-02-27 15:00
    @SerializedName("enablePushNotifications")
    private Boolean enablePushNotifications;
    @SerializedName("enableSMS")
    private Boolean enableSMS;
    @SerializedName("enableEmail")
    private Boolean enableEmail;
    @SerializedName("coordinates")
    private Coordinates coordinates;
    //Intentionally not serialized
    private String circleId;

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAlertRadius() {
        return alertRadius;
    }

    public void setAlertRadius(String alertRadius) {
        this.alertRadius = alertRadius;
    }

    public Boolean getEnablePushNotifications() {
        if(enablePushNotifications == null){
            enablePushNotifications = false;
        }
        return enablePushNotifications;
    }

    public void setEnablePushNotifications(Boolean enablePushNotifications) {
        this.enablePushNotifications = enablePushNotifications;
    }

    public Boolean getEnableSMS() {
        if(enableSMS == null){
            enableSMS = false;
        }
        return enableSMS;
    }

    public void setEnableSMS(Boolean enableSMS) {
        this.enableSMS = enableSMS;
    }

    public Boolean getEnableEmail() {
        if(enableEmail == null){
            enableEmail = false;
        }
        return enableEmail;
    }

    public void setEnableEmail(Boolean enableEmail) {
        this.enableEmail = enableEmail;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public static class Coordinates {

        public Coordinates(){}

        public Coordinates(double lat, double lng){
            this.lat = lat;
            this.lng = lng;
        }

        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;

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
    }
}
