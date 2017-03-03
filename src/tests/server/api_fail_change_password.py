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
print("Token: " + stringToken + "\n")
# build the new headers with the token
headers = APIWrapper.WebBuilders.getHeaders(stringToken)

#----------------------Get USER ID-------------------------------------
# Make a new base URL for the next call
baseUrl = APIWrapper.Constants.BASE_URL \
            + APIWrapper.Constants.USERS \
            + APIWrapper.Constants.GET_USER_BY_EMAIL \
            + strEmail

# Make a new request to get the user object from a different endpoint
api = APIWrapper.WebCalls(baseUrl)

# Make a new get request with the updated API
response = api.makeGetRequest(None, headers)

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get the String uid from the response object
stringUID = APIWrapper.WebBuilders.getUserId(responseObject)

# Print the userId
APIWrapper.Utilities.printStrs("UserId =", stringUID)



#-------------------Change Password------------------------------
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS \
          + APIWrapper.Constants.CHANGE_PASSWORD\

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)


# Generate POST / PUT Body
print ('\nChanging password of user with ID that doesn\'t exist')
passwordFailBody = {
    'id' : '1111111111111111',
    'password' : 'password1'
}
#make outbound call
response0 = api.makePostRequest(headers, passwordFailBody)
APIWrapper.Utilities.printStr("Intended to fail: " + str(response0))



# End test
APIWrapper.Utilities.endTest()