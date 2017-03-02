import APIWrapper

# Begin test
APIWrapper.Utilities.startTest()


#--------------------Login Token-------------------------------------
APIWrapper.Utilities.startTest()

# Base Url + Path + endpoint
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.PHONE_SIGNIN

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

print ("\nTesting login with registered phone number but invalid password")
# Generate POST / PUT Body
loginBody2 = {
	'phone' : APIWrapper.Constants.phone,
	'password' : 'failpassword'
}

# Make outbound call
response2 = api.makePostRequest(headers, loginBody2)
APIWrapper.Utilities.printStr(response2)
#-----------------------------------------------------------


# End test
APIWrapper.Utilities.endTest()
