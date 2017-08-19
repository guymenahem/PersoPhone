'use strict';
var express = require('express');
var router = express.Router();


router.post('/',
    function(req, res) {
        //var jsonReq = JSON.parse(req);

        var user_id = req.body.user;
        var battery = req.body.battery;
        var idle_time = req.body.idle;
        var appsuse = req.body.appsuse;
        var stor_used = req.body.stor_used;
        var stat = 'OK';

        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
                    return console.error('error fetching client from pool', err);
                }
                client.query(
                    'INSERT INTO phoneusage (user_id, battery_usage, idle_time,stor_used, apps_usage, time_stamp) VALUES ($1, $2, $3, $4, $5, CURRENT_TIMESTAMP);',
                    [user_id, battery, idle_time, stor_used, appsuse],
                    function(err, result) {
                        done();

                        if (err) {
                            console.error('error happened during query', err);
                            stat = 'ERROR';
                            res.send("ERROR");
                        }
                        if (done) {
                            res.send("OK");
                        }
                    });
            });
    });

router.get('/',
    function(req, res) {
        var jsonRet = '{ "users" : [{"name" : "GUY",	"ID"   : "308121383" }]}';

        var user_id = req.query.user;

        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
                    return console.error('error fetching client from pool', err);
                }
                client.query('SELECT * FROM phoneusage WHERE user_id = $1 ORDER BY time_stamp DESC LIMIT 1',
                    [user_id],
                    function(err, result) {
                        done();

                        if (err) {
                            return console.error('error happened during query', err);
                        }


                        res.send(result.rows[0]);
                    });
            });
    });

module.exports = router;
