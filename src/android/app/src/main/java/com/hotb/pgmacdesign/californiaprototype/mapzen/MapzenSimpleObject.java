package com.hotb.pgmacdesign.californiaprototype.mapzen;

import com.google.gson.annotations.SerializedName;

/**
 * Simple object for use in Listviews and whatnot
 * Created by pmacdowell on 2017-02-17.
 */

public class MapzenSimpleObject {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("simpleLocation")
    private String simpleLocation;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("distanceFromEnteredLocation")
    private Double distanceFromEnteredLocation;
    @SerializedName("country")
    private String country;
    @SerializedName("state")
    private String state;
    @SerializedName("stateAbbreviation")
    private String stateAbbreviation;
    @SerializedName("county")
    private String county;
    @SerializedName("city")
    private String city;
    @SerializedName("postalcode")
    private String postalcode;
    @SerializedName("neighbourhood")
    private String neighbourhood;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSimpleLocation() {
        return simpleLocation;
    }

    public void setSimpleLocation(String simpleLocation) {
        this.simpleLocation = simpleLocation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDistanceFromEnteredLocation() {
        return distanceFromEnteredLocation;
    }

    public void setDistanceFromEnteredLocation(Double distanceFromEnteredLocation) {
        this.distanceFromEnteredLocation = distanceFromEnteredLocation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }
}
