package com.hotb.pgmacdesign.californiaprototype.utilities;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAMasterObject;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;
import com.hotb.pgmacdesign.californiaprototype.pojos.PlaceChosen;

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

    /**
     * Converter method to convert into a persistable, custom object
     * @param place {@link Place}
     * @return {@link PlaceChosen}
     */
    public static PlaceChosen convertPlaceToPlaceChosen(Place place){
        if(place == null){
            return null;
        }
        PlaceChosen placeChosen = new PlaceChosen();
        try {
            placeChosen.setAttributions(place.getAttributions().toString());
        } catch (NullPointerException npe){}
        try {
            placeChosen.setAddress(place.getAddress().toString());
        } catch (NullPointerException npe){}
        try {
            placeChosen.setLat(place.getLatLng().latitude);
        } catch (NullPointerException npe){}
        try {
            placeChosen.setLng(place.getLatLng().longitude);
        } catch (NullPointerException npe){}
        try {
            placeChosen.setName(place.getName().toString());
        } catch (NullPointerException npe){}
        try {
            placeChosen.setPhoneNumber(place.getPhoneNumber().toString());
        } catch (NullPointerException npe){}
        try {
            placeChosen.setWebsite(place.getWebsiteUri().toString());
        } catch (NullPointerException npe){}
        try {
            placeChosen.setPlaceId(place.getId());
        } catch (NullPointerException npe){}

        return placeChosen;
    }

    public static String checkErrorString(Object result) {
        String str = null;
        try {
            CAUser caUser = (CAUser) result;
            str = caUser.getError();
        } catch (Exception e){
            try {
                CAMasterObject caMasterObject = (CAMasterObject) result;
                str = caMasterObject.getError();
            } catch (Exception e1){
                e1.printStackTrace();
                str = MyApplication.getContext().getString(R.string.generic_error_text);
            }
        }
        return str;
    }
}
