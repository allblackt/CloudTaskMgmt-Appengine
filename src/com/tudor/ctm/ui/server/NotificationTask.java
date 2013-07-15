package com.tudor.ctm.ui.server;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudTask;

public class NotificationTask implements DeferredTask {

	private static final long serialVersionUID = -80580992232262649L;
	CloudTask task;
	CloudProject project;
	
	public NotificationTask(CloudTask task) {
		this.task = task;
	}
	
	public NotificationTask(CloudProject project) {
		this.project = project;
	}

	@Override
	public void run() {
		if(task != null) {
			NotificationManager.sendNewTaskAndroidNotification(task.getOwner());
			NotificationManager.sendNewTaskEmailNotification(task);
		} 
		if(project != null) {
			NotificationManager.sendProjectReport(project);
		}
	}

}
