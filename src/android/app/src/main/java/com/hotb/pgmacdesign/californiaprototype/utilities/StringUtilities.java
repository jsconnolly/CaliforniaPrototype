package com.hotb.pgmacdesign.californiaprototype.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.net.URISyntaxException;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class StringUtilities {

    public static final String REGEX_WEB_URL_ENCODING = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String REGEX_PASSWORD_PATTERN = "^\\S*(?=\\S*[a-zA-Z])(?=\\S*[0-9])\\S*$";


    /**
     * Returns just the numbers of a String
     * @param s Charsequence to analyze
     * @return String, containing only numbers
     */
    public static String keepNumbersOnly(CharSequence s) {
        try {
            return s.toString().replaceAll("[^0-9]", "");
        } catch (Exception e){
            return null;
        }
    }

    /**
     * This will format a String passed in (7145551234) and convert it into standard US phone
     * number formatting ((714) 555-1234)
     * @param str String to be converted
     * @return Converted String
     */
    public static String formatStringLikePhoneNumber(String str){
        if(StringUtilities.isNullOrEmpty(str)){
            return str;
        }
        //Format out everything else
        str = str.trim();
        str = StringUtilities.keepNumbersOnly(str);
        if(StringUtilities.isNullOrEmpty(str)){
            return str;
        }

        if(str.length() < 3){
           return str;
        }

        // >=3 && < 7
        if(str.length() >= 3 && str.length() < 7){
            try {
                String phoneRawString = str;

                if(str.length() >= 4 ){
                    //Length 4 - 7
                    java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1}");
                    String[] phoneNumArr = {
                            phoneRawString.substring(0, 3),
                            phoneRawString.substring(3)};

                    String formatted = phoneMsgFmt.format(phoneNumArr);
                    return formatted;
                } else {
                    //Length 3
                    return str; //This is to prevent 'stuck states' when they backspace
                }

            } catch (Exception e){}
        }

        // >=7 && < 10
        if(str.length() >= 7 && str.length() <= 10){
            try {
                String phoneRawString = str;
                String formatted = null;
                java.text.MessageFormat phoneMsgFmt = new java.text.MessageFormat("({0}) {1} - {2}");
                String[] phoneNumArr = {
                        phoneRawString.substring(0, 3),
                        phoneRawString.substring(3,6),
                        phoneRawString.substring(6)};
                formatted = phoneMsgFmt.format(phoneNumArr);
                return formatted;

            } catch (Exception e){}
        }

        // >=3 && < 7
        if(str.length() > 10){
            //check if the first number is one, if it is, sub out the first number
            String testFirst = str.substring(0, 1);
            if(testFirst.equalsIgnoreCase("1")){
                try {
                    String phoneRawString = str;
                    java.text.MessageFormat phoneMsgFmt=new java.text.MessageFormat("+{0}({1}) {2}-{3}");
                    String[] phoneNumArr={
                            phoneRawString.substring(0, 1),
                            phoneRawString.substring(1, 4),
                            phoneRawString.substring(4,7),
                            phoneRawString.substring(7)};

                    String formatted = phoneMsgFmt.format(phoneNumArr);
                    return formatted;
                } catch (Exception e){}
            } else {
                try {
                    String phoneRawString = str;
                    java.text.MessageFormat phoneMsgFmt=new java.text.MessageFormat("({0}) {1} - {2}");
                    String[] phoneNumArr={
                            phoneRawString.substring(0, 3),
                            phoneRawString.substring(3,6),
                            phoneRawString.substring(6)};

                    String formatted = phoneMsgFmt.format(phoneNumArr);
                    return formatted;
                } catch (Exception e){}
            }


        }
        return str;
    }
    /**
     * Checks if a string passed in is numeric (IE pass in "2" and it will return true)
     * @param str String to check against
     * @return Return true if it is numeric, false if it is not
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        final int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    /**
     * Increments a String (IE, converts a to b)
     * @param str String to convert
     * @return Returns a converted String
     */
    public static String incrementString(String str){
        if(StringUtilities.isNullOrEmpty(str)){
            return str;
        }
        if(str.equalsIgnoreCase("#")){
            return "A";
        }
        StringBuilder sb = new StringBuilder();
        for(char c: str.toCharArray()){
            sb.append(++c);
        }
        return sb.toString();
    }

    /**
     * Convert a String to a Java URI (Not Uri)
     * @param path String path to convert
     * @return converted java.net.URI
     */
    public static java.net.URI convertStringToJavaUri(String path){
        try {
            java.net.URI toReturn = new java.net.URI(path);
            return toReturn;
        } catch (URISyntaxException e){
            return null;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Convert a String to an Android Uri (Not URI)
     * @param path String path to convert
     * @return android.net.Uri
     */
    public static android.net.Uri convertStringToAndroidUri(String path){
        try {
            android.net.Uri toReturn = Uri.parse(path);
            return toReturn;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Convert an Android Uri to a String
     * @param uri android.net.Uri to convert
     * @return String
     */
    public static String convertAndroidUriToString(android.net.Uri uri){
        return uri.toString();
    }

    /**
     * Convert a Java URI to a String (Not Uri)
     * @param uri java.net.URI to convert
     * @return String
     */
    public static String convertJavaUriToString(java.net.URI uri){
        return uri.toString();
    }


    /**
     * Convert a Java URI to an Android Uri
     * @param uri Java URI
     * @return Android Uri
     */
    public static android.net.Uri convertJavaURIToAndroidUri(java.net.URI uri){
        String ss = StringUtilities.convertJavaUriToString(uri);
        return StringUtilities.convertStringToAndroidUri(ss);
    }

    /**
     * Using code from this link: http://hmkcode.com/android-display-selected-image-and-its-real-path/
     * This method will return the absolute path Android.net.Uri.
     * NOTE!!! THIS DOES NOT SUPPORT API 10 OR BELOW!!! IF YOU NEED TO WORK WITH THAT, CHECK LINK ABOVE
     * @param context Context
     * @param uri Uri to check
     * @return String for the absolute path
     */
    public static String getAbsolutePath(Context context, android.net.Uri uri) {
        if(Build.VERSION.SDK_INT >= 19){
            try {
                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(uri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                String result = null;

                CursorLoader cursorLoader = new CursorLoader(
                        context,
                        uri, proj, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if(cursor != null){
                    int column_index =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    result = cursor.getString(column_index);
                }
                return result;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }
    //On to different System Utilities
    public static String getDataDirectoryLocation(){
        return (Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
    }
    /**
     * Checks if it is a valid email address. IE, no.com would not pass but bob@gmail.com would.
     * @param target
     * @return
     */
    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            boolean booleanToReturn = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            return booleanToReturn;
        }
    }


    public static String removeSpaces(String str){
        if(str == null){
            return null;
        }
        str.replace(" ", "");
        str = str.trim();
        return  str;
    }

    /**
     * Encode a URI String using a regex
     * @param uriToEncode The String URI to encode
     * @return Returns an encoded URI String
     */
    public static String encodeURIStringWithRegex(String uriToEncode){
        try {
            //TEST
            uriToEncode = uriToEncode.replaceAll(StringUtilities.REGEX_WEB_URL_ENCODING, uriToEncode);

            return uriToEncode;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Formats by adding a hyphen for every 4 numbers (IE like a credit card)
     * @param s Charsequence being altered.
     * @return Return an altered String with hyphens in it
     */
    public static String formatNumbersAsCreditCard(CharSequence s) {
        int groupDigits = 0;
        String tmp = "";
        for (int i = 0; i < s.length(); ++i) {
            tmp += s.charAt(i);
            ++groupDigits;
            if (groupDigits == 4) {
                if(groupDigits == 16){
                } else {
                    tmp += "-";
                }
                groupDigits = 0;
            }
        }
        if(tmp.length() == 20){
            tmp = tmp.substring(0, tmp.length()-1); //Get rid of last digit
        }
        return tmp;
    }

    /**
     * Checks if the passed String is null or empty
     * @param t object to check
     * @return boolean, true if it is null or empty, false if it is not.
     */
    public static <T> boolean isNullOrEmpty(T t){
        if(t == null){
            return true;
        }
        String str = t.toString();
        if(str.isEmpty()){
            return true;
        }
        if(str.length() == 0){
            return true;
        }
        return false;
    }

    /**
     * Checks if the passed String is null or empty
     * @param str String to check
     * @return Boolean, true if it is null or empty, false if it is not.
     */
    public static boolean isNullOrEmpty(String str){
        if(str == null){
            return true;
        }
        if(str.isEmpty()){
            return true;
        }
        if(str.length() == 0){
            return true;
        }
        if(str.equalsIgnoreCase(" ")){
            return true;
        }
        return false;
    }

    /**
     * Builds a String via the passed String array. If the individual String is null or empty,
     * it skips it. If the delimiter is empty, skips that too
     * @param args String array to use
     * @param delimiter Delimiter to use (IE , or a space or _ or / or | )
     * @return Fully completed and written String. Example:
     *         String str = buildAStringFromUnknowns(new String[]{"2016", "07", "04"}, "/");
     *         str would print as: "2016/07/04"
     */
    public static String buildAStringFromUnknowns(String[] args, String delimiter){
        StringBuilder sb = new StringBuilder();
        sb.append(""); //So that it will always return something
        try {
            for (int i = 0; i < args.length; i++) {
                String str = args[i];

                //Boot out nulls and blanks
                if (str == null) {
                    continue;
                }
                if (str.equalsIgnoreCase("")) {
                    continue;
                }

                sb.append(str);
                if (i < args.length - 1) {
                    boolean checkNext = true;
                    try {
                        String str1 = args[(i+1)];
                        if(str1 == null){
                            checkNext = false;
                        } else {
                            if (str1.isEmpty()) {
                                checkNext = false;
                            }
                        }
                    } catch (Exception e){

                    }
                    if(checkNext) {
                        //Format via delimiter
                        if (delimiter != null) {
                            sb.append(delimiter);
                        }
                    }
                }
            }
        } catch (Exception e){}

        return sb.toString();
    }

    /**
     * From - http://stackoverflow.com/questions/19985286/convert-content-uri-to-actual-path-in-android-4-4
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * OTHER file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (StringUtilities.isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                }
                // DownloadsProvider
                else if (StringUtilities.isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (StringUtilities.isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * Checks if the URI is an external downloads document
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and OTHER file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}

