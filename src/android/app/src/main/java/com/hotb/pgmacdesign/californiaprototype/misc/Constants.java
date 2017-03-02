package com.hotb.pgmacdesign.californiaprototype.misc;

import com.daimajia.androidanimations.library.Techniques;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class Constants {

    ///////////////////
    /////Misc Strings//
    ///////////////////

    public static final String STAGING_SERVER_URL = "http://ec2-54-241-144-61.us-west-1.compute.amazonaws.com/";
    public static final String PHONE_URI_TO_WRITE_TO = "/storage/emulated/0/Download/";
    public static final String FILE_NAME = "debugLoggingData.txt";

    /////////////////
    //Fragment ints//
    /////////////////

    public static final int ACTIVITY_MAIN = -10;
    public static final int ACTIVITY_ONBOARDING = -11;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_MAP = 1;
    public static final int FRAGMENT_EMAIL_LOGIN = 2;
    public static final int FRAGMENT_SMS_VERIFICATION = 3;
    public static final int FRAGMENT_PERMISSIONS_REQUEST= 4;
    public static final int FRAGMENT_ALERT_BEACON_POPUP = 5;
    public static final int FRAGMENT_ADD_LOCATION = 6;
    public static final int FRAGMENT_ADD_CONTACT = 7;
    public static final int FRAGMENT_PROFILE = 8;

    ///////////////////////////////////////////
    //Database / Shared Preferences Constants//
    ///////////////////////////////////////////

    public static final String DB_NAME = "CaliforniaPrototype.db";
    public static final boolean DELETE_DB_IF_NEEDED = true;
    public static final int DB_VERSION = 1;
    public static final String SHARED_PREFS_NAME = "CaliforniaPrototype";

    public static final String LOCATION_LATITUDE = "location_latitude";
    public static final String LOCATION_LONGITUDE = "location_longitude";
    public static final String LOCATION_LAST_TIME_SET = "location_last_time_set";


    /////////////////////////////////////////////////////////////////
    /////Custom Tags (There is no specific order to these numbers)///
    /////////////////////////////////////////////////////////////////

    //Request codes used for permission requests
    public static final int TAG_PERMISSIONS_ACCESS_NETWORK_STATE = 4398;
    public static final int MY_PERMISSIONS_REQUEST_CONTACTS = 4399;
    public static final int TAG_PERMISSIONS_REQUEST_BASE_CALL = 4400;
    public static final int TAG_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4401;
    public static final int TAG_PERMISSIONS_REQUEST_CAMERA = 4402;
    public static final int TAG_PERMISSIONS_REQUEST_ALL = 4403;
    public static final int TAG_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 4404;
    public static final int TAG_PERMISSIONS_REQUEST_READ_PHONE_STATE = 4405;
    public static final int TAG_PERMISSIONS_REQUEST_CONTACTS = 4406;
    public static final int TAG_PERMISSIONS_ACCESS_WIFI_STATE = 4407;
    public static final int TAG_PERMISSIONS_ACCESS_FINE_LOCATION = 4408;
    public static final int TAG_PERMISSIONS_ACCESS_COARSE_LOCATION = 4409;
    public static final int TAG_PERMISSIONS_RECEIVE_BOOT_COMPLETED = 4410;
    public static final int TAG_PERMISSIONS_SYSTEM_ALERT_WINDOW = 4411;
    public static final int TAG_CONNECTIVITY_ISSUE = 4412;
    public static final int TAG_API_CALL_FAILURE = 4413;
    public static final int TAG_CA_USER = 4414;
    public static final int TAG_CA_LOCATION = 4415;
    public static final int TAG_EMPTY_OBJECT = 4416;
    public static final int TAG_API_ERROR = 4417;
    public static final int TAG_FORGOT_PASSWORD = 4418;

    //Date Formatting Tags, used for comparison and formatting
    public static final int DATE_MM_DD_YYYY = 4405;
    public static final int DATE_MM_DD_YY = 4406;
    public static final int DATE_YYYY_MM_DD = 4407;
    public static final int DATE_MM_DD = 4408;
    public static final int DATE_MM_YY = 4409;
    public static final int DATE_MM_YYYY = 4410;
    public static final int DATE_MILLISECONDS = 4411;
    public static final int DATE_EPOCH = 4412;
    public static final int DATE_MM_DD_YYYY_HH_MM = 4413;

    public static final int TAG_CLICK_NO_TAG_SENT = 4440;
    public static final int TAG_LONG_CLICK_NO_TAG_SENT = 4441;
    public static final int DATE_YYYY_MM_DD_T_HH_MM_SS_SSS_Z = 4449;
    public static final int DATE_YYYY_MM_DD_T_HH_MM_SS_Z = 4450;

    ////////////////////
    /////Shared Prefs //
    ////////////////////

    public static final String CURRENT_FRAGMENT = "current_fragment";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE_NUMBER = "user_phone_number";
    public static final String USER_PW = "user_pw";
    public static final String USER_EMAIL = "user_email";

    ///////////////////
    /////Time Values //
    ///////////////////

    //Time values in milliseconds
    public static final long ONE_SECOND = (1000);
    public static final long ONE_MINUTE = (1000*60);
    public static final long ONE_HOUR = (1000*60*60);
    public static final long ONE_DAY = (1000*60*60*24);
    public static final long ONE_WEEK = (1000*60*60*24*7);
    public static final long ONE_MONTH = (1000*60*60*24*30);
    public static final long ONE_YEAR = (1000*60*60*24*365);
    public static final int PROGRESS_BAR_TIMEOUT = 5000;

      ////////////////////////////////////////////////////////////////////////////////////////////////
    /////Colors/////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Color Strings
    public static final String COLOR_BLACK = "#000000";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////Animation Constants and Tags///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Animation Techniques (From the library com.daimajia.androidanimations:library:1.1.3@aar).
    //Prefix: In means it brings them into sight while out takes them out of sight
    public static final Techniques IN_ZOOM_UP = Techniques.ZoomInUp; //Fun one, opposite of ZoomOutDown, zooms it up and then in
    public static final Techniques OUT_ZOOM_DOWN = Techniques.ZoomOutDown; //Fun one, zooms away (back) and then down, useful for getting rid of things
    public static final Techniques IN_ROLL = Techniques.RollIn;//Fly in effect looks nice for new views popping in
    public static final Techniques IN_PULSE = Techniques.Pulse; //Pops the view front and back, good for a focuser
    public static final Techniques OUT_HINGE = Techniques.Hinge; //Looks like broken hinge on view and it falls off, fun looking
    public static final Techniques IN_RUBBERBAND = Techniques.RubberBand; //Good for a 'de-select' kind of effect
    public static final Techniques OUT_FLIP_Y = Techniques.FlipOutY; //Clean looking flip out on y Axis, good for deletion/ removal
    public static final Techniques IN_FLIP_X = Techniques.FlipInX;  //Clean flip look. Looks like it's rotating on the X axis in
    public static final Techniques OUT_FLIP_X = Techniques.FlipOutX; //Opposite of FlipInX, good for deletetion / removal
    public static final Techniques OUT_SLIDE = Techniques.SlideOutUp; //Using SlideOutUp in conjunction with SlideInUp would look kinda cool...
    public static final Techniques IN_RIGHT_SLIDE = Techniques.SlideInRight; //Using SlideOutUp in conjunction with SlideInUp would look kinda cool...
    public static final Techniques IN_LEFT_SLIDE = Techniques.SlideInLeft; //Using SlideOutUp in conjunction with SlideInUp would look kinda cool...
    public static final Techniques IN_SLIDE = Techniques.SlideInUp; //Using SlideOutUp in conjunction with SlideInUp would look kinda cool...
    public static final Techniques IN_FADE_DOWN = Techniques.FadeInDown; //Simple fade in and down animation
    public static final Techniques IN_FADE_UP = Techniques.FadeInUp; //Simple fade in and up animation
    public static final Techniques IN_DROP = Techniques.DropOut; //Looks cool, falls down from top and bounces
    public static final Techniques IN_TADA = Techniques.Tada; //Fun one, seems useful for focusing on a view
    public static final Techniques OUT_ROLL = Techniques.RollOut;
    public static final Techniques OUT_ZOOM = Techniques.ZoomOut;
    public static final Techniques IN_FLASH = Techniques.Flash; //Quick flash


    ////////
    //Misc//
    ////////

    public static final double DEFAULT_LATITUDE = 34.0522;
    public static final double DEFAULT_LONGITUDE = -117.7348;
    public static final float LOCATION_MINIMUM_DISTANCE =  8050f;
    public static final long LOCATION_TIME_PERIOD_GAP = (Constants.ONE_HOUR * 4);

}
