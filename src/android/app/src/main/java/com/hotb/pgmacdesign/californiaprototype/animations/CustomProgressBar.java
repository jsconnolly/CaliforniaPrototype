package com.hotb.pgmacdesign.californiaprototype.animations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.jrummyapps.android.widget.AnimatedSvgView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class CustomProgressBar extends ProgressDialog {

    private boolean stopSVG = false;
    private Timer timer;
    private com.jrummyapps.android.widget.AnimatedSvgView animated_svg_view;

    public static final int SVG_DIALOG = 0;
    public static final int CALIFORNIA_SVG_DIALOG = 2;
    private int whichSelected;

    /**
     * Builder for the Custom Progress bar
     * NOTE!!! I Utilized the style/CustomTransparentDialog for this to work effectively.
     * In future projects, it may not be needed, but due to custom styles in this, it was.
     * @param context Context
     * @param cancelable Is cancellable progress bar or not
     * @return ProgressDialog
     */
    public static Dialog buildSVGDialog(Context context, boolean cancelable){

        Dialog customAlertDialog = new CustomProgressBar(context, CALIFORNIA_SVG_DIALOG);

        //customAlertDialog.setIndeterminate(true);
        customAlertDialog.setCancelable(cancelable);
        //customAlertDialog.setInverseBackgroundForced(true);
        Window window = customAlertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(
                ContextCompat.getColor(context, R.color.Transparent)
        ));

        return customAlertDialog;
    }

    /**
     * Overloaded method, excludes boolean in case it is not added
     * @param context
     * @return
     */
    public static Dialog buildSVGDialog(Context context){
        return (CustomProgressBar.buildSVGDialog(context, false));
    }

    /**
     * Alert dialog constructor
     * @param context
     */
    private CustomProgressBar(Context context) {
        super(context);
        whichSelected = 0;
    }

    /**
     * Overloaded for secondary alert popup
     * @param context
     * @param whichSelected
     */
    private CustomProgressBar(Context context, int whichSelected) {
        super(context, R.style.CustomTransparentDialog);
        this.whichSelected = whichSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (whichSelected){

            case SVG_DIALOG:
            case CALIFORNIA_SVG_DIALOG:
                setContentView(R.layout.california_svg_view);
                animated_svg_view = (AnimatedSvgView) this.findViewById(
                        R.id.animated_svg_view);

        }
    }


    @Override
    public void show() {
        super.show();

        switch (whichSelected){
            case CALIFORNIA_SVG_DIALOG:
            case SVG_DIALOG:
                stopSVG = false;
                startSVGAnimation();
                break;
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        try {
            stopSVG = true;
            timer.cancel();
            animated_svg_view.reset();
        } catch (Exception e){}
    }

    private void startSVGAnimation(){
        animated_svg_view.start();
        if(timer == null){
            timer = new Timer();
        }
        if(!stopSVG) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    animated_svg_view.reset();
                    startSVGAnimation();
                }
            }, ((long) (Constants.ONE_SECOND * 2.1)));
        }
    }

    @Override
    protected void onStop() {
        try {
            stopSVG = true;
            timer.cancel();
            animated_svg_view.reset();
        } catch (Exception e){}
        super.onStop();
    }

}
