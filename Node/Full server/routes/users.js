'use strict';
var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function (req, res) {
    res.send('OK 200');
});

/* GET ALL Users ID */
router.get('/listIds', function (req, res) {

        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials
        console.log("Try list IDS");
        var jsonRet = '{ "users" : [{"name" : "GUY",	"ID"   : "308121383" }]}';

        var test = JSON.parse(jsonRet);
        var name = test.users[0].name;

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
                    return console.error('error fetching client from pool', err);
                }
                client.query('SELECT * FROM test_tbl',
                    [],
                    function(err, result) {
                        done();

                        if (err) {
                            return console.error('error happened during query', err);
                        }
                        console.log(result.rows[0]);
                        res.send(result.rows);
                    });
            });
    });

module.exports = router;
