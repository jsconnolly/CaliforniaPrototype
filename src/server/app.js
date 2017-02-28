var cluster = require('cluster');
var cron = require('cron');

if (cluster.isMaster) {

    cluster.fork();
    //cluster.fork();

    cluster.on('disconnect', function (worker) {
        console.error('disconnect!');
        cluster.fork();
    });
}
else {


    var express = require('express');
    var app = express();
    var config = require('./config.json');
    var user = require('./routes/user');
    var incident = require('./routes/incident');
    var bodyParser = require('body-parser');

    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(function (req, res, next) {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
        next();
    });
    app.put('/users/:id/contacts/:cid', user.updateContact);
    app.put('/users/:id/contacts', user.addContact);
    app.put('/users/:id/locations/:lid', user.updateLocation);
    app.put('/users/:id/locations', user.addLocation);
    app.post('/users/phoneCode', user.phoneCode);
    app.post('/users/changePassword', user.updatePassword);
    app.post('/users/forgotPassword', user.forgotPassword);
    app.post('/users/resetPassword', user.resetPassword);
    app.put('/users/:id', user.update);
    app.post('/users/signin', user.login);
    app.post('/users/phoneSignin', user.phoneLogin);
    app.get('/users/phone/:phone', user.getUserByPhone);
    app.get('/users/email/:email', user.getUserByEmail);
    app.get('/users/:id', user.getUserById);
    app.post('/users/', user.add);
    app.get('/', function (req, res) {

        res.type('html');
        res.send("<!DOCTYPE html><html><head><title>Hello</title><meta charset=\"utf-8\" /></head><body><p>Hello, World!</p></body></html>");
    });

var cronJob = cron.job(config.cron, function(){
    incident.importIncident();
    console.log("test cron");
}); 
cronJob.start();


/*
    setInterval(function () {
        console.log('test');
        incident.importIncident();
        console.log('test end');
    }, config.cron * 60 * 1000);
*/
    app.use(function (err, req, res, next) {
        res.status(err.status || 500);
        res.send({
            'Error': err.message
        });
    });
    process.on('uncaughtException', function (err) {
        console.log('Caught exception: ' + err);
        process.exit(1);
        cluster.worker.disconnect();
    });

    app.listen(config.port, function () {
        console.log('5 Listening on port:' + config.port);
    });






} 