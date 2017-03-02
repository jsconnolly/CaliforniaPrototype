package com.hotb.pgmacdesign.californiaprototype.utilities;

import com.hotb.pgmacdesign.californiaprototype.BuildConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class MiscUtilities {


    /**
     * Checks a list for either being empty or containing objects within it
     * @param myList List to check
     * @return Boolean, true if it is null or empty, false it if is not
     */
    public static boolean isListNullOrEmpty(List<?> myList){
        if(myList == null){
            return true;
        }
        if(myList.size() <= 0){
            return true;
        }
        return false;
    }

    /**
     * Checks a boolean for null (returns false if it is null) and then returns actual
     * bool if not null
     * @param bool boolean to check
     * @return Boolean, true if it is null or empty, false it if is not
     */
    public static boolean isBooleanNullTrueFalse(Boolean bool){
        if(bool == null){
            return false;
        } else {
            return bool;
        }
    }

    /**
     * Checks a map for either being empty or containing objects within it
     * @param myMap map to check
     * @return Boolean, true if it is null or empty, false it if is not
     */
    public static boolean isMapNullOrEmpty(Map<?, ?> myMap){
        if(myMap == null){
            return true;
        }
        if(myMap.size() <= 0){
            return true;
        }
        return false;
    }

    /**
     * Gets the package name. If null returned, send call again with context
     * @return
     */
    public static String getPackageName(){
        try {
            return BuildConfig.APPLICATION_ID;
        } catch (Exception e){
            return null;
        }
    }

}
