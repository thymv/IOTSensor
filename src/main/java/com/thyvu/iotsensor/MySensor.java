package com.thyvu.iotsensor;

import com.amazonaws.services.iot.client.AWSIotMqttClient;


import java.time.Instant;
import java.util.Date;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.sample.sampleUtil.CommandArguments;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;
import com.fasterxml.jackson.databind.util.ISO8601Utils;


public class MySensor {

	public static void main(String[] args) throws AWSIotException {
		int numOfTrials = 3;		// default number of trials to run
		
		// for publishing
		String clientEndpoint = "ac4k999ew6y7z.iot.us-west-2.amazonaws.com";       
		String clientId = "justme";                              //  client ID. Use unique client IDs for concurrent connections.
		String certificateFile = "sensor.cert.pem";              // X.509 based certificate file
		String privateKeyFile = "sensor.private.key";            // PKCS#1 or PKCS#8 PEM encoded private key file


		KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile);
		AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);

		client.connect();
		System.out.println("Connected, ready for publishing");

		// Define quality of service
		AWSIotQos qos = AWSIotQos.QOS0;
		
		// topic this sensor will send message to
		String topicStr = "sensorTopic";
		
		// subcribe to topic
		MyTopic subscribedTopic = new MyTopic("messageToSensor",qos);
		client.subscribe(subscribedTopic);
		
		// Send a message to AWS IOT every 1.5 minute
		for (int i = 0 ; i< numOfTrials ; i++){
			String srcTimestamp = "" + Instant.now().toEpochMilli();
			String message = "{ \"sensorID\" : \"1\", \"srcTime\": \""+ srcTimestamp + "\" }";
			client.publish(topicStr, AWSIotQos.QOS0, message);
			if( (i+1) < numOfTrials){
				try {
					Thread.sleep(45000);
				} catch (InterruptedException e) {
					e.printStackTrace();
		
				}	
			}
		}
		
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished.");
		System.out.println("Please retrieve this log file for latency analysis:");
		System.out.println(subscribedTopic.getLogFilePath());
		
	}

}
