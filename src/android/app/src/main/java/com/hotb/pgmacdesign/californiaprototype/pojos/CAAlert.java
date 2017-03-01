package com.hotb.pgmacdesign.californiaprototype.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pmacdowell on 2017-02-28.
 */

public class CAAlert {
    //TBD

    @SerializedName("circleId")
    private String circleId;

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
}
