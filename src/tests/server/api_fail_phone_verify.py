import APIWrapper
import datetime
import requests
import json

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


baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.PHONE_VERIFICATION\

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

# Generate POST / PUT Body
phoneBody = {
	'phone' : '1111111111111111'
}
print ("\nTest invalid phone number ")
# Make outbound call
response1 = makeCustomPostRequest(baseUrl, headers, phoneBody)
APIWrapper.Utilities.printStr(response1)

# End test
APIWrapper.Utilities.endTest()

