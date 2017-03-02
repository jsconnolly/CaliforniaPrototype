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

stringToken = APIWrapper.Utilities.loginUser1()

# build the new headers with the token
headers = APIWrapper.WebBuilders.getHeaders(stringToken)

#-----------------------------------------------------------------

# Base Url + Path + endpoint
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.RESET_PASSWORD\

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate POST / PUT Body
resetPassCurrentBody = {
	'email' : strEmail,
	'password' : strPW
}

# Make outbound call
print ("Test Reset password to current password")
response1 = api.makePostRequest(headers, resetPassCurrentBody)
APIWrapper.Utilities.printStr(response1)

response1 = api.makePostRequest(headers, resetPassCurrentBody)



# End test
APIWrapper.Utilities.endTest()

