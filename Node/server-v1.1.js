var express = require('express');
var app = express();
var fs = require("fs");
var bodyParser = require("body-parser");
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.get('/', function(req, res){
  res.send('hello world my world');
});

app.get('/recommendedPhones', function(req,res){
	const pg = require('pg')  
		const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials

		pg.connect(conString, function (err, client, done) {  
		if (err) {
			res.send('err');
			return console.error('error fetching client from pool', err)
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
})

app.get('/phonesList', function(req,res){
	const pg = require('pg')  
		const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials

		pg.connect(conString, function (err, client, done) {  
		if (err) {
			res.send('err');
			return console.error('error fetching client from pool', err)
		  }
		  
		  client.query('SELECT * FROM phones', [], function (err, result) {
			done()

			if (err) {
				res.send('err2');
			  return console.error('error happened during query', err)
			}
			console.log('phonesList succeeded');
			res.send(result.rows);
		  })
		})
})

app.get('/phonesNames', function(req,res){
	const pg = require('pg')  
		const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials

		pg.connect(conString, function (err, client, done) {  
		if (err) {
			res.send('err');
			return console.error('error fetching client from pool', err)
		  }
		  
		  client.query('SELECT name FROM phones', [], function (err, result) {
			done()

			if (err) {
				res.send('err2');
			  return console.error('error happened during query', err)
			}
			
			for (var i = 0; i<result.rows.length; i++){
				console.log("mishk: " + result.rows[i].name);
			}
				
			res.send(result.rows);
		  })
		})
})

app.get('/listUsers', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
       console.log( data );
       res.end( data );
   });
})

app.get('/listIds', function (req, res) {
		var jsonRet = '{ "users" : [{"name" : "GUY",	"ID"   : "308121383" }]}';
					  
		var test = JSON.parse(jsonRet);
		var name = test.users[0].name;
		
		const pg = require('pg')  
		const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials

		pg.connect(conString, function (err, client, done) {  
		if (err) {
			return console.error('error fetching client from pool', err)
		  }
		  client.query('SELECT * FROM test_tbl', [], function (err, result) {
			done()

			if (err) {
			  return console.error('error happened during query', err)
			}
			console.log(result.rows[0]);
			res.send(result.rows);
		  })
		})
})

app.post('/phoneUsage', function (req, res) {
		//var jsonReq = JSON.parse(req);
		
		var user_id = req.body.user;
		var battery = req.body.battery;
		var idle_time = req.body.idle;
		var appsuse = req.body.appsuse;
		var stor_used = req.body.stor_used;
		var stat = 'OK';
		
		const pg = require('pg')  
		const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials

		pg.connect(conString, function (err, client, done) {  
		if (err) {
			return console.error('error fetching client from pool', err)
		  }
		  client.query('INSERT INTO phoneusage (user_id, battery_usage, idle_time,stor_used, apps_usage, time_stamp) VALUES ($1, $2, $3, $4, $5, CURRENT_TIMESTAMP);', [user_id, battery, idle_time, stor_used, appsuse], function (err, result) {
			done()

			if (err) {
			  console.error('error happened during query', err);
			  stat = 'ERROR';
			  res.send("ERROR");
			}
			if (done){
				res.send("OK");
			}
		  })
		})
})

app.post('/logSignal', function (req, res) {
	var user_id = req.query.user;
	
	const pg = require('pg');
	const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres'; // make sure to match your own database's credentials
	
	pg.connect(conString, function (err, client, done) {  
	if (err) {
		return console.error('error fetching client from pool', err)
	  }
	  client.query('INSERT INTO signal (userid, sampletime) VALUES ($1, CURRENT_TIMESTAMP);', [user_id], function (err, result) {
		done()

		if (err) {
		  console.error('error happened during query', err);
		  stat = 'ERROR';
		  res.send("ERROR");
		}
		if (done){
			res.send("OK");
		}
	  })
	})
})

app.post('/testAPI', function (req, res) {
	var debg = req.query.debg;
	
	console.log('debg param: ' + debg);
	console.log(req.headers);
	console.log(req.body);
	console.log(req.body.debg);
	var data = "ok";
	
	res.sendStatus(200)
})

app.get('/phoneUsage', function (req, res) {
	var jsonRet = '{ "users" : [{"name" : "GUY",	"ID"   : "308121383" }]}';
					  
		var user_id = req.query.user;
		
		const pg = require('pg')  
		const conString = 'postgres://postgres:postgres@persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com/postgres' // make sure to match your own database's credentials

		pg.connect(conString, function (err, client, done) {  
		if (err) {
			return console.error('error fetching client from pool', err)
		  }
		  client.query('SELECT * FROM phoneusage WHERE user_id = $1 ORDER BY time_stamp DESC LIMIT 1', [user_id], function (err, result) {
			done()

			if (err) {
			  return console.error('error happened during query', err)
			}
			
			
			res.send(result.rows[0]);
		  })
		})
})


var server = app.listen(80, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("App listening at http://%s:%s", host, port)

})



