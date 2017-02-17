var express    = require('express');
var app        = express();
var config = require('./config.json')

app.listen(config.port, function() {
    console.log('Listening on port:' + config.port);
});


app.get('/', function(req, res){

    res.type('html');
    res.send("<!DOCTYPE html><html><head><title>Hello</title><meta charset=\"utf-8\" /></head><body><p>Hello, World!</p></body></html>");
    });