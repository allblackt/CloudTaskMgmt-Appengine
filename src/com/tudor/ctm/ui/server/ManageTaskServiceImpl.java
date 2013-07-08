package com.tudor.ctm.ui.server;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tudor.ctm.endpoint.CloudTaskEndpoint;
import com.tudor.ctm.ui.client.ManageTaskService;
import com.tudor.ctm.ui.shared.CTMException;
import com.tudor.ctm.ui.shared.CTMUser;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.CloudUser;

public class ManageTaskServiceImpl extends RemoteServiceServlet implements
		ManageTaskService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 360822648630248115L;

	@Override
	public CloudTask addTask(String taskTitle, String taskDescription,
			Date taskDueDate, String ownerEmail) throws CTMException {
		CloudUser owner = new CloudUser();
		owner.setEmail(ownerEmail);
		CloudTask ct = new CloudTask(taskTitle, taskDescription, taskDueDate, owner);
		
		System.out.println(ct.toString());
		
		CloudTaskEndpoint endpoint = new CloudTaskEndpoint();
		
		endpoint.insertCloudTask(ct);
		
		return ct;
	}

	@Override
	public CloudTask editTask(Long taskId, String taskTitle,
			String taskDescription, Date taskDueDate, CloudUser owner)
			throws CTMException {
		CloudTask ct = new CloudTask.Builder().id(taskId).taskTitle(taskTitle).taskDescription(taskDescription).taskDueDate(taskDueDate).owner(owner).build();
		
		CloudTaskEndpoint endpoint = new CloudTaskEndpoint();
		
		endpoint.updateCloudTask(ct);
		
		return ct;
	}

	@Override
	public List<CloudTask> getProjectTasks(CloudProject project, CloudUser user) {
		if(project.getOwner().equals(user)) {
			return new CloudTaskEndpoint().getProjectTasks(project);
		} else {
			return new CloudTaskEndpoint().getUserTasks(project, user);
		}
	}

}
