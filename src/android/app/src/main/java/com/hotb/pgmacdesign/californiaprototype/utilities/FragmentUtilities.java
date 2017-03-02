package com.hotb.pgmacdesign.californiaprototype.utilities;

import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;

/**
 * Created by pmacdowell on 2017-02-15.
 */

public class FragmentUtilities {

    /**
     * Switch Fragment
     * @param x int x corresponding to a specific fragment {@link Constants}
     * @param parentInterface The parent activity that is implementing {@link CustomFragmentListener}
     */
    public static void switchFragments(int x, CustomFragmentListener parentInterface){
        //For now, refactored back into Respective activities for simplicity
        parentInterface.setNewFragment(x);
    }

    public static String getFragmentName(int fragmentId){
        switch (fragmentId){
            case Constants.FRAGMENT_MAP:
                return "Map"; //

            case Constants.FRAGMENT_EMAIL_LOGIN:
                return "Login";

            case Constants.FRAGMENT_SMS_VERIFICATION:
                return "SMS Verification";

            case Constants.FRAGMENT_PERMISSIONS_REQUEST:
                return "Permissions";

            case Constants.FRAGMENT_ALERT_BEACON_POPUP:
                return "Information";
        }
        return "";
    }
    /**
     * Overloaded method, allows for objects to be persisted into the DB before switching fragments
     * @param x int x corresponding to a specific fragment {@link Constants}
     * @param parentInterface The parent activity that is implementing {@link CustomFragmentListener}
     * @param objToPersist Object to persist
     * @param objectType Class type of the object
     */
    public static void switchFragments(int x, CustomFragmentListener parentInterface,
                                       Object objToPersist, Class objectType){
        if(objectType != null && objToPersist != null){
            MyApplication.getDatabaseInstance().persistObject(objectType, objToPersist);
        }
        switchFragments(x, parentInterface);
    }

    public static boolean isValidFragment(int x){
        switch (x){
            case Constants.FRAGMENT_MAP:
                return true;

            default:
                return false;
        }
    }
}
