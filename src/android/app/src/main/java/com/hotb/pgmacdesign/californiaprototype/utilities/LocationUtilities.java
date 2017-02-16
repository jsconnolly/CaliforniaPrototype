package com.hotb.pgmacdesign.californiaprototype.utilities;

import android.content.Context;
import android.location.LocationManager;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.MyLocationListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;

/**
 * This class works with the location API to
 * Created by pmacdowell on 2017-01-18.
 */
public class LocationUtilities {

    /**
     * Listener to start listening for location updates. This assumes that permission has already
     * been granted. Make sure to request it before making this static call
     * @param context Context
     * @param listener Listener to send data back on
     */
    public static void startListeningForLocation(Context context, MyLocationListener listener){

        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    (Constants.LOCATION_TIME_PERIOD_GAP),
                    Constants.LOCATION_MINIMUM_DISTANCE, listener);

        } catch (SecurityException se){
            se.printStackTrace();
            listener.getListener().locationError(context.getString(R.string.gps_must_be_enabled));
        }
    }
}
