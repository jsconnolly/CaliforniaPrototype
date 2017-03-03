import APIWrapper

# Begin test
APIWrapper.Utilities.startTest()

# init variables needed:
strEmail = APIWrapper.Constants.email
strPW = APIWrapper.Constants.password

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

# Use to search
searchBody = {
        'email' : 'fake_email_should_fail@fake.com'
}

# Make outbound call - contains user dict. values
response = api.makePostRequest(headers, loginBody)

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get the String uid from the response object
errorMsg = APIWrapper.WebBuilders.getError(responseObject)

# If error exists, print it
if errorMsg != APIWrapper.Constants.ERROR_STRING:
    print(errorMsg)

# Get Token from object
stringToken = APIWrapper.WebBuilders.getToken(responseObject)

# build the new headers with the token
headers = APIWrapper.WebBuilders.getHeaders(stringToken)

# Make a new base URL for the next call
baseUrl = APIWrapper.Constants.BASE_URL \
            + APIWrapper.Constants.USERS \
            + APIWrapper.Constants.GET_USER_BY_EMAIL \
            + strEmail

# Make a new request to get the user object from a different endpoint
api = APIWrapper.WebCalls(baseUrl)

# Make a new get request with the updated API
response = api.makeGetRequest(searchBody, headers)

#Grab the phone number if its exists
for keys in response:
    if keys == 'email':
        userEmail = response[keys]

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get the String uid from the response object
errorMsg = APIWrapper.WebBuilders.getError(responseObject)

#Print the error message if it exists
if errorMsg != APIWrapper.Constants.ERROR_STRING:
    print(errorMsg)

#Try to print the user email
try:
    # Print the user email
    APIWrapper.Utilities.printStrs("User email: " + userEmail)
except NameError:
    pass

# End test
APIWrapper.Utilities.endTest()
