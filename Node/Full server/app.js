'use strict';
var debug = require('debug');
var express = require('express');
var path = require('path');
var logger = require('morgan');
var bodyParser = require('body-parser');
var fs = require("fs");


var routes = require('./routes/index');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);

var users = require('./routes/users');
app.use('/users', users);

var phones = require('./routes/phones');
app.use('/phones', phones);

var phones_usage = require('./routes/phones_usage');
app.use('/phones_usage', phones_usage);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers
app.set('port', 80);

var pg = require('pg');
var conString =
    'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';

var server = app.listen(80,
    function() {

        var host = server.address().address;
        var port = server.address().port;

        console.log("App listening at http://%s:%s", host, port);
        debug('Express server listening on port ' + server.address().port);
    });
