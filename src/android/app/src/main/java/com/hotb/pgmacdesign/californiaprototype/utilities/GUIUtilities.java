package com.hotb.pgmacdesign.californiaprototype.utilities;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class GUIUtilities {



    /**
     * This will hide the keyboard
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Set the back-button content description. This is used for accessibility options;
     * specifically, with regards to the back button (nav up button)
     * @param activity Activity
     */
    public static void setBackButtonContentDescription(Activity activity){
        try {
            ((View) activity.getWindow()
                    .getDecorView()
                    .findViewById(android.R.id.home)
                    .getParent()
                    .getParent())
                    .setContentDescription("Back Button");
        } catch (Exception e){}
    }

}
