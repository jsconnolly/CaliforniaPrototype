package com.hotb.pgmacdesign.californiaprototype.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
public class MainActivity extends AppCompatActivity implements CustomFragmentListener {

    //UI
    private Toolbar toolbar;
    private TextView toolbar_title;

    //Misc Variables
    private DatabaseUtilities dbUtilities;
    private SharedPrefs sharedPrefs;
    private DisplayManagerUtilities dmu;

    //Fragment Variables
    private int fragmentContainerId, currentFragment;
    private Fragment fragmentActive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initVariables();
        this.setupToolbar();
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
        this.getSupportActionBar().setHomeAsUpIndicator(SystemDrawableUtilities.
                getToolbarBackArrow(this, R.color.white));

        //Set the textView
        setToolbarDetails("ddddddddd", null);

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
    public int getCurrentFragment() {
        return this.currentFragment;
    }

    @Override
    public void setToolbarDetails(String title, Integer color) {
        if(color != null){
            this.toolbar.setBackgroundColor(color);
        }
        if(!StringUtilities.isNullOrEmpty(title)){
            this.toolbar_title.setText(title);
            String currentScreen = getString(R.string.currently_on_screen_string);
            this.toolbar_title.setContentDescription(currentScreen + title);
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
