package com.hotb.pgmacdesign.californiaprototype.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parent class for CA Objects for error parsing
 * Created by pmacdowell on 2017-02-27.
 */

public class CAMasterObject {
    @SerializedName("Error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
