package com.tudor.ctm.ui.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tudor.ctm.ui.shared.CloudTask;

public interface AddTaskServiceAsync {
	void addTask(String taskTitle, String taskDescription, Date taskDueDate,
			String ownerEmail, AsyncCallback<CloudTask> callback);

}
