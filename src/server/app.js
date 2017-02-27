var express    = require('express');
var app        = express();
var config = require('./config.json');
var user=require('./routes/user');
var bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.put('/users/:id/contacts/:cid',user.updateContact);
app.put('/users/:id/contacts',user.addContact);
app.put('/users/:id/locations/:lid',user.updateLocation);
app.put('/users/:id/locations',user.addLocation);
app.post('/users/phoneCode',user.phoneCode);
app.post('/users/changePassword',user.updatePassword);
app.post('/users/resetPassword',user.resetPassword);
app.put('/users/:id',user.update);
app.post('/users/signin',user.login);
app.post('/users/phoneSignin',user.phoneLogin);
app.get('/users/phone/:phone',user.getUserByPhone);
app.get('/users/email/:email',user.getUserByEmail);
app.get('/users/:id',user.getUserById);
app.post('/users/', user.add);
app.get('/', function(req, res){

    res.type('html');
    res.send("<!DOCTYPE html><html><head><title>Hello</title><meta charset=\"utf-8\" /></head><body><p>Hello, World!</p></body></html>");
});
setInterval(function(){

}, 1000 * 1000);

app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.send({
        'Error': err.message
    });
});
//process.on('uncaughtException', function (err) {
//  console.log('Caught exception: ' + err);
//});

app.listen(config.port, function() {
    console.log('4 Listening on port:' + config.port);
});