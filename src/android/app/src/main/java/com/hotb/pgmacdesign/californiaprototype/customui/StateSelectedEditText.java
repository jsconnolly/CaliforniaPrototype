package com.hotb.pgmacdesign.californiaprototype.customui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.EditText;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;

/**
 * Created by pmacdowell on 2017-02-16.
 */

public class StateSelectedEditText extends EditText {

    public static enum EditTextState {
        FOCUSED, NOT_FOCUSED, ERROR
    }

    public StateSelectedEditText(Context context) {
        super(context);
    }

    public StateSelectedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StateSelectedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StateSelectedEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setState(EditTextState state){
        switch (state){
            case FOCUSED:
                this.setBackground(ContextCompat.getDrawable(
                        MyApplication.getContext(),
                        R.drawable.custom_background_white_back_dblue_edges));
                break;

            case NOT_FOCUSED:
                this.setBackground(ContextCompat.getDrawable(
                        MyApplication.getContext(),
                        R.drawable.custom_background_white_back_lightgrey_edges));
                break;

            case ERROR:
                this.setBackground(ContextCompat.getDrawable(
                        MyApplication.getContext(),
                        R.drawable.custom_background_white_back_red_edges));
                break;
        }
    }

}
