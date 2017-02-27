package com.hotb.pgmacdesign.californiaprototype.geocoding;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenAPICalls;
import com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenInterface;
import com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenPOJO;
import com.hotb.pgmacdesign.californiaprototype.networking.RetrofitClient;
import com.hotb.pgmacdesign.californiaprototype.utilities.NetworkUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenAPICalls.TAG_GEOCODING_CALL_ERROR;

/**
 * This is a Rest API to interface with the Mapzen back-end. Calls can be made here to return
 * lists MapzenPOJO objects {@link MapzenPOJO}, which contain location data.
 * This utilizes {@link MapzenInterface} and {@link RetrofitClient} for calls
 * Mapzen Documentation - https://mapzen.com/documentation/search/reverse/
 * Created by pmacdowell on 2017-02-17.
 */
public class GeocodingAPICalls {

    private Context context;
    private RetrofitClient retrofitClient;
    private GeocodingInterface geocodingInterface;
    private String ApiKey;
    private OnTaskCompleteListener listener;

    //Final Var Strings and Int Tags
    private static final String URL_BASE = "https://maps.googleapis.com/maps/api/geocode/";

    public GeocodingAPICalls(@NonNull Context context, @NonNull OnTaskCompleteListener listener) {
        this.context = context;
        this.initClient();
        this.ApiKey = context.getResources().getString(R.string.google_api_key);
        this.listener = listener;
    }

    /**
     * Client and interface initializer
     */
    private void initClient() {
        if (retrofitClient == null) {
            RetrofitClient.Builder builder = new RetrofitClient.Builder(
                    GeocodingInterface.class, GeocodingAPICalls.URL_BASE);
            builder.callIsJSONFormat();
            retrofitClient = builder.build();
        }
        if (geocodingInterface == null) {
            geocodingInterface = retrofitClient.buildServiceClient();
        }
    }

    /**
     * Query the map
     *
     * @param query              Query to search
     */
    public void searchMap(String query) {
        if (!checkForBadQuery(query) || !checkForInternetConnectivity()) {
            return;
        }

        //Initialize the call
        Call<GeocodingPOJO> call = geocodingInterface.geocodePostalCode(
                ApiKey, query);

        //Enqueue the call asynchronously
        call.enqueue(new Callback<GeocodingPOJO>() {
                         @Override
                         public void onResponse(Call<GeocodingPOJO> call, Response<GeocodingPOJO> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 listener.onTaskComplete(null, TAG_GEOCODING_CALL_ERROR);
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     GeocodingPOJO body = (GeocodingPOJO) response.body();
                                     listener.onTaskComplete(body, MapzenAPICalls.TAG_GEOCODING_CALL_SUCCESS);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), TAG_GEOCODING_CALL_ERROR);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<GeocodingPOJO> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), TAG_GEOCODING_CALL_ERROR);
                         }
                     }
        );

    }

    /**
     * Simple checker to make sure query string is not empty or null
     *
     * @param query Query to search
     * @return boolean, false if it is bad, true if it is good. If the bad
     * bool is triggered, it will send back the response along the
     * listener so there is no need to implement in each call.
     */
    private boolean checkForBadQuery(String query) {
        if (StringUtilities.isNullOrEmpty(query)) {
            listener.onTaskComplete(null, TAG_GEOCODING_CALL_ERROR);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check for internet connection before making call
     * If no network connectivity, sends back response on the listener from here
     *
     * @return boolean. False if no network, true if has network
     */
    private boolean checkForInternetConnectivity() {
        boolean bool = NetworkUtilities.haveNetworkConnection(this.context);
        if (!bool) {
            listener.onTaskComplete(null, MapzenAPICalls.TAG_ERROR_CONNECTIVITY_ISSUE);
        }
        return bool;
    }

    /**
     * Utility method for conversions
     * @param pojo {@link GeocodingPOJO}
     * @return {@link MapzenPOJO}
     */
    public static MapzenPOJO convertGeocodingResponseToMapzen(GeocodingPOJO pojo){
        MapzenPOJO toReturn = new MapzenPOJO();
        List<MapzenPOJO.MapzenFeatures> featuresList = new ArrayList<>();
        //Setting to empty list so that it won't throw NPE in MapFragment
        toReturn.setFeatures(featuresList);

        if(pojo == null){
            return toReturn;
        }

        GeocodingPOJO.Results[] results = pojo.getResults();
        if(results == null){
            return toReturn;
        }

        if(results.length == 0){
            return toReturn;
        }

        GeocodingPOJO.Results result = results[0];
        String formattedAddress = result.getFormatted_address();
        double lat = 0, lng = 0;

        try {
            lat = result.getGeometry().getLocation().getLat();
            lng = result.getGeometry().getLocation().getLng();
        } catch (Exception e){
            return toReturn;
        }

        if(lat == 0 && lng == 0){
            return toReturn;
        }

        MapzenPOJO.MapzenFeatures feature = new MapzenPOJO.MapzenFeatures();
        MapzenPOJO.MapzenGeometry geo = new MapzenPOJO.MapzenGeometry();

        double[] toset = {lng, lat};
        geo.setCoordinates(toset);
        feature.setGeometry(geo);
        feature.setType(formattedAddress);

        featuresList.add(feature);
        toReturn.setFeatures(featuresList);
        return toReturn;
    }
}
