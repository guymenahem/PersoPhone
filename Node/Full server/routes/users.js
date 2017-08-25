﻿'use strict';
var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function (req, res) {
    res.send('OK 200');
});

router.post('/newUser',
    function(req, res) {
        //var jsonReq = JSON.parse(req);

        var user_name = req.body.user_name;
		var phone_name = req.body.phone_name;
        var user_email = req.body.email;

        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
                    return console.error('error fetching client from pool', err);
                }				
				var params = [user_name, phone_name];
				console.log("all parameters: " + params.toString());
				var users_sequenceText = "'users_sequence'";
				var nextValQuery = client.query('select nextval(' + users_sequenceText + ') as sequence_value',[],function(err, result) {
					done();

					if (err) {
						return console.error('error happened during query', err);
					}
					
					var seq_val = result.rows[0].sequence_value;
					
					var query = client.query('INSERT INTO users(id, name, password, mail, isadmin, phone_name) VALUES ($1, $2, $3, $4, $5,$6);',
					[seq_val,user_name, "", user_email, false, phone_name]);
							
					query.on('end', endHandler);

					function endHandler () {
					   console.log("done all 6 insert queries")
					   // sends the new user_id
					   res.send({user_id:seq_val});
					}
				})
				
            });
    }
);


router.post('/userRates', function (req, res) {
	
	var user_id = req.body.user;
	var phone_name = req.body.phone_name;
	var battery = req.body.battery;
	var screen = req.body.screen;
	var camera = req.body.camera;
	var reactivity = req.body.reactivity;
	var overall = req.body.overall;
	var isNew = req.body.isNew;

	var pg = require('pg');
	var conString =
		'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

	pg.connect(conString,
		function(err, client, done) {
			if (err) {
				return console.error('error fetching client from pool', err);
			}
			if (isNew){									
				client.query(
					'INSERT INTO users_rates (user_id, phone_name, battery, camera, screen, reactivity, overall, last_update_date) VALUES ($1, $2, $3, $4, $5,$6,$7, CURRENT_TIMESTAMP);',
					[user_id, phone_name, battery, camera, screen, reactivity,overall],
					function(err, result) {
						done();

						if (err) {
							console.error('error happened during query', err);
							var stat = 'ERROR';
							res.send("ERROR");
						}
						if (done) {
							res.send({status:"OK"});
						}
					}
				);
			}
			else{
				client.query(
					'UPDATE users_rates set battery=$1, camera=$2, screen=$3, reactivity=$4,overall=$5, last_update_date=CURRENT_TIMESTAMP'
					+ ' WHERE user_id = $6 and phone_name=$7',
					[battery, camera, screen, reactivity,overall,user_id,phone_name],
					function(err, result) {
						done();

						if (err) {
							console.error('error happened during query', err);
							var stat = 'ERROR';
							res.send("ERROR");
						}
						if (done) {
							console.log("post userRates succeed");
							res.send({status:"OK"});
						}
					}
				);
			}
		});
});

router.get('/userRates', function (req, res) {
	var pg = require('pg');
	var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

	var user_id = req.query.user;		
	console.log(user_id);
	pg.connect(conString,
		function(err, client, done) {
			if (err) {					
				return console.log("ERR MISHK");
				//return console.error('error fetching client from pool', err);
			}
			client.query('SELECT * FROM users_rates where user_id = $1',
				[user_id],
				function(err, result) {
					done();

					if (err) {
						return console.error('error happened during query', err);
					}
					console.log("GET userRates succeed");
					res.send(result.rows);
				}
			);
		}
	);
});


router.post('/userPreferences', function (req, res) {
	//var jsonReq = JSON.parse(req);

        var user_id = req.body.user;
		var brand = req.body.brand;
        var os = req.body.os;
        var screen = req.body.screen;
        var price = req.body.price;
        var isNew = req.body.isNew;

        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
                    return console.error('error fetching client from pool', err);
                }
				if (isNew){
					client.query(
						'INSERT INTO users_preferences (user_id, brand, operating_system, screen_size, price, last_update_date) VALUES ($1, $2, $3, $4, $5, CURRENT_TIMESTAMP);',
						[user_id, brand, os, screen, price],
						function(err, result) {
							done();

							if (err) {
								console.error('error happened during query', err);
								var stat = 'ERROR';
								res.send("ERROR");
							}
							if (done) {
								res.send({status:"OK"});
							}
						}
					);
				}
				else{
					client.query(
						'UPDATE users_preferences set brand=$1, operating_system=$2, screen_size=$3, price=$4, last_update_date=CURRENT_TIMESTAMP'
						+ ' WHERE user_id = $5',
						[brand, os, screen, price,user_id],
						function(err, result) {
							done();

							if (err) {
								console.error('error happened during query', err);
								var stat = 'ERROR';
								res.send("ERROR");
							}
							if (done) {
								res.send({status:"OK"});
							}
						}
					);
				}
            });
});

router.get('/userPreferences', function (req, res) {
		var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        var user_id = req.query.user;		
		console.log(user_id);
        pg.connect(conString,
            function(err, client, done) {
                if (err) {					
					return console.log("ERR MISHK");
                    //return console.error('error fetching client from pool', err);
                }
                client.query('SELECT * FROM users_preferences where user_id = $1',
                    [user_id],
                    function(err, result) {
                        done();

                        if (err) {
                            return console.error('error happened during query', err);
                        }
                        console.log("GET userPreferences succeed");
                        res.send(result.rows);
                    }
				);
            }
		);
});

router.getBatteryGrade = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
                    return console.log("ERR MISHK");
                    //return console.error('error fetching client from pool', err);
                }
                client.query('SELECT * FROM battery_usage where user_id = $1 and phone_name = $2 order by insertion_time asc;',
                    [user_id, phone_name],
                    function (err, result) {


                        if (err) {
                            reject(err);
                            return console.error('error happened during query', err);
                        }

                        var rows = result.rows;

                        if (rows.length > 0) {
                            var deltas = [];

                            for (var i = 0; i < rows.length - 1; i++) {
                                if (rows[i + 1].value < rows[i].value) {
                                    deltas.push(value);
                                }
                            }
                        }

                        var grade = 0;

                        console.log("GET userPreferences succeed");
                        var result = { "batteryGrade": grade };
                        fulfill(result);
                    }
                );
            }
        );
    });    
}

router.getCpuGrade = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
                    return console.log("ERR MISHK");
                    //return console.error('error fetching client from pool', err);
                }
                client.query('SELECT * FROM cpu_usage where user_id = $1 and phone_name = $2 order by insertion_time asc;',
                    [user_id, phone_name],
                    function (err, result) {


                        if (err) {
                            reject(err);
                            return console.error('error happened during query', err);
                        }

                        var rows = result.rows;

                        if (rows.length > 0) {
                            var deltas = [];

                            for (var i = 0; i < rows.length - 1; i++) {
                                if (rows[i + 1].value < rows[i].value) {
                                    deltas.push(value);
                                }
                            }
                        }

                        var grade = 0;

                        console.log("GET cpu gragd succeed");
                        var result = { "cpuGrade": grade };
                        fulfill(result);
                    }
                );
            }
        );
    });
}

router.getCameraGrade = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
                    return console.log("ERR MISHK");
                    //return console.error('error fetching client from pool', err);
                }
                client.query('SELECT * FROM camera_usage where user_id = $1 and phone_name = $2 order by insertion_time asc;',
                    [user_id, phone_name],
                    function (err, result) {


                        if (err) {
                            reject(err);
                            return console.error('error happened during query', err);
                        }

                        var rows = result.rows;

                        if (rows.length > 0) {
                            var deltas = [];

                            for (var i = 0; i < rows.length - 1; i++) {
                                if (rows[i + 1].value < rows[i].value) {
                                    deltas.push(value);
                                }
                            }
                        }

                        var grade = 0;

                        console.log("GET camera grade succeed");
                        var result = { "cameraGrade": grade };
                        fulfill(result);
                    }
                );
            }
        );
    });
}

router.getStorageGrade = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
                    return console.log("ERR MISHK");
                    //return console.error('error fetching client from pool', err);
                }
                client.query('SELECT * FROM storage_usage where user_id = $1 and phone_name = $2 order by insertion_time asc;',
                    [user_id, phone_name],
                    function (err, result) {


                        if (err) {
                            reject(err);
                            return console.error('error happened during query', err);
                        }

                        var rows = result.rows;

                        if (rows.length > 0) {
                            var deltas = [];

                            for (var i = 0; i < rows.length - 1; i++) {
                                if (rows[i + 1].value < rows[i].value) {
                                    deltas.push(value);
                                }
                            }
                        }

                        var grade = 0;

                        console.log("GET storage grade succeed");
                        var result = { "storageGrade": grade };
                        fulfill(result);
                    }
                );
            }
        );
    });
}

router.getAllGrades = function (user_id, phone_name) {
    
}

router.get('/batteryUsageGrade', function (req, res) {

    var user_id = req.query.user;
    var phone_name = req.query.phone_name;

    router.getBatteryGrade(user_id, phone_name).then(function (result) {
        res.send(result);
    });
});

router.get('/cpuUsageGrade', function (req, res) {
    var user_id = req.query.user;
    var phone_name = req.query.phone_name;

    router.getCpuGrade(user_id, phone_name).then(function (result) {
        res.send(result);
    });
});

router.get('/cameraUsageGrade', function (req, res) {
    var user_id = req.query.user;
    var phone_name = req.query.phone_name;

    router.getBatteryGrade(user_id, phone_name).then(function (result) {
        res.send(result);
    });
});

router.get('/storageUsageGrade', function (req, res) {
    var user_id = req.query.user;
    var phone_name = req.query.phone_name;

    router.getStorageGrade(user_id, phone_name).then(function (result) {
        res.send(result);
    });
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
