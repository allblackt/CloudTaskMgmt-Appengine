package com.tudor.ctm.ui.server;

import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.tudor.ctm.ui.shared.CloudUser;
import com.tudor.ctm.util.Constants;

public class NotificationManager {
	
	public static void sendNewTaskAndroidNotification(CloudUser user) {
		Sender sender = new Sender(Constants.GCM_API_KEY);
		Message message = new Message.Builder().build();
		MulticastResult result;
		
		try {
			result = sender.send(message, user.getDeviceKeys(), 1);
			System.out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		for (String deviceKey : user.getDeviceKeys()) {
			MulticastResult result;
			System.out.println("Attempting to send to device key: " + deviceKey);
			try {
				result = sender.send(message, deviceKey, 1);
				System.out.println(result.getErrorCodeName());
				System.out.println(result.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
		
	}

}
