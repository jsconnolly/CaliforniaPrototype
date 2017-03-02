import APIWrapper

# Begin test
APIWrapper.Utilities.startTest()


#--------------------Login Token-------------------------------------
# init variables needed:
strEmail = APIWrapper.Constants.email
strPW = APIWrapper.Constants.password
APIWrapper.Utilities.startTest()

# Base Url + Path + endpoint
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.EMAIL_SIGNIN

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

print ("\nTesting login with registered email and correct password")
# Generate POST / PUT Body
loginBody1 = {
	'email' : strEmail,
	'password' : strPW
}
# Make outbound call
response = api.makePostRequest(headers, loginBody1)
APIWrapper.Utilities.printStr(response)


# End test
APIWrapper.Utilities.endTest()
