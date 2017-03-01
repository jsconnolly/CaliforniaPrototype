import APIWrapper
import time

# Begin test & generate fields.
APIWrapper.Utilities.startTest()

time = str(time.time())
email = "dionk-" + str(time[12:]) + "@hotbsoftware.com"
password = "password2"
name = "Test DK"
phone = str(time[:10])
address = "540 Test Road"
city = "Testvine"
zip = "92617"
state = "CA"

# Base Url + Path + endpoint
newUrl = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.REGISTER_USER

# Instantiate Object
api = APIWrapper.WebCalls(newUrl)

# Generate Headers (Replace none with token if needed / known)
headers = APIWrapper.WebBuilders.getHeaders(None)

# Generate PUT Body
workingPhoneBody = {
    "email": email,
    "password": password,
    "name": name,
    "phone": phone,
    "address": address,
    "city": city,
    "zip": zip,
    "state": state}

dupePhoneBody = {
    "email": email,
    "password": password,
    "name": name,
    "phone": phone,
    "address": address,
    "city": city,
    "zip": zip,
    "state": state}

print("\nTest original/duplicate email:phone combinations...")
print("Original: " + workingPhoneBody['email'] + " | " + workingPhoneBody['phone'])
print("Duplicate: " + dupePhoneBody['email'] + " | " + dupePhoneBody['phone'])
print("----------------------------------------------------\n")


# Make outbound call & verify errors (intentional and unintentional)

print("Making request to register original user...")
response = APIWrapper.Utilities.convertJsonToObject(api.makePostRequest(headers, workingPhoneBody))
print("Verifying user information...\n")
for i in ['email', 'name', 'phone', 'address', 'city', 'zip', 'state']:
    assert response.__dict__[i] == eval(i)

print("Making request to register duplicate user...")
responseDupe = APIWrapper.Utilities.convertJsonToObject(api.makePostRequest(headers, dupePhoneBody))
error = APIWrapper.WebBuilders.getError(responseDupe)
print("Validating that the test failed...")
assert error == "Records already exist"
print(error + " (Expected)\n")



# End test
APIWrapper.Utilities.endTest()

