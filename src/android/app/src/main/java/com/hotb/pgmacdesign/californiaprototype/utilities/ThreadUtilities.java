package com.hotb.pgmacdesign.californiaprototype.utilities;

import android.os.Handler;

/**
 * Class for managing and handling threads {@link Thread}
 * Created by pmacdowell on 2017-02-21.
 */
public class ThreadUtilities {


    /**
     * Get a handler object tied to the callback implemented in the activity / fragment
     * @return {@link Handler}
     */
    public static Handler getHandlerWithCallback(Handler.Callback callback){
        Handler handler = new Handler(callback);
        return handler;
    }


}
