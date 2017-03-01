package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.networking.APICalls;
import com.hotb.pgmacdesign.californiaprototype.pojos.CALocation;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;
import com.hotb.pgmacdesign.californiaprototype.utilities.DialogUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ProgressBarUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * Created by pmacdowell on 2017-02-24.
 */

public class ProfileFragment extends Fragment implements OnTaskCompleteListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public final static String TAG = "ProfileFragment";

    //Vars
    private CAUser user;
    private int numRetryCalls;
    private APICalls api;

    //Simple Vars
    private String id, phone, email, name;
    private Boolean enableSMSNotifications, enableEmailNotifications, userHasLocations;

    //UI
    private Switch fragment_profile_sms_switch, fragment_profile_email_switch;
    private TextView fragment_profile_email, fragment_profile_name, fragment_profile_phone;
    private Button fragment_profile_logout;

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilize instanceState here
        this.numRetryCalls = 0;
        this.user = null;
        this.api = new APICalls(getActivity(), this);
        this.name = this.phone =  this.email = this.id = null;
        this.enableEmailNotifications = this.enableSMSNotifications = this.userHasLocations = false;
        this.phone = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_PHONE_NUMBER, null);
        this.email = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_EMAIL, null);
        this.id = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_ID, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {

        //UI
        this.fragment_profile_sms_switch = (Switch) view.findViewById(
                R.id.fragment_profile_sms_switch);
        this.fragment_profile_email_switch = (Switch) view.findViewById(
                R.id.fragment_profile_email_switch);
        this.fragment_profile_email = (TextView) view.findViewById(
                R.id.fragment_profile_email);
        this.fragment_profile_name = (TextView) view.findViewById(
                R.id.fragment_profile_name);
        this.fragment_profile_phone = (TextView) view.findViewById(
                R.id.fragment_profile_phone);
        this.fragment_profile_logout = (Button) view.findViewById(
                R.id.fragment_profile_logout);

        //Defaults
        this.fragment_profile_sms_switch.setChecked(false);
        this.fragment_profile_email_switch.setChecked(false);
        this.fragment_profile_email.setText("");
        this.fragment_profile_name.setText("");
        this.fragment_profile_phone.setText("");
        this.fragment_profile_logout.setEnabled(true);
        this.fragment_profile_logout.setTransformationMethod(null);

        //Listeners
        this.fragment_profile_email.setOnClickListener(this);
        this.fragment_profile_name.setOnClickListener(this);
        this.fragment_profile_phone.setOnClickListener(this);
        this.fragment_profile_logout.setOnClickListener(this);
        this.fragment_profile_sms_switch.setOnCheckedChangeListener(this);
        this.fragment_profile_email_switch.setOnCheckedChangeListener(this);
    }

    private void updateUser(){
        if(this.user == null){
            return;
        }
        String name = fragment_profile_name.getText().toString();
        String email = fragment_profile_email.getText().toString();
        String phone = fragment_profile_phone.getText().toString();

        //Check for setting both email and phone to null
        if(StringUtilities.isNullOrEmpty(phone) && StringUtilities.isNullOrEmpty(email)){
            L.Toast(getActivity(), getString(R.string.must_keep_phone_or_email));
            return;
        }

        user.setEmail(email);
        user.setPhone(phone);
        user.setName(name);

        ProgressBarUtilities.showSVGProgressDialog(getActivity());
        api.updateUser(user);
    }
    /**
     * Reloads UI to update the text fields with user's data if they change it
     */
    private void reloadUI(){
        if(user == null){
            return;
        }
        this.name = user.getName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        CALocation[] locations = user.getLocations();
        if(locations != null){
            if(locations.length > 0){
                this.enableEmailNotifications = this.anyLocationsAllowEmail(locations);
                this.enableSMSNotifications = this.anyLocationsAllowSMS(locations);
                userHasLocations = true;
            }
        }

        this.fragment_profile_email_switch.setChecked(this.enableEmailNotifications);
        this.fragment_profile_sms_switch.setChecked(this.enableSMSNotifications);

        if(StringUtilities.isNullOrEmpty(phone)){
            fragment_profile_phone.setText("");
        } else {
            fragment_profile_phone.setText(phone);
        }
        if(StringUtilities.isNullOrEmpty(email)){
            fragment_profile_email.setText("");
        } else {
            fragment_profile_email.setText(email);
        }
        fragment_profile_name.setText(name);
        ((CustomFragmentListener)getActivity()).setToolbarDetails(
                name, null, true, false
        );
    }

    /**
     * Iterate through their list of preferred locations and see if any of them
     * allow sms notifications, if so, return true for at least 1 hit.
     * @return true of >=1 allow sms notifications
     */
    private boolean anyLocationsAllowSMS(CALocation[] locations){
        if(locations == null){
            return false;
        }
        if(locations.length <= 0){
            return false;
        }
        for(CALocation loc : locations){
            boolean bool = loc.getEnableSMS();
            if(bool){
                return true;
            }
        }
        return false;
    }

    /**
     * Iterate through their list of preferred locations and see if any of them
     * allow email notifications, if so, return true for at least 1 hit.
     * @return true of >=1 allow email notifications
     */
    private boolean anyLocationsAllowEmail(CALocation[] locations){
        if(locations == null){
            return false;
        }
        if(locations.length <= 0){
            return false;
        }
        for(CALocation loc : locations){
            boolean bool = loc.getEnableEmail();
            if(bool){
                return true;
            }
        }
        return false;
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
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_PROFILE);
    }

    private void reloadUser(){
        if(!StringUtilities.isNullOrEmpty(email)) {
            ProgressBarUtilities.showSVGProgressDialog(getActivity());
            api.getUserByEmail(email);
        } else if(!StringUtilities.isNullOrEmpty(id)){
            ProgressBarUtilities.showSVGProgressDialog(getActivity());
            api.getUserById(id);
        } else if(!StringUtilities.isNullOrEmpty(phone)){
            ProgressBarUtilities.showSVGProgressDialog(getActivity());
            api.getUserByPhone(phone);
        } else {
            //Error, one should trigger
            L.toast(getActivity(), R.string.generic_error_text);
        }
    }

    @Override
    public void onResume() {
        L.m("onResume in profilefragment");
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_PROFILE) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    getString(R.string.your_profile_fragment), null, true, true);
            reloadUser();
        }
        super.onResume();
    }

    @Override
    public void onTaskComplete(Object result, int customTag) {
        ProgressBarUtilities.dismissProgressDialog();
        switch(customTag){
            case Constants.TAG_CA_USER:
                this.user = (CAUser) result;
                reloadUI();
                break;

        }
    }

    /**
     * Update all locations to either allow or disallow sms notifications
     * @param bool True will enable sms notifications, false will disable
     */
    private void setSMSNotificationsOnLocations(boolean bool){
        if(user == null){
            return;
        }
        CALocation[] locations = user.getLocations();
        if(locations == null){
            return;
        }
        if(locations.length <= 0){
            return;
        }
        //Iterate through, set the global bool and update account info
        for(CALocation location : locations){
            location.setEnableSMS(bool);
            APICalls tempApi = new APICalls(getActivity(),
                    new OnTaskCompleteListener() {
                        @Override
                        public void onTaskComplete(Object result, int customTag) {
                            //Doing nothing here, this shows nothing for users to see
                        }
                    });
            tempApi.updateLocation(location);
        }
    }

    /**
     * Update all locations to either allow or disallow email notifications
     * @param bool True will enable email notifications, false will disable
     */
    private void setEmailNotificationsOnLocations(boolean bool){
        if(user == null){
            return;
        }
        CALocation[] locations = user.getLocations();
        if(locations == null){
            return;
        }
        if(locations.length <= 0){
            return;
        }
        //Iterate through, set the global bool and update account info
        for(CALocation location : locations){
            location.setEnableEmail(bool);
            APICalls tempApi = new APICalls(getActivity(),
                    new OnTaskCompleteListener() {
                        @Override
                        public void onTaskComplete(Object result, int customTag) {
                            //Doing nothing here, this shows nothing for users to see
                        }
                    });
            tempApi.updateLocation(location);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == this.fragment_profile_email_switch){
            setEmailNotificationsOnLocations(isChecked);
        } else if(buttonView == this.fragment_profile_sms_switch){
            setSMSNotificationsOnLocations(isChecked);
        } else {
            L.m("other hit on switches, check IDs");
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragment_profile_name:
                Dialog nameDialog = DialogUtilities.buildEditTextDialog(
                        getActivity(), new DialogUtilities.DialogFinishedListener() {
                            @Override
                            public void dialogFinished(Object object, int tag) {
                                if(tag == DialogUtilities.SUCCESS_RESPONSE){
                                    try {
                                        String str = (String) object;
                                        if (!StringUtilities.isNullOrEmpty(str)) {
                                            str = str.trim();
                                            fragment_profile_name.setText(str);
                                            user.setName(str);
                                            updateUser();
                                        } else {
                                            //They canceled, no reason to move on
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    //Not success response, can ignore
                                }
                            }
                        }, getString(R.string.done), getString(R.string.cancel),
                        getString(R.string.change_your_name), getString(R.string.enter_new_name),
                        getString(R.string.first_name),
                        InputType.TYPE_CLASS_TEXT
                );
                nameDialog.show();
                break;

            case R.id.fragment_profile_email:
                Dialog emailDialog = DialogUtilities.buildEditTextDialog(
                        getActivity(), new DialogUtilities.DialogFinishedListener() {
                            @Override
                            public void dialogFinished(Object object, int tag) {
                                if(tag == DialogUtilities.SUCCESS_RESPONSE){
                                    try {
                                        String str = (String) object;
                                        if (!StringUtilities.isNullOrEmpty(str)) {
                                            str = str.trim();
                                            if(!StringUtilities.isValidEmail(str)){
                                                L.toast(getActivity(), getString(R.string.enter_valid_email));
                                                return;
                                            }
                                            fragment_profile_email.setText(str);
                                            user.setEmail(str);
                                            updateUser();
                                        } else {
                                            //They canceled, no reason to move on
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    //Not success response, can ignore
                                }
                            }
                        }, getString(R.string.done), getString(R.string.cancel),
                        getString(R.string.change_your_email), getString(R.string.enter_new_email),
                        getString(R.string.youremail_email_com),
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                );
                emailDialog.show();
                break;

            case R.id.fragment_profile_phone:
                Dialog phoneDialog = DialogUtilities.buildEditTextDialog(
                        getActivity(), new DialogUtilities.DialogFinishedListener() {
                            @Override
                            public void dialogFinished(Object object, int tag) {
                                if(tag == DialogUtilities.SUCCESS_RESPONSE){
                                    try {
                                        String str = (String) object;
                                        if (!StringUtilities.isNullOrEmpty(str)) {
                                            str = StringUtilities.keepNumbersOnly(str);
                                            str = str.trim();
                                            user.setPhone(str);
                                            str = StringUtilities.formatStringLikePhoneNumber(str);
                                            fragment_profile_phone.setText(str);
                                            updateUser();
                                        } else {
                                            //They canceled, no reason to move on
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    //Not success response, can ignore
                                }
                            }
                        }, getString(R.string.done), getString(R.string.cancel),
                        getString(R.string.change_phone_number),
                        getString(R.string.enter_new_phone_number),
                        getString(R.string.sample_phone_number),
                        InputType.TYPE_CLASS_PHONE
                );
                phoneDialog.show();
                break;

            case R.id.fragment_profile_logout:
                switchFragment(Constants.ACTIVITY_ONBOARDING);
                break;
        }
    }
}
