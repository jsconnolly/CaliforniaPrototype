package com.hotb.pgmacdesign.californiaprototype.listeners;

import android.support.v4.app.Fragment;

/**
 * Created by pmacdowell on 2017-02-14.
 */

public interface CustomFragmentListener {
    public void setFragment(Fragment fragment, String TAG);
    public void setCurrentFragment(int idFragment);
    public void setNewFragment(int idFragment);
    public int getCurrentFragment();
    public void setToolbarDetails(String title, Integer color, Boolean enableBackButton,
                                  Boolean enableTopRightPicture, Boolean isEmergency);
}
