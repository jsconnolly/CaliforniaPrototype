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

time = str(time.time())
phone = "1111111111" + str(time[12:])

baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.PHONE_VERIFICATION\

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

# Generate POST / PUT Body
phoneBody = {
	'phone' : phone
}

print ("\nTest valid phone number: " + phone)
# Make outbound call
response1 = makeCustomPostRequest(baseUrl, headers, phoneBody)
APIWrapper.Utilities.printStr(response1)

# End test
APIWrapper.Utilities.endTest()

#if number does not exist in database then run test twice
