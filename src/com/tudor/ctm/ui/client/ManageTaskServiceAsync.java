package com.tudor.ctm.ui.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.CloudUser;

public interface ManageTaskServiceAsync {

	void addTask(String taskTitle, String taskDescription, Date taskDueDate,
			String ownerEmail, AsyncCallback<CloudTask> callback);

	void editTask(Long taskId, String taskTitle, String taskDescription,
			Date taskDueDate, CloudUser owner, AsyncCallback<CloudTask> callback);

	void getProjectTasks(CloudProject project, CloudUser user,
			AsyncCallback<List<CloudTask>> callback);

}
