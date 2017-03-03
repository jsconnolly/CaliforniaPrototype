# Documentation for requests class:
# http://docs.python-requests.org/en/latest/user/quickstart/
import requests
# Documentation for parsing, serialization, and deserialization:
import json
import time


# User Model
class User:
	#Begin class {
	# String
	id = None
	# String
	password = None
	# String
	email = None
	# String
	name = None
	# String
	phone = None
	# String
	address = None
	# String
	city = None
	# String
	state = None
	# String
	zip = None
	# String / DateObject
	date = None

	def __init__(self, id, email, phone, password, name, address, city, state, zip, date):
		self.id = id
		self.email = email
		self.phone = phone
		self.password = password
		self.name = name
		self.address = address
		self.city = city
		self.state = state
		self.zip = zip
		self.date = date

	def __setattr__(self, *args, **kwargs):
		return super().__setattr__(*args, **kwargs)

	def toJson(self):
		return json.dumps(self.__dict__)

	@classmethod
	def fromJson(cls, json_str):
		json_dict = json.loads(json_str)
		return cls(**json_dict)

	#End class }

# Class for constants
class Constants:
	#--Begin class {
	# URL Base
	BASE_URL = "http://ec2-54-241-144-61.us-west-1.compute.amazonaws.com"

		# User Path {
	USERS = "/users"
			# User Endpoints {
	# Post Request
	REGISTER_USER = ""
	# Post Request
	EMAIL_SIGNIN = "/signin"
	# Post Request
	PHONE_SIGNIN = "/phoneSignin"
	# Post Request
	FORGOT_PASSWORD = "/forgotPassword"
	# Get Request / REQUIRES TOKEN HEADER STRING
	GET_USER_BY_EMAIL = "/email/" # + Append Email
	# Get Request / REQUIRES TOKEN HEADER STRING
	GET_USER_BY_ID = "/" # + Append User ID
	# Get Request / REQUIRES TOKEN HEADER STRING
	GET_USER_BY_PHONE = "/phone/" # + Append Phone Number
	# Put Request / REQUIRES TOKEN HEADER STRING
	UPDATE_USER = "/" # + Append User ID
	# Post Request / REQUIRES TOKEN HEADER STRING
	RESET_PASSWORD = "/resetPassword"
	# Post Request / REQUIRES TOKEN HEADER STRING
	PHONE_VERIFICATION = "/phoneCode"
	# Post Request / REQUIRES TOKEN HEADER STRING
	CHANGE_PASSWORD = "/changePassword"
	# Put Request / REQUIRES TOKEN HEADER STRING
	ADD_LOCATION = "/locations"
	# Put Request / REQUIRES TOKEN HEADER STRING
	UPDATE_LOCATION = "/locations/" # + Append User ID

					# End User Endpoints }
		# End User Path }

	START_TEST_STR = "Starting test at: "
	END_TEST_STR = "Finished test at: "
	ERROR_STRING = "Unknown Error"
	EXCEPTION_STRING = "Parse Exception"

	# Test Users:
	# Test User 1:
	name = 'Universal Test'
	email = 'test@hotbsoftware.com'
	phone = '8885552222'
	address = '540 Tester Dr'
	password = 'password123'
	city = 'Testvine'
	state = 'CA'
	zip = '92618'
	date = '2017-03-01T22:47:59.848Z'
	id = '58b74f9fc4d2090015177094'

	# Test User 2:
	name2 = 'Universal Test'
	email2 = 'test2@hotbsoftware.com'
	phone2 = '8885554444'
	address2 = '540 Tester Dr'
	password2 = 'password123'
	city2 = 'Testvine'
	state2 = 'CA'
	zip2 = '92618'
	date2 = '2017-03-01T22:48:49.943Z'
	id2 = '58b74fd1c4d2090015177095'

	# Test User 1 Dictionary / Map
	User1 = {
		'email' : email,
		'password' : password,
		'name' : name,
		'phone' : phone,
		'address' : address,
		'city' : city,
		'state' : state,
		'zip' : zip,
		'id' : id,
		'date' : date
	}

	# Test User 1 Dictionary / Map
	User2 = {
		'email' : email,
		'password' : password,
		'name' : name,
		'phone' : phone,
		'address' : address,
		'city' : city,
		'state' : state,
		'zip' : zip,
		'id' : id,
		'date' : date
	}

	#--End class }

# Web Utility builders (IE Headers)
class WebBuilders:
	#--Begin class {
	# Get Headers. Pass in String auth token if needed or not null
	@staticmethod
	def getHeaders(token):
		data = {}
		data["Content-Type"] = "application/json"
		if token is not None:
			# If the client id is not null / none,
			data["token"] = token
		return (data)

	# Get String token from a response. If no attribute is found, will return null/ None
	@staticmethod
	def getToken(apiResponse):
		if apiResponse is not None:
			stringToken = getattr(apiResponse, "token", None)
			return stringToken

		return (None)

	# Get String Error from a response. If no attribute is found, will return null/ None
	@staticmethod
	def getError(apiResponse):
		if apiResponse is not None:
			try:
				stringToken = getattr(apiResponse, "error", None)
				if stringToken is None:
					raise Exception (Constants.EXCEPTION_STRING)
				return stringToken
			except :
				try :
					stringToken = getattr(apiResponse, "Error", None)
					if stringToken is None:
						raise Exception (Constants.EXCEPTION_STRING)
					return stringToken
				except :
					try:
						errorObj = getattr(apiResponse, "error", None)
						stringToken = getattr(errorObj, "message", None)
						if stringToken is None:
							raise Exception (Constants.EXCEPTION_STRING)
						return stringToken
					except :
						return Constants.ERROR_STRING
		return (Constants.ERROR_STRING)

	# Get String userId (id) from a response. If no attribute is found, will return null/ None
	@staticmethod
	def getUserId(apiResponse):
		if apiResponse is not None:
			stringToken = getattr(apiResponse, "id", None)
			return stringToken

		return (None)

	#--End class }

# Web calls (Get, Post, Put, Delete)
class WebCalls:
	#--Begin class {
	strUrlBase = None

	def __init__(self, strUrlBase):
		self.strUrlBase = strUrlBase

	# Make a get request.
	# Pass 'None' / Null if param is not used (IE, pass None for headers if not needed)
	def makeGetRequest(self, queryParamsArray, myHeaders):
		if queryParamsArray is None and myHeaders is None:
			request = requests.get(self.strUrlBase)
		elif queryParamsArray is not None and myHeaders is None:
			request = requests.get(self.strUrlBase, params=queryParamsArray)
		elif queryParamsArray is None and myHeaders is not None:
			request = requests.get(self.strUrlBase, headers=myHeaders)
		else:
			request = requests.get(self.strUrlBase, params=queryParamsArray, headers=myHeaders)

		jsonResponse = request.json()
		return (jsonResponse)

	# Make a post request
	# Pass 'None' / Null if param is not used (IE, pass None for myHeaders if not needed)
	def makePostRequest(self, myHeaders, bodyObject):
		if myHeaders is None:
			request = requests.post(self.strUrlBase, data=json.dumps(bodyObject))
		else:
			request = requests.post(self.strUrlBase, data=json.dumps(bodyObject), headers=myHeaders)

		jsonResponse = request.json()
		return (jsonResponse)

	# Make a put request
	# Pass 'None' / Null if param is not used (IE, pass None for myHeaders if not needed)
	def makePutRequest(self, myHeaders, bodyObject):
		if myHeaders is None:
			request = requests.put(self.strUrlBase, data=json.dumps(bodyObject))
		else:
			request = requests.put(self.strUrlBase, data=json.dumps(bodyObject), headers=myHeaders)

		jsonResponse = request.json()
		return (jsonResponse)

	# Make a Delete request
	# Pass 'None' / Null if param is not used (IE, pass None for myHeaders if not needed)
	def makeDeleteRequest(self, myHeaders, bodyObject):
		if myHeaders is None:
			request = requests.delete(self.strUrlBase, data=json.dumps(bodyObject))
		else:
			request = requests.delete(self.strUrlBase, data=json.dumps(bodyObject), headers=myHeaders)

		jsonResponse = request.json()
		return (jsonResponse)
	#--End class }

#Various Utilities
class Utilities:
	#--Begin class {


	# Attempt to login user1. If this succeeds, it will return an auth token, if not, returns None (null)
	@staticmethod
	def loginUser1():
		User1 = Constants.User1
		headers = WebBuilders.getHeaders(None)
		urlBase = Constants.BASE_URL + Constants.USERS + Constants.EMAIL_SIGNIN
		api = WebCalls(urlBase)
		response = api.makePostRequest(headers, User1)
		objectResponse = Utilities.convertJsonToObject(response)
		authToken = Utilities.getTokenFromResponse(objectResponse)
		return authToken


	# Attempt to login user2. If this succeeds, it will return an auth token, if not, returns None (null)
	@staticmethod
	def loginUser2():
		try :
			User2 = Constants.User2
			headers = WebBuilders.getHeaders(None)
			urlBase = Constants.BASE_URL + Constants.USERS + Constants.EMAIL_SIGNIN
			api = WebCalls(urlBase)
			response = api.makePostRequest(headers, User2)
			objectResponse = Utilities.convertJsonToObject(response)
			authToken = Utilities.getTokenFromResponse(objectResponse)
			return authToken

		except :
			return None

	# Print, auto-cast into string
	@staticmethod
	def printStr(oneThingToPrint):
		print(str(oneThingToPrint))

	# Print, auto-cast args
	@staticmethod
	def printStrs(*args):
		myStr = ""
		intPos = 0
		for x in args:
			if intPos == 0:
				myStr = str(x)
			else:
				myStr = myStr + ", " + str(x)
			# Increment by one
			intPos += 1

		print(str(myStr))

	# Deserialize a json string into an object
	@staticmethod
	def convertJsonToObject(jsonStr):
		obj = Converter(**jsonStr)
		return obj

	# Pass in the Json map or object and obtain the Token String
	@staticmethod
	def getTokenFromResponse(jsonResponse):
		stringToken = getattr(jsonResponse, "token", None)
		return (stringToken)

	# Serialize an object into a Json String. Can also take in a dictionary / map
	@staticmethod
	def convertObjectToJson(objectToConvert):
		return json.dumps(objectToConvert, default=lambda o: o.__dict__, sort_keys=True, indent=4)

	@staticmethod
	def startTest():
		print(Constants.START_TEST_STR + time.strftime("%c"))

	@staticmethod
	def endTest():
		print(Constants.END_TEST_STR + time.strftime("%c"))

	#--End class }

class Converter:
	#--Begin class {
    def __init__(self, **entries):
        self.__dict__.update(entries)

	#--End class }
