var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cors = require('cors');
//var hbase = require('hbase-rpc-client');
var fs = require('fs');





var app = express();
//var ab2str = require('arraybuffer-to-string')
//var uint8 = new Uint8Array([72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33])

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
//const tableName = 'heightPointDataDG1';



app.get('/getOsm1', function (req, res) {
    
  // Create a relative path URL
  let reqPath = path.join(__dirname, 'front/localData/osmdata1.json');
  
      //Read JSON from relative path of this file
      fs.readFile(reqPath , 'utf8', function (err, data) {
          //Handle Error
         if(!err) {
           //Handle Success
           // console.log("Success"+data);
           // Parse Data to JSON OR
            var jsonObj = JSON.parse(data)
           //Send back as Response
            res.json( jsonObj );
          }else {
             //Handle Error
             res.end("Error: "+err )
          }
     });
  
   
  })

app.get('/getOsm2', function (req, res) {
    
  // Create a relative path URL
  let reqPath = path.join(__dirname, 'front/localData/osmdata2.json');
  
      //Read JSON from relative path of this file
      fs.readFile(reqPath , 'utf8', function (err, data) {
          //Handle Error
         if(!err) {
           //Handle Success
           // console.log("Success"+data);
           // Parse Data to JSON OR
            var jsonObj = JSON.parse(data)
           //Send back as Response
            res.json( jsonObj );
          }else {
             //Handle Error
             res.end("Error: "+err )
          }
     });
  
   
  })

app.get('/getOsm3', function (req, res) {
    
  // Create a relative path URL
  let reqPath = path.join(__dirname, 'front/localData/osmdata3.json');
  
      //Read JSON from relative path of this file
      fs.readFile(reqPath , 'utf8', function (err, data) {
          //Handle Error
         if(!err) {
           //Handle Success
           // console.log("Success"+data);
           // Parse Data to JSON OR
            var jsonObj = JSON.parse(data)
           //Send back as Response
            res.json( jsonObj );
          }else {
             //Handle Error
             res.end("Error: "+err )
          }
     });
  
   
  })

  
  app.get('/getPoly1', function (req, res) {
    
    // Create a relative path URL
    let reqPath = path.join(__dirname, 'front/localData/poly1.json');
    
        //Read JSON from relative path of this file
        fs.readFile(reqPath , 'utf8', function (err, data) {
            //Handle Error
           if(!err) {
             //Handle Success
             // console.log("Success"+data);
             // Parse Data to JSON OR
              var jsonObj = JSON.parse(data)
             //Send back as Response
              res.json( jsonObj );
            }else {
               //Handle Error
               res.end("Error: "+err )
            }
       });
    
     
    })
    app.get('/getPoly2', function (req, res) {
    
      // Create a relative path URL
      let reqPath = path.join(__dirname, 'front/localData/poly2.json');
      
          //Read JSON from relative path of this file
          fs.readFile(reqPath , 'utf8', function (err, data) {
              //Handle Error
             if(!err) {
               //Handle Success
               // console.log("Success"+data);
               // Parse Data to JSON OR
                var jsonObj = JSON.parse(data)
               //Send back as Response
                res.json( jsonObj );
              }else {
                 //Handle Error
                 res.end("Error: "+err )
              }
         });
      
       
      })
      app.get('/getPoly3', function (req, res) {
    
        // Create a relative path URL
        let reqPath = path.join(__dirname, 'front/localData/poly3.json');
        
            //Read JSON from relative path of this file
            fs.readFile(reqPath , 'utf8', function (err, data) {
                //Handle Error
               if(!err) {
                 //Handle Success
                 // console.log("Success"+data);
                 // Parse Data to JSON OR
                  var jsonObj = JSON.parse(data)
                 //Send back as Response
                  res.json( jsonObj );
                }else {
                   //Handle Error
                   res.end("Error: "+err )
                }
           });
        
         
        })
    
  app.get('/getLevelData2', function (req, res) {
    
    // Create a relative path URL
    let reqPath = path.join(__dirname, 'front/localData/leveldata2.json');
    
        //Read JSON from relative path of this file
        fs.readFile(reqPath , 'utf8', function (err, data) {
            //Handle Error
           if(!err) {
             //Handle Success
             // console.log("Success"+data);
             // Parse Data to JSON OR
              var jsonObj = JSON.parse(data)
             //Send back as Response
              res.json( jsonObj );
            }else {
               //Handle Error
               res.end("Error: "+err )
            }
       });
    
     
    })
    app.get('/getLevelData3', function (req, res) {
      
      //Create a relative path URL
      let reqPath = path.join(__dirname, 'front/localData/leveldata3.json');
      
          //Read JSON from relative path of this file
          fs.readFile(reqPath , 'utf8', function (err, data) {
              //Handle Error
             if(!err) {
               //Handle Success
               // console.log("Success"+data);
               // Parse Data to JSON OR
                var jsonObj = JSON.parse(data)
               //Send back as Response
                res.json( jsonObj );
              }else {
                 //Handle Error
                 res.end("Error: "+err )
              }
         });
      
       
      })


app.get('/getLevelData1', function (req, res) {

  // Create a relative path URL
  let reqPath = path.join(__dirname, 'front/localData/leveldata1.json');
  
      //Read JSON from relative path of this file
      fs.readFile(reqPath , 'utf8', function (err, data) {
          //Handle Error
         if(!err) {
           //Handle Success
           // console.log("Success"+data);
           // Parse Data to JSON OR
            var jsonObj = JSON.parse(data)
           //Send back as Response
            res.json( jsonObj );
          }else {
             //Handle Error
             res.end("Error: "+err )
          }
     });
  
})

app.get('/getPopData1', function (req, res) {
  let reqPath = path.join(__dirname, 'front/localData/pop1.json');
  
      //Read JSON from relative path of this file
      fs.readFile(reqPath , 'utf8', function (err, data) {
          //Handle Error
         if(!err) {
           //Handle Success
           // console.log("Success"+data);
           // Parse Data to JSON OR
            var jsonObj = JSON.parse(data)
           //Send back as Response
            res.json( jsonObj );
          }else {
             //Handle Error
             res.end("Error: "+err )
          }
     });

})
app.get('/getPopData2', function (req, res) {
 

  let reqPath = path.join(__dirname, 'front/localData/pop2.json');
  
      //Read JSON from relative path of this file
      fs.readFile(reqPath , 'utf8', function (err, data) {
          //Handle Error
         if(!err) {
           //Handle Success
           // console.log("Success"+data);
           // Parse Data to JSON OR
            var jsonObj = JSON.parse(data)
           //Send back as Response
            res.json( jsonObj );
          }else {
             //Handle Error
             res.end("Error: "+err )
          }
     });

})

app.get('/getPopData3', function (req, res) {
  let reqPath = path.join(__dirname, 'front/localData/pop3.json');
  
      //Read JSON from relative path of this file
      fs.readFile(reqPath , 'utf8', function (err, data) {
          //Handle Error
         if(!err) {
           //Handle Success
           // console.log("Success"+data);
           // Parse Data to JSON OR
            var jsonObj = JSON.parse(data)
           //Send back as Response
            res.json( jsonObj );
          }else {
             //Handle Error
             res.end("Error: "+err )
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
app.get('/3', function (req, res) {
  res.sendFile(path.join(__dirname + '/front/indexOsm.html'));
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
