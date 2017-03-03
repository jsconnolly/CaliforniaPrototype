import APIWrapper

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


print ("Send forgot password request for email that dosent exist")
# Generate POST / PUT Body
forgotPasswordNoExistBody = {
    'email' : 'failtest@test.com'
}
# make outbound call
response1 = api.makePostRequest(headers, forgotPasswordNoExistBody)
APIWrapper.Utilities.printStr(response1)
APIWrapper.Utilities.printStr("Expected to fail")
# End Test
APIWrapper.Utilities.endTest()