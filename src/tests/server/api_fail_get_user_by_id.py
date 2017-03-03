import APIWrapper

# Begin test
APIWrapper.Utilities.startTest()

strEmail = ''
strPassword = ''

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
	'password' : strPassword
}

# Make outbound call
response = api.makePostRequest(headers, loginBody)

# Contains user dict. info
for keys in response:
    if keys == 'id':
        print('User id: ' + response[keys])

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get the String uid from the response object
errorMsg = APIWrapper.WebBuilders.getError(responseObject)

# Print error msg, if it exists
if errorMsg != APIWrapper.Constants.ERROR_STRING:
    print(errorMsg)

APIWrapper.Utilities.printStr("(intended to fail)")

# End test
APIWrapper.Utilities.endTest()
