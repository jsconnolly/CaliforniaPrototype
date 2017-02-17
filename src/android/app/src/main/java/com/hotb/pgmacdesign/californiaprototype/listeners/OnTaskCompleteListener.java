package com.hotb.pgmacdesign.californiaprototype.listeners;

/**
 * Listener for sending back data. The int custom tag is used for identifying
 * what is being sent back
 * Created by pmacdowell on 2017-02-13.
 */
public interface OnTaskCompleteListener {
    public void onTaskComplete(Object result, int customTag);
}
