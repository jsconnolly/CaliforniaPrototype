package com.hotb.pgmacdesign.californiaprototype.networking;

import com.hotb.pgmacdesign.californiaprototype.pojos.CALocation;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by pmacdowell on 2017-02-27.
 */

public interface ApiInterface {

    //BASE URL = http://ec2-54-241-144-61.us-west-1.compute.amazonaws.com
    //Empty version for now, if updated in the future, just insert V'X' here
    static final String VERSION = "";
    //User Endpoints path
    static final String USERS = "/users";
    //Endpoints
    static final String SIGNIN_EMAIL = "/signin";
    static final String SIGNIN_PHONE = "/phoneSignin";
    static final String RESET_PASSWORD = "/resetPassword";
    static final String CHANGE_PASSWORD = "/changePassword";
    static final String FORGOT_PASSWORD = "/forgotPassword";
    static final String PHONE_VERIFICATION = "/phoneCode";
    static final String EMAIL = "/email";
    static final String PHONE = "/phone";
    static final String LOCATIONS = "/locations";


    /**
     * Register User with new account
     *
     * @param body {@link CAUser} Required params: (email && password) or (phoneNumber && password)
     * @return {@link CAUser}
     */
    @POST(VERSION + USERS)
    Call<CAUser> registerUser(@Body CAUser body);

    /**
     * Login with email
     *
     * @param body {@link CAUser} Required params: (email && password)
     * @return {@link CAUser} Note, only the token will be returned in the user object
     */
    @POST(VERSION + USERS + SIGNIN_EMAIL)
    Call<CAUser> loginWithEmail(@Body CAUser body);

    /**
     * Login with phone
     *
     * @param body {@link CAUser} Required params: (phoneNumber && password)
     * @return {@link CAUser} Note, only the token will be returned in the user object
     */
    @POST(VERSION + USERS + SIGNIN_PHONE)
    Call<CAUser> loginWithPhone(@Body CAUser body);

    /**
     * Update a user in the DB
     *
     * @param body {@link CAUser} Required params: Fields to update
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @PUT(VERSION + USERS + "/{userId}")
    Call<CAUser> updateUser(@Header("token") String authToken,
                            @Path("userId") String userId,
                            @Body CAUser body);

    /**
     * Reset a password
     *
     * @param body {@link CAUser} Required params: email && password
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @PUT(VERSION + USERS + RESET_PASSWORD)
    Call<CAUser> resetPassword(@Header("token") String authToken,
                               @Body CAUser body);

    /**
     * Change a password
     *
     * @param body {@link CAUser} Required params: id  &&  password
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @PUT(VERSION + USERS + CHANGE_PASSWORD)
    Call<CAUser> changePassword(@Header("token") String authToken,
                                @Body CAUser body);

    /**
     * Change a password
     *
     * @param body {@link CAUser} Required params: id  &&  password
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @POST(VERSION + USERS + FORGOT_PASSWORD)
    Call<Void> forgotPassword(@Body CAUser body);

    /**
     * Send an SMS for verification
     * @param body {@link CAUser} Required params: phone
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @POST(VERSION + USERS + PHONE_VERIFICATION)
    Call<Void> phoneVerification(@Body CAUser body);

    /**
     * Get a user via their email
     *
     * @return {@link CAUser} If the user exists and the token is not expired, returns user obj
     */
    @GET(VERSION + USERS + EMAIL + "/{email}")
    Call<CAUser> getUserByEmail(@Header("token") String authToken,
                                @Path("email") String email);

    /**
     * Get a user via their phoneNumber
     *
     * @return {@link CAUser} If the user exists and the token is not expired, returns user obj
     */
    @GET(VERSION + USERS + PHONE + "/{phone}")
    Call<CAUser> getUserByPhone(@Header("token") String authToken,
                                @Path("phone") String phone);

    /**
     * Get a user via their user id
     *
     * @return {@link CAUser} If the user exists and the token is not expired, returns user obj
     */
    @GET(VERSION + USERS + "/{id}")
    Call<CAUser> getUserById(@Header("token") String authToken,
                             @Path("id") String id);

    /**
     * Add a location to a user
     *
     * @param authToken Auth token
     * @param userId    String userId
     * @param body      {@link CALocation} Required Fields: displayName, coordinates, alertRadius,
     *                  enablePushNotifications, enableSMS, enableEmail
     * @return {@link CAUser} User is updated with the location object added into their locations array
     */
    @PUT(VERSION + USERS + "/{userId}" + LOCATIONS)
    Call<CAUser> addLocation(@Header("token") String authToken,
                             @Path("userId") String userId,
                             @Body CALocation body);


    /**
     * Update a location attached to a user
     *
     * @param authToken Auth token
     * @param userId    String userId
     * @param body      {@link CALocation} Required Fields: displayName, coordinates, alertRadius,
     *                  enablePushNotifications, enableSMS, enableEmail
     * @return {@link CAUser} Returns an empty object if it succeeds, error if it did not
     */
    @PUT(VERSION + USERS + "/{userId}" + LOCATIONS + "/{locationId}")
    Call<CAUser> updateLocation(@Header("token") String authToken,
                                @Path("userId") String userId,
                                @Path("locationId") String locationId,
                                @Body CALocation body);


    /**
     * Update a location attached to a user
     *
     * @param authToken Auth token
     * @param userId    String userId
     * @return {@link CAUser} Returns an empty object if it succeeds, error if it did not
     */
    @DELETE(VERSION + USERS + "/{userId}" + LOCATIONS + "/{locationId}")
    Call<CAUser> deleteLocation(@Header("token") String authToken,
                                @Path("userId") String userId,
                                @Path("locationId") String locationId);
}

