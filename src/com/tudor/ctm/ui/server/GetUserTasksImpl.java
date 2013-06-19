package com.tudor.ctm.ui.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tudor.ctm.endpoint.CloudTaskEndpoint;
import com.tudor.ctm.ui.client.GetUserTasks;
import com.tudor.ctm.ui.shared.CloudTask;

public class GetUserTasksImpl extends RemoteServiceServlet implements
		GetUserTasks {

	/**
	 * 
	 */
	private static final long serialVersionUID = -112503543260440395L;

	@Override
	public List<CloudTask> getUserTaks(String userEmail) {
		CloudTaskEndpoint endpoint = new CloudTaskEndpoint();
		return endpoint.getUserTasks(userEmail);
	}

}
