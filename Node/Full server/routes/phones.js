'use strict';
var express = require('express');
var router = express.Router();
var users = require('./users');

function getAllPhones() {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
                    res.send('err');
                    return console.error('error fetching client from pool', err);
                }

                client.query('SELECT * FROM phones',
                    [],
                    function (err, result) {
                        done();

                        if (err) {
                            res.send('err2');
                            reject('error happened during query', err);
                        }
                        console.log('phonesList succeeded');
                        fulfill(result.rows);
                    });
            });
    });
}

router.get('/all',
    function (req, res) {
        getAllPhones().then(function (result) {
            res.send(result);
    });
});
	
router.get('/recommendedPhones', 
	function(req,res){
		const pg = require('pg')  
        const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials
        
		var user_id = req.query.user;
		var phone_name = req.query.phone_name;
        var user_screen_size = req.query.screen_size;

        users.getAllGrades(user_id, phone_name).then(function (result) {
            var batterGrade = result[0];

            pg.connect(conString, function (err, client, done) {
                if (err) {
                    res.send('err');
                    return console.error('error fetching client from pool', err);
                }

                client.query('SELECT * FROM phones order by random() limit 3', [], function (err, result) {
                    done()

                    if (err) {
                        res.send('err2');
                        return console.error('error happened during query', err)
                    }
                    console.log('recommendedPhones succeeded');
                    res.send(result.rows);
                })
            })
        });		
})

router.get('/phones_names',
    function(req, res) {
        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
                    res.send('err');
                    return console.error('error fetching client from pool', err);
                }

                client.query('SELECT name FROM phones',
                    [],
                    function(err, result) {
                        done();

                        if (err) {
                            res.send('err2');
                            return console.error('error happened during query', err);
                        }

                        for (var i = 0; i < result.rows.length; i++) {
                            console.log("mishk: " + result.rows[i].name);
                        }

                        res.send(result.rows);
                    });
            });
    });

module.exports = router;
