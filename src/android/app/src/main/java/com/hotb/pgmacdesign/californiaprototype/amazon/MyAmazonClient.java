package com.hotb.pgmacdesign.californiaprototype.amazon;

import android.os.AsyncTask;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;

/**
 * Serves as a utility class for the Amazon client info
 * Links:
 * 1) https://github.com/aws/aws-sdk-android/blob/master/aws-android-sdk-sns/src/main/java/com/amazonaws/services/sns/AmazonSNSClient.java
 * 2) http://docs.aws.amazon.com/sns/latest/dg/mobile-push-gcm.html
 * 3) http://docs.aws.amazon.com/sns/latest/dg/mobile-push-send-register.html
 * 4) http://docs.aws.amazon.com/mobile/sdkforandroid/developerguide/setup.html
 * 5) www.mobileaws.com/2015/03/25/amazon-sns-push-notification-tutorial-android-using-gcm/
 * 6) http://docs.aws.amazon.com/sns/latest/dg/mobile-push-gcm.html
 * 7) http://docs.aws.amazon.com/sns/latest/dg/mobile-push-gcm.html#create-project-gcm
 * 8) --> http://stackoverflow.com/questions/38300450/fcm-with-aws-sns
 * 9) --> https://console.firebase.google.com/project/california-prototype-hotb/notification
 *
 * Created by pmacdowell on 2017-02-14.
 */
public class MyAmazonClient {

    private static AWSCredentials credentials;
    private static AmazonSNSClient client;

    private static final String MY_ENDPOINT = "???????????";
    private static final String MY_PROTOCOL = "???????????";

    static {
        credentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return MyApplication.getContext().getResources()
                        .getString(R.string.aws_access_id);
            }

            @Override
            public String getAWSSecretKey() {
                return MyApplication.getContext().getResources()
                        .getString(R.string.aws_client_secret);
            }
        };
        client = new AmazonSNSClient(credentials);
    }

    public static void subscribeToTopic(final String topic){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    client.subscribe(buildSubscribeRequest(topic));
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public static void unsubscribeToTopic(final String topic){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    client.unsubscribe(buildUnsubscribeRequest(topic));
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }


    private static SubscribeRequest buildSubscribeRequest(String topic){
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setEndpoint(MY_ENDPOINT);
        subscribeRequest.setProtocol(MY_PROTOCOL);
        subscribeRequest.setTopicArn(topic);
        return subscribeRequest;
    }

    private static UnsubscribeRequest buildUnsubscribeRequest(String topic){
        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
        unsubscribeRequest.setSubscriptionArn(topic);
        return unsubscribeRequest;
    }
}
