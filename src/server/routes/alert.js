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
        alertDB = db.collection('alert');
        util.log("We are all connected");
    }
});

exports.importAlert = function () {

/*
userDB.update(
      {'_id':{'$ne':'k'} }, 
      { $set : {active : true} }, 
      {
     
     multi: true
     
   },
       function(err){  
                    });
*/


    getWildFire();
    getRiverGauges();
    getEarthquakes();


    createAlerts();



};





var getWildFire = function () {
    //var url = 'https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&returnIdsOnly=false&returnCountOnly=false&where=1%3D1&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*';
    var url='https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&returnGeometry=false&spatialRel=esriSpatialRelIntersects&objectIds=1%2C2%2C3%2C4%2C5%2C6&outFields=*&orderByFields=objectid%20ASC';
    //var url='https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&returnGeometry=false&spatialRel=esriSpatialRelIntersects&objectIds=1%2C2%2C3%2C4%2C5%2C6&where=state%20%3D%20%27CA%27&outFields=*&orderByFields=objectid%20ASC';
    http.get(url, function (res) {
        var body = '';

        res.on('data', function (chunk) {
            body += chunk;
        });

        res.on('end', function () {
            var caResponse = JSON.parse(body);
            //console.log("Got a response: ", caResponse.features[0].attributes);

            caResponse.features.forEach(function (item, index) {
                    //if(!item.attributes)return;
                    var incidentobj = item.attributes;
if(incidentobj.state.match(/^(CA|ca)$/)){                    
                    var alertobj = {};
                    alertobj.type = "wildfire";
                    alertobj.name = incidentobj.incidentname;
                    alertobj.date = new Date(incidentobj.reportdatetime);
                    alertobj.loc = [incidentobj.longitude, incidentobj.latitude];
                    alertobj.location = incidentobj.state;
                    alertobj.createdby = "server";
                    alertobj.description = "acres:"+incidentobj.acres;
                    alertobj.url = incidentobj.hotlink;
                    alertDB.findOne({ 'name': alertobj.name, "type": "wildfire" }, function (e, result) {


                        if (!result) {
                            alertDB.update(alertobj, alertobj, { upsert: true, })
                        }
                    });
                }


            });


        });

    }).on('error', function (e) {
        util.log("Got an error: " + e);
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

                    var incidentobj = item.attributes;
if(incidentobj.status.match(/^(action|near|minor|moderate|major)$/)){                    
                    var alertobj = {};
                    alertobj.type = "rivergauge";
                    alertobj.name = incidentobj.gaugelid;
                    alertobj.date = (incidentobj.fcstissunc!='N/A')?(new Date(incidentobj.fcstissunc)):(new Date());
                    alertobj.loc = [incidentobj.longitude, incidentobj.latitude];
                    alertobj.location = incidentobj.location;
                    alertobj.createdby = "server";
                    alertobj.description = "";
                    alertobj.url = incidentobj.url;
                    alertDB.findOne({ 'name': alertobj.name, "type": "rivergauge" }, function (e, result) {


                        if (!result) {
                            alertDB.update(alertobj, alertobj, { upsert: true, })
                        }
                    });
                }

                });

            }
        });

    }).on('error', function (e) {
        util.log("Got an error: " + e);
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

                    var incidentobj = item.attributes;
if(parseFloat(incidentobj.magnitude)>3.5){ 


                    var alertobj = {};
                    alertobj.type = "earthquake";
                    alertobj.name = incidentobj.eqid;
                    alertobj.date = new Date(incidentobj.datetime);
                    alertobj.loc = [incidentobj.latitude, incidentobj.longitude];
                    alertobj.location = incidentobj.region;
                    alertobj.createdby = "server";
                    alertobj.description = "magnitude:"+incidentobj.magnitude;
                    alertobj.url = "";


                    alertDB.findOne({ 'name': alertobj.name, "type": "earthquake" }, function (e, result) {
                        //console.log("result: ", result);

                         if (!result) {
                            alertDB.update(alertobj, alertobj, { upsert: true, })
                        }



                        
                    });

                }


                });
            }

        })

    }).on('error', function (e) {
        util.log("Got an error: " + e);
    });
}

var createAlerts = function () {

    var ts = Math.round(new Date().getTime() / 1000);
    var tsYesterday = ts - (24 * 3600);

    // Create the index
    alertDB.ensureIndex(
        { 'loc': "2dsphere" }, function (err, result) {

            var cursor = userDB.find({'active':true});

            // Execute the each command, triggers for each document
            cursor.each(function (err, item) {
                if (item)
                    if (item.locations) {






                        if (item.locations.length > 0)
                            item.locations.forEach(function (value) {

                                
                                alertDB.find(
                                    {
                                        'loc':
                                        {
                                            $near:
                                            {
                                                $geometry:
                                                { type: "Point", coordinates: [value.coordinates.lng, value.coordinates.lat] },
                                                $maxDistance: value.alertRadius ? (value.alertRadius * 1609) : 1609 * 2
                                            }
                                        },
                                        'date':
                                        {
                                            $gt: new Date(tsYesterday)
                                        }
                                    }
                                ).toArray(function (err, docs) {

                                    //console.log("err" + err);
                                    if (docs) {
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
                                                if(docValue.description)alert.description=docValue.description;
                                                item.alerts.push(alert);
                                                //var emessage = alert.type + ' ' + alert.name + ' at ' + alert.location + ' on ' + alert.date;
                                                var emessage = 'CA-EMRG-AL: An alert type ' + alert.type + ' has been posted for your location: ' + value.displayName + '. Please visit our site for further info '+'https://goo.gl/ULsA2y'
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
                                                        , subject: 'CA Emergency Alert'
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

