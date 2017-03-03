import APIWrapper
import datetime

# Begin test
APIWrapper.Utilities.startTest()


#--------------------Login-------------------------------------
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

# Generate POST / PUT Body
loginBody = {
	'email' : strEmail,
	'password' : strPW
}

# Make outbound call
response = api.makePostRequest(headers, loginBody)

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get Token from object
stringToken = APIWrapper.WebBuilders.getToken(responseObject)
# build the new headers with the token
headers = APIWrapper.WebBuilders.getHeaders(stringToken)

#-----------------------------------------------------------------

# Base Url + Path + endpoint
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.RESET_PASSWORD\

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)
strPW = APIWrapper.Constants.password
# Generate POST / PUT Body
resetPassCurrentBody = {
	'email' : 'failtest@test.com',
	'password' : strPW
}

# Make outbound call
print ("Test Reset password to current password for invalid email")
response1 = api.makePostRequest(headers, resetPassCurrentBody)
APIWrapper.Utilities.printStr(response1)



# End test
APIWrapper.Utilities.endTest()

