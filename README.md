# IOTSensor
This program simulates an IoT sensor by sending MQTT messages to AWS IoT and receiving messages from AWS IoT.
The received message is the output necessary to run LatencyAnalysis program.

# Requirements
Must edit the following variables in MySensor java class to reflect your own AWS credentials:
clientEndpoint, certificateFile, privateKeyFile  

Configuration in AWS:
1) An AWS IoT Rule must be set up to insert incoming data from AWS IoT Topic called
"sensorTopic" to DynamoDB table called RawSensorData.
2) Dynamodb table named RawSensorData must have a trigger to call Lambda function called
MyFirstLambda whenever a change happens in the table.
3) MyFirstLambda function took each record from DynamoDB RawSensorData stream,
checked if the event was an insertion event, then send out a "hello from Lambda" message to
SNS Topic called "lambdaTopic" and another message that included DynamoDB record's data
(source time, iotTime) to AWS IoT Topic called "messageToSensor". The code for MyFirstLambda lambda function will be shared here soon.
4) SNS must be configured to send any message that arrives lambdaTopic to an email address.
