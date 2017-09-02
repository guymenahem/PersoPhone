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
    var note = req.body.note;
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
					'INSERT INTO users_rates (user_id, phone_name, battery, camera, screen, reactivity, overall, note, last_update_date) VALUES ($1, $2, $3, $4, $5,$6,$7,$8, CURRENT_TIMESTAMP);',
					[user_id, phone_name, battery, camera, screen, reactivity,overall,note],
					function(err, result) {
						done();

						if (err) {
                            console.error('error happened during query insert users_rates', err);
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
					'UPDATE users_rates set battery=$1, camera=$2, screen=$3, reactivity=$4,overall=$5, note = $6, last_update_date=CURRENT_TIMESTAMP'
					+ ' WHERE user_id = $7 and phone_name=$8',
					[battery, camera, screen, reactivity,overall,note,user_id,phone_name],
					function(err, result) {
						done();

						if (err) {
                            console.error('error happened during query UPDATE users_rates', err);
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
    var phone_name = req.query.phone;
	console.log(user_id);
	pg.connect(conString,
		function(err, client, done) {
			if (err) {					
				console.error('error happened during userRates query', err);
			}
			else{
				client.query('SELECT * FROM users_rates where user_id = $1 and phone_name = $2',
                    [user_id, phone_name],
					function(err, result) {						
						if (err) {
							console.error('error happened during query userRates query', err);
						}
						else{
							console.log("GET userRates succeed");
							res.send(result.rows);
							done();
						}
					}
				);
			}
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
                    console.error('error fetching client from pool', err);
                }
				else{
					if (isNew){
						client.query(
							'INSERT INTO users_preferences (user_id, brand, operating_system, screen_size, price, last_update_date) VALUES ($1, $2, $3, $4, $5, CURRENT_TIMESTAMP);',
							[user_id, brand, os, screen, price],
							function(err, result) {							
								if (err) {
									console.error('error happened during query', err);
									var stat = 'ERROR';
									res.send("ERROR");
								}else{
									res.send({status:"OK"});
									done();								
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
								

								if (err) {
									console.error('error happened during query', err);
									var stat = 'ERROR';
									res.send("ERROR");
								}
								else{
									res.send({status:"OK"});
									done();
								}
							}
						);
					}
				}
            });
});

router.getUserPreferences = function (user_id) {
	console.log(user_id);
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
					console.error('error happened during getUserPreferences query', err);
                    reject(err);
					return;					
                }
				else{
					var stream = client.query('SELECT * FROM users_preferences where user_id = $1;',
						[user_id],
						function (err, result) {
							
							if (err) {
								console.error('error happened during getUserPreferences query', err);
								reject(err);    
								return;
							}else{
								console.log("GET userPreferences succeed");
								fulfill(result.rows);
								done();
							}
						}
                    );

                    stream.on('end', done);

				}
            }
        );
    });
}
router.get('/userPreferences', function (req, res) {
    var user_id = req.query.user;
	
    router.getUserPreferences(user_id).then(function (result) {
        res.send(result);
    },function(err){
		return console.log(err);
	});
});

router.getBatteryGrade = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
					console.error('error happened during battery grade query', err);
					reject(err);
					return;
                }
				
                var stream = client.query('SELECT avg(value) avg_value FROM battery_usage where user_id = $1 and phone_name = $2 and value <> 100;',
                    [user_id, phone_name],
                    function (err, result) {

                        if (err) {
							console.error('error happened during battery grade query', err);
                            reject(err);
							return;
                        }
						/*
                        var rows = result.rows;
						var grade = 0.0;
						var avgRank = 0.0;
						var diffRank = 0.0;
						
                        if (rows.length > 0) {
                            var deltas = [];
							var avg = 0.0;
							var cnt = 0;
							
							// Fetch deltas and calculate average usage
                            for (var i = 0; i < rows.length - 1; i++) {
								if (rows[i].value < 100){
									cnt++;
									avg+= rows[i].value;
								}
                                if (rows[i + 1].value < rows[i].value) {
                                    deltas.push(rows[i].value - rows[i + 1].value);
                                }
                            }
							if (rows[rows.length - 1].value < 100){
								cnt++;
								avg+= rows[rows.length - 1].value;
							}
							
							// Get average usage (Half of the grade)
							if (cnt == 0){
								avgRank = 0.5;
							}
							else
							{
								avgRank = 0.5 - (avg / cnt / 2 / 100);
							}
							
							avg = 0.0;
							// Calculate deltas
							for (var i = 0; i < deltas.length; i++)
							{
								avg += (100 / deltas[i].value);
							}
							if (deltas.length == 0)
							{
								avg = 0;
							}
							else
							{
								avg = avg / deltas.length / 2 / 100;
							}
							
							diffRank = 0.5 - avg;
                        }

                        grade = avgRank + diffRank;*/
						var rows = result.rows;
                        var grade = 0;
						
                        if (rows.length > 0) {
                            grade = result.rows[0].avg_value / 100;
                        }

                        console.log("GET battery grade succeed");
                        var result = { "batteryGrade": grade };
                        fulfill(result);
                    }
                );
                stream.on('end',done);
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
					console.log("error happened during cpu grade query");
					reject(err);
					return;
                }
                var stream = client.query('SELECT avg(value) avg_value FROM cpu_usage where user_id = $1 and phone_name = $2;',
                    [user_id, phone_name],
                    function (err, result) {
                        //var avg_value = 0.2;
                        if (err) {
							console.error('error happened during cpu grade query', err);
                            reject(err);
							return;                            
                        }

                        var rows = result.rows;
                        var grade = 0;
						
                        if (rows.length > 0) {
                            grade = result.rows[0].avg_value / 100;
                        }

                        console.log("GET cpu grade succeed");
                        var result = { "cpuGrade": grade };
                        fulfill(result);
                    }
                );
                stream.on('end', done);
            }
        );
    });
}

router.getRamGrade = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
                    console.log("error happened during ram grade query");
                    reject(err);
                    return;
                }
                var stream = client.query('SELECT avg(value) avg_value FROM ram_usage where user_id = $1 and phone_name = $2;',
                    [user_id, phone_name],
                    function (err, result) {

                        if (err) {
                            console.error('error happened during ram grade query', err);
                            reject(err);
                            return;
                        }

                        var rows = result.rows;
                        var grade = 0;
						
                        if (rows.length > 0) {
                            grade = result.rows[0].avg_value / 100;
                        }

                        console.log("GET ram grade succeed");
                        var result = { "ramGrade": grade };
                        fulfill(result);
                    }
                );
                stream.on('end', done);
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
					console.log("error happened during getCameraGrade query", err);
					reject(err);
					return;
                }
                var stream = client.query('SELECT date_trunc(\'day\',insertion_time) insertion_day,sum(value) sum_value from camera_usage where user_id = $1 and phone_name = $2 group by date_trunc(\'day\',insertion_time);',
                    [user_id, phone_name],
                    function (err, result) {

                        if (err) {
							console.error('error happened during getCameraGrade query', err);
                            reject(err);
							return;
                        }
						
                        var rows = result.rows;
						var grade = 0;
						var maxPicsPerDay = 20;
						
                        if (rows.length > 0) {
							var avg = 0.0;
							
                            for (var i = 0; i < rows.length; i++) {
                                avg += rows[i].sum_value;
                            }
							avg = avg / rows.length;
							if (avg > maxPicsPerDay){
								grade = 1;
							}
							else
							{
								grade = avg / maxPicsPerDay;
							}
                        }

                        console.log("GET camera grade succeed");
                        var result = { "cameraGrade": grade };
                        fulfill(result);
                    }
                );
                stream.on('end', done);
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
					console.error('error happened during getStorageGrade query', err); 
					reject(err);
					return;
                }
                var stream = client.query('SELECT * FROM storage_usage where user_id = $1 and phone_name = $2 order by insertion_time desc limit 1;',
                    [user_id, phone_name],
                    function (err, result) {

                        if (err) {
							console.error('error happened during getStorageGrade query', err);
                            reject(err);
							return;
                        }

                        var rows = result.rows;
						var grade = 0;

                        if (rows.length == 1) {
							if (rows[0].total_storage == 0) {
								grade = 1;
							}
							else
							{
								grade = rows[0].free_storage / rows[0].total_storage;
							}
                        }

                        console.log("GET storage grade succeed");
                        var result = { "storageGrade": grade };
                        fulfill(result);
                    }
                );
                stream.on('end', done);
            }
        );
    });
}

///////////////////////////////////Added by adam 31/08 21:22///////////////////////////////////////////////
router.getBatteryUsageGraph = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var pg = require('pg');
        var conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres';// make sure to match your own database's credentials

        pg.connect(conString,
            function (err, client, done) {
                if (err) {
					console.error('error happened during getStorageGrade query', err); 
					reject(err);
					return;
                }
                var stream = client.query('select date_trunc(\'hour\', insertion_time) sample_hour,round(avg(value)) sample_value from battery_usage where user_id = $1 and phone_name = $2 group by date_trunc(\'hour\', insertion_time) order by date_trunc(\'hour\', insertion_time) limit 24;',
                    [user_id, phone_name],
                    function (err, result) {

                        if (err) {
							console.error('error happened during getStorageGrade query', err);
                            reject(err);
							return;
                        }

                        var rows = result.rows;
						var points = [];
						
						 if (rows.length > 0) {
							
                            for (var i = 0; i < rows.length; i++) {
                                points += { "x" : rows[i].sample_hour , "y" : rows[i].sample_value};
                            }
                        }

                        console.log("GET battery usage graph succeed");
                        var result = { "batteryUsageGraph": points };
                        fulfill(result);
                    }
                );
                stream.on('end', done);
            }
        );
    });
}

router.getBatteryUsage = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var p1 = router.getBatteryUsageGraph(user_id, phone_name);

        Promise.all([p1]).then(values => {
            (function formatUsage(usage) {
                var formattedUsage = {};
                for (var i = 0; i < usage.length; i++) {
                    var prop = Object.getOwnPropertyNames(usage[i])[0];
                    formattedUsage[prop] = usage[i][prop];
                }

                fulfill(formattedUsage);
            })(values);            
        },function(err){
			return console.log(err);
		})
    });    
}
///////////////////////////////////Added by adam 31/08 21:22///////////////////////////////////////////////

router.getAllGrades = function (user_id, phone_name) {
    return new Promise(function (fulfill, reject) {
        var p1 = router.getStorageGrade(user_id, phone_name);
        var p2 = router.getBatteryGrade(user_id, phone_name);
        var p3 = router.getCameraGrade(user_id, phone_name);
        var p4 = router.getCpuGrade(user_id, phone_name);
        var p5 = router.getRamGrade(user_id, phone_name);
        Promise.all([p1, p2, p3, p4, p5]).then(values => {
            (function formatGrades(grades) {
                var formattedGrades = {};
                for (var i = 0; i < grades.length; i++) {
                    var prop = Object.getOwnPropertyNames(grades[i])[0];
                    formattedGrades[prop] = grades[i][prop];
                }

                fulfill(formattedGrades);
            })(values);            
        },function(err){
			return console.log(err);
		})
    });    
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

router.get('/ramUsageGrade', function (req, res) {
    var user_id = req.query.user;
    var phone_name = req.query.phone_name;

    router.getRamGrade(user_id, phone_name).then(function (result) {
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

router.get('/getAllGrades', function (req, res) {
    var user_id = req.query.user;
    var phone_name = req.query.phone_name;

    router.getAllGrades(user_id, phone_name).then(function (result) {
        res.send(result);
    });
});

///////////////////////////////////Added by adam 31/08 21:22///////////////////////////////////////////////
router.get('/batteryUsageGraph', function (req, res) {

    var user_id = req.query.user;
    var phone_name = req.query.phone_name;

    router.getBatteryUsageGraph(user_id, phone_name).then(function (result) {
        res.send(result);
    });
});

///////////////////////////////////Added by adam 31/08 21:22///////////////////////////////////////////////
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
                    console.error('error fetching client from pool', err);
					return;
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
