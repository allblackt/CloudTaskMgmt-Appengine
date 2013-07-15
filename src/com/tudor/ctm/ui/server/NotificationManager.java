package com.tudor.ctm.ui.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.tudor.ctm.endpoint.CloudTaskEndpoint;
import com.tudor.ctm.endpoint.CloudUserEndpoint;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.CloudUser;
import com.tudor.ctm.util.Constants;

public class NotificationManager {
	
	private static Logger log = Logger.getLogger( NotificationManager.class.getName() );
	
	
	public static void queueTaskForNotification(CloudTask task) {
		NotificationTask nt = new NotificationTask(task);
		Queue queue = QueueFactory.getQueue("notificationpush-queue");
		queue.add(TaskOptions.Builder.withPayload(nt));
	}
	
	public static void queueProjectForReport(CloudProject project) {
		NotificationTask nt = new NotificationTask(project);
		Queue queue = QueueFactory.getQueue("notificationpush-queue");
		queue.add(TaskOptions.Builder.withPayload(nt));
	}
	
	public static void sendNewTaskAndroidNotification(CloudUser user) {
		Sender sender = new Sender(Constants.GCM_API_KEY);
		Message message = new Message.Builder().collapseKey("newtask").build();
		MulticastResult results;
		
		try {
			//force exit if no devices are defined
			if(user.getDeviceKeys() == null || user.getDeviceKeys().size() == 0) {
				log.warning("No devices defined for user.");
				return;
			}
			results = sender.send(message, user.getDeviceKeys(), 1);
			
			List<String> invalidKeys = new ArrayList<String>();
			
			//validate results and remove invalid registration IDs
			for(int i=0; i < results.getResults().size() ; i++ ) {
				Result result = results.getResults().get(i);
				String regId = user.getDeviceKeys().get(i);
				if(result.getErrorCodeName() != null) {
					invalidKeys.add(user.getDeviceKeys().get(i));
				}
				String messageId = result.getMessageId();
		          if (messageId != null) {
		            log.info("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
		            String canonicalRegId = result.getCanonicalRegistrationId();
		            if (canonicalRegId != null) {
		              // same device has more than on registration id: update it
		              log.info("canonicalRegId " + canonicalRegId);
		            }
		          }
				}
			
			log.info("user keys" + user.getDeviceKeys());
			log.info("to be removed keys " + invalidKeys);
			log.info("responses: " + results.getResults());
			
			if(invalidKeys.size() > 0) {
				user.getDeviceKeys().removeAll(invalidKeys);
			}
			
			new CloudUserEndpoint().updateCloudUser(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendNewTaskEmailNotification(CloudTask task) {
		
		String emailSubject = String.format("Task %s has been changed / added", task.getTaskTitle());
		String emailBody = getTaskHtmlTable(task);
		
		try {
			new EmailNotification.Builder().emailBody(emailBody).emailSubject(emailSubject).to(new InternetAddress(task.getOwner().getEmail())).build().send();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendProjectReport(CloudProject project) {
		List<CloudTask> projectTasks = new CloudTaskEndpoint().getProjectTasks(project);
		
		//exit if project has no tasks
		if(projectTasks == null || projectTasks.size() == 0) {
			return;
		}
		
		StringBuilder emailBody = new StringBuilder();
		String emailSubject = "Daily report for project " + project.getName();
		
		for(CloudTask task : projectTasks) {
			emailBody.append(getTaskHtmlTable(task));
			emailBody.append("<hr />");
		}
		
		emailBody.append("Generated on " + new Date() + " by CTM-Tudor");
		
		try {
			new EmailNotification.Builder().emailBody(emailBody.toString()).emailSubject(emailSubject).to(new InternetAddress(project.getOwner().getEmail())).build().send();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getTaskHtmlTable(CloudTask task) {
		return String.format("<table><tr><td>Task title: </td><td>%s</td></tr>" + 
				"<tr><td>Task Description: </td><td>%s</td></tr>"	+
				"<tr><td>Task due date: </td><td>%s</td></tr>" + 
				"<tr><td>Task remaining time: </td><td>%s</td></tr>" + 
				"<tr><td>Task total time: </td><td>%s</td></tr></table>",
				task.getTaskTitle(), task.getTaskDescription(), task.getTaskDueDate(), task.getRemainingTime(), task.getTotalTime());
	}

}
