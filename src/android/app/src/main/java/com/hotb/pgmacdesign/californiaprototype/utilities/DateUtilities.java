package com.hotb.pgmacdesign.californiaprototype.utilities;


import com.hotb.pgmacdesign.californiaprototype.misc.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class DateUtilities {


    /**
     * Get the SimpleDateFormat to be used
     * @param formatType The format type to do (See Constants, IE DATE_YYYY_MM_DD)
     * @param delimiter delimiter to separate them. (IE / or - or ,). If null, will use nothing
     * @param locale Locale object {@link Locale} . If null, defaults to United States (US)
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getSimpleDateFormat(int formatType, String delimiter, Locale locale){

        //Object to return
        SimpleDateFormat simpleDateFormat = null;

        //Get rid of nulls
        if(delimiter == null){
            delimiter = "";
        }
        if(locale == null){
            locale = Locale.US;
        }

        //Format
        if (formatType == Constants.DATE_MM_DD_YYYY) {
            simpleDateFormat = new SimpleDateFormat("MM" + delimiter + "dd" + delimiter + "yyyy", locale);
        }
        if (formatType == Constants.DATE_MM_DD_YY) {
            simpleDateFormat = new SimpleDateFormat("MM" + delimiter + "dd" + delimiter + "yy", locale);
        }
        if (formatType == Constants.DATE_YYYY_MM_DD) {
            simpleDateFormat = new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd", locale);
        }
        if (formatType == Constants.DATE_MM_DD) {
            simpleDateFormat = new SimpleDateFormat("MM" + delimiter + "dd", locale);
        }
        if (formatType == Constants.DATE_MM_YY) {
            simpleDateFormat = new SimpleDateFormat("MM" + delimiter + "yy", locale);
        }
        if (formatType == Constants.DATE_MM_YYYY) {
            simpleDateFormat = new SimpleDateFormat("MM" + delimiter + "yyyy", locale);
        }
        if (formatType == Constants.DATE_MM_DD_YYYY_HH_MM) {
            simpleDateFormat = new SimpleDateFormat("MM" + delimiter + "dd" + delimiter + "yyyy" + " HH:mm", locale);
        }
        if(formatType == Constants.DATE_YYYY_MM_DD_T_HH_MM_SS_SSS_Z){
            simpleDateFormat = new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd" + "'T'" + " HH:mm:ss.SSS'Z", locale);
        }
        if(formatType == Constants.DATE_YYYY_MM_DD_T_HH_MM_SS_Z){
            simpleDateFormat = new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd" + "'T'" + " HH:mm:ss'Z", locale);
        }

        return simpleDateFormat;
    }
    
    /**
     * Formats a date into a String
     * @param date The date to be converted
     * @param formatType The format type to do (See Constants, IE DATE_YYYY_MM_DD)
     * @param delimiter delimiter to separate them. (IE / or - or ,). If null, will use /
     * @param locale Locale object {@link Locale} . If null, defaults to United States (US)
     * @return Return a String of the converted date
     */
    public static String convertDateToString(Date date, int formatType, String delimiter, Locale locale){
        if(date == null){
            return null;
        }
        if(delimiter == null){
            delimiter = "/";
        }
        if(locale == null){
            locale = Locale.US;
        }
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(formatType, delimiter, locale);
        String convertedString = null;
        //simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            if (formatType == Constants.DATE_MM_DD_YYYY) {
                convertedString = simpleDateFormat.format(date);
            }
            if (formatType == Constants.DATE_MM_DD_YY) {
                convertedString = simpleDateFormat.format(date);
            }
            if (formatType == Constants.DATE_YYYY_MM_DD) {
                convertedString = simpleDateFormat.format(date);
            }
            if (formatType == Constants.DATE_MM_DD) {
                convertedString = simpleDateFormat.format(date);
            }
            if (formatType == Constants.DATE_MM_YY) {
                convertedString = simpleDateFormat.format(date);
            }
            if (formatType == Constants.DATE_MM_YYYY) {
                convertedString = simpleDateFormat.format(date);
            }
            if (formatType == Constants.DATE_MM_DD_YYYY_HH_MM) {
                convertedString = simpleDateFormat.format(date);
            }
            if (formatType == Constants.DATE_MILLISECONDS) {
                long millis = date.getTime();
                convertedString = Long.toString(millis);
            }
            return convertedString;
        //} catch (ParseException e1){
            //return "Unable to Parse Date";
        } catch (Exception e){
            e.printStackTrace();
        }
        //If nothing, return the date to String
        return date.toString();
    }

    /**
     * Converts a String object into a Date object by parsing
     * @param strDate The string to be converted
     * @return A date object determined by parsing the String
     */
    public static Date convertStringToDate(String strDate, int formatType, String delimiter, Locale locale) {
        if(locale == null){
            locale = Locale.US;
        }
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(formatType, delimiter, locale);
        try {
            return simpleDateFormat.parse(strDate);
        } catch(ParseException e) {
            e.printStackTrace();
            return null;
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * @return Return current day timestamp with YY, MM, dd info ONLY
     */
    public static Long getCurrentDateLong() {
        Date date = new Date();
        long ss = date.getTime();
        return ss;
    }

    /**
     * Checks start and end dates to compare which came first
     * @param startDate start date of event in Date object format
     * @param endDate end date of event in Date object format
     * @return True if first date BEFORE second date OR second date is NULL
     */
    public static boolean before(Date startDate, Date endDate) {
        if((startDate == null) && (endDate == null))
            return true;
        if((startDate == null) && (endDate != null))
            return false;
        if((startDate != null) && (endDate == null))
            return true;
        return startDate.before(endDate);
    }

    /**
     * Used to compare the start and end dates
     * @param startDateStr start date of event in Date object format
     * @param endDateStr end date of event in Date object format
     * @return Returns true if the first date is
     * BEFORE second date OR second date is NULL
     */
    public static boolean before(String startDateStr, String endDateStr, int formatType, String delimiter, Locale locale){
        //Get rid of nulls
        if(delimiter == null){
            delimiter = "/";
        }
        if(locale == null){
            locale = Locale.US;
        }
        if((startDateStr == null) || (startDateStr.isEmpty()))
            return false;

        if((endDateStr == null) || (endDateStr.isEmpty()))
            return true;

        Date startDate = convertStringToDate(startDateStr, formatType, delimiter, locale);
        Date endDate = convertStringToDate(endDateStr, formatType, delimiter, locale);

        return before(startDate, endDate);
    }
}
