var crypto = require('crypto');
var moment = require('moment');
var config = require('../config.json');
var AWS = require('aws-sdk');
var jwt = require('jsonwebtoken');
var ObjectID = require('mongodb').ObjectID;
var userDB;
var db;
var ses = require('node-ses')
  , client = ses.createClient({ key: config.accessKeyId, secret: config.secretAccessKey, amazon: config.emailEndpoint });

// Retrieve
var MongoClient = require('mongodb').MongoClient;

// Connect to the db
MongoClient.connect(config.db, function(err, db) {
  if(!err) {
    userDB=db.collection('user');
    console.log("We are connected");
  }
});



//var server = new Server(config.db, 49541, { auto_reconnect: true });//27017
/**
var fs = require("fs");
var tunnel = require('tunnel-ssh');
var dbstring=config.db;
var dbusername=config.dbuser;

var connect = require('mongodb-connection-model').connect;


connect(config.dboptions, (err, db1) => {
  if (err) {
    return console.log(err);
  }
  userDB=db1.db('ca').collection('user');

});

 */



/*
db = new Db('ca', server);


console.log("test" + db);
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
*/
//var userDB = db.collection('user');

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
    if (!userinfo.email && !userinfo.phone) {
        res.status(400).send({ 'Error': 'Phone or email is required' });
        return;
    }
    userDB.findOne({ $or: [{ 'email': userinfo.email == undefined ? "---" : userinfo.email }, { 'phone': userinfo.phone == undefined ? "---" : userinfo.phone }] }, function (err, item) {
        if (item) {
            res.status(400).send({ 'Error': 'Records already exist' });
            return;
        }

        saltAndHash(userinfo.password, function (hash) {
            userinfo.password = hash;
            // append date stamp when record was created //
            userinfo.date = new Date();//moment().format('MMMM Do YYYY, h:mm:ss a');
            userinfo.locations = [];
            userinfo.contacts = [];
            //userobj.token=generateToken(userobj.id);
            userDB.insert(userinfo, { safe: true }, function (err, result) {
                if (err) {
                    res.status(400).send({ 'Error': 'An error has occurred' });
                } else {
                    console.log('success: --' + JSON.stringify(result));
                    result.ops[0].token = generateToken(result.ops[0]._id);
                    userDB.save(result.ops[0], { safe: true }, function (e) {
                    });
                    var userobj = result.ops[0];
                    userobj.id = userobj._id;
                    delete userobj._id;
                    delete userobj.password;

                    res.status(200).send(userobj);
                }

            });

        });


    });









};
exports.getUserByPhone = function (req, res) {
    var userinfo = req.body;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };
    userDB.findOne({ 'phone': req.params.phone }, function (e, result) {
        if (result) {
            result.id = result._id;
            delete result._id;
            delete result.password;
            res.status(200).send(result);
        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });


}

exports.getUserByEmail = function (req, res) {
    var userinfo = req.body;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };
    userDB.findOne({ 'email': req.params.email }, function (e, result) {
        if (result) {
            result.id = result._id;
            delete result._id;
            delete result.password;
            res.status(200).send(result);
        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });
}

exports.forgotPassword = function (req, res) {
    var userinfo = req.body;

    userDB.findOne({ 'email': userinfo.email }, function (e, result) {
        if (result) {

var token = generateResetToken(result._id);
client.sendEmail({
   to: userinfo.email
 , from: 'noreply@hotbsoftware.com'
 , subject: 'Reset your password'
 , message: 'Click <a href="'+config.resetURL+'?token='+token+'&email='+userinfo.email+'">here</a> to reset your password.'
 
}, function (err, data, response) {
   if(err){
        res.status(500).send({ 'Error': 'Email failed' });
   }else{
        res.status(200).send();
   }
});


            
        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });


}



exports.getUserById = function (req, res) {
    var userinfo = req.body;
    var inputid = null;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };

    try {
        inputid = getObjectId(req.params.id);
    } catch (ex) {
        res.status(404).send({ 'Error': 'Record not found' });
        return;
    }



    userDB.findOne({ '_id': inputid }, function (e, result) {
        if (result) {
            result.id = result._id;
            delete result._id;
            delete result.password;
            res.status(200).send(result);
        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });


}

exports.login = function (req, res) {
    var userinfo = req.body;

    userDB.findOne({ 'email': userinfo.email }, function (e, result) {
        if (result == null) {
            res.status(404).send({ 'Error': 'User not found' });
        } else {
            validatePassword(userinfo['password'], result.password, function (err, o) {
                if (o) {
                    result.token = generateToken(result._id);
                    userDB.save(result, { safe: true }, function (e) {
                    });
                    var returnObj=result;
                    returnObj.id=result._id;
                    delete returnObj._id;
                    delete returnObj.password;
                    //res.status(200).send({ token: result.token });
                    res.status(200).send(returnObj);
                } else {
                    res.status(400).send({ 'Error': 'Login failed' });
                }
            });
        }
    });

}
exports.phoneLogin = function (req, res) {
    var userinfo = req.body;

    userDB.findOne({ 'phone': userinfo.phone }, function (e, result) {
        if (result == null) {
            res.status(404).send({ 'Error': 'User not found' });
        } else {
            if(!userinfo.password){
                res.status(400).send({ 'Error': 'Password is required' });
                return;
            }
            
            validatePassword(userinfo.password, result.password, function (err, o) {
                if (o) {
                    result.token = generateToken(result._id);
                    userDB.save(result, { safe: true }, function (e) {
                    });
                    var returnObj=result;
                    returnObj.id=result._id;
                    delete returnObj._id;
                    delete returnObj.password;
                    res.status(200).send(returnObj);
                    //res.status(200).send({ token: result.token });
                } else {
                    res.status(400).send({ 'Error': 'Login failed' });
                }
            });
        }
    });

}
exports.phoneCode = function (req, res) {
    var userinfo = req.body;
    var code = getRandomIntInclusive(123456, 999999);
    userDB.findOne({ 'phone': userinfo.phone }, function (e, result) {
        if (result == null) {
            
            userinfo.password=code;
           saltAndHash(userinfo.password, function (hash) {
            userinfo.password = hash;
            // append date stamp when record was created //
            userinfo.date = moment().format('MMMM Do YYYY, h:mm:ss a');
            userinfo.locations = [];
            userinfo.contacts = [];
            
            userDB.insert(userinfo, { safe: true }, function (err, result) {
                if (err) {
                    res.status(400).send({ 'Error': 'An error has occurred' });
                } else {
                    console.log('success: --' + JSON.stringify(result));
                    result.ops[0].token = generateToken(result.ops[0]._id);
                    userDB.save(result.ops[0], { safe: true }, function (e) {
                    });
                    var userobj = result.ops[0];
                    userobj.id = userobj._id;
                    delete userobj._id;
                    delete userobj.password;

                    //res.status(200).send({ 'code': code });
                    res.status(200).send();
                    return;
                }

            });

        });


        } else {
            
            sendSMS(userinfo.phone, 'Your phone verification code is: ' + code, function (err, o) {
                console.log("sms result", err);
                if (!err) {


           saltAndHash(code, function (hash) {
            result.password = hash;
                    userDB.save(result, { safe: true }, function (e) {
                    });

           });


                    res.status(200).send();
                } else {
                    res.status(500).send({ 'Error': 'SMS failed' });
                }
            });

        }
    });

}


exports.update = function (req, res) {
    var userinfo = req.body;
    var userid = null;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };


    try {
        userid = getObjectId(req.params.id);
    } catch (ex) {
        res.status(404).send({ 'Error': 'Record not found' });
        return;
    }
    userDB.findOne({ '_id': userid }, function (e, o) {
        if (userinfo['name']) o.name = userinfo['name'];
        if (userinfo['email']) o.email = userinfo['email'];
        if (userinfo['phone']) o.phone = userinfo.phone;
        if (userinfo['zip']) o.zip = userinfo['zip'];
        userDB.save(o, { safe: true }, function (e) {
            if (e) res.status(500).send({ 'Error': 'Error found' });
            else res.status(200).send({});
        });

    });
}

exports.updatePassword = function (req, res) {
    var userinfo = req.body;
    var userid = null;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };

    try {
        userid = getObjectId(userinfo['id']);
    } catch (ex) {
        res.status(404).send({ 'Error': 'Record not found' });
        return;
    }
    userDB.findOne({ '_id': userid }, function (e, o) {
        if (e) {
            res.status(404).send({ 'Error': 'Error found' });
        } else {
            saltAndHash(userinfo['password'], function (hash) {
                o.password = hash;
                userDB.save(o, { safe: true }, function (ex) {
                    if (ex) res.status(500).send({ 'Error': 'Error found' });
                    else res.status(200).send({});
                });
            });
        }
    });
}


exports.resetPassword = function (req, res) {
    var userinfo = req.body;
    var userid = null;
    var userheader = req.headers;

    try {
        var decoded = jwt.verify(userheader.token, config.secretKey);
        userid = getObjectId(decoded.user);
    } catch (ex) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    }

    userDB.findOne({ '_id': userid }, function (e, o) {
        if (e) {
            res.status(404).send({ 'Error': 'Error found' });
        } else {
            saltAndHash(userinfo.password, function (hash) {
                o.password = hash;
                userDB.save(o, { safe: true }, function (ex) {
                    if (ex) res.status(500).send({ 'Error': 'Error found' });
                    else res.status(200).send({});
                });
            });
        }
    });
}

exports.addLocation = function (req, res) {
    var locinfo = req.body;
    var userid = null;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };
    console.log(req.params.id);
    try {
        userid = getObjectId(req.params.id);
    } catch (ex) {
        res.status(404).send({ 'Error': 'Record not found' });
        return;
    }
    userDB.findOne({ '_id': userid }, function (e, result) {
        if (result) {
            if (!result.locations) {
                result.locations = [];
            }
            var output = result.locations.filter(function (value) { return value.displayName == locinfo.displayName; })
            console.log(output);
            if (output.length == 0) {
                locinfo.id = (new ObjectID()).toString();
                result.locations.push(locinfo);
                userDB.save(result, { safe: true }, function (ex) {
                    if (ex) {
                        res.status(500).send({ 'Error': 'Error found' });
                        return;
                    }

                });
                result.id = result.id;
                delete result._id;
                res.status(200).send(result);
            } else {
                res.status(400).send({ 'Error': 'Duplicated location' });
            }

        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });
}

exports.updateLocation = function (req, res) {
    var locinfo = req.body;
    var userid = null;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };

    try {
        userid = getObjectId(req.params.id);
    } catch (ex) {
        res.status(404).send({ 'Error': 'Record not found' });
        return;
    }
    userDB.findOne({ '_id': userid }, function (e, result) {
        if (result) {

            userDB.update(
                { _id: userid, "locations.id": req.params.lid },
                {
                    $set: {
                        "locations.$.displayName": locinfo.displayName,
                        "locations.$.coordinates": locinfo.coordinates,
                        "locations.$.alertRadius": locinfo.alertRadius,
                        "locations.$.enablePushNotifications": locinfo.enablePushNotifications,
                        "locations.$.enableSMS": locinfo.enableSMS,
                        "locations.$.enableEmail": locinfo.enableEmail
                    }
                },
                function (e, result) {
                    if (e) {
                        res.status(500).send({ 'Error': 'Update failed' });
                        return;
                    }
                }
            )



            res.status(200).send({});


        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });


}


exports.addContact = function (req, res) {
    var locinfo = req.body;
    var userid = null;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };
    console.log(req.params.id);
    try {
        userid = getObjectId(req.params.id);
    } catch (ex) {
        res.status(404).send({ 'Error': 'Record not found' });
        return;
    }
    userDB.findOne({ '_id': userid }, function (e, result) {
        if (result) {
            if (!result.contacts) {
                result.contacts = [];
            }
            var output = result.contacts.filter(function (value) { return value.name == locinfo.name; })
            console.log(output);
            if (output.length == 0) {
                locinfo.id = (new ObjectID()).toString();
                result.contacts.push(locinfo);
                userDB.save(result, { safe: true }, function (ex) {
                    if (ex) {
                        res.status(500).send({ 'Error': 'Error found' });
                        return;
                    }

                });
                result.id = result.id;
                delete result._id;
                res.status(200).send(result);
            } else {
                res.status(400).send({ 'Error': 'Duplicated Contact' });
            }



        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });
}

exports.updateContact = function (req, res) {
    var locinfo = req.body;
    var userid = null;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };

    try {
        userid = getObjectId(req.params.id);
    } catch (ex) {
        res.status(404).send({ 'Error': 'Record not found' });
        return;
    }
    userDB.findOne({ '_id': userid }, function (e, result) {
        if (result) {

            userDB.update(
                { _id: userid, "contacts.id": req.params.cid },
                {
                    $set: {
                        "contacts.$.name": locinfo.name,
                        "contacts.$.email": locinfo.email,
                        "contacts.$.phone": locinfo.phone,
                        "contacts.$.address": locinfo.address,
                        "contacts.$.city": locinfo.city,
                        "contacts.$.state": locinfo.state,
                        "contacts.$.zip": locinfo.zip
                    }
                },
                function (e, result) {
                    if (e) {
                        res.status(500).send({ 'Error': 'Update failed' });
                        return;
                    }
                }
            )


/**
let connection = new SMTPConnection({port:465,secure:false});
connection.connect(function(){ 

connection.send({from:"jhuang@hotbsoftware.com",to:"cctech@gmail.com"}, "aslkdfaklsdfj");

});

 */
            res.status(200).send({});


        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });


}


var generateToken = function (user) {
    var token = jwt.sign({ user: user }, config.secretKey);

    return token;
}
var generateResetToken = function (user) {
    var token = jwt.sign({ user: user }, config.secretKey, { expiresIn: '1h' });

    return token;
}
var verifyToken = function (token) {

    var result = true;
    try {
        var decoded = jwt.decode(token);
        console.log(JSON.stringify(decoded));
        //if(decoded.user!=user){
        //result=false;
        //}
        userDB.findOne({ '_id': getObjectId(decoded.user) }, function (e, result) {
            if (!result) {
                result = false;
            }
        });
    } catch (ex) {
        result = false;
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

var getObjectId = function (id) {
    return new require('mongodb').ObjectID(id);
}

var sendSMS = function (to_number, message, func_callback) {


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
    }, function (error, data) {
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

        sns.publish(params, function (err_publish, data) {
            if (err_publish) {
                console.log('Error sending a message', err_publish);

            } else {
                console.log('Sent message:', data.MessageId);
            }

            var params = {
                SubscriptionArn: SubscriptionArn
            };

            sns.unsubscribe(params, function (err, data) {
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