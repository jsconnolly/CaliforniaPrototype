package com.hotb.pgmacdesign.californiaprototype.utilities;

import com.hotb.pgmacdesign.californiaprototype.fragments.AddContactFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.AddLocationFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.AlertBeaconPopupFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.EmailLoginFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.HomeFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.MapFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.PermissionsRequestFragment;
import com.hotb.pgmacdesign.californiaprototype.fragments.SMSVerificationFragment;
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
        if(x < 0){
            x = Constants.FRAGMENT_HOME;
        }
        switch(x){
            case Constants.FRAGMENT_MAP:
                MapFragment mapFragment = MapFragment.newInstance();
                parentInterface.setFragment(mapFragment, MapFragment.TAG);
                break;

            case Constants.FRAGMENT_HOME:
                HomeFragment homeFragment = HomeFragment.newInstance();
                parentInterface.setFragment(homeFragment, HomeFragment.TAG);
                break;

            case Constants.FRAGMENT_EMAIL_LOGIN:
                EmailLoginFragment emailLoginFragment = EmailLoginFragment.newInstance();
                parentInterface.setFragment(emailLoginFragment, EmailLoginFragment.TAG);
                break;

            case Constants.FRAGMENT_SMS_VERIFICATION:
                SMSVerificationFragment smsVerificationFragment = SMSVerificationFragment.newInstance();
                parentInterface.setFragment(smsVerificationFragment, SMSVerificationFragment.TAG);
                break;

            case Constants.FRAGMENT_PERMISSIONS_REQUEST:
                PermissionsRequestFragment permissionsRequestFragment = PermissionsRequestFragment.newInstance();
                parentInterface.setFragment(permissionsRequestFragment, PermissionsRequestFragment.TAG);
                break;

            case Constants.FRAGMENT_ALERT_BEACON_POPUP:
                AlertBeaconPopupFragment alertBeaconPopupFragment = AlertBeaconPopupFragment.newInstance();
                parentInterface.setFragment(alertBeaconPopupFragment, AlertBeaconPopupFragment.TAG);
                break;

            case Constants.FRAGMENT_ADD_LOCATION:
                AddLocationFragment addLocationFragment = AddLocationFragment.newInstance();
                parentInterface.setFragment(addLocationFragment, AddLocationFragment.TAG);
                break;

            case Constants.FRAGMENT_ADD_CONTACT:
                AddContactFragment addContactFragment = AddContactFragment.newInstance();
                parentInterface.setFragment(addContactFragment, AddContactFragment.TAG);
                break;
        }
    }

    public static String getFragmentName(int fragmentId){
        switch (fragmentId){
            case Constants.FRAGMENT_MAP:
                return "Home"; // TODO: 2017-02-23 ask about this one name...

            case Constants.FRAGMENT_HOME:
                return "Home"; // TODO: 2017-02-23 ask about this one name...

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
            case Constants.FRAGMENT_HOME:
                return true;

            case Constants.FRAGMENT_MAP:
                return true;

            default:
                return false;
        }
    }
}
