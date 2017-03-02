package com.hotb.pgmacdesign.californiaprototype.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Alerts class
 * Created by pmacdowell on 2017-02-28.
 */

public class CAAlert {

    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("date")
    private String date; //Date object in format matching default date in constants
    @SerializedName("loc")
    private double[] loc; //Order is Longitude, latitude (0, 1)
    @SerializedName("location")
    private String location;
    //Not serialized on purpose
    private String circleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double[] getLoc() {
        return loc;
    }

    public void setLoc(double[] loc) {
        this.loc = loc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
}
