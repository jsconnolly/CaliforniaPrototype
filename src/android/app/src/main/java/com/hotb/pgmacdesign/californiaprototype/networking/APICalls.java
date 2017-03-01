package com.hotb.pgmacdesign.californiaprototype.networking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.pojos.CALocation;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAMasterObject;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;
import com.hotb.pgmacdesign.californiaprototype.utilities.NetworkUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pmacdowell on 2017-02-27.
 */

public class APICalls {

    private Context context;
    private RetrofitClient retrofitClient;
    private ApiInterface apiInterface;
    private OnTaskCompleteListener listener;

    /////////////////////////////////
    // Initializers / Constructors //
    /////////////////////////////////

    public APICalls(@NonNull Context context, @NonNull OnTaskCompleteListener listener) {
        this.context = context;
        this.initClient();
        this.listener = listener;
    }

    /**
     * Client and interface initializer
     */
    private void initClient() {
        if (retrofitClient == null) {
            RetrofitClient.Builder builder = new RetrofitClient.Builder(
                    ApiInterface.class, Constants.STAGING_SERVER_URL);
            builder.callIsJSONFormat();
            retrofitClient = builder.build();
        }
        if (apiInterface == null) {
            apiInterface = retrofitClient.buildServiceClient();
        }
    }

    ///////////////
    // API Calls///
    ///////////////

    /**
     * Register a User
     *
     * @param user {@link CAUser}
     */
    public void registerUser(CAUser user) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        //Initialize the call
        Call<CAUser> call = apiInterface.registerUser(user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Update an existing user
     *
     * @param user {@link CAUser}
     */
    public void updateUser(CAUser user) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        //Initialize the call
        Call<CAUser> call = apiInterface.updateUser(getAuthToken(), getUserId(), user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     listener.onTaskComplete(responseObject, Constants.TAG_EMPTY_OBJECT);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Verify a phone number by sending SMS to user
     *
     * @param phoneNumber String phoneNumber
     */
    public void phoneVerification(String phoneNumber) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        CAUser user = new CAUser();
        user.setPhone(phoneNumber);
        //Initialize the call
        Call<CAUser> call = apiInterface.phoneVerification(user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     listener.onTaskComplete(responseObject, Constants.TAG_EMPTY_OBJECT);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Reset a password
     *
     * @param email email
     * @param pw    password
     */
    public void resetPassword(String email, String pw) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        CAUser user = new CAUser();
        user.setEmail(email);
        user.setPassword(pw);
        //Initialize the call
        Call<CAUser> call = apiInterface.resetPassword(getAuthToken(), user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     listener.onTaskComplete(responseObject, Constants.TAG_EMPTY_OBJECT);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * change a password
     *
     * @param id UserId
     * @param pw password
     */
    public void changePassword(String id, final String pw) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        CAUser user = new CAUser();
        user.setId(id);
        user.setPassword(pw);
        //Initialize the call
        Call<CAUser> call = apiInterface.changePassword(getAuthToken(), user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                     responseObject.setPassword(pw);
                                     persistData(responseObject);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * forgot a password, send an email for reset
     *
     * @param email email
     */
    public void forgotPassword(String email) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        CAUser user = new CAUser();
        user.setEmail(email);
        //Initialize the call
        Call<Void> call = apiInterface.forgotPassword(user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<Void>() {
                         @Override
                         public void onResponse(Call<Void> call, Response<Void> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response),
                                             Constants.TAG_FORGOT_PASSWORD);
                                 } else {
                                     listener.onTaskComplete(null,
                                             Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     listener.onTaskComplete(null, Constants.TAG_EMPTY_OBJECT);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(),
                                             Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<Void> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Login with email and password
     *
     * @param email String email
     * @param pw    String password
     *              NOTE: This persists the authToken
     */
    public void loginWithEmail(String email, final String pw) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        CAUser user = new CAUser();
        user.setEmail(email);
        user.setPassword(pw);
        //Initialize the call
        Call<CAUser> call = apiInterface.loginWithEmail(user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     responseObject.setPassword(pw);
                                     persistData(responseObject);
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Login with phone number and 6 digit password
     *
     * @param phoneNumber phone Number
     * @param pw          password (6 digit code)
     *                    NOTE: This persists the authToken
     */
    public void loginWithPhone(String phoneNumber, final String pw) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        CAUser user = new CAUser();
        user.setPhone(phoneNumber);
        user.setPassword(pw);
        //Initialize the call
        Call<CAUser> call = apiInterface.registerUser(user);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     persistData(responseObject);
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                     responseObject.setPassword(pw);
                                     persistData(responseObject);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Get a user by their user ID
     *
     * @param id userId to query with
     */
    public void getUserById(String id) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        //Initialize the call
        Call<CAUser> call = apiInterface.getUserById(getAuthToken(), id);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     persistData(responseObject);
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Get a user by their email
     *
     * @param email email to query with
     */
    public void getUserByEmail(String email) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        //Initialize the call
        Call<CAUser> call = apiInterface.getUserByEmail(getAuthToken(), email);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     persistData(responseObject);
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Get a user by their phone number
     *
     * @param phoneNumber phoneNumber to query with
     */
    public void getUserByPhone(String phoneNumber) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        //Initialize the call
        Call<CAUser> call = apiInterface.getUserByPhone(getAuthToken(), phoneNumber);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     persistData(responseObject);
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }


    /**
     * Add a location to a user object
     *
     * @param locationObject {@link CALocation}
     */
    public void addLocation(CALocation locationObject) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        //Initialize the call
        Call<CAUser> call = apiInterface.addLocation(getAuthToken(), getUserId(), locationObject);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    /**
     * Update location on a user object
     *
     * @param locationObject {@link CALocation}
     */
    public void updateLocation(CALocation locationObject) {
        if (!checkForInternetConnectivity()) {
            return;
        }
        //Initialize the call
        String id = null;
        try {
            id = locationObject.getId();
        } catch (Exception e){
            e.printStackTrace();
        }
        Call<CAUser> call = apiInterface.updateLocation(getAuthToken(),
                getUserId(), id, locationObject);
        //Enqueue the call asynchronously
        call.enqueue(new Callback<CAUser>() {
                         @Override
                         public void onResponse(Call<CAUser> call, Response<CAUser> response) {
                             //Check for response or not
                             if (!response.isSuccessful()) {
                                 if (checkForError(response) != null) {
                                     listener.onTaskComplete(checkForError(response), Constants.TAG_API_ERROR);
                                 } else {
                                     listener.onTaskComplete(null, Constants.TAG_API_CALL_FAILURE);
                                 }
                             } else {
                                 //Response was successful. Send back via listener
                                 try {
                                     CAUser responseObject = (CAUser) response.body();
                                     listener.onTaskComplete(responseObject, Constants.TAG_CA_USER);
                                 } catch (Exception e) {
                                     listener.onTaskComplete(e.getMessage(), Constants.TAG_API_CALL_FAILURE);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<CAUser> call, Throwable t) {
                             t.printStackTrace();
                             listener.onTaskComplete(t.getMessage(), Constants.TAG_API_CALL_FAILURE);
                         }
                     }
        );
    }

    ///////////
    // Utils //
    ///////////

    /**
     * Checks the response for an error. If the response contains one, it will attempt to parse it
     *
     * @param response Response from the API Endpoints
     * @return CAUser object {@link CAUser}
     */
    private CAUser checkForError(Response response) {
        if (response == null) {
            return null;
        }
        try {
            CAUser aUser = (CAUser) response.body();
            if (!StringUtilities.isNullOrEmpty(aUser.getError())) {
                return aUser;
            }
        } catch (Exception e) {
        }
        try {
            ResponseBody rb = response.errorBody();
            CAUser caUser = new Gson().fromJson(rb.string(), CAUser.class);
            if (StringUtilities.isNullOrEmpty(caUser.getError())) {
                return caUser;
            }
        } catch (Exception e) {
        }
        try {
            ResponseBody rb = response.errorBody();
            CAMasterObject caUser = new Gson().fromJson(rb.string(), CAMasterObject.class);
            if (StringUtilities.isNullOrEmpty(caUser.getError())) {
                CAUser user = new CAUser();
                user.setError(caUser.getError());
                return user;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Persist the data into Shared preferences
     *
     * @param responseObject Response from server. If null, will not proceed
     */
    public static void persistData(CAUser responseObject) {
        if (responseObject == null) {
            return;
        }

        String pw = responseObject.getPassword();
        String userId = responseObject.getId();
        String token = responseObject.getToken();
        String email = responseObject.getEmail();
        String phone = responseObject.getPhone();

        if (!StringUtilities.isNullOrEmpty(userId)) {
            MyApplication.getSharedPrefsInstance().save(Constants.USER_ID, userId);
        }
        if (!StringUtilities.isNullOrEmpty(token)) {
            MyApplication.getSharedPrefsInstance().save(Constants.AUTH_TOKEN, token);
        }
        if (!StringUtilities.isNullOrEmpty(email)) {
            MyApplication.getSharedPrefsInstance().save(Constants.USER_EMAIL, email);
        }
        if (!StringUtilities.isNullOrEmpty(phone)) {
            MyApplication.getSharedPrefsInstance().save(Constants.USER_PHONE_NUMBER, phone);
        }
        if (!StringUtilities.isNullOrEmpty(pw)){
            MyApplication.getSharedPrefsInstance().save(Constants.USER_PW, pw);
        }
        if(!StringUtilities.isNullOrEmpty(userId) && !StringUtilities.isNullOrEmpty(token)){
            //If this procs, it means it is a full login object, can parse and save
            MyApplication.getDatabaseInstance().persistObject(CAUser.class, responseObject);
        }
    }

    /**
     * Check for internet connection before making call
     * If no network connectivity, sends back response on the listener from here
     *
     * @return boolean. False if no network, true if has network
     */
    private boolean checkForInternetConnectivity() {
        boolean bool = NetworkUtilities.haveNetworkConnection(this.context);
        if (!bool) {
            listener.onTaskComplete(null, Constants.TAG_CONNECTIVITY_ISSUE);
        }
        return bool;
    }

    /**
     * Private get the auth token from shared prefs
     *
     * @return String auth token
     */
    private String getAuthToken() {
        String str = MyApplication.getSharedPrefsInstance().getString(Constants.AUTH_TOKEN, null);
        return str;
    }

    /**
     * Private get the user id from shared prefs
     *
     * @return String user id
     */
    private String getUserId() {
        String str = MyApplication.getSharedPrefsInstance().getString(Constants.USER_ID, null);
        return str;
    }

}
