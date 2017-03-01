package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.customui.ScaleBar;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.MyLocationListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.networking.APICalls;
import com.hotb.pgmacdesign.californiaprototype.pojos.AlertBeacon;
import com.hotb.pgmacdesign.californiaprototype.pojos.CALocation;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;
import com.hotb.pgmacdesign.californiaprototype.utilities.AnimationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.CaliforniaPrototypeCustomUtils;
import com.hotb.pgmacdesign.californiaprototype.utilities.DisplayManagerUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.LocationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.MiscUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.NumberUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.PermissionUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ProgressBarUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ThreadUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by pmacdowell on 2017-02-14.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, MyLocationListener.LocationLoadedListener,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnCircleClickListener, GoogleMap.OnCameraMoveListener, Handler.Callback, OnTaskCompleteListener {

    //Tag
    public final static String TAG = "MapFragment";
    private static final double DEFAULT_RADIUS_METERS = 1000000;
    private static final double RADIUS_OF_EARTH_METERS = 6371009;
    private static final String DISMISS_SCALE_BAR = "dismiss_scale_bar";

    //Map Objects
    private GoogleMap googleMap;
    private Circle lastManuallyDrawnCircle;
    private Location location;
    private List<Marker> markersOnMap;
    private List<AlertBeacon> alertBeacons;

    //UI
    private RelativeLayout fragment_map_main_layout, scale_view_layout_holder;
    private LinearLayout fragment_map_search_error_view;
    private ImageView fragment_map_error_image;
    private TextView fragment_map_error_top_tv, fragment_map_error_bottom_tv;
    private ScaleBar mScaleBar;

    //Variables
    private int DEFAULT_ZOOM_LEVEL = 15;
    private boolean mapHasLoaded, locationIsEnabled;
    private PermissionUtilities.permissionsEnum locPerm = PermissionUtilities
            .permissionsEnum.ACCESS_FINE_LOCATION;
    private String email, phone, id, pw;
    private CAUser user;
    private CALocation[] userSavedLocations;
    private List<CALocation> userSavedLocationsList;
    private List<Circle> userSavedLocationCircles;
    private String[] colorArray;

    //Misc
    private Timer scaleBarTimer;
    private APICalls api;
    private Handler handler;
    private DisplayManagerUtilities dmu;

    private static final int NUM_SECONDS_ON_SCALEBAR_HIDE = 2;

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
        this.handler = ThreadUtilities.getHandlerWithCallback(this);
        this.dmu = new DisplayManagerUtilities(getActivity());
        this.api = new APICalls(getActivity(), this);
        this.phone = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_PHONE_NUMBER, null);
        L.m("phone = " + phone);
        this.email = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_EMAIL, null);
        L.m("email = " + email);
        this.id = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_ID, null);
        L.m("id = " + id);
        this.pw = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_PW, null);
        //Utilize instanceState here
    }

    /**
     * OnCreate
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initVariables();
        initUi(view);
        return view;
    }

    /**
     * Setup variables
     */
    private void initVariables(){
        this.locationListener = new MyLocationListener(MyApplication.getInstance(), this);
        this.alertBeacons = new ArrayList<>();
        this.markersOnMap = new ArrayList<>();
    }

    /**
     * Setup the ui
     * @param view
     */
    private void initUi(View view) {
        setupMap();

        this.fragment_map_main_layout = (RelativeLayout) view.findViewById(
                R.id.fragment_map_main_layout);
        this.scale_view_layout_holder = (RelativeLayout) view.findViewById(
                R.id.scale_view_layout_holder);
        this.fragment_map_error_top_tv = (TextView) view.findViewById(
                R.id.fragment_map_error_top_tv);
        this.fragment_map_error_bottom_tv = (TextView) view.findViewById(
                R.id.fragment_map_error_bottom_tv);
        this.fragment_map_error_image = (ImageView) view.findViewById(
                R.id.fragment_map_error_image);
        this.fragment_map_search_error_view = (LinearLayout) view.findViewById(
                R.id.fragment_map_search_error_view);
    }

    /**
     * Setup the map and initialize it
     */
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
        this.colorArray = getActivity().getResources().getStringArray(R.array.circle_color_options);
    }

    /**
     * Last call, init the web calls and set the respective views
     */
    private void initWebCalls(){
        ProgressBarUtilities.showSVGProgressDialog(getActivity(), true);
        if(!StringUtilities.isNullOrEmpty(email)){
            ProgressBarUtilities.showSVGProgressDialog(getActivity());
            api.getUserByEmail(email);
        } else if (!StringUtilities.isNullOrEmpty(phone)) {
            ProgressBarUtilities.showSVGProgressDialog(getActivity());
            api.getUserByPhone(phone);
        } else if (!StringUtilities.isNullOrEmpty(id)){
            ProgressBarUtilities.showSVGProgressDialog(getActivity());
            api.getUserById(id);
        } else {
            L.m("User is null, make them log in again");
            L.toast(getActivity(), getString(R.string.account_null_error));
            switchFragment(Constants.ACTIVITY_ONBOARDING);
        }
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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
        this.googleMap.animateCamera(cameraUpdate);
    }

    /**
     * Map loaded. Initialize it and make it ready to manipulate
     * @param aGoogleMap
     */
    @Override
    public void onMapReady(GoogleMap aGoogleMap) {
        this.googleMap = aGoogleMap;
        this.mapHasLoaded = true;
        this.enableScaleBar();
        this.googleMap.setOnCameraMoveListener(this);
        this.googleMap.setOnMapLongClickListener(this);
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
        this.initWebCalls();
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



    /**
     * Add a marker to the list and to the map
     * @param lat Latitude
     * @param lng Longitude
     * @param locationName Name of location
     */
    private void addMarker(Double lat, Double lng, String locationName){
        if(lat == null || lng == null || locationName == null){
            return;
        }

        MarkerOptions options = new MarkerOptions();
        options.position(CaliforniaPrototypeCustomUtils.convertToLatLng(lat, lng));
        options.draggable(false);
        options.title(locationName);
        Marker marker = googleMap.addMarker(options);
        if(!CaliforniaPrototypeCustomUtils.isMarkerOnList(this.markersOnMap, marker)){
            this.markersOnMap.add(marker);
        }
    }



    /**
     * Calculate the meters per inch on the screen
     * @param projection
     * @return
     */
    private float getMetersPerInch(Projection projection){
        float xdpi = this.dmu.getXdpi();
        View view = getChildFragmentManager().findFragmentById(R.id.fragment_map_map).getView();

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

        Location locationP1 = new Location(ScaleBar.SCALEBAR_LOCATION_PART_1);
        Location locationP2 = new Location(ScaleBar.SCALEBAR_LOCATION_PART_2);

        locationP1.setLatitude(p1.latitude);
        locationP2.setLatitude(p2.latitude);
        locationP1.setLongitude(p1.longitude);
        locationP2.setLongitude(p2.longitude);
        float xMetersPerInch = locationP1.distanceTo(locationP2);
        //If happy with 1 inch on screen, return, else, multiply by a % and return
        return xMetersPerInch;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        if(true){
            //Removed on 2017-02-28 For refactoring purposes
            return;
        }
        if(this.lastManuallyDrawnCircle != null){
            this.lastManuallyDrawnCircle.remove();
        }
        Projection projection = googleMap.getProjection();
        float xMetersPerInch = getMetersPerInch(projection);

        // Create the circle.
        CircleOptions options = new CircleOptions();
        options.center(point);
        options.radius(xMetersPerInch);
        options.strokeColor(R.color.white);
        options.fillColor(R.color.SemiTransparentBlue);
        options.clickable(true);
        this.lastManuallyDrawnCircle = googleMap.addCircle(options);

    }

    /**
     * Adds alert beacons to the map that are clickable
     * @param alertBeacons {@link AlertBeacon}
     */
    private void addAlertBeacons(List<AlertBeacon> alertBeacons){
        this.alertBeacons = alertBeacons;
        if(MiscUtilities.isListNullOrEmpty(alertBeacons)){
            return;
        }

        for(int i = 0; i < alertBeacons.size(); i++){
            AlertBeacon beacon = alertBeacons.get(i);
            double radius = beacon.getCircleRadius();
            double lat = beacon.getLat();
            double lng = beacon.getLng();

            if(lat == 0 && lng == 0){
                continue;
            }

            Projection projection = googleMap.getProjection();
            float xMetersPerInch = getMetersPerInch(projection);

            // Create the circle.
            CircleOptions options = new CircleOptions();
            options.center(CaliforniaPrototypeCustomUtils.convertToLatLng(lat, lng));
            if(radius < 50) {
                //Too small, use the xMeters per inch metric instead
                options.radius(xMetersPerInch);
            } else {
                options.radius(radius);
            }
            options.strokeColor(R.color.white);
            options.fillColor(R.color.SemiTransparentRed);
            options.clickable(true);
            Circle aCircle = googleMap.addCircle(options);
            beacon.setCircleId(aCircle.getId());
            alertBeacons.set(i, beacon);
            this.googleMap.setOnCircleClickListener(this);
        }
    }

    @Override
    public void onCircleClick(Circle circle) {
        if(circle == null){
            return;
        }
        if(MiscUtilities.isListNullOrEmpty(this.alertBeacons)){
            return;
        }
        String id = circle.getId();
        if(StringUtilities.isNullOrEmpty(id)){
            return;
        }

        AlertBeacon beacon = null;
        for(AlertBeacon beacon1 : this.alertBeacons){
            String newId = beacon1.getCircleId();
            if(!StringUtilities.isNullOrEmpty(newId)){
                if(newId.equals(id)){
                    beacon = beacon1;
                }
            }
        }

        if(beacon != null){
            showPopupForBeacon(beacon);
        }
    }

    private void showPopupForBeacon(AlertBeacon beacon){
        if(beacon == null){
            return;
        }

        if(MyApplication.getDatabaseInstance().persistObject(AlertBeacon.class, beacon)){
            switchFragment(Constants.FRAGMENT_ALERT_BEACON_POPUP);
        }
    }

    private void enableScaleBar(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(800, 800);

        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMarginStart((int)(dmu.getPixelsWidth() * 0.3));
        mScaleBar = new ScaleBar(getActivity(), this.googleMap);
        mScaleBar.setLayoutParams(params);
        fragment_map_main_layout.addView(mScaleBar);
    }

    @Override
    public void onCameraMove() {
        //Used to invalidate the scale bar when they move
        if(mScaleBar != null){
            mScaleBar.invalidate();
            //mScaleBar.invalidateNumbersOnly();
            AnimationUtilities.animateMyView(mScaleBar, (100), Techniques.FadeIn);
            dismissScalebarAfterXSeconds(NUM_SECONDS_ON_SCALEBAR_HIDE);
        }
    }

    /**
     * Hide / dismiss the scale bar after X seconds so that it will fade away and not
     * stay permanently on the screen.
     * @param seconds Num seconds to stay on the screen before it disappears
     */
    private void dismissScalebarAfterXSeconds(int seconds){
        seconds *= 1000; //Milliseconds
        if(scaleBarTimer == null){
            scaleBarTimer = new Timer();
        }
        scaleBarTimer.cancel();
        scaleBarTimer = new Timer();
        scaleBarTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AnimationUtilities.animateMyView(mScaleBar,
                                    (int)(Constants.ONE_SECOND * 1.4), Techniques.FadeOut);
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, seconds);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(msg != null){
            Bundle bundle = msg.getData();
            if(bundle != null){
                boolean bool = bundle.getBoolean(DISMISS_SCALE_BAR, false);
                if(bool){

                }
            }

        }
        return false;
    }


    @Override
    public void onResume() {
        L.m("onResume in mapfragment");
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_MAP) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    getString(R.string.map_fragment_name), null, false, true);
        }
        initWebCalls();
        super.onResume();
    }

    /**
     * Load the user saved locations. this should only ping if the array is not null and >0
     * todo look into whether or not we are persisting secondary list to show here (Emergencies)
     */
    private void loadUserSavedLocs(){

        if(this.userSavedLocationCircles == null){
            this.userSavedLocationCircles = new ArrayList<>();
        }
        if(this.userSavedLocationsList == null){
            this.userSavedLocationsList = new ArrayList<>();
        }
        //Iterate to remove any previous ones they had saved
        for(Circle circle : this.userSavedLocationCircles){
            circle.remove();
        }
        //Checking to be sure
        if(userSavedLocations == null){
            return;
        }
        if(userSavedLocations.length <= 0){
            return;
        }

        this.userSavedLocationCircles = new ArrayList<>();
        this.userSavedLocationsList = new ArrayList<>();

        L.m("NUM LOCATIONS SAVED = " + userSavedLocations.length);

        for(int i = 0; i < this.userSavedLocations.length; i++){
            CALocation location = this.userSavedLocations[i];
            if(location == null){
                continue;
            }
            String radiusS = location.getAlertRadius();
            CALocation.Coordinates coordinates = location.getCoordinates();
            String name = location.getDisplayName();

            if(coordinates == null){
                continue;
            }

            float localRadius;
            try {
                localRadius = Float.parseFloat(radiusS);
            } catch (Exception e){
                localRadius = -1;
            }

            if(localRadius < 0){
                localRadius = getMetersPerInch(this.googleMap.getProjection());
            } else {
                localRadius = (float)(NumberUtilities.convertMilesToKilometers(localRadius) * 1000);
            }
            double localLat = coordinates.getLat();
            double localLng = coordinates.getLng();

            if(StringUtilities.isNullOrEmpty(name)){
                name = "";
            }
            /*
            String whichColor = null;
            if(i > 9){
                int random = (int )(Math.random() * 9);
                whichColor = this.colorArray[random];
            } else {
                whichColor = this.colorArray[i];
            }
            int circleColor = ColorUtilities.parseMyColor(whichColor);
            */
            CircleOptions options = new CircleOptions();
            options.center(new LatLng(localLat, localLng));
            options.radius(localRadius);
            options.strokeColor(R.color.white);
            options.fillColor(R.color.SemiTransparentOrchid);
            options.clickable(true);
            Circle circleAdded = googleMap.addCircle(options);
            location.setCircleId(circleAdded.getId());
            this.userSavedLocationCircles.add(circleAdded);
            this.userSavedLocationsList.add(location);

            L.m("made it to end of iteration #" + i);
        }
    }

    /**
     * Handle responses from the server
     * @param result
     * @param customTag
     */
    @Override
    public void onTaskComplete(Object result, int customTag) {
        ProgressBarUtilities.dismissProgressDialog();
        switch(customTag){
            case Constants.TAG_CA_USER:
                // TODO: 2017-02-28 check on other retrieval data. Ask Jason
                user = (CAUser) result;
                APICalls.persistData(user);
                CALocation[] locations = user.getLocations();
                if(locations != null){
                    if(locations.length > 0){
                        this.userSavedLocations = locations;
                        loadUserSavedLocs();
                    }
                }
                break;

            //todo Count for other cases here once back-end finishes
        }
    }
}
