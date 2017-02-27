package com.hotb.pgmacdesign.californiaprototype.utilities;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

/**
 * Collection of Map Utilities (IE Google Maps)
 * Created by pmacdowell on 2017-02-24.
 */
public class MapUtilities  {


    private static final String SCALEBAR_LOCATION_PART_1 = "ScaleBar location p1";
    private static final String SCALEBAR_LOCATION_PART_2 = "ScaleBar location p2";

    /**
     * Calculates the float for meters per inch on the screen (of the map) at the time this
     * method is called.
     * @param context Context
     * @param map Map to check against
     * @param googleMapView The view for the map in the xml. This could be obtained like this:
     *                      getChildFragmentManager().findFragmentById(R.id.fragment_map_map).getView();
     * @return
     */
    public static float getMetersPerInch(Context context, GoogleMap map, View googleMapView){
        if(googleMapView == null || map == null || context == null){
            return 0;
        }

        Projection projection = map.getProjection();
        DisplayManagerUtilities dmu = new DisplayManagerUtilities(context);
        float xdpi = dmu.getXdpi();
        View view = googleMapView;

        int point1, point2;
        point1 = (int) (((view.getWidth() / 2) - (xdpi / 2)));
        point2 =  (int) (view.getHeight() / 2);
        LatLng p1 = projection.fromScreenLocation(
                new Point(point1, point2));

        int point3, point4;
        point3 = (int) (((view.getWidth() / 2) + (xdpi / 2)));
        point4 =  (int) (view.getHeight() / 2);
        LatLng p2 = projection.fromScreenLocation(
                new Point(point3, point4));

        Location locationP1 = new Location(SCALEBAR_LOCATION_PART_1);
        Location locationP2 = new Location(SCALEBAR_LOCATION_PART_2);

        locationP1.setLatitude(p1.latitude);
        locationP2.setLatitude(p2.latitude);
        locationP1.setLongitude(p1.longitude);
        locationP2.setLongitude(p2.longitude);
        float xMetersPerInch = locationP1.distanceTo(locationP2);
        //If happy with 1 inch on screen, return, else, multiply by a % and return
        return xMetersPerInch;
    }
}
