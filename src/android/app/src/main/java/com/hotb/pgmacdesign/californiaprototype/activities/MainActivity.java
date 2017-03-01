package com.hotb.pgmacdesign.californiaprototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.fragments.AddContactFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.AddLocationFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.AlertBeaconPopupFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.EmailLoginFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.HomeFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.MapFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.PermissionsRequestFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.ProfileFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.SMSVerificationFragment;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.utilities.DatabaseUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.DisplayManagerUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.GUIUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ProgressBarUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.SharedPrefs;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class MainActivity extends AppCompatActivity implements CustomFragmentListener,
        View.OnClickListener {

    //UI
    private Toolbar toolbar;
    private TextView toolbar_title, activity_main_emergency_tv,
            activity_main_emergency_sos_button;
    private LinearLayout activity_main_bottom_nav_bar;
    private RelativeLayout activity_main_emergency_sos_layout;
    private ImageView activity_main_map_icon, activity_main_user_icon, app_bar_top_right_button;

    //Fragments
    private MapFragment mapFragment;
    private HomeFragment homeFragment;
    private EmailLoginFragment emailLoginFragment;
    private SMSVerificationFragment smsVerificationFragment;
    private PermissionsRequestFragment permissionsRequestFragment;
    private AlertBeaconPopupFragment alertBeaconPopupFragment;
    private AddLocationFragment addLocationFragment;
    private AddContactFragment addContactFragment;
    private ProfileFragment profileFragment;

    //Misc Variables
    private DatabaseUtilities dbUtilities;
    private SharedPrefs sharedPrefs;
    private DisplayManagerUtilities dmu;
    private long lastBackPressTime;

    //Fragment Variables
    private int fragmentContainerId, currentFragment;
    private Fragment fragmentActive;

    //Emergency status to show
    private EmergencyStates currentEmergencyState;
    enum EmergencyStates {
        CALM, EMERGENCY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initVariables();
        this.setupToolbar();
        this.setupUI();
        FragmentUtilities.switchFragments(Constants.FRAGMENT_MAP, this);
    }

    /**
     * Setup variables
     */
    private void initVariables(){
        this.fragmentContainerId = R.id.activity_main_fragment_layout;
        this.dbUtilities = MyApplication.getDatabaseInstance();
        this.sharedPrefs = MyApplication.getSharedPrefsInstance();
        this.dmu = MyApplication.getDMU();
        this.currentFragment = sharedPrefs.getInt(Constants.CURRENT_FRAGMENT, -1);
        this.lastBackPressTime = 0;
        //MyAmazonClient.subscribeToTopic("testing");
    }

    private void setupUI(){

        this.activity_main_emergency_sos_layout = (RelativeLayout) this.findViewById(
                R.id.activity_main_emergency_sos_layout);
        this.activity_main_bottom_nav_bar = (LinearLayout) this.findViewById(
                R.id.activity_main_bottom_nav_bar);
        this.activity_main_emergency_sos_button = (TextView) this.findViewById(
                R.id.activity_main_emergency_sos_button);
        this.activity_main_emergency_tv = (TextView) this.findViewById(
                R.id.activity_main_emergency_tv);
        this.activity_main_user_icon = (ImageView) this.findViewById(
                R.id.activity_main_user_icon);
        this.activity_main_map_icon = (ImageView) this.findViewById(
                R.id.activity_main_map_icon);

        this.setEmergencyState(EmergencyStates.CALM, null);

        this.activity_main_emergency_sos_button.setOnClickListener(this);
        this.activity_main_map_icon.setOnClickListener(this);
        this.activity_main_user_icon.setOnClickListener(this);
    }
    /**
     * Setup the toolbar up top
     */
    private void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.app_bar);
        this.setSupportActionBar(toolbar);

        //Textview defined
        this.toolbar_title = (TextView) toolbar.findViewById(R.id.app_bar_title);
        this.app_bar_top_right_button = (ImageView) toolbar.findViewById(
                R.id.app_bar_top_right_button);
        this.app_bar_top_right_button.setOnClickListener(this);

        //Set up (back button) as enabled
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        //Disable the standard title, use the textview instead
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set the back arrow to the respective color
        //this.getSupportActionBar().setHomeAsUpIndicator(SystemDrawableUtilities.
                //getToolbarBackArrow(this, R.color.white));
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        //Set the textView
        setToolbarDetails("", null, true, true);

        GUIUtilities.setBackButtonContentDescription(this);

        this.toolbar.bringToFront();

    }

    // TODO: 2017-02-23  add this into onboarding as well
    public void setEmergencyState(EmergencyStates state, String emergencyStateText){
        switch (state){
            case CALM:
                this.activity_main_emergency_tv.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.white));
                this.activity_main_emergency_tv.setVisibility(View.GONE);
                this.activity_main_emergency_tv.setText("");
                this.activity_main_emergency_sos_layout.setVisibility(View.GONE);
                this.setToolbarDetails(FragmentUtilities.getFragmentName(currentFragment),
                        ContextCompat.getColor(this, R.color.colorPrimary), null, null);
                break;

            case EMERGENCY:
                this.activity_main_emergency_tv.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.red));
                this.activity_main_emergency_tv.setVisibility(View.VISIBLE);
                this.activity_main_emergency_tv.setText(R.string.current_state_emergency
                        + "\n" + emergencyStateText);
                this.activity_main_emergency_sos_layout.setVisibility(View.VISIBLE);
                this.setToolbarDetails("EMERGENCY",
                        ContextCompat.getColor(this, R.color.red), null, null);
                break;
        }
    }

    /**
     * Back button hit
     */
    @Override
    public void onBackPressed() {
        backHit();
    }
    /**
     * Operates the back button to previous activity
     * @param item The item in the menu being selected
     * @return boolean, true if you want it to do nothing
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        backHit();
        return true;
    }
    private void backHit(){
        //Check current fragment, send them back to home if not at home.
        if(getCurrentFragment() != Constants.FRAGMENT_MAP
                || getCurrentFragment() != Constants.FRAGMENT_HOME){
            setNewFragment(Constants.FRAGMENT_MAP);
            return;
        }

        //Checks where the first time they press the button,
        // it sets the time. If they press it once more within 3 seconds, exits
        if (this.lastBackPressTime < System.currentTimeMillis() - 3000) {
            L.Toast(this, "Press back once more to exit");
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


    ///////////////////////
    //Fragment Management//
    ///////////////////////

    @Override
    public void setFragment(Fragment fragment, String TAG) {
        if(fragment == null){
            return;
        }

        this.fragmentActive = fragment;

        //Cancel any outbound requests
        //.....cancel

        FragmentManager manager = getSupportFragmentManager();
        for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
            manager.popBackStack();
        }

        //Fragment Transaction
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentContainerId, fragment, TAG);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();

        //Update the onResume here for each fragment upon loading
        this.fragmentActive.onResume();

        //Execute other things here

        //...update images

        //...update toolbar

        //...update
    }

    @Override
    public void setCurrentFragment(int idFragment) {
        this.currentFragment = idFragment;
    }


    @Override
    public void setNewFragment(int x) {

        if(x == -1){
            x = Constants.FRAGMENT_MAP;
        }
        switch(x){
            case Constants.FRAGMENT_MAP:
                if(mapFragment == null) {
                    mapFragment = MapFragment.newInstance();
                }
                MainActivity.this.setFragment(mapFragment, MapFragment.TAG);

                activity_main_map_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.CaliforniaGold));
                activity_main_user_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                break;

            /* todo Using map as home until further notice
                if(homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                }
                MainActivity.this.setFragment(homeFragment, HomeFragment.TAG);
                break;
            */

            case Constants.FRAGMENT_ALERT_BEACON_POPUP:
                if(alertBeaconPopupFragment == null) {
                    alertBeaconPopupFragment = AlertBeaconPopupFragment.newInstance();
                }
                MainActivity.this.setFragment(alertBeaconPopupFragment, AlertBeaconPopupFragment.TAG);

                activity_main_map_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                activity_main_user_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                break;

            case Constants.FRAGMENT_ADD_LOCATION:
                if(addLocationFragment == null) {
                    addLocationFragment = AddLocationFragment.newInstance();
                }
                MainActivity.this.setFragment(addLocationFragment, AddLocationFragment.TAG);

                activity_main_map_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                activity_main_user_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                break;

            case Constants.FRAGMENT_ADD_CONTACT:
                if(addContactFragment == null) {
                    addContactFragment = AddContactFragment.newInstance();
                }
                MainActivity.this.setFragment(addContactFragment, AddContactFragment.TAG);

                activity_main_map_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                activity_main_user_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                break;

            case Constants.FRAGMENT_PROFILE:
                if(profileFragment == null) {
                    profileFragment = ProfileFragment.newInstance();
                }
                MainActivity.this.setFragment(profileFragment, ProfileFragment.TAG);

                activity_main_map_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.White));
                activity_main_user_icon.setBackgroundColor(ContextCompat.getColor(
                        this, R.color.CaliforniaGold));
                break;

            case Constants.ACTIVITY_ONBOARDING: //For logout
                sharedPrefs.clearAllPrefs();
                dbUtilities.deleteAllPersistedObjects(true, false);
                Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity.this.startActivity(intent);
                try {
                    this.finish();
                } catch (Exception e){}
                break;
        }
    }

    @Override
    public int getCurrentFragment() {
        return this.currentFragment;
    }

    @Override
    public void setToolbarDetails(String title, Integer color,
                                  Boolean enableBackButton, Boolean enableTopRightPicture) {
        if(this.toolbar == null){
            return;
        }
        if(color != null){
            this.toolbar.setBackgroundColor(color);
        }
        if(!StringUtilities.isNullOrEmpty(title)){
            this.toolbar_title.setText(title);
            String currentScreen = getString(R.string.currently_on_screen_string);
            this.toolbar_title.setContentDescription(currentScreen + title);
        }

        if(enableBackButton != null){
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(enableBackButton);
            if(enableBackButton){
                //Set the back arrow to the respective color
                //this.getSupportActionBar().setHomeAsUpIndicator(SystemDrawableUtilities.
                        //getToolbarBackArrow(this, R.color.white));
            } else {
                //Set the back arrow to the respective color
                //this.getSupportActionBar().setHomeAsUpIndicator(SystemDrawableUtilities.
                        //getToolbarBackArrow(this, R.color.white));
            }
        }

        if(enableTopRightPicture != null){
            if(enableTopRightPicture){
                this.app_bar_top_right_button.setVisibility(View.VISIBLE);
            } else {
                this.app_bar_top_right_button.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_emergency_sos_button:

                break;

            case R.id.activity_main_map_icon:
                setNewFragment(Constants.FRAGMENT_MAP);
                break;

            case R.id.activity_main_user_icon:
                setNewFragment(Constants.FRAGMENT_PROFILE);
                break;

            case R.id.app_bar_top_right_button:
                setNewFragment(Constants.FRAGMENT_ADD_LOCATION);
                break;
        }
    }

    /**
     * Get the user data from shared prefs to use if needed
     */
    private void getUserData(){
        //
    }

    //////////////////////////////
    //Activity Lifecycle Methods//
    //////////////////////////////


    @Override
    protected void onResume() {
        L.m("onResume hit in main activity");
        getUserData();
        super.onResume();

    }

    @Override
    protected void onStart() {
        L.m("onStart hit in main activity");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        L.m("onStop hit in main activity");
        MyApplication.getSharedPrefsInstance().save(
                Constants.CURRENT_FRAGMENT, getCurrentFragment());
        if(ProgressBarUtilities.isDialogShowing()){
            //Dismiss progress bar if onStop called, prevents NPEs
            ProgressBarUtilities.dismissProgressDialog();
        }
        super.onStop();
    }



}
