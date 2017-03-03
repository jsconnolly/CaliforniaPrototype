import APIWrapper

# Begin test
APIWrapper.Utilities.startTest()

# init variables needed:
strEmail = "testLD@hotbsoftware.com"
strPW = ""
strPhoneNumber = '9518671640'

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

phoneBody = {
        'phone' : strPhoneNumber
}
    

# Make outbound call
response = api.makePostRequest(headers, loginBody)

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get the String uid from the response object
errorMsg = APIWrapper.WebBuilders.getError(responseObject)

# Get error if it exists
if errorMsg != APIWrapper.Constants.ERROR_STRING:
    print(errorMsg)

# Get Token from object
stringToken = APIWrapper.WebBuilders.getToken(responseObject)

# build the new headers with the token
headers = APIWrapper.WebBuilders.getHeaders(stringToken)

# Make a new base URL for the next call
baseUrl = APIWrapper.Constants.BASE_URL \
            + APIWrapper.Constants.USERS \
            + APIWrapper.Constants.GET_USER_BY_PHONE \
            + strPhoneNumber

# Make a new request to get the user object from a different endpoint
api = APIWrapper.WebCalls(baseUrl)

# Make a new get request with the updated API
response = api.makeGetRequest(phoneBody, headers)

#Grab the phone number if its exists
for keys in response:
    if keys == 'phone':
        phoneNumber = response[keys]

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get the String uid from the response object
errorMsg = APIWrapper.WebBuilders.getError(responseObject)

#Print the error message if it exists
if errorMsg != 'Unknown Error':
    print(errorMsg)

try:
    # Print the userId
    APIWrapper.Utilities.printStrs("User phone number: " + phoneNumber)
except NameError:
    pass

# End test
APIWrapper.Utilities.endTest()
