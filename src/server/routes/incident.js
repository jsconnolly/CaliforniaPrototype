var crypto = require('crypto');
var moment = require('moment');
var config = require('../config.json');
var jwt = require('jsonwebtoken');
var ObjectID = require('mongodb').ObjectID;
var http = require('https');
var ses = require('node-ses')
    , client = ses.createClient({ key: config.accessKeyId, secret: config.secretAccessKey, amazon: config.emailEndpoint });
var userDB;
var db;
var MongoClient = require('mongodb').MongoClient;
var util = require('./util');
// Connect to the db
MongoClient.connect(config.db, function (err, db) {
    if (!err) {
        userDB = db.collection('user');
        incidentDB = db.collection('incident');
        util.log("We are connected");
    }
});

exports.importIncident = function () {

    //getWildFire();
    getRiverGauges();
    //getEarthquakes();


    createAlerts();























    // Give SES the details and let it construct the message for you.
    /*
    client.sendEmail({
       to: 'cctech@gmail.com'
     , from: 'noreply@hotbsoftware.com'
     , subject: 'greetings'
     , message: 'your <b>message</b> goes here'
     , altText: 'plain text'
    }, function (err, data, res) {
       console.log(err);
    });
    */
};





var getWildFire = function () {
    var url = 'https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&returnIdsOnly=false&returnCountOnly=false&where=1%3D1&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*';

    http.get(url, function (res) {
        var body = '';

        res.on('data', function (chunk) {
            body += chunk;
        });

        res.on('end', function () {
            var caResponse = JSON.parse(body);
            console.log("Got a response: ", caResponse.features[0].attributes);

            caResponse.features.forEach(function (item, index) {

                var incidentObj = item.attributes;
                incidentObj.type = "wildfire";
                incidentDB.findOne({ 'incidentname': incidentObj.incidentname, "type": "wildfire" }, function (e, result) {
                    console.log("result: ", result);

                    if (!result) {
                        incidentDB.insert(incidentObj, { safe: true }, function (err, result) {

                        });
                    }
                });


            });


        });

    }).on('error', function (e) {
        util.log("Got an error: "+e);
    });
}

var getRiverGauges = function () {
    //var url = 'https://idpgis.ncep.noaa.gov/arcgis/rest/services/NWS_Observations/ahps_riv_gauges/MapServer/0/query?f=json&where=state%20%3D%20%27CA%27&returnIdsOnly=false&returnCountOnly=false&where=1%3D1&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&state=CA';
    var url = "https://idpgis.ncep.noaa.gov/arcgis/rest/services/NWS_Observations/ahps_riv_gauges/MapServer/1/query?f=json&returnIdsOnly=false&where=(state%20%3D%20%27CA%27)%20AND%20(1%3D1)&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*";
    http.get(url, function (res) {
        var body = '';

        res.on('data', function (chunk) {
            body += chunk;
        });

        res.on('end', function () {
            var caResponse = JSON.parse(body);
            if (caResponse.features) {

                caResponse.features.forEach(function (item, index) {

                    var incidentObj = item.attributes;
                    incidentObj.type = "rivergauge";
                    incidentObj.name = incidentObj.gaugelid;
                    incidentObj.date = new Date(incidentObj.fcstissunc);
                    incidentObj.loc = [incidentObj.longitude, incidentObj.latitude];
                    incidentDB.findOne({ 'gaugelid': incidentObj.gaugelid, "type": "rivergauge" }, function (e, result) {
                        //console.log("result: ", result);

                        if (!result) {
                            incidentDB.update(incidentObj, incidentObj, { upsert: true, })
                        }
                    });


                });

            }
        });

    }).on('error', function (e) {
        util.log("Got an error: "+e);
    });
}


var getEarthquakes = function () {
    var url = 'https://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Earthquakes/EarthquakesFromLastSevenDays/MapServer/0/query?f=json&returnIdsOnly=false&where=((region%20LIKE%20%27%25California%25%27)%20AND%20(magnitude%20>%202))%20AND%20(1%3D1)&returnGeometry=true&spatialRel=esriSpatialRelIntersects&outFields=*&outSR=102100';

    http.get(url, function (res) {
        var body = '';

        res.on('data', function (chunk) {
            body += chunk;
        });

        res.on('end', function () {
            var caResponse = JSON.parse(body);
            if (caResponse.features) {

                caResponse.features.forEach(function (item, index) {

                    var incidentObj = item.attributes;
                    incidentObj.type = "earthquake";
                    //var t = new Date(incidentObj.datetime);
                    //var formatted = t.format("dd.mm.yyyy hh:MM:ss"); 
                    incidentObj.date = new Date(incidentObj.datetime);
                    incidentObj.name = incidentObj.eqid;
                    incidentObj.loc = [incidentObj.latitude, incidentObj.longitude];
                    incidentDB.findOne({ 'eqid': incidentObj.eqid, "type": "earthquake" }, function (e, result) {
                        //console.log("result: ", result);

                        if (!result) {

                            incidentDB.update(
                                incidentObj,
                                incidentObj,
                                {
                                    upsert: true,
                                }
                            )



                            //incidentDB.insert(incidentObj, { safe: true }, function (err, result) {

                            //});
                        }
                    });


                });
            }

        })

    }).on('error', function (e) {
        console.log("Got an error: ", e);
    });
}

var createAlerts = function () {


    // Create the index
    incidentDB.ensureIndex(
        { 'loc': "2dsphere" }, function (err, result) {

            var cursor = userDB.find();

            // Execute the each command, triggers for each document
            cursor.each(function (err, item) {
                if (item)
                    if (item.locations) {






                if (item.locations.length > 0)
                            item.locations.forEach(function (value) {

                                console.log("3" + value.coordinates.lat);

                                incidentDB.find(
                                    {
                                        'loc':
                                        {
                                            $near:
                                            {
                                                $geometry:
                                                { type: "Point", coordinates: [value.coordinates.lng, value.coordinates.lat] },
                                                $maxDistance: value.alertRadius ? (value.alertRadius * 1609) : 1609 * 2
                                            }
                                        }
                                    }
                                ).toArray(function (err, docs) {

                                    //console.log("err" + err);
                                    if(docs){
                                    docs.forEach(function (docValue) {
                                        //console.log("4" + docValue-item.date);
                                        if (!item.alerts) item.alerts = [];
                                        var output = item.alerts.filter(function (value) { return value.name == docValue.name; })
                                        if (output.length == 0) {
                                            var alert = {};
                                            alert.name = docValue.name;
                                            alert.type = docValue.type;
                                            alert.date = docValue.date;
                                            alert.loc = docValue.loc;
                                            alert.location = docValue.location;
                                            item.alerts.push(alert);
                                            //var emessage = alert.type + ' ' + alert.name + ' at ' + alert.location + ' on ' + alert.date;
                                            var emessage='CA-EMRG-AL: An alert for '+alert.type+' has been posted for your location: '+value.name+'. Please visit our site for further info'
                                            console.log("sending");

                                            if (value.enableSMS && item.phone) {
                                                util.sendSMS(item.phone, emessage, function (err, o) {

                                                    if (err) {


                                                    }

                                                });
                                            }
                                            if (value.enableEmail && item.email) {
                                                //if (item.email != "cctech@gmail.com")
                                                    client.sendEmail({
                                                        to: item.email
                                                        , from: 'noreply@hotbsoftware.com'
                                                        , subject: 'CA emergency alert'
                                                        , message: emessage

                                                    }, function (err, data, response) {
                                                        if (err) {

                                                        }
                                                    });
                                            }



                                        }
                                    });

                                    userDB.save(item, { safe: true }, function (ex) {

                                    });


                                }

                                    //console.log("Found the following records" + err);
                                    //console.log(docs);

                                });

                            });






                    };
            });
        });

}

