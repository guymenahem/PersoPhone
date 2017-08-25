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
                    reject(err);
                    console.error('error in connect getAllPhones', err);
                }

                var stream = client.query('SELECT * FROM phones',[],
                    function (err, result,done) {

                        if (err) {
                            reject(err);
                            client.end();
							return console.error('error in query getAllPhones', err);
                        }

                        console.log('phonesList succeeded');
                        fulfill(result.rows);
                    });
                stream.on('end', done);
            });
    });
}

router.get('/all',
    function (req, res) {
        getAllPhones().then(function (result) {
            res.send(result);
    },function(err){
		return console.log(err);		
	});
});
	
router.get('/recommendedPhones', 
	function(req,res){
		const pg = require('pg')  
        const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials
        
		var user_id = req.query.user;
		var phone_name = req.query.phone_name;
        var user_screen_size = req.query.screen_size;

        Promise.all([getAllPhones(), users.getAllGrades(user_id, phone_name), users.getUserPreferences(user_id)]).then(function (values) {
			var phones = values[0];								
			var grades = values[1];
            var prefernces = values[2][0];

			var brands = [
				{ name: 'apple', point: 1.0, factor: 1.12, screenFactor: 1.07, os:'ios' },
				{ name: 'samsung', point: 0.81, factor: 1.12, screenFactor: 1.07, os: 'android' },
				{ name: 'lg', point: 0.66, factor: 1.0, screenFactor: 1.07, os: 'android' },
				{ name: 'xiaomi', point: 0.5, factor: 0.86, screenFactor: 1.02, os: 'android' },
				{ name: 'motorola', point: 0.36, factor: 0.76, screenFactor: 0.94, os: 'android' },
				{ name: 'other', point: 0.18, factor: 0.72, screenFactor: 0.87, os: 'other' }
			];

			var operating_systems = [
				{ name: 'ios', point: 1.0 },
				{ name: 'android', point: 0.0 },
				{ name: 'other', point: 0.5 }
			];

			function getPhoneVector(phone) {
				// the order is brand, screen, battery, cpu,storage,ram, price
				var phoneVector = [];

				function BetweenOneAndZero(value) {
					return Math.max(0, Math.min(value, 1.0));
				}

				// *************** brand ******************************                

				function getBrand(name) {
					for (var i = 0; i < brands.length - 1; i++) {
						if (name.toLowerCase().indexOf(brands[i].name + ' ') != -1) {
							return brands[i];
						}
					}

					// else returns the last one the other
					return brands[i];
				}
				var brand = getBrand(phone.name);
				phoneVector.push(brand.point);

				// *************** os ******************************
				   
				function getOs(name) {
					for (var i = 0; i < operating_systems.length - 1; i++) {
						if (name.toLowerCase().indexOf(operating_systems[i].name) != -1) {
							return operating_systems[i];
						}
					}

					// else returns the last one the other
					return operating_systems[i];
				}

				var os = getOs(phone.os);
				// creating binnary dummy variables for OS
				// is ios
				phoneVector.push(os.point == 1 ? 1 :0);
				// is android
				phoneVector.push(os.point == 0 ? 1 :0);

				// *************** battery ***************************
				function getBattery(batteryValue) {
					var mAhValue = parseFloat(batteryValue);

					// calculates the mAh on scale between min 1000 mAh to 4500 mAh
					return BetweenOneAndZero(((mAhValue - 1000) / (4500 - 1000)) * brand.factor);
				}
				var battery = getBattery(phone.battery);
				phoneVector.push(battery);

				// *************** screen ***************************
				function getScreen(inches, resolution) {

					var resHelper = resolution.toLowerCase().replace(' ', '').split('x');
					resHelper[0] = parseInt(resHelper[0]);
					resHelper[1] = parseInt(resHelper[1]);

					var width = Math.min(resHelper[0], resHelper[1]);
					var height = Math.min(resHelper[0], resHelper[1]);

					var inchesValue = BetweenOneAndZero(((inches - 3.6) / 3.6)) / 2;
					// pixel per inch
					var ppi = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2) / inches);
					var resValue = BetweenOneAndZero(ppi / 520)/2;
					// 50% goes to scrren size and 50% to ppi
					return BetweenOneAndZero((inchesValue + resValue) * brand.screenFactor);
				}

				var screen = getScreen(phone.screen_size, phone.screen_resolution);
				phoneVector.push(screen);

				// *************** camera ***************************
				function getCamera(cameraValue) {
					var value = (cameraValue - 8) / (16 - 8);
					return BetweenOneAndZero(value * brand.factor);
				}

				var camera = getCamera(parseFloat(phone.camera));
				phoneVector.push(camera);

				// *************** cpu ***************************
				function getCpu(cpuValue) {
					var values = {
						"1": 0.5, "2": 0.6, "3": 0.65, "4": 0.7,
						"5": 0.8, "6": 0.86, "7": 0.92, "8": 1,
						"9": 1, "10": 1.05, "11": 1.08, "12": 1.08,
						"13": 1.08, "14": 1.1, "15": 1.2, "16": 1.3
					};
					return BetweenOneAndZero((values[cpuValue.toString()] || 1.3) * brand.factor);
				}

				var cpu = getCpu(phone.cpu_cores);
				phoneVector.push(cpu);

				// *************** storage ***************************
				function getStorage(storageValue) {
					if (storageValue <= 2) {
						return 0;
					}
					else if (storageValue <= 4) {
						return 0.08;
					}
					else if (storageValue <= 8) {
						return 0.24;
					}
					else if (storageValue <= 12) {
						return 0.3;
					}
					else if (storageValue <= 16) {
						return 0.5;
					}
					else if (storageValue <= 20) {
						return 0.55;
					}
					else if (storageValue <= 32) {
						return 0.65;
					}
					else if (storageValue <= 64) {
						return 0.72;
					}
					else if (storageValue <= 128) {
						return 0.78;
					}
					else if (storageValue <= 256) {
						return 0.90;
					}
					else {
						return 1.0;
					}
				}

				var storage = getStorage(phone.storage);
				phoneVector.push(storage);

				// *************** ram ***************************
				function getRam(ramValue) {
					var values = {
						"1": 0.4, "2": 0.6, "3": 0.75, "4": 0.8,
						"5": 0.85, "6": 0.89, "7": 0.92, "8": 1
					};
					return BetweenOneAndZero((values[ramValue.toString()] || 1.1) * brand.factor);
				}

				var ram = getRam(phone.cpu_cores);
				phoneVector.push(ram);

				// *************** price ***************************
				function getPrice(priceValue) {
					return BetweenOneAndZero((1 - ((priceValue - 500) / 3700)));
				}

				var price = getPrice(phone.price);
				phoneVector.push(price);

				return phoneVector;
			}

			var phonesVectors = [];
			for (var i = 0; i < phones.length; i++) {
				phonesVectors.push(getPhoneVector(phones[i]));
			}

			function GetUserDesiredPhone(grades, prefernces) {
				var userVector = [];
				// *************** brand ******************************
				var brand;
				if (prefernces['brand']) {
					brand = brands.filter(function (brand) {
						return brand.name == prefernces['brand'].toLowerCase();
					})[0].point;

					userVector.push(brand);
				}
				else {
					userVector.push(0.5);
				}

				// *************** os *********************************
				if (prefernces['operating_system']) {
					os = operating_systems.filter(function (opsys) {
						return opsys.name == prefernces['operating_system'].toLowerCase();
					})[0].point;

					// creating binnary dummy variables for OS
					// is ios
					userVector.push(os.point == 1 ? 1 :0);
					// is android
					userVector.push(os.point == 0 ? 1 :0);
						
				} else if (prefernces['brand']) {
					var os1 = brands.filter(function (brand) {
						return brand.name == prefernces['brand'].toLowerCase();
					})[0].os;

					var os = operating_systems.filter(function (opsys) {
						return opsys.name == os1.toLowerCase();
					})[0].point;

					// is ios
					userVector.push(os.point == 1 ? 1 :0);
					// is android
					userVector.push(os.point == 0 ? 1 :0);
				} else {
					// none ios, android it's ohter
					userVector.push(0);
					userVector.push(0);
				}

				// *************** battery *********************************
				userVector.push(grades['batteryGrade']);

				// *************** screen *********************************
				var screens = {
					'Very Big': 1,
					'Regular': 0.7,
					'Small': 0.4,
				}

				if (prefernces['screen']) {
					userVector.push(screens[prefernces['screen']]);
				}
				else {
					userVector.push(0.5);
				}

				// *************** camera *********************************
				userVector.push(grades['cameraGrade']);

				// *************** cpu *********************************
				userVector.push(grades['cpuGrade']);

				// *************** storage *********************************
				userVector.push(grades['storageGrade']);

				// *************** ram *********************************
				userVector.push(grades['cpuGrade']);
				// TODO:: userVector.push(grades['ramGrade']);

				// *************** screen *********************************
				var prices = {
					'Unlimited': 1,
					'Average': 0.56,
					'Cheap': 0.15,
				}

				if (prefernces['price']) {
					userVector.push(screens[prefernces['price']]);
				}
				else {
					userVector.push(0.5);
				}

				return userVector;
			}

			var userDesiredPhoneVector = GetUserDesiredPhone(grades, prefernces);
				
			function runAlgorithem(userVector, phonesVectors, k) {

				function cosineSimilarity(vectorA, vectorB) {
					var dotProduct = 0.0;
					var normA = 0.0;
					var normB = 0.0;

					for (var i = 0; i < vectorA.length; i++) {
						dotProduct += vectorA[i] * vectorB[i];
						normA += Math.pow(vectorA[i], 2);
						normB += Math.pow(vectorB[i], 2);
					}

					return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
				}

				for (var i = 0; i < phonesVectors.length; i++) {
					phones[i].cosineSimilarity = cosineSimilarity(userVector, phonesVectors[i])
				}
			}

			runAlgorithem(userDesiredPhoneVector, phonesVectors, 3);

			// K nearest neighbors
			phones.sort(function (a, b) { return b.cosineSimilarity - a.cosineSimilarity });

			res.send(phones.slice(0, 3));
		},function(err){
			console.error("something wrong in all (2) promises",err)
		});	
	}
);

router.get('/phones_names',
    function(req, res) {
        var pg = require('pg');
        var conString =
            'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials

        pg.connect(conString,
            function(err, client, done) {
                if (err) {
					console.error('error fetching client from pool', err);
                    res.send('err');
					return;
                }

                client.query('SELECT name FROM phones',
                    [],
                    function(err, result) {
                        done();

                        if (err) {
							console.error('error happened during query', err);
                            res.send('err2');
							return;
                        }

                        for (var i = 0; i < result.rows.length; i++) {
                            console.log("mishk: " + result.rows[i].name);
                        }

                        res.send(result.rows);
                    });
            });
    });

module.exports = router;
