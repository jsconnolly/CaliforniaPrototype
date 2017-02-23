var mongo = require('mongodb');
var crypto 	= require('crypto');
var moment 		= require('moment');
var config = require('../config.json');
var AWS = require('aws-sdk');
var jwt = require('jsonwebtoken');
var Server = mongo.Server,
    Db = mongo.Db,
    BSON = mongo.BSONPure;

var server = new Server(config.db, 27017, { auto_reconnect: true });
db = new Db('ca', server);

db.open(function (err, db) {
    if (!err) {
        console.log("Connected to 'ca' database");
        db.collection('user', { strict: true }, function (err, collection) {
            if (err) {
                console.log("The 'ca' collection doesn't exist. Creating it with sample data...");
                db.createCollection('user');
            }
        });
    }
});

var userDB = db.collection('user');

exports.test = function (req, res) {
    var id = req.params.id;
    var lid = req.params.lid;
    console.log('Retrieving user: ' + id);
    console.log('Retrieving location: ' + lid);
    res.type('html');
    res.send("<!DOCTYPE html><html><head><title>Hello</title><meta charset=\"utf-8\" /></head><body><p>Hello, World!</p></body></html>");

};

exports.add = function (req, res) {
    var userinfo = req.body;
        if(!userinfo.email||!userinfo.phone){
                res.send({ 'success': 0, 'result': 'Phone and email is required' });
                return;            
        }
        userDB.findOne({$or:[{ 'email': userinfo.email==undefined?"---":userinfo.email },{ 'phone': userinfo.phone==undefined?"---":userinfo.phone }]}, function (err, item) {
            if (item) {
                res.send({ 'success': 0, 'result': 'Records already exist' });
                return;
            } 

                saltAndHash(userinfo.password, function (hash) {
                    userinfo.password = hash;
                    // append date stamp when record was created //
                    userinfo.date = moment().format('MMMM Do YYYY, h:mm:ss a');
                    //userobj.token=generateToken(userobj.id);
                    userDB.insert(userinfo, { safe: true }, function (err, result) {
                        if (err) {
                            res.send({ 'success':0,'result': 'An error has occurred' });
                        } else {
                            console.log('success: --' + JSON.stringify(result));
                            result.ops[0].token=generateToken(result.ops[0]._id);
                            userDB.save(result.ops[0], {safe: true}, function(e) {
			                });
                            var userobj=result.ops[0];
                            userobj.id=userobj._id;
                            delete userobj._id;
                            delete userobj.password;
                            
                            res.send({ 'success':1, 'result': userobj });
                        }

                    });

                });


        });





            



};


exports.getUserByEmail = function(req, res)
{
	var userinfo = req.body;
    var userheader=req.headers;
    if(!verifyToken(userheader.token)){
       res.send({ 'success':0, 'result': 'Invalid token' });
       return;
    };
     userDB.findOne({'email':userinfo['email']}, function(e, result){ 
        if(result){
            result.id=result._id;
            delete result._id;
            delete result.password;
            res.send({ 'success':1, 'result': result });
        }else{
            res.send({ 'success':0, 'result': 'Record not found' });
        }
        
    
    });
   

}


exports.getUserById = function(req, res)
{
	var userinfo = req.body;
    var inputid=null;
    var userheader=req.headers;
    if(!verifyToken(userheader.token)){
       res.send({ 'success':0, 'result': 'Invalid token' });
       return;
    };

  try {
     inputid=getObjectId(userinfo['id']);
  } catch (ex) {
    res.send({ 'success':0, 'result': 'Record not found' });
    return;
  }

 

    userDB.findOne({'_id':inputid}, function(e, result){ 
        if(result){
            result.id=result._id;
            delete result._id;
            delete result.password;
            res.send({ 'success':1, 'result': result });
        }else{
            res.send({ 'success':0, 'result': 'Record not found' });
        }
        
    
    });
   

}

exports.login = function(req, res)
{
	var userinfo = req.body;

   userDB.findOne({'email':userinfo.email}, function(e, result) {
		if (result == null){
			res.send({ 'success':0, 'result': 'Login failed' });
		}	else{
			validatePassword(userinfo['password'], result.password, function(err, o) {
				if (o){
                    result.token=generateToken(result._id);
                    userDB.save(result, {safe: true}, function(e) {
			        });
					res.send({ 'success':1, 'result': {token:result.token} });
				}	else{
					res.send({ 'success':0, 'result': 'Login failed'}); 
				}
			});
		}
	});
 
}
exports.phoneLogin = function(req, res)
{
	var userinfo = req.body;

   userDB.findOne({'phone':userinfo.phone}, function(e, result) {
		if (result == null){
			res.send({ 'success':0, 'result': 'Login failed' });
		}	else{
			validatePassword(userinfo.password, result.password, function(err, o) {
				if (o){
                    result.token=generateToken(result._id);
                    userDB.save(result, {safe: true}, function(e) {
			        });
					res.send({ 'success':1, 'result': {token:result.token} });
				}	else{
					res.send({ 'success':0, 'result': 'Login failed'}); 
				}
			});
		}
	});
 
}
exports.phoneCode = function(req, res)
{
	var userinfo = req.body;

   userDB.findOne({'phone':userinfo.phone}, function(e, result) {
		if (result == null){
			res.send({ 'success':0, 'result': 'Phone number not found' });
		}	else{
            var code=getRandomIntInclusive(123456,999999);
			sendSMS(userinfo.phone,'Your phone verification code is: '+code, function(err, o) {
                  console.log("sms result", err);
                  if(!err){
                      res.send({ 'success':1, 'result': {'code':code} });
                  }else{
                      res.send({ 'success':0, 'result': 'SMS failed' });
                  }
			});

		}
	});
 
}


exports.update = function(req, res)
{
	var userinfo = req.body;
    var userid=null;
    var userheader=req.headers;
    if(!verifyToken(userheader.token)){
       res.send({ 'success':0, 'result': 'Invalid token' });
       return;
    };


  try {
     userid=getObjectId(userinfo['id']);
  } catch (ex) {
    res.send({ 'success':0, 'result': 'Record not found' });
    return;
  }   
    userDB.findOne({'_id':userid}, function(e, o){
		if(userinfo['name'])o.name = userinfo['name'];
		if(userinfo['email'])o.email 	= userinfo['email'];
        if(userinfo['phone'])o.phone=userinfo.phone;
		if(userinfo['zip'])o.zip 	= userinfo['zip'];
			userDB.save(o, {safe: true}, function(e) {
				if (e) res.send({ 'success':0, 'result': 'Error found' });
				else res.send({ 'success':1, 'result': 'Update is Successful' });
			});

	});
}

exports.updatePassword = function(req, res)
{
     var userinfo = req.body;
     var userid=null;
    var userheader=req.headers;
    if(!verifyToken(userheader.token)){
       res.send({ 'success':0, 'result': 'Invalid token' });
       return;
    };

  try {
     userid=getObjectId(userinfo['id']);
  } catch (ex) {
    res.send({ 'success':0, 'result': 'Record not found' });
    return;
  }       
    userDB.findOne({'_id':userid}, function(e, o){
		if (e){
			res.send({ 'success':0, 'result': 'Error found' });
		}	else{
			saltAndHash(userinfo['password'], function(hash){
		        o.password = hash;
		        userDB.save(o, {safe: true}, function(ex) {
				if (ex) res.send({ 'success':0, 'result': 'Error found' });
				else res.send({ 'success':1, 'result': 'Update is Successful' });
			});
			});
		}
	});
}


exports.resetPassword = function(req, res)
{
     var userinfo = req.body;
     var userid=null;
    var userheader=req.headers;

    try{
       var decoded = jwt.verify(userheader.token, config.secretKey);
       userid=getObjectId(decoded.user);
    }catch(ex){
       res.send({ 'success':0, 'result': 'Invalid token' });
       return;       
    }

    userDB.findOne({'_id':userid}, function(e, o){
		if (e){
			res.send({ 'success':0, 'result': 'Error found' });
		}	else{
			saltAndHash(userinfo.password, function(hash){
		        o.password = hash;
		        userDB.save(o, {safe: true}, function(ex) {
				if (ex) res.send({ 'success':0, 'result': 'Error found' });
				else res.send({ 'success':1, 'result': 'Update is Successful' });
			});
			});
		}
	});
}



var generateToken = function (user) {
    var token = jwt.sign({ user: user }, config.secretKey);

    return token;
}
var generateResetToken = function (user) {
    var token = jwt.sign({ user: user }, config.secretKey,{ expiresIn: '1h' });

    return token;
}
var verifyToken = function (token) {
    
    var result=true;
    try{
       var decoded = jwt.decode(token);
       console.log(JSON.stringify(decoded));
       //if(decoded.user!=user){
           //result=false;
       //}
      userDB.findOne({'_id':getObjectId(decoded.user)}, function(e, result) {  
          if(!result){
              result=false;
          }
      });     
    }catch(ex){
      result=false;
    }
    return result;
}

var generateSalt = function () {
    var set = '0123456789abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ';
    var salt = '';
    for (var i = 0; i < 10; i++) {
        var p = Math.floor(Math.random() * set.length);
        salt += set[p];
    }
    return salt;
}

var md5 = function (str) {
    return crypto.createHash('md5').update(str).digest('hex');
}

var saltAndHash = function (pass, callback) {
    var salt = generateSalt();
    callback(salt + md5(pass + salt));
}

var validatePassword = function (plainPass, hashedPass, callback) {
    var salt = hashedPass.substr(0, 10);
    var validHash = salt + md5(plainPass + salt);
    callback(null, hashedPass === validHash);
}

var getObjectId = function(id)
{
	return new require('mongodb').ObjectID(id);
}

var sendSMS= function (to_number, message, func_callback) {

    
    AWS.config.update({
        accessKeyId: config.accessKeyId,
        secretAccessKey: config.secretAccessKey,
        region: config.region
    });

    var sns = new AWS.SNS();

    var SNS_TOPIC_ARN = config.topic;

    sns.subscribe({
        Protocol: 'sms',
        //You don't just subscribe to "news", but the whole Amazon Resource Name (ARN)
        TopicArn: SNS_TOPIC_ARN,
        Endpoint: to_number
    }, function(error, data) {
        if (error) {
            console.log("error when subscribe", error);
            return func_callback(false);
        }


        console.log("subscribe data", data);
        var SubscriptionArn = data.SubscriptionArn;

        var params = {
            TargetArn: SNS_TOPIC_ARN,
            Message: message,
            //hardcode now
            Subject: 'Admin'
        };

        sns.publish(params, function(err_publish, data) {
            if (err_publish) {
                console.log('Error sending a message', err_publish);

            } else {
                console.log('Sent message:', data.MessageId);
            }

            var params = {
                SubscriptionArn: SubscriptionArn
            };

            sns.unsubscribe(params, function(err, data) {
                if (err) {
                    console.log("err when unsubscribe", err);
                }
                return func_callback(err_publish != null);
            });
        });
    });
}

function getRandomIntInclusive(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min;
}