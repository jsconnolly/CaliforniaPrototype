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

print ("\nTesting login with invalid phone number and invalid password")
# Generate POST / PUT Body
loginBody3 = {
	'phone' : '1111111111111',
	'password' : 'failpassword'
}

# Make outbound call
response3 = api.makePostRequest(headers, loginBody3)
APIWrapper.Utilities.printStr(response3)

#-----------------------------------------------------------


# End test
APIWrapper.Utilities.endTest()