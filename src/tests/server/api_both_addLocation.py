import APIWrapper
import time


# Begin test
APIWrapper.Utilities.startTest()

time = str(time.time())
displayName = "locationTEST@" + time[11:]

strid1 = "58b74f9fc4d2090015177094"
strid2 = "58b74fd1c4d2090015177095"
strname = "Universal Test"
stremail = "test@hotbsoftware.com"
stremail2 = "test2@hotbsoftware.com"
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
for i in ['email', 'name']:
    print("Verifying " + i)
    assert responseDBH[i] == eval('APIWrapper.Constants.' + i)
for i in responseDBH['locations'][-1].items():
    try:
        if i[0] == "id":
            pass
        print("Verifying " + i[0])
        assert eval(i[0]) == i[1]
    except:
        if i[0] == "coordinates":
            print("     Verifying coordinates properties...")
            assert eval(i[0] + "DBH") == i[1]

print("\nTest two location name : " + displayName + " | Account email : " + stremail2 + "\n")
api = APIWrapper.WebCalls(newURL2)
headers = APIWrapper.WebBuilders.getHeaders(strtoken2)
responseCDM = api.makePutRequest(headers, testCDMLocation)
for i in ['email', 'name']:
    print("Verifying " + i)
    assert responseCDM[i] == eval('APIWrapper.Constants.' + i + '2')
for i in responseCDM['locations'][-1].items():
    try:
        if i[0] == "id":
            pass
        print("Verifying " + i[0])
        assert eval(i[0] + '2') == i[1]
    except:
        if i[0] == "coordinates":
            print("     Verifying coordinates properties...")
            assert eval(i[0] + "CDM") == i[1]
# End test
print('\n')
APIWrapper.Utilities.endTest()
