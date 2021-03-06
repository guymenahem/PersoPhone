﻿'use strict';
var express = require('express');
var router = express.Router();


router.post('/',
    function (req, res) {
        //var jsonReq = JSON.parse(req);

        var user_id = req.body.user;
        var phone_name = req.body.phone_name;
        var battery = req.body.battery;
        var idle_time = req.body.idle;
        var ram = req.body.ram_free_prc;
        var appsuse = req.body.appsuse;
        var stor_used = req.body.stor_used;
        var free_stor = req.body.free_stor;
        var camera = req.body.camera;
        var stat = 'OK';

        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
                    return console.error('error fetching client from pool', err);
                }

                function endHandler() {
                    count--; // decrement count by 1
                    if (count === 0) {
                        console.log("done all " + originalCount + " insert queries")
                        // two queries have ended, lets close the connection.
                        res.send("OK");
                        done();
                    }
                }

                function endFunc(err, result) {
                    if (err) {
                        return console.error('error happened during query', err);
                    }

                    endHandler();
                }
                var params = [user_id, phone_name, battery, idle_time, stor_used, free_stor, appsuse, camera];
                console.log("all parameters: " + params.toString());

                var queries = [];

                queries.push(client.query('INSERT INTO phoneusage (user_id, battery_usage, idle_time,stor_used, apps_usage, time_stamp) VALUES ($1, $2, $3, $4, $5, CURRENT_TIMESTAMP);',
                    [user_id, battery, idle_time, stor_used, appsuse], endFunc));

                queries.push(client.query('INSERT INTO battery_usage(user_id, phone_name, value, insertion_time) VALUES ($1, $2, $3, CURRENT_TIMESTAMP);',
                    [user_id, phone_name, battery], endFunc));

                queries.push(client.query('INSERT INTO cpu_usage(user_id, phone_name, value, insertion_time) VALUES ($1, $2, $3,  CURRENT_TIMESTAMP);',
                    [user_id, phone_name, idle_time], endFunc));

                queries.push(client.query('INSERT INTO ram_usage(user_id, phone_name, value, insertion_time) VALUES ($1, $2, $3,  CURRENT_TIMESTAMP);',
                    [user_id, phone_name, ram], endFunc));
				
                queries.push(client.query('INSERT INTO storage_usage(user_id, phone_name, total_storage, free_storage, insertion_time) VALUES ($1, $2, $3,$4, CURRENT_TIMESTAMP);',
                    [user_id, phone_name, stor_used, free_stor], endFunc));
				
                queries.push(client.query('INSERT INTO applications_usage(user_id, phone_name, value, insertion_time) VALUES ($1, $2, $3, CURRENT_TIMESTAMP);',
                    [user_id, phone_name, appsuse], endFunc));

                var count = queries.length;
                var originalCount = queries.length;

				/*query1.on('end', endHandler);
				query2.on('end', endHandler);
				query3.on('end', endHandler);
				query4.on('end', endHandler);
				query5.on('end', endHandler);*/

				
            });
    });
	
	router.post('/logCameraUse',
    function(req, res) {

        var user_id = req.body.user;
		var phone_name = req.body.phone_name;
		var camera = req.body.camera;
        var stat = 'OK';

        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
                    return console.error('error fetching client from pool', err);
                }				
				
				var query = client.query('INSERT INTO camera_usage(user_id, phone_name, value, insertion_time) VALUES ($1, $2, $3, CURRENT_TIMESTAMP);',
				[user_id,phone_name, camera]);
							
				query.on('end', endHandler);

				function endHandler () {
				   console.log("done logging camera use")
				   // two queries have ended, lets close the connection.
                   res.send("OK");
                   done();
				   
				}
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
