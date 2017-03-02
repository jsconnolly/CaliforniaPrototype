import APIWrapper
import time
import json
import requests
import sys


# Begin test
APIWrapper.Utilities.startTest()
print("\nTesting update location...\n")
time = str(time.time())
displayName = "locationTEST@" + time[11:]
print("Location test name : " + displayName)
alertRadius = "100"
enablePushNotifications = True
enableSMS = False
enableEmail = True
coordinates = {"lat": 12.3456789, "lng": 98.7654321}

# Login
newURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + "/" + APIWrapper.Constants.id \
         + '/locations'

login = {
  "email": APIWrapper.Constants.email,
  "password": APIWrapper.Constants.password
}
headers = APIWrapper.WebBuilders.getHeaders(None)
urlBase = APIWrapper.Constants.BASE_URL + APIWrapper.Constants.USERS + APIWrapper.Constants.EMAIL_SIGNIN
api = APIWrapper.WebCalls(urlBase)
token = APIWrapper.WebBuilders.getToken(APIWrapper.Utilities.convertJsonToObject(api.makePostRequest(headers, login)))

# Add new test location
testLocation = {
    "displayName": displayName,
    "alertRadius": alertRadius,
    "coordinates": coordinates,
    "enablePushNotifications": enablePushNotifications,
    "enableSMS": enableSMS,
    "enableEmail": enableEmail}

headers = APIWrapper.WebBuilders.getHeaders(token)
addURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + "/" + APIWrapper.Constants.id \
         + '/locations'
api = APIWrapper.WebCalls(addURL)

print("\nTest one location name : " + displayName + " | Account email : " + APIWrapper.Constants.email + "\n")
response = api.makePutRequest(headers, testLocation)
for i in ['email', 'name']:
    print("Verifying " + i)
    assert response[i] == eval('APIWrapper.Constants.' + i)
for i in response['locations'][-1].items():
    if i[0] == "id":
        continue
    print("Verifying " + i[0])
    assert eval(i[0]) == i[1]

coordinates1 = {
    "displayName": displayName + str(1),
    "alertRadius": alertRadius + '1',
    "coordinates": {"lat": 00.0, "lng": 0},
    "enablePushNotifications": enablePushNotifications,
    "enableSMS": enableSMS,
    "enableEmail": enableEmail}

print("\nTesting changing of location information")
newURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + APIWrapper.Constants.GET_USER_BY_ID \
         + APIWrapper.Constants.id

api = APIWrapper.WebCalls(newURL)
response = api.makeGetRequest(None, headers)
assert response['id'] == APIWrapper.Constants.id
assert response['email'] == APIWrapper.Constants.email
for i in response['locations']:
    if i['displayName'] == displayName:
        print("---FOUND RECORD TO CHANGE: " + i['displayName'] + '---')
        locationID = i['id']

changeURL = APIWrapper.Constants.BASE_URL \
         + APIWrapper.Constants.USERS \
         + "/" + APIWrapper.Constants.id \
         + "/locations/" + locationID

api = APIWrapper.WebCalls(changeURL)
headers = APIWrapper.WebBuilders.getHeaders(token)
response = api.makePutRequest(headers,coordinates1)
assert response == {}
print(" Record changed successfully\n")


api = APIWrapper.WebCalls(newURL)
response = api.makeGetRequest(None, headers)
assert response['id'] == APIWrapper.Constants.id
assert response['email'] == APIWrapper.Constants.email
for i in response['locations']:
    if i['id'] == locationID:
        print("---FOUND CHANGED---: " + i['displayName'] + '---')
        for j in i.keys():
            if j == 'id':
                assert locationID == i[j]
            else:
                assert i[j] == coordinates1[j]
print(" Changes recorded sucessfully\n")




# Generate PUT Body

# Make outbound call & verify errors (intentional and unintentional)

APIWrapper.Utilities.endTest()
