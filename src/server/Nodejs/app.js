var express    = require('express');
var app        = express();
var config = require('./config.json');
var user=require('./routes/user');
var bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.post('/users/phoneCode',user.phoneCode);
app.post('/users/changePassword',user.updatePassword);
app.post('/users/resetPassword',user.resetPassword);
app.post('/users/update',user.update);
app.post('/users/signin',user.login);
app.post('/users/phoneSignin',user.phoneLogin);
app.post('/users/email',user.getUserByEmail);
app.post('/users/id',user.getUserById);
app.post('/users/', user.add);
app.get('/user/:id/location/:lid', user.test);
app.get('/', function(req, res){

    res.type('html');
    res.send("<!DOCTYPE html><html><head><title>Hello</title><meta charset=\"utf-8\" /></head><body><p>Hello, World!</p></body></html>");
});
setInterval(function(){
  console.log('test');
}, 1000 * 1000);

process.on('uncaughtException', function (err) {
  console.log('Caught exception: ' + err);
});

app.listen(config.port, function() {
    console.log('3 Listening on port:' + config.port);
});