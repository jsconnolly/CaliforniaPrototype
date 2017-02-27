package com.hotb.pgmacdesign.californiaprototype.networking;

import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;

import retrofit2.Call;
import retrofit2.http.Body;
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

    /**
     * Register User with new account
     * @param body {@link CAUser} Required params: (email && password) or (phoneNumber && password)
     * @return {@link CAUser}
     */
    @POST(VERSION + USERS)
    Call<CAUser> registerUser(@Body CAUser body);

    /**
     * Login with email
     * @param body {@link CAUser} Required params: (email && password)
     * @return {@link CAUser} Note, only the token will be returned in the user object
     */
    @POST(VERSION + USERS + SIGNIN_EMAIL)
    Call<CAUser> loginWithEmail(@Body CAUser body);

    /**
     * Login with phone
     * @param body {@link CAUser} Required params: (phoneNumber && password)
     * @return {@link CAUser} Note, only the token will be returned in the user object
     */
    @POST(VERSION + USERS + SIGNIN_PHONE)
    Call<CAUser> loginWithPhone(@Body CAUser body);

    /**
     * Update a user in the DB
     * @param body {@link CAUser} Required params: Fields to update
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @PUT(VERSION + USERS + "{userId}")
    Call<CAUser> updateUser(@Header("token") String authToken,
                            @Path("userId") String userId,
                            @Body CAUser body);

    /**
     * Reset a password
     * @param body {@link CAUser} Required params: email && password
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @PUT(VERSION + USERS + RESET_PASSWORD)
    Call<CAUser> resetPassword(@Header("token") String authToken,
                               @Body CAUser body);

    /**
     * Send an SMS for verification
     * @param body {@link CAUser} Required params: phone
     * @return {@link CAUser} Returns an empty object if successful, contains Error string if not
     */
    @PUT(VERSION + USERS + "{userId}")
    Call<CAUser> phoneVerification(@Header("token") String authToken,
                            @Path("userId") String userId,
                            @Body CAUser body);

    /*


    POST - phoneVerification
    POST - changePassword
    GET - getUserByEmail
    GET - getUserById
    GET - getUserByPhone
    PUT - addLocation
    PUT - updateLocation
    PUT - addContact
    PUT - updateContact
     */
}

