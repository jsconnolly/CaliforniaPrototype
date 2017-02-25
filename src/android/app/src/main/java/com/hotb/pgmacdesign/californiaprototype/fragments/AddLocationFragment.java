package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.MyLocationListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.pojos.PlaceChosen;
import com.hotb.pgmacdesign.californiaprototype.utilities.CaliforniaPrototypeCustomUtils;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.GUIUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.LocationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.MapUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.PermissionUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by pmacdowell on 2017-02-23.
 */

public class AddLocationFragment extends Fragment implements OnMapReadyCallback, MyLocationListener.LocationLoadedListener,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnMyLocationButtonClickListener,
        View.OnClickListener, OnTaskCompleteListener {

    public final static String TAG = "AddLocationFragment";

    private String query;
    private Location location;

    //Listeners
    private MyLocationListener locationListener;

    //UI
    private RelativeLayout fragment_add_location_info_layout;
    private TextView fragment_add_location_title, fragment_add_location_body;
    private Button fragment_add_location_confirm_button, fragment_add_location_cancel_button,
            fragment_add_location_add_person_button;
    private GoogleMap googleMap;
    private Marker lastMarkerAdded;
    private Circle lastCircleAdded;

    //Variables
    private int DEFAULT_ZOOM_LEVEL = 15;
    private float currentZoomLevel;
    private boolean mapHasLoaded, locationIsEnabled, myLocationButtonHit;
    private PermissionUtilities.permissionsEnum locPerm = PermissionUtilities
            .permissionsEnum.ACCESS_FINE_LOCATION;
    private Place currentPlaceSelected;

    public AddLocationFragment() {}

    public static AddLocationFragment newInstance() {
        AddLocationFragment fragment = new AddLocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.locationIsEnabled = true;
        this.currentZoomLevel = DEFAULT_ZOOM_LEVEL;
        this.myLocationButtonHit = false;
        //Utilize instanceState here
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_location, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {
        this.locationListener = new MyLocationListener(MyApplication.getInstance(), this);

        this.fragment_add_location_info_layout = (RelativeLayout) view.findViewById(
                R.id.fragment_add_location_info_layout);
        this.fragment_add_location_title = (TextView) view.findViewById(
                R.id.fragment_add_location_title);
        this.fragment_add_location_body = (TextView) view.findViewById(
                R.id.fragment_add_location_body);
        this.fragment_add_location_confirm_button = (Button) view.findViewById(
                R.id.fragment_add_location_confirm_button);
        this.fragment_add_location_cancel_button = (Button) view.findViewById(
                R.id.fragment_add_location_cancel_button);
        this.fragment_add_location_add_person_button = (Button) view.findViewById(
                R.id.fragment_add_location_add_person_button);

        this.fragment_add_location_confirm_button.setTransformationMethod(null);
        this.fragment_add_location_cancel_button.setTransformationMethod(null);
        this.fragment_add_location_add_person_button.setTransformationMethod(null);

        this.fragment_add_location_confirm_button.setEnabled(true);
        this.fragment_add_location_cancel_button.setEnabled(true);
        this.fragment_add_location_add_person_button.setEnabled(true);

        this.fragment_add_location_confirm_button.setOnClickListener(this);
        this.fragment_add_location_cancel_button.setOnClickListener(this);
        this.fragment_add_location_add_person_button.setOnClickListener(this);

    }

    private void setupMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_add_location_map);
        mapFragment.getMapAsync(this);
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
        setupMap();
        clearUI();
    }

    /**
     * Setup the search locations autocomplete listener and ET
     */
    private void setupAutocomplete(){

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Get rid of any other buttons on the screen
                showButtons(false);
                setTVs(null);
                currentPlaceSelected = place;
                LatLng latLng = place.getLatLng();
                moveCamera(latLng.latitude, latLng.longitude);
                GUIUtilities.hideKeyboard(getActivity());
                showButtons(true);
                setTVs(currentPlaceSelected);
                createMarkerAndCircle(currentPlaceSelected);
            }

            @Override
            public void onError(Status status) {
                L.toast(getActivity(), getString(R.string.generic_error_text));
            }
        });

    }

    private void createMarkerAndCircle(Place place){
        //Remove all markers
        if(this.lastMarkerAdded != null) {
            this.lastMarkerAdded.remove();
        }
        if(this.lastCircleAdded != null){
            this.lastCircleAdded.remove();
        }
        if(place == null){
            this.lastMarkerAdded = null;
            this.lastCircleAdded = null;
            return;
        }

        String title = null;
        try {
            title = place.getAddress().toString();
        } catch (Exception e){
            try {
                title = place.getName().toString();
            } catch (Exception e1){
                title = getString(R.string.backup_selected_loc_text);
            }
        }
        MarkerOptions options = new MarkerOptions();
        options.position(place.getLatLng());
        options.draggable(false);
        options.title(title);
        this.lastMarkerAdded = googleMap.addMarker(options);

        float xMetersPerInch = 100;
        if(false){
            // TODO: 2017-02-24 check from server if we are manually adding radius here
        } else {
            xMetersPerInch = getMetersPerInch();
        }

        // Create the circle.
        CircleOptions options2 = new CircleOptions();
        options2.center(place.getLatLng());
        options2.radius(xMetersPerInch);
        options2.strokeColor(R.color.white);
        options2.fillColor(R.color.SemiTransparentBlue);
        options2.clickable(true);
        this.lastCircleAdded = googleMap.addCircle(options2);
        this.googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                //Clicked within circle
            }
        });
    }

    /**
     * Calculate the meters per inch on the screen
     * {@link MapUtilities#getMetersPerInch(Context, GoogleMap, View)}
     * @return
     */
    private float getMetersPerInch(){
        return MapUtilities.getMetersPerInch(getActivity(), googleMap,
                getChildFragmentManager().findFragmentById(
                        R.id.fragment_add_location_map).getView());
    }

    /**
     * Show or hide the buttons
     * @param bool true = show, false = hide
     */
    private void showButtons(boolean bool){
        if(bool){
            this.fragment_add_location_confirm_button.setVisibility(View.VISIBLE);
            this.fragment_add_location_cancel_button.setVisibility(View.VISIBLE);

        } else {
            this.fragment_add_location_confirm_button.setVisibility(View.GONE);
            this.fragment_add_location_cancel_button.setVisibility(View.GONE);
            this.fragment_add_location_add_person_button.setVisibility(View.GONE);
        }
    }

    /**
     * Set the 2 textviews with data
     * @param place {@link Place}
     */
    private void setTVs(Place place){
        if(place == null){
            fragment_add_location_info_layout.setVisibility(View.GONE);
            fragment_add_location_body.setText("");
            fragment_add_location_title.setText("");
            fragment_add_location_body.setVisibility(View.GONE);
            fragment_add_location_title.setVisibility(View.GONE);
        } else {
            String title = place.getAddress().toString();
            String details = null;
            try {
                details = place.getAttributions().toString();
            } catch (NullPointerException npe){}
            if(StringUtilities.isNullOrEmpty(details)){
                try {
                    details = place.getName().toString();
                } catch (NullPointerException npe){}
            }
            if(StringUtilities.isNullOrEmpty(details)){
                details = "";
            }
            fragment_add_location_body.setVisibility(View.VISIBLE);
            fragment_add_location_title.setVisibility(View.VISIBLE);
            fragment_add_location_info_layout.setVisibility(View.VISIBLE);
            fragment_add_location_body.setText(title);
            fragment_add_location_title.setText(details);
        }
    }

    /**
     * Map loaded. Initialize it and make it ready to manipulate
     * @param aGoogleMap
     */
    @Override
    public void onMapReady(GoogleMap aGoogleMap) {
        this.googleMap = aGoogleMap;
        this.mapHasLoaded = true;
        this.googleMap.setOnCameraMoveListener(this);
        this.googleMap.setIndoorEnabled(false);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.setOnMyLocationButtonClickListener(this);
        this.googleMap.setContentDescription(getString(R.string.map_content_description));
        try {
            this.googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se){
            se.printStackTrace();
            //locationError(getString(R.string.gps_must_be_enabled));
        }
        this.onMyLocationButtonClick();
        this.startLocationServices();

    }

    @Override
    public void onCameraMove() {
        //Nothing at the moment, but leaving in for future use
    }

    /**
     * Start location services. Check permission first, then make the loc call
     */
    private void startLocationServices(){
        if(checkPermission()){
            try {
                LocationUtilities.startListeningForLocation(getActivity(), locationListener);
            } catch (SecurityException se){
                //this would only ping if the user somehow managed to disable it milliseconds after enabling it
                se.printStackTrace();
            }
        }
    }

    /**
     * Manage permission results
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == locPerm.getPermissionCode()){
            if(grantResults[0] == PERMISSION_GRANTED){
                startLocationServices();
            } else {
                locationError(getString(R.string.gps_must_be_enabled));
            }
        }
    }
    /**
     * Make a check for permissions
     * @return True if granted, false if not
     */
    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ContextCompat.checkSelfPermission(getContext(),
                    locPerm.getPermissionManifestName()) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{locPerm.getPermissionManifestName()},
                        locPerm.getPermissionCode());
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
        /*
        return PermissionUtilities.PermissionsRequestShortcutReturn(getActivity(),
                new PermissionUtilities.permissionsEnum[]{
                        PermissionUtilities.permissionsEnum.ACCESS_FINE_LOCATION});
        */
    }

    /**
     * Move the camera to a specific location. To go to default (Los Angeles), pass in a
     * Longitude and Latitude of -1, -1.
     * @param latitude Latitude
     * @param longitude Longitude
     */
    private void moveCamera(double latitude, double longitude){
        if(latitude == -1){
            latitude = Constants.DEFAULT_LATITUDE;
        }
        if(longitude == -1){
            longitude = Constants.DEFAULT_LONGITUDE;
        }
        LatLng latLng = new LatLng(latitude, longitude);

        if(!this.myLocationButtonHit) {
            try {
                this.currentZoomLevel = googleMap.getCameraPosition().zoom;
            } catch (Exception e) {
                this.currentZoomLevel = DEFAULT_ZOOM_LEVEL;
                e.printStackTrace();
            }
        } else {
            this.myLocationButtonHit = false;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                latLng, this.currentZoomLevel);
        this.googleMap.animateCamera(cameraUpdate);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        this.myLocationButtonHit = true;
        if(!this.locationIsEnabled){
            L.Toast(getActivity(), getString(R.string.loading_last_known_loc));
        }
        this.currentZoomLevel = DEFAULT_ZOOM_LEVEL;
        moveCamera(MyApplication.getLastKnownLat(), MyApplication.getLastKnownLng());
        return false;
    }

    @Override
    public void locationTurnedOn(boolean bool) {
        this.locationIsEnabled = bool;
    }

    @Override
    public void locationLoaded(Location location) {
        if(location != null){
            this.location = location;
            try {
                this.googleMap.setMyLocationEnabled(true);
            } catch (SecurityException se){
                se.printStackTrace();
                locationError(getString(R.string.gps_must_be_enabled));
            }
        }
    }

    @Override
    public void locationError(String error) {
        if(!StringUtilities.isNullOrEmpty(error)){
            L.Toast(getActivity(), error);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragment_add_location_confirm_button:
                if(currentPlaceSelected == null){
                    L.toast(getActivity(), getString(R.string.no_location_selected_error_message));
                    return;
                }

                /*
                //Confirm place here, move on to next
                this.lastCircleAdded;
                this.lastMarkerAdded;
                this.currentPlaceSelected;
                */

                PlaceChosen place = CaliforniaPrototypeCustomUtils
                        .convertPlaceToPlaceChosen(currentPlaceSelected);
                MyApplication.getDatabaseInstance().persistObject(PlaceChosen.class, place);
                // TODO: 2017-02-24 add place, upon success, set options for adding contact
                L.Toast(getActivity(), "IN DEBUG MODE, LOCATION ADDED");
                onTaskComplete(place, Constants.TAG_API_LOCATION_ADDED);
                // TODO: 2017-02-24 Moving this code into the onTaskComplete listener for now

                break;

            case R.id.fragment_add_location_cancel_button:
                this.currentPlaceSelected = null;
                clearUI();
                break;

            case R.id.fragment_add_location_add_person_button:
                // TODO: 2017-02-24 switch to the add contact fragment
                switchFragment(Constants.FRAGMENT_ADD_CONTACT);
                break;
        }
    }

    /**
     * Clear UI on the screen
     */
    private void clearUI(){
        setTVs(null);
        showButtons(false);
        createMarkerAndCircle(null);
    }

    @Override
    public void onTaskComplete(Object result, int customTag) {
        clearUI();
        //Used for parsing server responses
        switch(customTag){
            case Constants.TAG_API_LOCATION_ADDED:
                fragment_add_location_body.setVisibility(View.VISIBLE);
                fragment_add_location_title.setVisibility(View.VISIBLE);
                fragment_add_location_info_layout.setVisibility(View.VISIBLE);
                fragment_add_location_body.setText(R.string.location_successfully_added);
                fragment_add_location_title.setText(R.string.add_person_button_explanation_1);
                this.fragment_add_location_add_person_button.setVisibility(View.VISIBLE);
                // TODO: 2017-02-24 ask about any other UI elements visible here
                break;

        }
    }

    @Override
    public void onResume() {
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_ADD_LOCATION) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    getString(R.string.add_location_fragment_name), null, true, false);
        }
        super.onResume();
    }
}
