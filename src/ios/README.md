#iOS

This is the iOS Documentation for the California Prototype Emergency Services application. For a more detailed explanation of the project, see the top-level ReadMe file.

This ReadMe will cover the application from a non-developer perspective and how to run it on the iOS simulator via Apple's Xcode IDE. 

##Application Documentation
This section covers the overall application and how to install and run it on your local desktop or laptop. 

###Mobile device installation
Due to Apple's security restrictions, a version of the app cannot be installed on your mobile device without special certificates being created. If you wish to install the app on your Apple iPhone device, download Apple's official TestFlight app and contact HotB Software with the following information:
	
* The Apple ID you have associated with your iPhone App Store Account.

###Desktop/Laptop installation
The California Prototype app can be run in Apple's iOS Simulator using the following steps:

1. Download and install Apple's latest version of Xcode from the Mac App Store on your Apple laptop or desktop from this link: <a href='https://itunes.apple.com/us/app/xcode/id497799835?mt=12&ls=1'> Xcode </a> 
2. Once installed, run Xcode and open the CaliforniaPrototype.xcodeproj in the iOS folder.
3. You can run the application in one of two ways:
	* Go to and select Product -> Run (the simulator should launch automatically and run the app)
	* Tap on the play arrow in the top left of the Xcode window (the simulator should launch automatically and run the app)

###Limitations
The simulator will prompt you to allow access to your location, which you should allow, but it cannot truly identify your location, so we have defaulted the location to San Francisco.

You can learn more about interacting with the iOS Simulator here: <a href='https://developer.apple.com/library/content/documentation/IDEs/Conceptual/iOS_Simulator_Guide/Introduction/Introduction.html'> About iOS Simulator </a>