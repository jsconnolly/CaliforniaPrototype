package com.hotb.pgmacdesign.californiaprototype.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class NumberUtilities {


    /**
     * Convert miles to feet
     * @param m
     * @return
     */
    public static double convertMilesToFeet(double m){
        double dbl = (m / 0.00019);
        return dbl;
    }

    /**
     * Convert feet to meters
     * @param f
     * @return
     */
    public static double convertFeetToMeters(double f){
        double dbl = f * 0.3048;
        return dbl;
    }

    /**
     * Rounds a double via passed in amount and places
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

     /**
     * Get a random number between a min and max
     * @param min lower end min
     * @param max higher end max
     * @return
     */
    public static int getRandomInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }


}
