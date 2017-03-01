package com.hotb.pgmacdesign.californiaprototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.fragments.EmailLoginFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.PermissionsRequestFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.SMSVerificationFragment;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.networking.APICalls;
import com.hotb.pgmacdesign.californiaprototype.utilities.DatabaseUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.DisplayManagerUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.GUIUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ProgressBarUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.SharedPrefs;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.SystemDrawableUtilities;

public class OnboardingActivity extends AppCompatActivity implements CustomFragmentListener, OnTaskCompleteListener {

    //UI
    private Toolbar toolbar;
    private TextView toolbar_title;

    //Misc Variables
    private DatabaseUtilities dbUtilities;
    private SharedPrefs sharedPrefs;
    private DisplayManagerUtilities dmu;
    private APICalls api;
    private boolean anAttemptWasMade;

    //Fragment Variables
    private int fragmentContainerId, currentFragment;
    private Fragment fragmentActive;

    //Fragments
    private SMSVerificationFragment smsVerificationFragment;
    private EmailLoginFragment emailLoginFragment;
    private PermissionsRequestFragment permissionsRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        this.initVariables();
        this.setupToolbar();
        this.api = new APICalls(this, this);
        this.anAttemptWasMade = false;

        //Check for logged in user
        String userId = MyApplication.getSharedPrefsInstance().getString(Constants.USER_ID, null);
        String token = MyApplication.getSharedPrefsInstance().getString(Constants.AUTH_TOKEN, null);

        //Determine which direction to go
        if(!StringUtilities.isNullOrEmpty(userId) && !StringUtilities.isNullOrEmpty(token)){
            //Check token validity:
            ProgressBarUtilities.showSVGProgressDialog(this, false);
            api.getUserById(userId);
        } else {
            FragmentUtilities.switchFragments(Constants.FRAGMENT_EMAIL_LOGIN, this);
        }
    }

    /**
     * Setup variables
     */
    private void initVariables(){
        this.fragmentContainerId = R.id.activity_onboarding_fragment_layout;
        this.dbUtilities = MyApplication.getDatabaseInstance();
        this.sharedPrefs = MyApplication.getSharedPrefsInstance();
        this.dmu = MyApplication.getDMU();
        this.currentFragment = sharedPrefs.getInt(Constants.CURRENT_FRAGMENT, -1);

        //MyAmazonClient.subscribeToTopic("testing");
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
        //this.getSupportActionBar().setHomeAsUpIndicator(SystemDrawableUtilities.
                //getToolbarBackArrow(this, R.color.black));
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        //Set the textView
        setToolbarDetails("", null, true, null);

        GUIUtilities.setBackButtonContentDescription(this);

        this.toolbar.bringToFront();
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
    public void setNewFragment(int x) {

        if(x == -1){
            x = Constants.FRAGMENT_EMAIL_LOGIN;
        }

        switch(x){

            case Constants.FRAGMENT_EMAIL_LOGIN:
                if(emailLoginFragment == null) {
                    emailLoginFragment = EmailLoginFragment.newInstance();
                }
                OnboardingActivity.this.setFragment(emailLoginFragment, EmailLoginFragment.TAG);
                break;

            case Constants.FRAGMENT_SMS_VERIFICATION:
                if(smsVerificationFragment == null) {
                    smsVerificationFragment = SMSVerificationFragment.newInstance();
                }
                OnboardingActivity.this.setFragment(smsVerificationFragment, SMSVerificationFragment.TAG);
                break;

            case Constants.FRAGMENT_PERMISSIONS_REQUEST:
                if(permissionsRequestFragment == null) {
                    permissionsRequestFragment = PermissionsRequestFragment.newInstance();
                }
                OnboardingActivity.this.setFragment(permissionsRequestFragment, PermissionsRequestFragment.TAG);
                break;

            case Constants.ACTIVITY_MAIN:
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                OnboardingActivity.this.startActivity(intent);
                try {
                    this.finish();
                } catch (Exception e){
                    e.printStackTrace();
                }
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
            //Do nothing here as the top right button is not in onboarding
        }
    }

    //////////////////////////////
    //Activity Lifecycle Methods//
    //////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();

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

    /**
     * Attempt to log the user in again without them having to input their credentials.
     * If it fails, it will clear saved creds and try again
     */
    private void attemptToLoginAgain(){
        String email = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_EMAIL, null);
        String phone = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_PHONE_NUMBER, null);
        String pw = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_PW, null);
        if(!StringUtilities.isNullOrEmpty(email) && !StringUtilities.isNullOrEmpty(pw)){
            ProgressBarUtilities.showSVGProgressDialog(this, false);
            api.loginWithEmail(email, pw);
        } else if(!StringUtilities.isNullOrEmpty(phone) && !StringUtilities.isNullOrEmpty(pw)){
            ProgressBarUtilities.showSVGProgressDialog(this, false);
            api.loginWithPhone(phone, pw);
        } else {
            this.onTaskComplete(null, -1);
        }
    }

    @Override
    public void onTaskComplete(Object result, int customTag) {
        ProgressBarUtilities.dismissProgressDialog();
        if(customTag != Constants.TAG_CA_USER){

            if(!this.anAttemptWasMade){
                //This means that the sessionId / Auth token is invalid and user needs to login again
                this.attemptToLoginAgain();
                this.anAttemptWasMade = true;
            } else {
                //This means sessionId timed out AND their creds were bad, clear the saved data
                MyApplication.getSharedPrefsInstance().clearAllPrefs();
                MyApplication.getDatabaseInstance().deleteAllPersistedObjects(true, false);
                FragmentUtilities.switchFragments(
                        Constants.FRAGMENT_EMAIL_LOGIN, OnboardingActivity.this);
                //Re-adjust the toolbar for visibility
                try {
                    this.getSupportActionBar().setHomeAsUpIndicator(SystemDrawableUtilities.
                            getToolbarBackArrow(this, R.color.black));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            FragmentUtilities.switchFragments(
                    Constants.ACTIVITY_MAIN, OnboardingActivity.this);
        }
    }
}
