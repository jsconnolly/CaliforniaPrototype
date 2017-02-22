# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import time

# All documentation can be found here: https://developer.android.com/studio/test/monkeyrunner/MonkeyRunner.html

#Class for constants
class Constants(object): #Begin class {

	# The package's internal name
	package = 'com.hotb.pgmacdesign.californiaprototype'
	apkLocation = 'D:\\CaliforniaPrototype\\src\\android\\app\\build\\outputs\\apk\\app-debug.apk'
	startTestString = "android automated test. Start at: "
	endTestString = "android automated test. End at: "
	apkInstalledString = "APK Successfully installed = "
	imageFileExtension = 'png'
	writeToFileLocation = 'D:\\CaliforniaPrototype\\src\\android\\app\\build\\outputs\\apk\\shot1.png'
	secondsToPauseBeforeScreenshot = 3

	# Packages (First part to choose from)
	# activities
	activityPackage = ".activities"
	# fragments
	fragmentPackage = ".fragments"

	# Activities (Second Part to choose From)
	# Main Activity, this encompasses most activities that are not in the onboarding flow
	mainActivity = '.MainActivity'

	# Keycode Events. For full list, see https://developer.android.com/reference/android/view/KeyEvent.html
	Keycode_menu = 'KEYCODE_MENU'
	Keycode_pressDown = 'KEYCODE_MENU'
	Keycode_releaseDown = 'ACTION_UP'
	Keycode_back = 'KEYCODE_BACK'

	# MokeyDevice Key events. For full list, see https://developer.android.com/studio/test/monkeyrunner/MonkeyDevice.html
	Press_Down = "DOWN"
	Press_UP = "UP"
	Press_Down_And_Up = "DOWN_AND_UP"

	#Flags (Same documentation as events) for adding specifics to the keycodes
	Keycode_Flag_longPress = 'FLAG_LONG_PRESS'

	# Misc
	# Position in x,y of the editText box on the mapFragment: 400, 400
	# Position in x,y of the map box on the mapFragment: 700, 700

	# End class }

#Device methods class. Initialize with device object
class DeviceMethods(object): #Begin class {

	def __init__(self, virtualdevice):
		self.virtualdevice = virtualdevice

	# Start an activity with a package + activity declaration
	def startActivity(self, activityComponentInfo):
		return self.virtualdevice.startActivity(component=activityComponentInfo)

	# Press on the screen somewhere with a pre-defined button (IE home, back, etc)
	def pressButton(self, keycodeEvent, monkeyKeyEvent):
		return self.virtualdevice.press(keycodeEvent, monkeyKeyEvent)

	# Press on the screen somewhere with specific X Y coordinates
	def touchScreen(self, xCoord, yCoord, monkeyKeyEvent):
		return self.virtualdevice.touch(xCoord, yCoord, monkeyKeyEvent)

	# Long Press on the screen somewhere with specific X Y coordinates
	def longTouchScreen(self, xCoord, yCoord):
		return self.virtualdevice.touch(xCoord, yCoord, Constants.Press_Down)

	# End class }


# starting script
now = time.strftime("%c")
print(Constants.startTestString + time.strftime("%c"))

# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()
myDevice = DeviceMethods(device)

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
installSuccess = device.installPackage(Constants.apkLocation)
print(Constants.apkInstalledString + str(installSuccess))

# sets the name of the component to start
runComponent = Constants.package + Constants.activityPackage + Constants.mainActivity
runComponentPart2 = Constants.package + "/" + runComponent

# Runs the component (Activity)
myDevice.startActivity(runComponentPart2)

#Insert a sleep to prevent synchronization miss matches
MonkeyRunner.sleep(Constants.secondsToPauseBeforeScreenshot)

# Presses the Menu button
#myDevice.pressButton(Constants.Keycode_menu, MonkeyDevice.DOWN_AND_UP)
#device.press(Constants.Keycode_menu, MonkeyDevice.DOWN_AND_UP)
myDevice.longTouchScreen(700, 700)

# Takes a screenshot
result = device.takeSnapshot()

# Writes the screenshot to a file
result.writeToFile(Constants.writeToFileLocation, Constants.imageFileExtension)

# end script
now = time.strftime("%c")
print(Constants.endTestString + time.strftime("%c"))

#Return success
