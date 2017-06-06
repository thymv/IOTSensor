package com.thyvu.iotsensor;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;



import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class MyTopic extends AWSIotTopic {
	
	private long receivedTime;
	private BufferedWriter out;
	private File logFile;
	private String logFilePath;
	
	
	public MyTopic(String topic, AWSIotQos qos){
		super(topic,qos);
		
		// create a new file with the same name as topic name
		logFile = new File(topic);
		logFilePath = logFile.getAbsolutePath();
		try {
			FileWriter fw = new FileWriter(logFile, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void onMessage(AWSIotMessage message){
		
		receivedTime = Instant.now().toEpochMilli();

		System.out.print(receivedTime);
		System.out.println(" is the time subscription arrived");
		System.out.println("Subscription:" + message.getStringPayload());
		
		// write message to file under same name as topic name
		try {
			out = new BufferedWriter(new FileWriter(topic, true));
			out.write("receivedTime " + receivedTime  + " " + message.getStringPayload() + " \n");
	        out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
	}
	
	// Return log file's path
	public String getLogFilePath(){
		return logFilePath;
	}

}
