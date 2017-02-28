var crypto = require('crypto');
var moment = require('moment');
var config = require('../config.json');
var AWS = require('aws-sdk');
var jwt = require('jsonwebtoken');
var ObjectID = require('mongodb').ObjectID;
var http = require('https');

var userDB;
var db;
var MongoClient = require('mongodb').MongoClient;

// Connect to the db
MongoClient.connect(config.db, function (err, db) {
    if (!err) {
        userDB = db.collection('user');
        incidentDB = db.collection('incident');
        console.log("We are connected");
    }
});

exports.importIncident = function () {

    //getWildFire();
    //getRiverGauges();
    getEarthquakes();


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
        console.log("Got an error: ", e);
    });
}

var getRiverGauges = function () {
    var url = 'https://idpgis.ncep.noaa.gov/arcgis/rest/services/NWS_Observations/ahps_riv_gauges/MapServer/0/query?f=json&where=state%20%3D%20%27CA%27&returnIdsOnly=false&returnCountOnly=false&where=1%3D1&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&state=CA';

    http.get(url, function (res) {
        var body = '';

        res.on('data', function (chunk) {
            body += chunk;
        });

        res.on('end', function () {
            var caResponse = JSON.parse(body);

            caResponse.features.forEach(function (item, index) {

                var incidentObj = item.attributes;
                incidentObj.type = "rivergauge";
                incidentDB.findOne({ 'gaugelid': incidentObj.gaugelid, "type": "rivergauge" }, function (e, result) {
                    console.log("result: ", result);

                    if (!result) {
                        incidentDB.insert(incidentObj, { safe: true }, function (err, result) {

                        });
                    }
                });


            });


        });

    }).on('error', function (e) {
        console.log("Got an error: ", e);
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