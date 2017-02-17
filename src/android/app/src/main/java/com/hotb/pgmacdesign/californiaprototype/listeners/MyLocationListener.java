package com.hotb.pgmacdesign.californiaprototype.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;

/**
 * Created by pmacdowell on 2017-02-15.
 */

public class MyLocationListener implements LocationListener {

    public static final String TAG = "MyLocationListener";

    public static interface LocationLoadedListener {
        public void locationTurnedOn(boolean bool);
        public void locationLoaded(Location location);
        public void locationError(String error);
    }

    private MyApplication sInstance;
    private LocationLoadedListener listener;

    //Constructor
    public MyLocationListener(MyApplication sInstance,  LocationLoadedListener listener){
        this.sInstance = sInstance;
        this.listener = listener;
    }

    public LocationLoadedListener getListener(){
        return this.listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            return;
        }

        sInstance.saveLocation(location);
        listener.locationLoaded(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        listener.locationTurnedOn(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        listener.locationTurnedOn(false);
    }
}
