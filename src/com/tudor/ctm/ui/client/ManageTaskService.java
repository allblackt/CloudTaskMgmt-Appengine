package com.tudor.ctm.ui.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tudor.ctm.ui.shared.CTMException;
import com.tudor.ctm.ui.shared.CloudTask;

@RemoteServiceRelativePath("manageTask")
public interface ManageTaskService extends RemoteService {
	public CloudTask addTask(String taskTitle, String taskDescription, Date taskDueDate, String ownerEmail) throws CTMException;
	public CloudTask editTask(Long taskId, String taskTitle, String taskDescription, Date taskDueDate, String ownerEmail) throws CTMException;
}
