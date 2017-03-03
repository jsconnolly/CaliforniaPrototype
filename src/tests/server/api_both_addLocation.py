import APIWrapper
import time


# Begin test
APIWrapper.Utilities.startTest()

time = str(time.time())
displayName = "locationTEST@" + time[11:]

strid1 = APIWrapper.Constants.id
strid2 = APIWrapper.Constants.id2
strname = APIWrapper.Constants.name
stremail = APIWrapper.Constants.email
stremail2 = APIWrapper.Constants.email2
strtoken1 = APIWrapper.Utilities.loginUser1()
strtoken2 = APIWrapper.Utilities.loginUser2()

alertRadius = "100"
enablePushNotifications = True
enableSMS = False
enableEmail = True
coordinatesDBH = {"lat": 33.643116, "lng": -117.84176}
coordinatesCDM = {"lat": 33.593604, "lng": -117.875953}

# Base Url + Path + endpoint
newURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + "/" + strid1 \
         + '/locations'

newURL2 = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + "/" + strid2 \
         + '/locations'

# Instantiate Object

api = APIWrapper.WebCalls(newURL)
headers = APIWrapper.WebBuilders.getHeaders(strtoken1)

# Generate PUT Body
testDBHLocation = {
    "displayName": displayName,
    "alertRadius": alertRadius,
    "coordinates": coordinatesDBH,
    "enablePushNotifications": enablePushNotifications,
    "enableSMS": enableSMS,
    "enableEmail": enableEmail}

testCDMLocation = {
    "displayName": displayName,
    "coordinates": coordinatesCDM,
    "alertRadius": alertRadius,
    "enablePushNotifications": enablePushNotifications,
    "enableEmail": enableEmail,
    "enableSMS": enableSMS}


# Make outbound call & verify errors (intentional and unintentional)
print("\nTesting add location...")
print("\nTest one location name : " + displayName + " | Account email : " + stremail + "\n")
responseDBH = api.makePutRequest(headers, testDBHLocation)
APIWrapper.Utilities.printStr("Printing response1 " + str(responseDBH))

print("\nTest two location name : " + displayName + " | Account email : " + stremail2 + "\n")
api = APIWrapper.WebCalls(newURL2)
headers = APIWrapper.WebBuilders.getHeaders(strtoken2)
responseCDM = api.makePutRequest(headers, testCDMLocation)

APIWrapper.Utilities.printStr("Printing response2 " + str(responseCDM))
# End test
print('\n')
APIWrapper.Utilities.endTest()
