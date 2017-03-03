var config = require('../config.json');
var AWS = require('aws-sdk');
  var winston = require('winston');
  require('winston-daily-rotate-file');
var fs = require('fs');
var dir = './log';

if (!fs.existsSync(dir)){
    fs.mkdirSync(dir);
}
  var transport = new winston.transports.DailyRotateFile({
    filename: './log/log',
    datePattern: 'yyyy-MM-dd.',
    prepend: true,
    level: process.env.ENV === 'development' ? 'debug' : 'info'
  });

  var logger = new (winston.Logger)({
    transports: [
      transport
    ]
  });

  //logger.info('Hello World!');

module.exports = {
log: function (message) {
   logger.info(message);
},

sendSMS1 : function (to_number, message, func_callback) {


    AWS.config.update({
        accessKeyId: config.accessKeyId,
        secretAccessKey: config.secretAccessKey,
        region: config.region
    });

    var sns = new AWS.SNS();

    var SNS_TOPIC_ARN = config.topic;

    sns.subscribe({
        Protocol: 'sms',
        //You don't just subscribe to "news", but the whole Amazon Resource Name (ARN)
        TopicArn: SNS_TOPIC_ARN,
        Endpoint: to_number
    }, function (error, data) {
        if (error) {
            console.log("error when subscribe", error);
            return func_callback(false);
        }


        console.log("subscribe data", data);
        var SubscriptionArn = data.SubscriptionArn;

        var params = {
            TargetArn: SNS_TOPIC_ARN,
            Message: message,
            //hardcode now
            Subject: 'Admin'
        };

        sns.publish(params, function (err_publish, data) {
            if (err_publish) {
                console.log('Error sending a message', err_publish);

            } else {
                console.log('Sent message:', data.MessageId);
            }

            var params = {
                SubscriptionArn: SubscriptionArn
            };

            sns.unsubscribe(params, function (err, data) {
                if (err) {
                    console.log("err when unsubscribe", err);
                }
                return func_callback(err_publish != null);
            });
        });
    });
},


sendSMS: function(number,message,func_callback){
    AWS.config.update({
        accessKeyId: config.accessKeyId,
        secretAccessKey: config.secretAccessKey,
        region: config.region
    });

    var sns = new AWS.SNS();
var params = {
  Message: message, 

  PhoneNumber: number
  

};
sns.publish(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data); 
  return func_callback(err != null);           // successful response
});


}




};