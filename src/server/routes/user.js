var crypto = require('crypto');
var moment = require('moment');
var config = require('../config.json');
var jwt = require('jsonwebtoken');
var ObjectID = require('mongodb').ObjectID;
var userDB;
var db;
var ses = require('node-ses')
    , client = ses.createClient({ key: config.accessKeyId, secret: config.secretAccessKey, amazon: config.emailEndpoint });
var util = require('./util');
// Retrieve
var MongoClient = require('mongodb').MongoClient;

// Connect to the db
MongoClient.connect(config.db, function (err, db) {
    if (!err) {
        userDB = db.collection('user');
        alertDB = db.collection('alert');
        console.log("We are connected");
    }
});



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
            if(item.active){
            res.status(400).send({ 'Error': 'Records already exist' });
            return;

            }
        }

        saltAndHash(userinfo.password, function (hash) {
            userinfo.password = hash;
            // append date stamp when record was created //
            userinfo.date = new Date();//moment().format('MMMM Do YYYY, h:mm:ss a');
            userinfo.locations = [];
            userinfo.contacts = [];
            userinfo.active=true;
            //userobj.token=generateToken(userobj.id);
            userDB.insert(userinfo, { safe: true }, function (err, result) {
                if (err) {
                    res.status(400).send({ 'Error': 'An error has occurred' });
                } else {

                    result.ops[0].token = generateToken(result.ops[0]._id);
                    userDB.save(result.ops[0], { safe: true }, function (e) {
                    });
                    var userobj = result.ops[0];
                    userobj.id = userobj._id;
                    delete userobj._id;
                    delete userobj.password;
                    if (userinfo.email)
                        client.sendEmail({
                            to: userinfo.email
                            , from: 'noreply@hotbsoftware.com'
                            , subject: 'Registration'
                            , message: 'Welcome to California Emergency Alert'

                        }, function (err, data, response) {
                            if (err) {
                                res.status(500).send({ 'Error': 'Email failed' });
                            } else {
                                
                            }
                        });
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
    userDB.findOne({ 'phone': req.params.phone,'active':true }, function (e, result) {
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
    userDB.findOne({ 'email': req.params.email,'active':true }, function (e, result) {
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

    userDB.findOne({ 'email': userinfo.email,'active':true }, function (e, result) {
        if (result) {

            var token = generateResetToken(result._id);
            client.sendEmail({
                to: userinfo.email
                , from: 'noreply@hotbsoftware.com'
                , subject: 'Reset your password'
                , message: 'Click <a href="' + config.resetURL + '?token=' + token + '&email=' + userinfo.email + '">here</a> to reset your password.'

            }, function (err, data, response) {
                if (err) {
                    res.status(500).send({ 'Error': 'Email failed' });
                } else {
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



    userDB.findOne({ '_id': inputid,'active':true }, function (e, result) {
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

    userDB.findOne({ 'email': userinfo.email,'active':true }, function (e, result) {
        if (result == null) {
            res.status(404).send({ 'Error': 'User not found' });
        } else {
            validatePassword(userinfo['password'], result.password, function (err, o) {
                if (o) {
                    result.token = generateToken(result._id);
                    userDB.save(result, { safe: true }, function (e) {
                    });
                    var returnObj = result;
                    returnObj.id = result._id;
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

    userDB.findOne({ 'phone': userinfo.phone,'active':true }, function (e, result) {
        if (result == null) {
            res.status(404).send({ 'Error': 'User not found' });
        } else {
            if (!userinfo.password) {
                res.status(400).send({ 'Error': 'Password is required' });
                return;
            }

            validatePassword(userinfo.password, result.password, function (err, o) {
                if (o) {
                    result.token = generateToken(result._id);
                    userDB.save(result, { safe: true }, function (e) {
                    });
                    var returnObj = result;
                    returnObj.id = result._id;
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
    userDB.findOne({ 'phone': userinfo.phone,'active':true }, function (e, result) {
        if (result == null) {

            userinfo.password = code;
            saltAndHash(userinfo.password, function (hash) {
                userinfo.password = hash;
                // append date stamp when record was created //
                userinfo.date = new Date();
                userinfo.locations = [];
                userinfo.contacts = [];
                userinfo.active=true;

                userDB.insert(userinfo, { safe: true }, function (err, result) {
                    if (err) {
                        res.status(400).send({ 'Error': 'An error has occurred' });
                    } else {
                        result.ops[0].token = generateToken(result.ops[0]._id);
                        userDB.save(result.ops[0], { safe: true }, function (e) {
                        });
                        var userobj = result.ops[0];
                        userobj.id = userobj._id;
                        delete userobj._id;
                        delete userobj.password;
                        util.sendSMS(userinfo.phone, 'Your phone verification code is: ' + code, function (err, o) {
                        });
                        //res.status(200).send({ 'code': code });
                        res.status(200).send();
                        return;
                    }

                });

            });


        } else {

            util.sendSMS(userinfo.phone, 'Your phone verification code is: ' + code, function (err, o) {
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


exports.webPhoneCode = function (req, res) {
    var userinfo = req.body;
    var userheader = req.headers;
    if (!verifyToken(userheader.token)) {
        res.status(401).send({ 'Error': 'Invalid token' });
        return;
    };
    userDB.findOne({ 'phone': userinfo.phone,'active':true }, function (e, result) {
        if (result == null) {
             var code = getRandomIntInclusive(123456, 999999);
            util.sendSMS(userinfo.phone, 'CA-EMRG-AL:Your phone verification code is: ' + code, function (err, o) {
                console.log("sms result", err);
                if (!err) {
                    res.status(200).send({ 'code': code });
                } else {
                    res.status(500).send({ 'Error': 'SMS failed' });
                }
            });           
        } else {
            res.status(401).send({ 'Error': 'Phone number already exist' });


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
    userDB.findOne({ '_id': userid,'active':true }, function (e, o) {

        if(o){
        if (userinfo.name) o.name = userinfo.name;
        if (userinfo.email) o.email = userinfo.email;
        if (userinfo.phone) o.phone = userinfo.phone;
        if (userinfo.zip) o.zip = userinfo.zip;
        userDB.save(o, { safe: true }, function (e) {
            if (e) res.status(500).send({ 'Error': 'Error found' });
            else res.status(200).send({});
        });
    }else{
        res.status(404).send({ 'Error': 'Record not found' });
    }
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
    userDB.findOne({ '_id': userid,'active':true }, function (e, o) {
        if (!o) {
            res.status(404).send({ 'Error': 'Record not found' });
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

    userDB.findOne({ '_id': userid,'active':true }, function (e, o) {
        if (!o) {
            res.status(404).send({ 'Error': 'Record not found' });
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
    userDB.findOne({ '_id': userid,'active':true }, function (e, result) {
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
    userDB.findOne({ '_id': userid,'active':true }, function (e, result) {
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



exports.deleteLocation = function (req, res) {
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
    userDB.findOne({ '_id': userid,'active':true }, function (e, result) {
        if (result) {
            

            userDB.update(
                { '_id': userid},
                { $pull: { "locations" : { id: req.params.lid } } },
                
                false,
                function (e, result) {
                    if (e) {
                        console.log(e);
                        res.status(500).send({ 'Error': 'Delete failed' });
                        return;
                    }else{
                        res.status(200).send({});
                        
                    }
                }
            )



            

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
    userDB.findOne({ '_id': userid,'active':true }, function (e, result) {
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
    userDB.findOne({ '_id': userid,'active':true }, function (e, result) {
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

exports.unsubscribe = function (req, res) {
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
    userDB.findOne({ '_id': userid,'active':true }, function (e, result) {
        if (result) {

            userDB.update(
                { _id: userid },
                {
                    $set: {
                        "active": false
                    }
                },
                function (e, result) {
                    if (e) {
                        res.status(500).send({ 'Error': 'Update failed' });
                        return;
                    }
                    res.status(200).send({});
                }
            )

        } else {
            res.status(404).send({ 'Error': 'Record not found' });
        }


    });


}




exports.getAlerts = function (req, res) {

    var alerts = [];
    alertDB.find().toArray(function (err, docs) {
        if (err) {
            res.status(500).send({ 'Error': "Failed" });
        }
        if (docs) {
            docs.forEach(function (item) {
                var alert = {};
                alert.name = item.name;
                alert.type = item.type;
                alert.date = item.date;
                alert.loc = item.loc;
                alert.location = item.location;
                alert.description=item.description;
                alert.createdby=item.createdby;
                alerts.push(alert);

            });

        }
        alerts.sort(function(a, b){
       var dateA=new Date(a.date), dateB=new Date(b.date);
    return dateB-dateA; 
})
        res.status(200).send(alerts);

    });





}

exports.addAlert = function (req, res) {

    var alert = req.body;
    if (!alert.name || !alert.type) {
        res.status(400).send({ 'Error': 'Alert Name and Type are required' });
        return;
    }
    alertDB.findOne({ $or: [{ 'name': alert.name }] }, function (err, item) {
        if (item) {
            res.status(400).send({ 'Error': 'Records already exist' });
            return;
        }
        alert.date = new Date();
        alert.createdby="admin";
        alertDB.insert(alert, { safe: true }, function (err, result) {
            if (err) {
                res.status(400).send({ 'Error': 'An error has occurred' });
            } else {
                var alertobj = result.ops[0];
                alertobj.id = alertobj._id;
                delete alertobj._id;

                res.status(200).send(alertobj);
            }

        });




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

function getRandomIntInclusive(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}