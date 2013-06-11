package com.tudor.ctm.ui.server;

import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tudor.ctm.ui.client.AddTaskService;
import com.tudor.ctm.ui.shared.CTMException;
import com.tudor.ctm.ui.shared.CTMUser;
import com.tudor.ctm.ui.shared.CloudTask;

public class AddTaskServiceImpl extends RemoteServiceServlet implements
		AddTaskService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 360822648630248115L;

	@Override
	public CloudTask addTask(String taskTitle, String taskDescription,
			Date taskDueDate, String ownerEmail) throws CTMException {
		// TODO Auto-generated method stub
		CTMUser owner = new CTMUser();
		owner.setEmail(ownerEmail);
		CloudTask ct = new CloudTask(taskTitle, taskDescription, taskDueDate, owner);
		
		System.out.println(ct.toString());
		
		return null;
	}

}
