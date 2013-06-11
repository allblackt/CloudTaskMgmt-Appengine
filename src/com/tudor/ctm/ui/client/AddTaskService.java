package com.tudor.ctm.ui.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tudor.ctm.ui.shared.CTMException;
import com.tudor.ctm.ui.shared.CloudTask;

@RemoteServiceRelativePath("addTask")
public interface AddTaskService extends RemoteService {
	public CloudTask addTask(String taskTitle, String taskDescription, Date taskDueDate, String ownerEmail) throws CTMException;
}
