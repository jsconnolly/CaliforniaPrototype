package com.hotb.pgmacdesign.californiaprototype.utilities;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * Created by pmacdowell on 2017-02-23.
 */

public class CaliforniaPrototypeCustomUtils {

    public static LatLng convertToLatLng(double lat, double lng){
        return new LatLng(lat, lng);
    }

    public static boolean isMarkerOnList(List<Marker> markers, Marker newMarker){
        if(MiscUtilities.isListNullOrEmpty(markers)){
            return false;
        }
        String id = newMarker.getId();
        String title = newMarker.getTitle();
        double lat = 0, lng = 0;
        try {
            lat = newMarker.getPosition().latitude;
            lng = newMarker.getPosition().longitude;
        } catch (Exception e){
            e.printStackTrace();
        }
        for(Marker marker : markers){
            try {
                String oldId = marker.getId();
                String oldTitle = marker.getTitle();
                double oldLat = marker.getPosition().latitude;
                double oldLng = marker.getPosition().longitude;

                if(oldId.equals(id)){
                    return true;
                }

                if(lat == oldLat && lng == oldLng && title.equals(oldTitle)){
                    return true;
                }

            } catch (Exception e){
                //In the odd event that the server sends back coords without lat / lng
                e.printStackTrace();
            }
        }
        return false;
    }
}
