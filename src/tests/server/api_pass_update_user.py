import APIWrapper
import time

# Begin test
APIWrapper.Utilities.startTest()

# init variables needed:
strEmail = APIWrapper.Constants.email
strPW = APIWrapper.Constants.password
updatePhone = APIWrapper.Constants.phone

# Base Url + Path + endpoint - Sign in
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

#Make outbound call
response = api.makePostRequest(headers, loginBody)

print()
#DELETE THIS LATER - LOGGED IN 
for keys in response:
    if str(keys) == 'id':
        userID = response[keys]
    print(str(keys) + ': ' + str(response[keys]))
print()


# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

# Get Token from object
stringToken = APIWrapper.WebBuilders.getToken(responseObject)

### build the new headers with the token
headers = APIWrapper.WebBuilders.getHeaders(stringToken)

# Make a new base URL for the next call
baseUrl = APIWrapper.Constants.BASE_URL \
            + APIWrapper.Constants.USERS \
            + APIWrapper.Constants.UPDATE_USER \
            + userID

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Randomize a String for appending to the city
time = str(time.time())

newEmail = APIWrapper.Constants.email
newName = APIWrapper.Constants.name
newCity = 'changed on ' + str(time[11:])
newState = APIWrapper.Constants.state
newZip = APIWrapper.Constants.zip

# Updates to be made
updateBody = {
    'email' : newEmail,
    'name': newName,
    'city': newCity,
    'state': newState,
    'zip' : newZip
}

# Make outbound call
response = api.makePutRequest(headers, updateBody)

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

APIWrapper.Utilities.printStr("Update User (change) Success")

# Update once more to change back to original
updateBody = {
    'email' : newEmail,
    'name': newName,
    'city': APIWrapper.Constants.city,
    'state': newState,
    'zip' : newZip
}

# Make outbound call
response = api.makePutRequest(headers, updateBody)

# Convert response to parseable object
responseObject = APIWrapper.Utilities.convertJsonToObject(response)

APIWrapper.Utilities.printStr("Update User (change back) Success")

# End test
APIWrapper.Utilities.endTest()

