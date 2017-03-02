import APIWrapper
import time

# Begin test & generate fields.
APIWrapper.Utilities.startTest()
print()
email = 'test2@hotbsoftware.com'
password = 'password123'
name = "Universal Test"
phone = "8885554444"
address = "540 Tester Dr"
city = "Testvine"
state = "CA"
zip = "92618"
id = '58b4b6d16a872a000f6cf1fe'
id2 = '58b4aaa78c7472000f65839d'
id3 = "nub123123123123123"

# Base Url + Path + endpoint && Unregistered user URL
loginURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.EMAIL_SIGNIN

baseURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.GET_USER_BY_ID \
         + id

baseURL2 = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.GET_USER_BY_ID \
         + id2

baseURL3 = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.GET_USER_BY_ID \
         + id3

# Instantiate Object
apiLogin = APIWrapper.WebCalls(loginURL)
apiBase = APIWrapper.WebCalls(baseURL)
apiBase2 = APIWrapper.WebCalls(baseURL2)
apiBase3 = APIWrapper.WebCalls(baseURL3)
headers = APIWrapper.WebBuilders.getHeaders(None)

# Login body & login to server
loginBody = {
    "email" : "test2@hotbsoftware.com",
    "password" : "password123"}
responseLogin = apiLogin.makePostRequest(headers, loginBody)
token = APIWrapper.Utilities.convertJsonToObject(responseLogin).__dict__['token']
headers = APIWrapper.WebBuilders.getHeaders(token)

# Make get call to retrieve user information.
print("Sending get request to get real user information via: ID...\n")

print("Testing first ID...")
responseBase = APIWrapper.Utilities.convertJsonToObject(apiBase.makeGetRequest(None, headers))
print("Validating test user information...")
for i in ['email', 'name', 'phone', 'address', 'city', 'zip', 'state', 'id']:
    assert responseBase.__dict__[i] == eval(i)

#Set phone/email attributes to new values for second ID.

phone = "8885552222"
email = 'test@hotbsoftware.com'

print("\nTesting second ID...")
responseBase = APIWrapper.Utilities.convertJsonToObject(apiBase2.makeGetRequest(None, headers))
print("Validating test user information...")
for i in ['email', 'name', 'phone', 'address', 'city', 'zip', 'state', 'id']:
    if i is not 'id':
        assert responseBase.__dict__[i] == eval(i)
    else:
        assert responseBase.__dict__[i] == id2

print("\nTesting third (invalid) ID...")
responseBase = APIWrapper.Utilities.convertJsonToObject(apiBase3.makeGetRequest(None, headers))
error = APIWrapper.WebBuilders.getError(responseBase)
print("Validating that the test failed...")
assert error == "Record not found"
print(error + " (Expected)")

# End test
print()
APIWrapper.Utilities.endTest()

