package com.hotb.pgmacdesign.californiaprototype.pojos;

import com.google.gson.annotations.SerializedName;
import com.hotb.pgmacdesign.californiaprototype.utilities.DateUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * This is a smaller, more lightweight version of the Place object. It is used for persistance
 * Created by pmacdowell on 2017-02-24.
 */
public class PlaceChosen {
    @SerializedName("placeId")
    private String placeId;
    @SerializedName("attributions")
    private String attributions;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("website")
    private String website;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("radius")
    private float radius;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public static CALocation convertToLocation(PlaceChosen placeChosen){
        if(placeChosen == null){
            return null;
        }

        String name = placeChosen.getName();
        if(StringUtilities.isNullOrEmpty(name)){
            name = placeChosen.getAddress();
        }
        if(StringUtilities.isNullOrEmpty(name)){
            name = placeChosen.getAttributions();
        }
        if(StringUtilities.isNullOrEmpty(name)){
            name = "Location: " + (DateUtilities.getCurrentDateLong() / 1000);
        }

        double lat = placeChosen.getLat();
        double lng = placeChosen.getLng();

        CALocation.Coordinates coordinates = new CALocation.Coordinates(lat, lng);

        float radiusInMeters = placeChosen.getRadius();
        float radiusInMiles = 0;
        if(radiusInMeters < 1610){
            radiusInMiles = 1;
        } else {
            radiusInMiles = (float)(radiusInMeters / 1609.344);
        }

        CALocation caLocation = new CALocation();
        caLocation.setAlertRadius("" + radiusInMiles);
        caLocation.setCoordinates(coordinates);
        caLocation.setDisplayName(name);
        caLocation.setEnableEmail(true);
        caLocation.setEnablePushNotifications(true);
        caLocation.setEnableSMS(true);
        return caLocation;
    }
}
