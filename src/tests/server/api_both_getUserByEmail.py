import APIWrapper
import time

# Begin test & generate fields.
APIWrapper.Utilities.startTest()
print()
email = APIWrapper.Constants.email2
password = APIWrapper.Constants.password
unregEmail = 'testfail@hotbsoftware.com'
name = APIWrapper.Constants.name
phone = APIWrapper.Constants.phone2
address = "540 Tester Dr"
city = "Testvine"
state = "CA"
zip = "92618"
id = APIWrapper.Constants.id2

# Base Url + Path + endpoint && Unregistered user URL
loginURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.EMAIL_SIGNIN

baseURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.GET_USER_BY_EMAIL \
         + email

unregURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.GET_USER_BY_EMAIL \
         + unregEmail

# Instantiate Object
apiLogin = APIWrapper.WebCalls(loginURL)
apiBase = APIWrapper.WebCalls(baseURL)
apiUnreg = APIWrapper.WebCalls(unregURL)
headers = APIWrapper.WebBuilders.getHeaders(None)

# Login body & login to server
loginBody = {
    "email" : "test2@hotbsoftware.com",
    "password" : "password123"}
responseLogin = apiLogin.makePostRequest(headers, loginBody)
token = APIWrapper.Utilities.convertJsonToObject(responseLogin).__dict__['token']
headers = APIWrapper.WebBuilders.getHeaders(token)

# Make get call to retrieve user information.
print("Sending get request to get real user information via: EMAIL...")
responseBase = APIWrapper.Utilities.convertJsonToObject(apiBase.makeGetRequest(None, headers))
print("Validating test user information...\n")
for i in ['email', 'name', 'phone', 'address', 'city', 'zip', 'state', 'id']:
        assert responseBase.__dict__[i] == eval(i)

print("Sending get request to get unregistered user information via: EMAIL...")
responseUnreg= APIWrapper.Utilities.convertJsonToObject(apiUnreg.makeGetRequest(None, headers))
error = APIWrapper.WebBuilders.getError(responseUnreg)
print("Validating that the test failed...")
assert error == "Record not found"
print(error + "(Expected)")

# End test
print()
APIWrapper.Utilities.endTest()

