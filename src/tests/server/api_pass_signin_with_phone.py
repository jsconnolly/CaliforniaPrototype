import APIWrapper
import time
import sys

# Begin test
APIWrapper.Utilities.startTest()

#--------------------Login Token-------------------------------------


# Base Url + Path + endpoint
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.PHONE_SIGNIN

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

print ("\nTesting login with registered phone number and correct password")
# Generate POST / PUT Body
loginBody = {
	'phone' : APIWrapper.Constants.phone,
	'password' : APIWrapper.Constants.password
}

# Make outbound call
response1 = api.makePostRequest(headers, loginBody)
APIWrapper.Utilities.printStr(response1)

#-----------------------------------------------------------


# End test
APIWrapper.Utilities.endTest()
