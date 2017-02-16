package com.hotb.pgmacdesign.californiaprototype.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.hotb.pgmacdesign.californiaprototype.animations.CustomProgressBar;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class ProgressBarUtilities {

    private static Dialog myDialog;
    private static Timer timeoutTimer;
    private static Context mContext;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////SVG Progress Dialog////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Shows an SVG Dialog. Overloaded for only context needed
     * @param context context
     */
    public static void showSVGProgressDialog(@NonNull Context context){
        showSVGProgressDialog(context, false, null);
    }

    /**
     * Shows an SVG Dialog. Overloaded for boolean dismissable editability
     * @param context context
     */
    public static void showSVGProgressDialog(@NonNull Context context, boolean dismissable){
        showSVGProgressDialog(context, dismissable, null);
    }

    /**
     * Shows an SVG Dialog. Overloaded for only context needed
     * @param context context
     * @param timeoutInMilliseconds timeout in milliseconds. If null, will default to 5 seconds.
     *                              If <0, indefinite
     */
    public static void showSVGProgressDialog(@NonNull Context context, Integer timeoutInMilliseconds){
        showSVGProgressDialog(context, false, timeoutInMilliseconds);
    }

    /**
     * Builds an SVG Progress Dialog and shows it. For more detailed param info,
     * see {@link CustomProgressBar}
     * @param context Context
     * @param dismissible Is cancellable progress bar or not
     * @param timeoutInMilliseconds timeout in milliseconds. If null, will default to 5 seconds.
     *                              If <0, indefinite
     */
    public static void showSVGProgressDialog(@NonNull final Context context, boolean dismissible,
                                             Integer timeoutInMilliseconds) {
        mContext = context;
        try {
            if (myDialog == null) {
                myDialog = CustomProgressBar.buildSVGDialog(context, dismissible);
                myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        L.toast(context, "Canceled");
                    }
                });
                myDialog.show();
            }else {
                myDialog.show();
            }
        } catch (Exception ex) {
            setupTimeoutTimer();
            try {
                myDialog = CustomProgressBar.buildSVGDialog(mContext);
                myDialog.show();
            } catch (Exception e2){
                e2.printStackTrace();
            }
        }
        if(timeoutInMilliseconds != null) {
            setupTimeoutTimer(timeoutInMilliseconds);
        } else {
            setupTimeoutTimer();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////Dismiss and Timers/////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Dismiss the progress Dialog
     */
    public static void dismissProgressDialog() {
        try {
            if (myDialog != null && myDialog.isShowing()) {
                myDialog.dismiss();
                myDialog = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Dismiss the progress dialog with a toast
     * @param message The message String to toast
     */
    public static void dismissProgressDialog(String message) {
        try {
            dismissProgressDialog();
            L.toast(mContext, message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Setup a timeout timer to auto-dismiss the progress dialog after X seconds
     */
    private static void setupTimeoutTimer(){
        setupTimeoutTimer((Constants.ONE_SECOND * 5));
    }

    /**
     * Overloaded method for setting up timeout to include custom long param
     * @param millisecondsToEnd Long milliseconds timeout (5,000 would be 5 seconds)
     */
    private static void setupTimeoutTimer(long millisecondsToEnd){
        if(timeoutTimer == null){
            timeoutTimer = new Timer();
        }

        timeoutTimer.cancel();
        timeoutTimer = new Timer();
        if(millisecondsToEnd < 0){
            return;
        }
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    dismissProgressDialog();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, ((int)(millisecondsToEnd)));
    }

    /**
     * Quick method for determining whether or not the dialog is showing. Useful for onStop calls
     * @return boolean, true if it is showing, false if it is not or is null.
     */
    public static boolean isDialogShowing(){
        if(myDialog == null){
            return false;
        }
        return myDialog.isShowing();
    }
}