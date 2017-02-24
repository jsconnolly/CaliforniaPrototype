package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.customui.StateSelectedEditText;
import com.hotb.pgmacdesign.californiaprototype.geocoding.GeocodingAPICalls;
import com.hotb.pgmacdesign.californiaprototype.geocoding.GeocodingPOJO;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenAPICalls;
import com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenPOJO;
import com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenSimpleObject;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.MiscUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.NumberUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pmacdowell on 2017-02-23.
 */

public class AddLocationFragment extends Fragment implements TextWatcher, AdapterView.OnItemClickListener, OnTaskCompleteListener {

    public final static String TAG = "AddLocationFragment";

    //UI
    private ListView fragment_add_location_lv;
    private TextView fragment_add_location_error;
    private StateSelectedEditText fragment_add_location_et;

    private MapzenAPICalls api;
    private GeocodingAPICalls apiGeocoding;

    //Adapters
    private ArrayAdapter adapter;

    private List<MapzenSimpleObject> searchResultsList;
    private List<String> searchResultsToShow;
    private MapzenPOJO lastMapQueryData;

    private Timer timer;

    private boolean callInProgress, secondaryCall;
    private String query;


    //Gap so that it doesn't query every single time they type a letter
    private static final long TYPING_GAP = ((int)(Constants.ONE_SECOND * 0.35));



    public AddLocationFragment() {}

    public static AddLocationFragment newInstance() {
        AddLocationFragment fragment = new AddLocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilize instanceState here
        this.api = new MapzenAPICalls(getActivity(), this);
        this.apiGeocoding = new GeocodingAPICalls(getActivity(), this);
        this.callInProgress = false;
        this.secondaryCall = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_location, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {

        this.fragment_add_location_lv = (ListView) view.findViewById(
                R.id.fragment_add_location_lv);
        this.fragment_add_location_et = (StateSelectedEditText) view.findViewById(
                R.id.fragment_add_location_et);
        this.fragment_add_location_error = (TextView) view.findViewById(
                R.id.fragment_add_location_error);
        this.fragment_add_location_et.addTextChangedListener(this);

        this.fragment_add_location_lv.setOnItemClickListener(this);
    }

    /**
     * Switch to a different fragment
     * @param x
     */
    private void switchFragment(int x){
        FragmentUtilities.switchFragments(x, ((CustomFragmentListener)getActivity()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_ADD_LOCATION);

        setupAutocomplete();
    }

    private void setupAutocomplete(){

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(fragment_add_location_et.getText() == null){
            return;
        }

        if(fragment_add_location_et.getText().toString().length() < 1){
            fragment_add_location_et.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
        } else {
            fragment_add_location_et.setState(StateSelectedEditText.EditTextState.FOCUSED);

            checkWhichApi(s.toString().trim());

        }

    }


    /**
     * Manage clicks on the list
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(this.searchResultsList == null){
            this.searchResultsList = new ArrayList<>();
        }
        if(this.searchResultsList.size() == 0){
            return;
        } else {
            MapzenSimpleObject pojo = searchResultsList.get(position);
            double latitude = pojo.getLatitude();
            double longitude = pojo.getLongitude();

            //Do stuff here with the location
            // TODO: 2017-02-23 ^^
        }
    }

    /**
     * Sets map results to null and hides the listview
     * @param mapData MapData retrieved from the API Call
     * {@link com.hotb.pgmacdesign.californiaprototype.mapzen.MapzenAPICalls}
     */
    private void setSearchResults(MapzenPOJO mapData){
        this.lastMapQueryData = null;
        if(mapData == null){
            fragment_add_location_error.setVisibility(View.VISIBLE);
            if(!StringUtilities.isNullOrEmpty(query)){
                fragment_add_location_error.setText(R.string.no_search_results + ": " + query);
            } else {
                fragment_add_location_error.setText(R.string.no_search_results);
            }
            return;
        } else {
            fragment_add_location_error.setVisibility(View.GONE);

        }

        this.searchResultsList = new ArrayList<>();
        this.searchResultsToShow = new ArrayList<>();
        this.lastMapQueryData = mapData;

        List<MapzenPOJO.MapzenFeatures> featuresList = this.lastMapQueryData.getFeatures();
        if(MiscUtilities.isListNullOrEmpty(featuresList)){
            String noResults = getString(R.string.no_search_results);
            this.searchResultsToShow.add(noResults);
            adjustAdapters();
            return;
        }

        for(MapzenPOJO.MapzenFeatures features : featuresList){
            String str = null;
            MapzenSimpleObject simpleObject = new MapzenSimpleObject();

            MapzenPOJO.MapzenGeometry geometry = features.getGeometry();
            MapzenPOJO.MapzenProperties properties = features.getProperties();

            if(geometry != null){
                double[] coords = geometry.getCoordinates();
                if(coords != null){
                    if(coords.length == 2){
                        simpleObject.setLongitude(coords[0]);
                        simpleObject.setLatitude(coords[1]);
                    }
                }
            }
            if(properties != null){
                simpleObject.setId(properties.getId());
                simpleObject.setName(properties.getName());
                simpleObject.setCity(properties.getCity());
                simpleObject.setState(properties.getState());
                simpleObject.setCounty(properties.getCounty());
                simpleObject.setCountry(properties.getCountry());
                simpleObject.setSimpleLocation(properties.getLabel());
                simpleObject.setPostalcode(properties.getPostalcode());
                simpleObject.setNeighbourhood(properties.getNeighbourhood());
                simpleObject.setStateAbbreviation(properties.getStateAbbreviation());
                simpleObject.setDistanceFromEnteredLocation(properties.getDistance());

                if(!StringUtilities.isNullOrEmpty(properties.getPostalcode())){
                    String label = simpleObject.getSimpleLocation();
                    label = label + ", " + properties.getPostalcode();
                    simpleObject.setSimpleLocation(label);
                }
            }

            str = simpleObject.getSimpleLocation();
            if(StringUtilities.isNullOrEmpty(str)){
                String ss1 = simpleObject.getName();
                String ss2 = simpleObject.getCity();
                if(StringUtilities.isNullOrEmpty(ss1) && StringUtilities.isNullOrEmpty(ss2)){
                    str = getString(R.string.unknown_location);
                } else {
                    str = ss1 + ", " + ss2;
                }
            }

            this.searchResultsToShow.add(str);
            this.searchResultsList.add(simpleObject);
        }

        if(searchResultsList.size() == 0){
            this.setSearchResults(null);
            return;
        }

        adjustAdapters();
    }

    /**
     * Reload the adapters and the listview to include new return results
     */
    private void adjustAdapters(){
        boolean useLV;
        try {
            if(this.searchResultsToShow.get(0).equals(getString(R.string.no_search_results))){
                useLV = false;
            } else {
                useLV = true;
            }
        } catch (Exception e){
            e.printStackTrace();
            useLV = true;
        }

        if(useLV) {
            this.adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.simple_listview_layout, this.searchResultsToShow);
            this.fragment_add_location_lv.setAdapter(this.adapter);
            this.adapter.notifyDataSetChanged();
        } else {
            setSearchResults(null);
        }
    }


    @Override
    public void onTaskComplete(Object result, int customTag) {
        callInProgress = false;
        switch(customTag){
            case MapzenAPICalls.TAG_ERROR_CONNECTIVITY_ISSUE:
                //todo Make call here to update snackbar with no internet;
                break;

            case MapzenAPICalls.TAG_MAPZEN_SUCCESS:
                MapzenPOJO pojo = (MapzenPOJO) result;
                List<MapzenPOJO.MapzenFeatures> checkingFeatures = pojo.getFeatures();
                if(MiscUtilities.isListNullOrEmpty(checkingFeatures)){
                    if(this.secondaryCall){
                        this.secondaryCall = false;
                    } else {
                        String str = query;
                        if(!StringUtilities.isNullOrEmpty(str)){
                            if(Character.isDigit(str.charAt(0))){
                                int pos = 0;
                                for(int i = 0; i < str.length(); i++){
                                    try {
                                        if (Character.isDigit(str.charAt(i))) {
                                            pos = i;
                                        } else {
                                            break;
                                        }
                                    } catch (Exception e){
                                        pos = i - 1;
                                        if(pos < 0){
                                            pos = 0;
                                        }
                                        break;
                                    }
                                }
                                String sub1 = str.substring(pos);
                                if(!StringUtilities.isNullOrEmpty(sub1)){
                                    this.query = sub1;
                                    this.secondaryCall = true;
                                    makeSearchCall();
                                    return;
                                }
                            }
                        }
                    }
                }
                this.setSearchResults(pojo);
                break;

            case MapzenAPICalls.TAG_MAPZEN_FAILURE:
                this.setSearchResults(null);
                break;

            case MapzenAPICalls.TAG_MAPZEN_INVALID_QUERY:
                this.setSearchResults(null);
                break;

            case MapzenAPICalls.TAG_GEOCODING_CALL_ERROR:
                this.setSearchResults(null);
                break;

            case MapzenAPICalls.TAG_GEOCODING_CALL_SUCCESS:
                GeocodingPOJO pojo1 = (GeocodingPOJO) result;
                MapzenPOJO pojo2 = GeocodingAPICalls
                        .convertGeocodingResponseToMapzen(pojo1);
                this.searchResultsToShow = new ArrayList<>();
                this.searchResultsList = new ArrayList<>();
                try {
                    String str = pojo2.getFeatures().get(0).getType();
                    double[] coord = pojo2.getFeatures().get(0).getGeometry().getCoordinates();
                    MapzenSimpleObject simpleObject = new MapzenSimpleObject();
                    simpleObject.setSimpleLocation(str);
                    simpleObject.setLongitude(coord[0]);
                    simpleObject.setLatitude(coord[1]);
                    this.searchResultsToShow.add(str);
                    this.searchResultsList.add(simpleObject);
                    adjustAdapters();
                } catch (Exception e){
                    this.setSearchResults(null);
                }
                break;
        }
    }

    private void queryMaps(final String query){
        this.query = query;
        if(timer == null){
            timer = new Timer();
        }
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                makeSearchCall();
            }
        }, TYPING_GAP);
    }

    private void makeSearchCall(){
        if(callInProgress){
            return;
        }
        callInProgress = true;
        Double lastKnownLat = MyApplication.getLastKnownLat();
        Double lastKnownLng = MyApplication.getLastKnownLng();

        if(lastKnownLat == -1){
            lastKnownLat = null;
        }
        if(lastKnownLng == -1){
            lastKnownLng = null;
        }

        if(this.query.length() == 5 && NumberUtilities.isNumber(this.query)){
            apiGeocoding.searchMap(this.query);
        } else {
            api.searchMap(AddLocationFragment.this.query, lastKnownLat, lastKnownLng);
        }
    }


    private void checkWhichApi(String str){
        if(StringUtilities.isNullOrEmpty(str)){
            return;
        }
        queryMaps(str);
    }

}
