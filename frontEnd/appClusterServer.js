var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cors = require('cors');
var hbase = require('hbase-rpc-client');




var app = express();
var ab2str = require('arraybuffer-to-string')
var uint8 = new Uint8Array([72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33])

app.use(cors({ origin: true, credentials: true }));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ 'extended': 'false' }));
app.use(express.static(__dirname + '/front'));
// Permit server to receive heavy file
app.use(bodyParser());
app.use(bodyParser({ limit: '50mb' }));
app.use(bodyParser.urlencoded({ limit: '50mb' }));
// Port Number 
const port = process.env.PORT || 8000;


var client = hbase({
  zookeeperHosts: 'localhost:2181',
  zookeeperRoot: '/hbase',
  rootRegionZKPath: '/meta-region-server',
  rpcTimeout: 30000,
  pingTimeout: 30000,
  callTimeout: 5000,
  tcpNoDelay: "no",
  tcpKeepAlive: "yes"
});



app.get('/getPoly1', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('heightPointDataDG0');
  var map = new Map();
  var i = 0;
  var stepLat = 5;
  var stepLng = 10;
  var ymin = -180;
  var xmin = -90;
  var len = 0;

  for (i = 0; i < 1296; i++) {
    var obj = {};
    obj.latmin = ymin;
    obj.latmax = ymin + stepLat;
    obj.lngmin = xmin;
    obj.lngmax = xmin + stepLng;
    len += 1;;
    xmin += stepLng;
    if (len % 36 == 0) {
      ymin += stepLat;
      xmin = -180;

    }
    map.set(i, obj);

  };
  //console.log(map.get(500));
  scan.each(function (err, result) {
    try {
      let key = ab2str(result.row, encoding = 'utf8');
      let ht = ab2str(result.columns[0].value, encoding = 'utf8');
      let lat = ab2str(result.columns[1].value, encoding = 'utf8');
      let lng = ab2str(result.columns[2].value, encoding = 'utf8');

      let grid = map.get(parseInt(key));
      //console.log(key);
      //console.log(map.get(parseInt(key)));
      let obj = {
        "type": "Feature",
        "properties": {
          "xlevel": ht
        },
        "geometry": {
          "type": "Polygon",
          "coordinates": [[
            grid.lngmin
            , grid.latmin
          ], [
            grid.lngmin
            ,
            grid.latmax
          ], [
            grid.lngmax
            ,
            grid.latmax
          ], [
            grid.lngmax
            ,
            grid.latmin
          ]]
        }
      };
      tab.push(obj);
      i++;

    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

  //res.json(map.get(1295));

})


app.get('/getPoly2', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('heightPointDataDG1');
  var map = new Map();
  var i = 0;
  var stepLat = 2.5;
  var stepLng = 5;
  var ymin = -180;
  var xmin = -90;
  var len = 0;

  for (i = 0; i < 5184; i++) {
    var obj = {};
    obj.latmin = ymin;
    obj.latmax = ymin + stepLat;
    obj.lngmin = xmin;
    obj.lngmax = xmin + stepLng;
    len += 1;;
    xmin += stepLng;
    if (len % 72 == 0) {
      ymin += stepLat;
      xmin = -180;

    }
    map.set(i, obj);

  };
  //console.log(map.get(500));
  scan.each(function (err, result) {
    try {
      let key = ab2str(result.row, encoding = 'utf8');
      let ht = ab2str(result.columns[0].value, encoding = 'utf8');
      let lat = ab2str(result.columns[1].value, encoding = 'utf8');
      let lng = ab2str(result.columns[2].value, encoding = 'utf8');

      let grid = map.get(parseInt(key));
      //console.log(key);
      //console.log(map.get(parseInt(key)));
      let obj = {
        "type": "Feature",
        "properties": {
          "xlevel": ht
        },
        "geometry": {
          "type": "Polygon",
          "coordinates": [[
            grid.lngmin
            , grid.latmin
          ], [
            grid.lngmin
            ,
            grid.latmax
          ], [
            grid.lngmax
            ,
            grid.latmax
          ], [
            grid.lngmax
            ,
            grid.latmin
          ]]
        }
      };
      tab.push(obj);
      i++;

    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

  //res.json(map.get(1295));

})

app.get('/getPoly3', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('heightPointDataDG2');
  var map = new Map();
  var i = 0;
  var stepLat = 1.25;
  var stepLng = 2.5;
  var ymin = -180;
  var xmin = -90;
  var len = 0;

  for (i = 0; i < 20736; i++) {
    var obj = {};
    obj.latmin = ymin;
    obj.latmax = ymin + stepLat;
    obj.lngmin = xmin;
    obj.lngmax = xmin + stepLng;
    len += 1;;
    xmin += stepLng;
    if (len % 144 == 0) {
      ymin += stepLat;
      xmin = -180;

    }
    map.set(i, obj);

  };
  //console.log(map.get(500));
  scan.each(function (err, result) {
    try {
      let key = ab2str(result.row, encoding = 'utf8');
      let ht = ab2str(result.columns[0].value, encoding = 'utf8');
      let lat = ab2str(result.columns[1].value, encoding = 'utf8');
      let lng = ab2str(result.columns[2].value, encoding = 'utf8');

      let grid = map.get(parseInt(key));
      //console.log(key);
      //console.log(map.get(parseInt(key)));
      let obj = {
        "type": "Feature",
        "properties": {
          "xlevel": ht
        },
        "geometry": {
          "type": "Polygon",
          "coordinates": [[
            grid.lngmin
            , grid.latmin
          ], [
            grid.lngmin
            ,
            grid.latmax
          ], [
            grid.lngmax
            ,
            grid.latmax
          ], [
            grid.lngmax
            ,
            grid.latmin
          ]]
        }
      };
      tab.push(obj);
      i++;

    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

  //res.json(map.get(1295));

})

app.get('/getLevelData1', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('heightPointDataDG0');
  var i = 0;

  scan.each(function (err, result) {
    try {
      let ht = ab2str(result.columns[0].value, encoding = 'utf8');
      let lat = ab2str(result.columns[1].value, encoding = 'utf8');
      let lng = ab2str(result.columns[2].value, encoding = 'utf8');


      let obj = {
        "type": "Feature",
        "properties": {
          "xlevel": ht
        },
        "geometry": {
          "type": "Point",
          "coordinates": [lng, lat]
        }
      };
      tab.push(obj);
      i++;
    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

})
app.get('/getLevelData2', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('heightPointDataDG1');
  var i = 0;

  scan.each(function (err, result) {
    try {
      let ht = ab2str(result.columns[0].value, encoding = 'utf8');
      let lat = ab2str(result.columns[1].value, encoding = 'utf8');
      let lng = ab2str(result.columns[2].value, encoding = 'utf8');


      let obj = {
        "type": "Feature",
        "properties": {
          "xlevel": ht
        },
        "geometry": {
          "type": "Point",
          "coordinates": [lng, lat]
        }
      };
      tab.push(obj);
      i++;
    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

})
app.get('/getLevelData3', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('heightPointDataDG2');
  var i = 0;

  scan.each(function (err, result) {
    try {
      let ht = ab2str(result.columns[0].value, encoding = 'utf8');
      let lat = ab2str(result.columns[1].value, encoding = 'utf8');
      let lng = ab2str(result.columns[2].value, encoding = 'utf8');


      let obj = {
        "type": "Feature",
        "properties": {
          "xlevel": ht
        },
        "geometry": {
          "type": "Point",
          "coordinates": [lng, lat]
        }
      };
      tab.push(obj);
      i++;
    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

})

app.get('/getPopData1', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('worldCitiesPopPaysDG1');
  var i = 0;

  scan.each(function (err, result) {
    try {
      console.log(ab2str(result.row, encoding = 'utf8'));
      let codeP = ab2str(result.row, encoding = 'utf8');
      let pop = ab2str(result.columns[2].value, encoding = 'utf8');
      let lat = ab2str(result.columns[1].value, encoding = 'utf8');
      let lng = ab2str(result.columns[0].value, encoding = 'utf8');


      let obj = {
        "type": "Feature",
        "properties": {
          "codeP": codeP,
          "pop": pop
        },
        "geometry": {
          "type": "Point",
          "coordinates": [lng, lat]
        }
      };
      tab.push(obj);
      i++;
    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

})
app.get('/getPopData2', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('worldCitiesPopPaysDG2');
  var i = 0;

  scan.each(function (err, result) {
    try {
      let region = ab2str(result.columns[3].value, encoding = 'utf8');
      let pop = ab2str(result.columns[2].value, encoding = 'utf8');
      let lat = ab2str(result.columns[0].value, encoding = 'utf8');
      let lng = ab2str(result.columns[1].value, encoding = 'utf8');


      let obj = {
        "type": "Feature",
        "properties": {
          "region": region,
          "pop": pop
        },
        "geometry": {
          "type": "Point",
          "coordinates": [lng, lat]
        }
      };
      tab.push(obj);
      i++;
    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

})

app.get('/getPopData3', function (req, res) {

  client.on('error', function (err) {
    console.log(err)
  })
  var scan;
  var tab = [];
  scan = client.getScanner('worldCitiesPopPaysDG3');
  var i = 0;

  scan.each(function (err, result) {
    try {

      //let region = ab2str(result.columns[2].value, encoding = 'utf8');
      let pop = ab2str(result.columns[2].value, encoding = 'utf8');
      let lat = ab2str(result.columns[0].value, encoding = 'utf8');
      let lng = ab2str(result.columns[1].value, encoding = 'utf8');
      let city = ab2str(result.columns[3].value, encoding = 'utf8');

      let obj = {
        "type": "Feature",
        "properties": {
          "city": city,
          "pop": pop
        },
        "geometry": {
          "type": "Point",
          "coordinates": [lng, lat]
        }
      };
      tab.push(obj);
      i++;
    }
    catch (fin) {
      console.log('fin');
      res.json(tab);
    }
  });

})

app.get('/1', function (req, res) {
  res.sendFile(path.join(__dirname + '/front/indexCountryRegion.html'));
});
app.get('/2', function (req, res) {
  res.sendFile(path.join(__dirname + '/front/indexLevelPolygon.html'));
});
app.get('/2b', function (req, res) {
  res.sendFile(path.join(__dirname + '/front/index.html'));
});
// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


app.listen(1994);


module.exports = app;
