package com.hotb.pgmacdesign.californiaprototype.networking;

import android.support.annotation.NonNull;

import com.hotb.pgmacdesign.californiaprototype.BuildConfig;
import com.hotb.pgmacdesign.californiaprototype.utilities.MiscUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by pmacdowell on 8/25/2016.
 */
public class RetrofitClient {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DEFAULT_DATE_FORMAT_WITHOUT_MILLISECONDS = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private String urlBase;
    private Map<String, String> headers;
    private HttpLoggingInterceptor.Level logLevel;
    private int readTimeout, writeTimeout;
    private String dateFormat;
    private Class serviceInterface;

    /**
     * Constructor
     */
    private <T> RetrofitClient(RetrofitClient.Builder builder) {
        this.urlBase = builder.builder_urlBase;
        this.headers = builder.builder_headers;
        this.logLevel = builder.builder_logLevel;
        this.dateFormat = builder.builder_dateFormat;
        this.readTimeout = builder.builder_readTimeout;
        this.writeTimeout = builder.builder_writeTimeout;
        this.serviceInterface = builder.builder_serviceInterface;
    }

    /**
     * Build a Workable service client Client
     * This should be used after the class is initialized and the setters are all set
     * @param <T> Service Interface class
     * @return Service client for making calls. Will be directly linked to the Interface passed in
     *         as well as to its calls.
     * @throws IllegalArgumentException If the BASE_URL String does not end in a forward slash
     *                                  (/), this will throw an illegal argument exception.
     */
    public <T> T buildServiceClient() throws IllegalArgumentException{
        T t = this.buildRetrofitClient();
        return t;
    }

    /**
     * Build headers from the headers map.
     * {@link Headers}
     * @return
     */
    private Headers buildHeaders(){
        Headers.Builder builder = new Headers.Builder();
        if(MiscUtilities.isMapNullOrEmpty(headers)){
            return builder.build();
        } else {
            for (Map.Entry<String, String> myMap : headers.entrySet()) {
                String key = myMap.getKey();
                String value = myMap.getValue();
                if (!StringUtilities.isNullOrEmpty(key) &&
                        !StringUtilities.isNullOrEmpty(value)) {
                    builder.add(key, value);
                }
            }
            return builder.build();
        }
    }
    /**
     * This builds a client that will be used for network calls
     */
    private  <T> T buildRetrofitClient(){
        //First create the interceptor, which will be used in the Retrofit call
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        //This is where you would add headers if need be. An example would be:
                        .addHeader("Content-Type", "application/json")
                        .build(); //Finally, build it
                return chain.proceed(newRequest);
            }
        };

        /*
        //First create the interceptor, which will be used in the Retrofit call
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.headers(buildHeaders());
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        };
         */
        //Next, we set the logging level
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(logLevel);

        //Next, create the OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(readTimeout < 0){
            readTimeout = Builder.SIXTY_SECONDS;
        }
        if(writeTimeout < 0){
            writeTimeout = Builder.SIXTY_SECONDS;
        }
        if(readTimeout > 0){
            builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        }
        if(writeTimeout > 0){
            builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        }

        //Add logging and interceptor
        builder.addInterceptor(interceptor);
        builder.addInterceptor(logging);

        //Configure SSL
        builder = configureClient(builder);

        //Build the client
        OkHttpClient client = builder.build();

        //Create the retrofit object, which will use the variables/ objects we have created above
        Retrofit.Builder myBuilder = new Retrofit.Builder();
        myBuilder.baseUrl(urlBase);
        myBuilder.addConverterFactory(new CustomConverterFactory());
        myBuilder.client(client);
        Retrofit retrofit = myBuilder.build();

        //Now that it is built, create the service client, which references the interface we made
        Object obj = retrofit.create(serviceInterface);
        T serviceClient = null;
        try {
            serviceClient = (T) obj;
        } catch (ClassCastException e){
            e.printStackTrace();
        }

        return serviceClient;
    }

    /**
     * This class will configure the OkHttpClient to add things like SSL, certs, etc.
     * @param builder The builder that will be altered and returned
     * @return Altered builder method.
     * For more information on this, please see
     * {@link OkHttpClient.Builder} <-- sslSocketFactory
     */
    private OkHttpClient.Builder configureClient(final OkHttpClient.Builder builder) {

        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, trustManager);

        } catch (KeyManagementException kme){
            kme.printStackTrace();
        } catch (NoSuchAlgorithmException nsa){
            nsa.printStackTrace();
        } catch (KeyStoreException kse){
            kse.printStackTrace();
        } catch (IllegalStateException ise){
            ise.printStackTrace();
        }

        return builder;
    }

    public <T> Builder newBuilder(@NonNull final Class<T> serviceInterface, @NonNull String urlBase){
        return new Builder(serviceInterface, urlBase);
    }

    //Builder class below
    public static final class Builder <T> {

        String builder_urlBase;
        Class<T> builder_serviceInterface;
        Map<String, String> builder_headers;
        HttpLoggingInterceptor.Level builder_logLevel;
        int builder_readTimeout, builder_writeTimeout;
        String builder_dateFormat;
        static final int SIXTY_SECONDS = (int)(1000*60);

        /**
         * Constructor visible to the outside
         * @param serviceInterface Service interface class. This should be like the ones shown here:
         *                         https://guides.codepath.com/android/Consuming-APIs-with-Retrofit#define-the-endpoints
         * @param urlBase String url base to use, IE, http://www.myapi.com
         *                This excludes any paths and any versioning here (IE /V1 and no /users/...)
         */
        public Builder(@NonNull final Class<T> serviceInterface, @NonNull String urlBase){
            this.builder_urlBase = urlBase;
            this.builder_serviceInterface = serviceInterface;
            this.builder_headers = null;
            if(BuildConfig.DEBUG) {
                this.setLogLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                this.setLogLevel(HttpLoggingInterceptor.Level.NONE);
            }
            this.setDateFormat(DEFAULT_DATE_FORMAT);
            this.setTimeouts(SIXTY_SECONDS, SIXTY_SECONDS);
        }

        /**
         * Set the logging level. Log level is text displayed in the logcat for testing / debugging
         * For more info, see {@link HttpLoggingInterceptor.Level}
         * @param logLevel
         */
        public Builder setLogLevel(HttpLoggingInterceptor.Level logLevel){
            if(logLevel != null){
                this.builder_logLevel = logLevel;
            }
            return this;
        }

        /**
         * Set the headers. This would be where you would send in a map with header Strings.
         * Samples would be a map containing types like these:
         * <"Authentication", "password123">
         * <"Content-Type", "application/json">
         * <"Content-Type", "multipart/form-data">
         * @param headers
         */
        public Builder setHeaders(Map<String, String> headers){
            if(!MiscUtilities.isMapNullOrEmpty(headers)) {
                this.builder_headers = headers;
            }
            return this;
        }

        /**
         * Set the read and write timeout IN MILLISECONDS
         * @param readTimeoutInMilliseconds Read timeout (60000 == 1 minute). Pass 0 for no timeout
         * @param writeTimeoutInMilliseconds Write timeout (60000 == 1 minute). Pass 0 for no timeout
         */
        public Builder setTimeouts(int readTimeoutInMilliseconds, int writeTimeoutInMilliseconds){
            builder_readTimeout = readTimeoutInMilliseconds;
            builder_writeTimeout = writeTimeoutInMilliseconds;
            return this;
        }

        /**
         * Set the data format.
         * @param dateFormat Date format String structured similar to this:
         *                   "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
         *                   If not set or null passed, it will be set to default listed above.
         */
        public Builder setDateFormat(String dateFormat){
            if(!StringUtilities.isNullOrEmpty(dateFormat)) {
                this.builder_dateFormat = dateFormat;
            }
            return this;
        }

        /**
         * Simple setter method for those who are too lazy to make a hashmap containing
         * "Content-Type", "application/json" as headers. Just call this and it will set it.
         * Keep in mind this will replace other headers you have in place so call this before
         * you make any other header alterations (IE with a client id or auth token)
         */
        public Builder callIsJSONFormat(){
            Map<String, String> myMap = new HashMap<>();
            myMap.put("Content-Type", "multipart/form-data");
            this.builder_headers = myMap;
            return this;
        }

        /**
         * Simple setter method for those who are too lazy to make a hashmap containing
         * "Content-Type", "multipart/form-data" as headers. Just call this and it will set it.
         * Keep in mind this will replace other headers you have in place so call this before
         * you make any other header alterations (IE with a client id or auth token)
         */
        public Builder callIsMultipartFormat(){
            Map<String, String> myMap = new HashMap<>();
            myMap.put("Content-Type", "multipart/form-data");
            this.builder_headers = myMap;
            return this;
        }

        /**
         * Shortcut for getting a map of the content-type application json strings.
         * @return Map<String, String> containing one set of "Content-Type", "application/json"
         */
        public static Map<String, String> getApplicationJSONMap(){
            Map<String, String> myMap = new HashMap<>();
            myMap.put("Content-Type", "application/json");
            return myMap;
        }

        /**
         * Simple setter method for those who are too lazy to make a hashmap containing
         * "Content-Type", "multipart/form-data" as headers. Just call this and it will set it
         */
        public static Map<String, String> getMultipartFormat(){
            Map<String, String> myMap = new HashMap<>();
            myMap.put("Content-Type", "multipart/form-data");
            return myMap;
        }

        /**
         * Builds a Retrofit client and returns it
         * @return
         */
        public RetrofitClient build(){
            return new RetrofitClient(this);
        }
    }


}



