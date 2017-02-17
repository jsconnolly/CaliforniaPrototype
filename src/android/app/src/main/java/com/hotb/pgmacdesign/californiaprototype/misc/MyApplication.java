package com.hotb.pgmacdesign.californiaprototype.misc;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.multidex.MultiDexApplication;

import com.hotb.pgmacdesign.californiaprototype.utilities.DatabaseUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.DateUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.DisplayManagerUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.SharedPrefs;

import io.realm.RealmConfiguration;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class MyApplication extends MultiDexApplication {

    //Instance of the application
    private static MyApplication sInstance;
    //Context
    private static Context context;
    //Shared preferences wrapper class
    private static SharedPrefs sp;
    //Localized Database management class.
    private static DatabaseUtilities dbUtilities;
    //Used for managing screen width, height, and other visual metrics
    private static DisplayManagerUtilities dmu;
    //Latitude and Longitude for location purposes
    private static double lastKnownLat, lastKnownLng;
    private static long lastTimeGPSChecked;
    //Google Location objects and listeners
    private static Location location;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;

    /**
     * Constructor
     */
    public MyApplication(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sInstance = this;
        context = getContext();
        dbUtilities = getDatabaseInstance();
        sp = getSharedPrefsInstance();
        setupLocationServices();
    }

    /**
     * Setup location services
     */
    private void setupLocationServices(){
        lastKnownLat = getSharedPrefsInstance().getDouble(Constants.LOCATION_LATITUDE, -1);
        lastKnownLng = getSharedPrefsInstance().getDouble(Constants.LOCATION_LONGITUDE, -1);
        lastTimeGPSChecked = getSharedPrefsInstance().getLong(Constants.LOCATION_LAST_TIME_SET, 0);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    //Location getters
    public static synchronized double getLastKnownLat(){
        return lastKnownLat;
    }
    public static synchronized double getLastKnownLng(){
        return lastKnownLng;
    }
    public static synchronized long getLastGPSCheckTime(){
        return getSharedPrefsInstance().getLong(Constants.LOCATION_LAST_TIME_SET, 0);
    }

    /**
     * Save a location both to last known in this class and the shared prefs
     * @param location
     */
    public void saveLocation(Location location){
        if(location == null){
            return;
        }

        lastKnownLat = location.getLatitude();
        getSharedPrefsInstance().save(Constants.LOCATION_LATITUDE, lastKnownLat);
        lastKnownLng = location.getLongitude();
        getSharedPrefsInstance().save(Constants.LOCATION_LONGITUDE, lastKnownLng);
        lastTimeGPSChecked = DateUtilities.getCurrentDateLong();
        getSharedPrefsInstance().save(Constants.LOCATION_LAST_TIME_SET, lastTimeGPSChecked);

    }

    /**
     * Get context, if it is null, get an instance first and then return it
     * @return Context
     */
    public static synchronized Context getContext(){
        if(context == null){
            MyApplication.context = getInstance().getApplicationContext();
        }
        return context;
    }

    /**
     * Build and return a DatabaseUtilities instance
     * @return {@link DatabaseUtilities}
     */
    public static synchronized DatabaseUtilities getDatabaseInstance(){
        if(dbUtilities == null){
            RealmConfiguration config = DatabaseUtilities.buildRealmConfig(
                getContext(), Constants.DB_NAME, Constants.DB_VERSION,
                Constants.DELETE_DB_IF_NEEDED
            );
            dbUtilities = new DatabaseUtilities(getContext(), config);
        }
        return dbUtilities;
    }

    /**
     * Build and return a shared prefs instance state.
     * NOTE! Although I have code in here for secured, encrypted shared prefs, I did
     * not include it in the final CA Prototype code. I did leave it in however as it
     * is very useful to have at the ready.
     * @return {@link SharedPrefs}
     */
    public static synchronized SharedPrefs getSharedPrefsInstance(){
        if(sp == null){
            sp = SharedPrefs.getSharedPrefsInstance(getContext(), Constants.SHARED_PREFS_NAME);
        }
        return sp;
    }

    /**
     * DMU is utilized in screen measurements in pixels and DP. Useful for setting view
     * dimensions on the fly.
     * @return {@link DisplayManagerUtilities}
     */
    public static synchronized DisplayManagerUtilities getDMU(){
        if(dmu == null) {
            dmu = new DisplayManagerUtilities(getContext());
        }
        return dmu;
    }

    /**
     * Get an instance of the application. This will cascade down and define/ initialize
     * other variables like context as well.
     * @return {@link MyApplication}
     */
    public static synchronized MyApplication getInstance(){
        if(sInstance == null) {
            sInstance = new MyApplication();
        }
        return sInstance;
    }
}
