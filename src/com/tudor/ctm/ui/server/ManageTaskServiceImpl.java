package com.tudor.ctm.ui.server;

import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tudor.ctm.endpoint.CloudTaskEndpoint;
import com.tudor.ctm.ui.client.ManageTaskService;
import com.tudor.ctm.ui.shared.CTMException;
import com.tudor.ctm.ui.shared.CTMUser;
import com.tudor.ctm.ui.shared.CloudTask;

public class ManageTaskServiceImpl extends RemoteServiceServlet implements
		ManageTaskService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 360822648630248115L;

	@Override
	public CloudTask addTask(String taskTitle, String taskDescription,
			Date taskDueDate, String ownerEmail) throws CTMException {
		CTMUser owner = new CTMUser();
		owner.setEmail(ownerEmail);
		CloudTask ct = new CloudTask(taskTitle, taskDescription, taskDueDate, ownerEmail);
		
		System.out.println(ct.toString());
		
		CloudTaskEndpoint endpoint = new CloudTaskEndpoint();
		
		endpoint.insertCloudTask(ct);
		
		return ct;
	}

	@Override
	public CloudTask editTask(Long taskId, String taskTitle,
			String taskDescription, Date taskDueDate, String ownerEmail)
			throws CTMException {
		CloudTask ct = new CloudTask(taskTitle, taskDescription, taskDueDate, ownerEmail);
		ct.setId(taskId);
		
		CloudTaskEndpoint endpoint = new CloudTaskEndpoint();
		
		endpoint.updateCloudTask(ct);
		
		return ct;
	}

}
