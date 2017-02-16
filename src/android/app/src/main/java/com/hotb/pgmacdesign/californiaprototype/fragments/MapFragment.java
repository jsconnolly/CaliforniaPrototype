package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.hotb.pgmacdesign.californiaprototype.utilities.LocationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.PermissionUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by pmacdowell on 2017-02-14.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, MyLocationListener.LocationLoadedListener {

    //Tag
    public final static String TAG = "MapFragment";

    //Map Objects
    private GoogleMap googleMap;
    private Location location;

    //UI
    private SearchView searchView;

    //Variables
    private int DEFAULT_ZOOM_LEVEL = 15;
    private boolean mapHasLoaded, locationIsEnabled;
    private PermissionUtilities.permissionsEnum locPerm = PermissionUtilities
            .permissionsEnum.ACCESS_FINE_LOCATION;


    //Listeners
    private MyLocationListener locationListener;

    //Empty constructor
    public MapFragment() {}

    //Instance
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mapHasLoaded = false;
        this.locationIsEnabled = true;
        //Utilize instanceState here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initVariables();
        initUi(view);
        return view;
    }

    private void initVariables(){
        locationListener = new MyLocationListener(MyApplication.getInstance(), this);
    }

    private void initUi(View view) {
        setupMap();
        this.searchView = (SearchView) view.findViewById(R.id.fragment_map_searchview);
        this.searchView.setIconified(false);
        this.searchView.clearFocus();

    }

    private void setupMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map_map);
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
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_MAP);

    }

    /**
     * Move the camera to a specific location. To go to default (Los Angeles), pass in a
     * Longitude and Latitude of -1, -1.
     * @param latitude Latitude
     * @param longitude Longitude
     */
    private void moveCamera(double latitude, double longitude){
        if(latitude < 0){
            latitude = Constants.DEFAULT_LATITUDE;
        }
        if(longitude < 0){
            longitude = Constants.DEFAULT_LONGITUDE;
        }
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
        L.m("move camera to lat = " + latitude + " and lng = " + longitude);
        this.googleMap.animateCamera(cameraUpdate);
    }



    /**
     * Map loaded. Initialize it and make it ready to manipulate
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.mapHasLoaded = true;
        this.googleMap.setIndoorEnabled(false);
        this.googleMap.setContentDescription(getString(R.string.map_content_description));
        try {
            this.googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se){
            se.printStackTrace();
            //locationError(getString(R.string.gps_must_be_enabled));
        }
        this.onMyLocationButtonClick();
        startLocationServices();

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
}
