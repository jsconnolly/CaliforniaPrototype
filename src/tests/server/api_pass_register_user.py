import APIWrapper
import time

# Begin test
APIWrapper.Utilities.startTest()

time = str(time.time())
displayName = "locationTEST@"
strEmail = 'testLD101' + str(time[11:]) + '@hotbsoftware.com'
strPassword = APIWrapper.Constants.password
strPhone = APIWrapper.Constants.phone + str(time[11:])


# Base Url + Path + endpoint
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

# Generate POST / PUT Body
registerBody = {
	'email' : strEmail,
	'password' : strPassword,
        'phone' : strPhone
}

# Make outbound call
response = api.makePostRequest(headers, registerBody)

for key in response:
    print(response[key])
    if key == "Errors":
        print(response[key])

# End test
APIWrapper.Utilities.endTest()
