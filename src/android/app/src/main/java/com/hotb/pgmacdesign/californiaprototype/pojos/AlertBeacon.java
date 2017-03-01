package com.hotb.pgmacdesign.californiaprototype.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * This class will likely change once back-end has working code for me to match data model to
 * Created by pmacdowell on 2017-02-23.
 */
public class AlertBeacon {

    @SerializedName("location")
    private CALocation location;
    @SerializedName("user")
    private CAUser user;
    @SerializedName("alert")
    private CAAlert alert;

    public CALocation getLocation() {
        return location;
    }

    public void setLocation(CALocation location) {
        this.location = location;
    }

    public CAUser getUser() {
        return user;
    }

    public void setUser(CAUser user) {
        this.user = user;
    }

    public CAAlert getAlert() {
        return alert;
    }

    public void setAlert(CAAlert alert) {
        this.alert = alert;
    }
}
