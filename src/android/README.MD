#Android

This is the Android Documentation for the California Prototype Emergency Services application. For a more detailed explanation of the project, see the top-level ReadMe located <a href='https://github.com/HOTB-Software/CaliforniaPrototype/blob/master/README.md '>here</a>.

This ReadMe is separated into two parts: the first will cover the application from a non-developer perspective and how to run it while the second part will go over the low-level guide to installation into Android Studio and how to integrate it into your IDE. 

##Application Documentation
This section covers the overall application and how to install and run it on your device via the apk.

###Preparing your Android Device
Before you can install this on your phone, you will need to make a few changes on your device to allow installation. 

####Enabling USB Debugging Mode

There are already many well-written guides on enabling USB debugging on your device. Although the process changes slightly depending on the maker of your phone, it still follows the same basic principles:

   1. Find 'Build number' in your settings and tap on it 7 times until you see "You are now a developer"
	
   2. Go back and find Developer Settings and check the USB Debugging option. 

Here are two tutorials that cover this process in more detail:

 * <a href='https://www.howtogeek.com/129728/how-to-access-the-developer-options-menu-and-enable-usb-debugging-on-android-4.2/'>Option 1</a>   

 * <a href='http://www.phonearena.com/news/How-to-enable-USB-debugging-on-Android_id53909'>Option 2</a>
	
####Allowing Installation From Unknown Sources

Because this application is not installed in the <a href='https://play.google.com/store?hl=en'>Google Play Store</a>, the phone automatically blocks installation of the app as it cannot be sure it has passed the security checkpoints needed to prevent malware from being given to you. Luckily, not only is this code malware-free, but changing the settings to allow this is quite simple. Just follow these steps:

   1. Navigate into settings and tap on Security
	
   2. Uncheck the option for "Unknown Sources"
	
   3. You will receive a prompt alerting you to what you are doing, simply press Ok.
	
If you need more assistance here, see <a href='https://www.applivery.com/docs/troubleshooting/android-unknown-sources'>this link</a> or <a href='https://android.gadgethacks.com/how-to/android-basics-enable-unknown-sources-sideload-apps-0161947/'>this one</a> 

####Install the apk File
Now that the two things stopping us have been resolved, we can install the apk file. First, download the apk from <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/CaliforniaPrototype.apk'>this link</a>.

Download it on to your phone directly either by transferring it from another source or just downloading it from the link above while on your mobile device. 

Open the apk file in a <a href='http://www.tomsguide.com/us/pictures-story/518-best-android-file-managers.html#s3'>File Explorer</a> and install it. 

Note, you may also need to install a package installer if that does not work; it depends on the maker of your device. 

####Final Steps

You should be ready to go at this point. If you have any problems getting it installed, make sure know whether or not your phone is rooted as that can change the installation instructions. 

If you would prefer not to register for a new account, we have a test account you are welcome to log into for observational purposes:

<b>Email:</b> android@hotbsoftware.com

<b>Password:</b> password

##Code Documentation

This section covers how to run this code in <a href='https://developer.android.com/studio/index.html'>Android Studio</a> and install missing credentials.

###Installation

First, install Android Studio and update the SDK to the most current version available (At the time of writing this, the <a href='https://developer.android.com/about/versions/nougat/android-7.1.html'>newest API is 25, Nougat</a>)

Next, download this code either via the newest release and import it, or clone it via git using the url in this path of Github.

Last, you will need to install the missing credentials for this to work. This application has 2 credentials that are currently not pushed to the repository and are kept local; they both need to be created using your own credentials for this application to function properly. Once these two steps are completed, the project should be ready to build; I will outline both of them below.

####Google API Key

To obtain your Google api key, you will first need to log into the <a href='https://console.developers.google.com'>Google Dev Console</a> and create a project. 
Once created, click on 'Library' and add the <b>Google Maps Android API</b> and <b>Google Places API for Android</b> APIs. They are free to use for a large number of hits (25,000 per day). If you want to read more on their quotas, see <a href='https://developers.google.com/maps/faq'>this link</a>

Once you have the String api key, open up the gradle.properties file (underneath the Gradle Scripts directory in Android Studio). The file will have quite a bit of boiler-plate code in it. Somewhere in the file (anywhere), add this line (and replace the placeholder text):

```java
    googleApiKey=YOUR_API_KEY_GOES_HERE
```

####Google Services Json File

To obtain your google-services.json file, first go to the <a href='https://console.firebase.google.com'>Firebase Developer Console</a>. Import the Google project (option on the front) and once you have been refirected into the dev console, click on Crash Reporting on the left side. Follow the instructions to enable it and it will walk you through downloading the google-services.json file to be used in the project.

This file will get dropped into your 'app' directory.



##Libraries and Open Source Dependencies

This application makes use of many different open source dependencies and I highly reccommend you look into them as they offer enormous versatility to your application's functionality. 

Below is a list of each dependency and a link to it's documentation and source code:

* <a href='https://github.com/square/retrofit'>Retrofit 2.1.0</a>

* <a href='https://github.com/square/okhttp'>Okhttp 3.5.0</a>

* <a href='https://github.com/square/retrofit/tree/master/retrofit-converters/gson'>Gson Converter 2.1.0 (Via Retrofit)</a>

* <a href='https://github.com/google/gson'>Gson 2.8.0</a>

* <a href='https://github.com/jaredrummler/AnimatedSvgView'>Animated SVG View 1.0.0</a>

* <a href='https://github.com/square/picasso'>Picasso 2.5.2</a>

* <a href='https://github.com/daimajia/AndroidViewAnimations'>Android View Animations 2.4.0</a>

* <a href='https://github.com/yshrsmz/KeyboardVisibilityEvent'>Keyboard Visibility Event 2.0.1</a>

* <a href='https://github.com/realm/realm-java'>Realm 2.3.0</a>

	
	
