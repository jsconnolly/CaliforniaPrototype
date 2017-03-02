import APIWrapper
import requests
import json

# Begin test
APIWrapper.Utilities.startTest()

strEmail = APIWrapper.Constants.email

# Base Url + Path + endpoint
baseUrl = APIWrapper.Constants.BASE_URL \
          + APIWrapper.Constants.USERS\
          + APIWrapper.Constants.FORGOT_PASSWORD

# Instantiate Object
api = APIWrapper.WebCalls(baseUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)
#-------------------------------------

print ("Send forgot password request for email that exists")
# Generate POST / PUT Body
forgotPasswordBody = {
	'email' : strEmail
}
# make outbound call
request = requests.post(baseUrl, data=json.dumps(forgotPasswordBody))
response = request.content

APIWrapper.Utilities.printStr("Success")

# End Test
APIWrapper.Utilities.endTest()
