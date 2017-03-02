import APIWrapper
import datetime
import requests
import json
import time

# Make a post request
# Pass 'None' / Null if param is not used (IE, pass None for myHeaders if not needed)
def makeCustomPostRequest(strURl, myHeaders, bodyObject):
    if myHeaders is None:
        request = requests.post(strURl, data=json.dumps(bodyObject))
    else:
        request = requests.post(strURl, data=json.dumps(bodyObject), headers=myHeaders)

    jsonResponse = request
    return (jsonResponse)

# Begin test
APIWrapper.Utilities.startTest()

phoneNum = '8885552222'

baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.PHONE_VERIFICATION\

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

# Generate POST / PUT Body
phoneBody = {
	'phone' : phoneNum
}

print ("\nTest valid phone number: " + phoneNum)
# Make outbound call
response1 = makeCustomPostRequest(baseUrl, headers, phoneBody)
APIWrapper.Utilities.printStr(response1)
print()


# Generate Dynamic POST / PUT Body
n = 2
while n > 0:
    now = datetime.datetime.now()
    currTime = time.time()
    phoneDynamicBody = {
        'phone' : currTime
    }
    print ("Testing phone with dynamic value: ")
    print (currTime)
    # Make outbound call
    response2 = makeCustomPostRequest(baseUrl, headers, phoneDynamicBody)
    APIWrapper.Utilities.printStr(response2)
    n = n-1


# End test
APIWrapper.Utilities.endTest()

#if number does not exist in database then run test twice
