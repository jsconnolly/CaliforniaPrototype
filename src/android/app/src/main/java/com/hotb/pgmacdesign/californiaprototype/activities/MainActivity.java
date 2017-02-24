package com.hotb.pgmacdesign.californiaprototype.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
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
import com.hotb.pgmacdesign.californiaprototype.utilities.SystemDrawableUtilities;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class MainActivity extends AppCompatActivity implements CustomFragmentListener, View.OnClickListener {

    //UI
    private Toolbar toolbar;
    private TextView toolbar_title, activity_main_emergency_tv,
            activity_main_emergency_sos_button;
    private RelativeLayout activity_main_bottom_nav_bar, activity_main_emergency_sos_layout;

    //Misc Variables
    private DatabaseUtilities dbUtilities;
    private SharedPrefs sharedPrefs;
    private DisplayManagerUtilities dmu;

    //Fragment Variables
    private int fragmentContainerId, currentFragment;
    private Fragment fragmentActive;

    private EmergencyStates currentEmergencyState;



    enum EmergencyStates {
        CALM, EMERGENCY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initVariables();
        this.setupToolbar();
        this.setupUI();
        FragmentUtilities.switchFragments(Constants.FRAGMENT_ADD_LOCATION, this);
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

        //MyAmazonClient.subscribeToTopic("testing");
    }

    private void setupUI(){

        this.activity_main_emergency_sos_layout = (RelativeLayout) this.findViewById(
                R.id.activity_main_emergency_sos_layout);
        this.activity_main_bottom_nav_bar = (RelativeLayout) this.findViewById(
                R.id.activity_main_bottom_nav_bar);
        this.activity_main_emergency_sos_button = (TextView) this.findViewById(
                R.id.activity_main_emergency_sos_button);
        this.activity_main_emergency_tv = (TextView) this.findViewById(
                R.id.activity_main_emergency_tv);

        this.setEmergencyState(EmergencyStates.CALM, null);

        this.activity_main_emergency_sos_button.setOnClickListener(this);
    }
    /**
     * Setup the toolbar up top
     */
    private void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.app_bar);
        this.setSupportActionBar(toolbar);

        //Textview defined
        this.toolbar_title = (TextView) toolbar.findViewById(R.id.app_bar_title);

        //Set up (back button) as enabled
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        //Disable the standard title, use the textview instead
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set the back arrow to the respective color
        this.getSupportActionBar().setHomeAsUpIndicator(SystemDrawableUtilities.
                getToolbarBackArrow(this, R.color.white));

        //Set the textView
        setToolbarDetails("ddddddddd", null);

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
                        ContextCompat.getColor(this, R.color.colorPrimary));
                break;

            case EMERGENCY:
                this.activity_main_emergency_tv.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.red));
                this.activity_main_emergency_tv.setVisibility(View.VISIBLE);
                this.activity_main_emergency_tv.setText(R.string.current_state_emergency
                        + "\n" + emergencyStateText);
                this.activity_main_emergency_sos_layout.setVisibility(View.VISIBLE);
                this.setToolbarDetails("EMERGENCY",
                        ContextCompat.getColor(this, R.color.red));
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

        this.finish();
    }


    ///////////////////////
    //Fragment Management//
    ///////////////////////

    @Override
    public void setFragment(Fragment fragment, String TAG) {
        L.m("setting fragment - " + fragment.toString());
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

        L.m("added fragment");
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
    public int getCurrentFragment() {
        return this.currentFragment;
    }

    @Override
    public void setToolbarDetails(String title, Integer color) {
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_emergency_sos_button:

                break;
        }
    }

    //////////////////////////////
    //Activity Lifecycle Methods//
    //////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();
        L.m("onResume hit in main activity");

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(ProgressBarUtilities.isDialogShowing()){
            //Dismiss progress bar if onStop called, prevents NPEs
            ProgressBarUtilities.dismissProgressDialog();
        }
        super.onStop();
    }



}
