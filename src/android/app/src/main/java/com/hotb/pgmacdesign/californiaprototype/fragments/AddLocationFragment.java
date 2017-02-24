package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.MyLocationListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.GUIUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.LocationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.PermissionUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by pmacdowell on 2017-02-23.
 */

public class AddLocationFragment extends Fragment implements OnMapReadyCallback, MyLocationListener.LocationLoadedListener,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnMyLocationButtonClickListener, View.OnClickListener {

    public final static String TAG = "AddLocationFragment";

    private String query;
    private Location location;

    //Listeners
    private MyLocationListener locationListener;

    //UI
    private TextView fragment_add_location_title, fragment_add_location_body;
    private Button fragment_add_location_confirm_button, fragment_add_location_cancel_button;
    private GoogleMap googleMap;

    //Variables
    private int DEFAULT_ZOOM_LEVEL = 15;
    private boolean mapHasLoaded, locationIsEnabled;
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

        this.fragment_add_location_title = (TextView) view.findViewById(
                R.id.fragment_add_location_title);
        this.fragment_add_location_body = (TextView) view.findViewById(
                R.id.fragment_add_location_body);
        this.fragment_add_location_confirm_button = (Button) view.findViewById(
                R.id.fragment_add_location_confirm_button);
        this.fragment_add_location_cancel_button = (Button) view.findViewById(
                R.id.fragment_add_location_cancel_button);

        fragment_add_location_confirm_button.setTransformationMethod(null);
        fragment_add_location_cancel_button.setTransformationMethod(null);
        fragment_add_location_confirm_button.setEnabled(true);
        fragment_add_location_cancel_button.setEnabled(true);
        fragment_add_location_confirm_button.setOnClickListener(this);
        fragment_add_location_cancel_button.setOnClickListener(this);

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
        setTVs(null);
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
                currentPlaceSelected = place;
                LatLng latLng = place.getLatLng();
                moveCamera(latLng.latitude, latLng.longitude);
                GUIUtilities.hideKeyboard(getActivity());
                showButtons(true);
                setTVs(currentPlaceSelected);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /**
     * Show or hide the buttons
     * @param bool true = show, false = hide
     */
    private void showButtons(boolean bool){
        if(bool){
            fragment_add_location_confirm_button.setVisibility(View.VISIBLE);
            fragment_add_location_cancel_button.setVisibility(View.VISIBLE);

        } else {
            fragment_add_location_confirm_button.setVisibility(View.GONE);
            fragment_add_location_cancel_button.setVisibility(View.GONE);
        }
    }

    /**
     * Set the 2 textviews with data
     * @param place {@link Place}
     */
    private void setTVs(Place place){
        if(place == null){
            fragment_add_location_body.setText("");
            fragment_add_location_title.setText("");
            fragment_add_location_body.setVisibility(View.GONE);
            fragment_add_location_title.setVisibility(View.GONE);
            showButtons(false);
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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                latLng, DEFAULT_ZOOM_LEVEL);
        this.googleMap.animateCamera(cameraUpdate);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        if(!this.locationIsEnabled){
            L.Toast(getActivity(), getString(R.string.loading_last_known_loc));
        }
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
                //Confirm place here, move on to next
                break;

            case R.id.fragment_add_location_cancel_button:
                //Cancel, popbackstack here
                break;
        }
    }
}
