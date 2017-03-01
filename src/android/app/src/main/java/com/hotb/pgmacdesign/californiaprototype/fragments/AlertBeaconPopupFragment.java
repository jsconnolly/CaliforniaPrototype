package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.customui.StateSelectedEditText;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.networking.APICalls;
import com.hotb.pgmacdesign.californiaprototype.pojos.AlertBeacon;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAAlert;
import com.hotb.pgmacdesign.californiaprototype.pojos.CALocation;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ProgressBarUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * Created by pmacdowell on 2017-02-23.
 */

public class AlertBeaconPopupFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    public final static String TAG = "AlertBeaconPopupFragment";

    //UI
    private TextView alert_beacon_popup_title, alert_beacon_popup_body;
    private LinearLayout fragment_alert_button_layout, fragment_alert_edit_layout;
    private Button fragment_alert_edit_button, fragment_alert_delete_button,
            fragment_alert_button_submit;
    private StateSelectedEditText fragment_alert_edit_layout_name,
            fragment_alert_edit_layout_lat, fragment_alert_edit_layout_lng,
            fragment_alert_edit_layout_radius;
    private SwitchCompat fragment_alert_edit_layout_sms_notif,
            fragment_alert_edit_layout_email_notif;

    //Vars
    private String locationName, locationShortName,
            locationDetails, locationAddress, locationDescription;
    private boolean isSmsEnabled, isEmailEnabled;
    private double lat, lng, radius;
    private boolean isEmergencyAlert;

    //Objects
    private CAAlert alert;
    private CAUser user;
    private CALocation location;

    public AlertBeaconPopupFragment() {}

    public static AlertBeaconPopupFragment newInstance() {
        AlertBeaconPopupFragment fragment = new AlertBeaconPopupFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilize instanceState here
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_beacon_popup, container, false);
        initUi(view);
        return view;
    }

    /**
     * Setup the UI
     * @param view
     */
    private void initUi(View view) {
        this.alert_beacon_popup_body = (TextView) view.findViewById(
                R.id.alert_beacon_popup_body);
        this.alert_beacon_popup_title = (TextView) view.findViewById(
                R.id.alert_beacon_popup_title);
        this.fragment_alert_button_layout = (LinearLayout) view.findViewById(
                R.id.fragment_alert_button_layout);
        this.fragment_alert_edit_layout = (LinearLayout) view.findViewById(
                R.id.fragment_alert_edit_layout);
        this.fragment_alert_edit_button = (Button) view.findViewById(
                R.id.fragment_alert_edit_button);
        this.fragment_alert_delete_button = (Button) view.findViewById(
                R.id.fragment_alert_delete_button);
        this.fragment_alert_button_submit = (Button) view.findViewById(
                R.id.fragment_alert_button_submit);
        this.fragment_alert_edit_layout_name = (StateSelectedEditText) view.findViewById(
                R.id.fragment_alert_edit_layout_name);
        this.fragment_alert_edit_layout_lat = (StateSelectedEditText) view.findViewById(
                R.id.fragment_alert_edit_layout_lat);
        this.fragment_alert_edit_layout_lng = (StateSelectedEditText) view.findViewById(
                R.id.fragment_alert_edit_layout_lng);
        this.fragment_alert_edit_layout_radius = (StateSelectedEditText) view.findViewById(
                R.id.fragment_alert_edit_layout_radius);
        this.fragment_alert_edit_layout_sms_notif = (SwitchCompat) view.findViewById(
                R.id.fragment_alert_edit_layout_sms_notif);
        this.fragment_alert_edit_layout_email_notif = (SwitchCompat) view.findViewById(
                R.id.fragment_alert_edit_layout_email_notif);

        this.fragment_alert_edit_layout_radius.setOnFocusChangeListener(this);
        this.fragment_alert_edit_layout_lng.setOnFocusChangeListener(this);
        this.fragment_alert_edit_layout_lat.setOnFocusChangeListener(this);
        this.fragment_alert_edit_layout_name.setOnFocusChangeListener(this);

        this.fragment_alert_delete_button.setOnClickListener(this);
        this.fragment_alert_edit_button.setOnClickListener(this);
        this.fragment_alert_button_submit.setOnClickListener(this);

        this.fragment_alert_delete_button.setTransformationMethod(null);
        this.fragment_alert_edit_button.setTransformationMethod(null);
        this.fragment_alert_button_submit.setTransformationMethod(null);

        this.setStateDefaults();
    }

    /**
     * This is used to set the fields (Edit texts / switches)
     */
    private void setFields(){

        //Set loc details first
        if(location != null){
            fragment_alert_edit_layout_name.setText(locationShortName);
            fragment_alert_edit_layout_lat.setText(lat + "");
            fragment_alert_edit_layout_lng.setText(lng + "");
            fragment_alert_edit_layout_radius.setText(radius + "");

            fragment_alert_edit_layout_sms_notif.setChecked(isSmsEnabled);
            fragment_alert_edit_layout_email_notif.setChecked(isEmailEnabled);

        }


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
        ((CustomFragmentListener)getActivity()).setCurrentFragment(
                Constants.FRAGMENT_ALERT_BEACON_POPUP);

        AlertBeacon beaconClicked = null;
        try {
            beaconClicked = (AlertBeacon) MyApplication.getDatabaseInstance()
                    .getPersistedObject(AlertBeacon.class);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(beaconClicked == null){
            FragmentUtilities.switchFragments(Constants.FRAGMENT_MAP,
                    ((CustomFragmentListener)getActivity()));
            return;
        }

        alert = beaconClicked.getAlert();
        user = beaconClicked.getUser();
        location = beaconClicked.getLocation();

        if(alert == null){
            isEmergencyAlert = false;
        } else {
            isEmergencyAlert = true;
        }

        if(isEmergencyAlert){
            fragment_alert_button_layout.setVisibility(View.GONE);

            //First set emergency details
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    " ", null, true, false, true);
            //Next get and set data
            getEmergencyData(alert);

        } else {
            fragment_alert_button_layout.setVisibility(View.VISIBLE);
            //Next get and set data
            getLocationData(location);
        }

        //Get the location name to use
        if(StringUtilities.isNullOrEmpty(locationName)){
            locationName =
                    "Location at: Latitude, Longitude (" + lat + ", " + lng + ")";
        }

        if(StringUtilities.isNullOrEmpty(locationShortName)){
            locationShortName = getString(R.string.no_name);
        }

        //If emergency, set body data

        //Append to body data:
        if(locationDetails == null){
            locationDetails = "";
        }
        locationDetails = "\n"
                + "SMS Notifications enabled for location: " + isSmsEnabled
                +"\n\n"
                + "Email Notifications enabled for location: " + isEmailEnabled;

        // TODO: 2017-03-01 append in locationDescription to locationDetails once back-end finishes

        //Get all data here for info to set
        String title = locationName;
        String body = locationDetails;

        alert_beacon_popup_title.setText(title);
        alert_beacon_popup_body.setText(body);

        setFields();
    }

    /**
     * Get emergency data to set into Text fields
     * @param alert
     */
    private void getEmergencyData(CAAlert alert){

        // TODO: 2017-03-01 data pull here for string data
    }

    /**
     * Get CALocation data to set into Text fields
     * @param location
     */
    private void getLocationData(CALocation location){
        locationName = null;
        if(location != null){
            locationName = location.getDisplayName();
            locationShortName = location.getDisplayName();
            CALocation.Coordinates coordinates = location.getCoordinates();
            isEmailEnabled = location.getEnableEmail();
            isSmsEnabled = location.getEnableSMS();
            if(coordinates != null){
                lat = coordinates.getLat();
                lng = coordinates.getLng();
            }
            String radiusS = location.getAlertRadius();
            try {
                radius = Double.parseDouble(radiusS);
            } catch (Exception e){
                radius = 10;
            }

        }
    }

    @Override
    public void onResume() {
        L.m("onResume in alertbeaconpopupfragment");
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_ALERT_BEACON_POPUP) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    " ", null, true, false, null);
        }
        super.onResume();
    }

    /**
     * Set states back to default
     */
    private void setStateDefaults(){
        this.fragment_alert_edit_layout_radius.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        this.fragment_alert_edit_layout_lng.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        this.fragment_alert_edit_layout_lat.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        this.fragment_alert_edit_layout_name.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);

    }
    /**
     * Validate all TV Fields
     * @return
     */
    private boolean validateFields(){
        this.setStateDefaults();
        boolean nameOk = false, latOk = false, lngOk = false, radiusOk = false;

        boolean fEnableEmail = fragment_alert_edit_layout_email_notif.isChecked();
        boolean fEnableSMS = fragment_alert_edit_layout_sms_notif.isChecked();
        String fName = fragment_alert_edit_layout_name.getText().toString();
        String fLat = fragment_alert_edit_layout_lat.getText().toString();
        String fLng = fragment_alert_edit_layout_lng.getText().toString();
        String fRad = fragment_alert_edit_layout_radius.getText().toString();
        float fLatitude = 0;
        float fLongitude = 0;
        float fRadius = 0;

        if(StringUtilities.isNullOrEmpty(fName)){
            nameOk = false;
        } else {
            if(fName.length() > 1){
                nameOk = true;
            } else {
                nameOk = false;
            }
        }
        if(StringUtilities.isNullOrEmpty(fLat)){
            latOk = false;
        } else {
            try {
                fLatitude = Float.parseFloat(fLat);
                latOk = true;
            } catch (Exception e){
                latOk = false;
            }
        }
        if(StringUtilities.isNullOrEmpty(fLng)){
            lngOk = false;
        } else {
            try {
                fLongitude = Float.parseFloat(fLng);
                lngOk = true;
            } catch (Exception e){
                lngOk = false;
            }
        }
        if(StringUtilities.isNullOrEmpty(fRad)){
            radiusOk = false;
        } else {
            try {
                fRadius = Float.parseFloat(fRad);
                if(fRadius < 1){
                    radiusOk = false;
                } else {
                    radiusOk = true;
                }
            } catch (Exception e){
                radiusOk = false;
            }
        }

        //All fields have been parsed at this point:
        if(nameOk && latOk && lngOk && radiusOk){
            CALocation.Coordinates coordinates = new CALocation.Coordinates(fLatitude, fLongitude);
            location.setEnableEmail(fEnableEmail);
            location.setEnableSMS(fEnableSMS);
            location.setAlertRadius(fRadius + "");
            String str = fName;
            location.setDisplayName(str);
            location.setCoordinates(coordinates);
            return true;
        } else {
            if(!nameOk){
                this.fragment_alert_edit_layout_name.setState(
                        StateSelectedEditText.EditTextState.ERROR);
            }
            if(!radiusOk){
                this.fragment_alert_edit_layout_radius.setState(
                        StateSelectedEditText.EditTextState.ERROR);
            }
            if(!latOk){
                this.fragment_alert_edit_layout_lat.setState(
                        StateSelectedEditText.EditTextState.ERROR);
            }
            if(!lngOk){
                this.fragment_alert_edit_layout_lng.setState(
                        StateSelectedEditText.EditTextState.ERROR);
            }

            L.Toast(getActivity(), getString(R.string.fill_out_all_fields));
            return false;
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragment_alert_edit_button:
                if(location != null) {
                    fragment_alert_edit_layout.setVisibility(View.VISIBLE);
                    fragment_alert_button_layout.setVisibility(View.GONE);
                } else {
                    L.toast(getActivity(), getString(R.string.no_loc_to_edit));
                }
                break;

            case R.id.fragment_alert_delete_button:
                L.toast(getActivity(), getString(R.string.tbd_later));
                break;

            case R.id.fragment_alert_button_submit:
                if(validateFields()){
                    triggerSubmit();
                }
                break;
        }
    }

    /**
     * Update the location object
     */
    private void triggerSubmit(){
        if(location == null){
            return;
        }
        ProgressBarUtilities.showSVGProgressDialog(getActivity());
        APICalls api = new APICalls(getActivity(), new OnTaskCompleteListener() {
            @Override
            public void onTaskComplete(Object result, int customTag) {
                ProgressBarUtilities.dismissProgressDialog();
                switch(customTag){
                    case Constants.TAG_CA_USER:
                        //Success
                        callWasSuccess(true);
                        break;

                    case Constants.TAG_API_CALL_FAILURE:
                        //Error
                        callWasSuccess(false);
                        break;

                    case Constants.TAG_API_ERROR:
                        //Error
                        callWasSuccess(false);
                        break;


                }
            }
        });
        api.updateLocation(location);
    }

    /**
     * For handling results from response on api call
     * @param bool if true, was success
     */
    private void callWasSuccess(boolean bool){
        if(bool){
            L.toast(getActivity(), getString(R.string.location_successfully_updated));
            switchFragment(Constants.FRAGMENT_MAP);
        } else {
            L.Toast(getActivity(), getString(R.string.update_location_error));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v == null){
            return;
        }
        if(v instanceof StateSelectedEditText){
            StateSelectedEditText ss = (StateSelectedEditText) v;
            if(hasFocus) {
                ss.setState(StateSelectedEditText.EditTextState.FOCUSED);
            } else {
                ss.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
            }
        }
    }
}

