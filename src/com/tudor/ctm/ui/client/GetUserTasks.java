package com.tudor.ctm.ui.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tudor.ctm.ui.shared.CloudTask;

@RemoteServiceRelativePath("getUserTasks")
public interface GetUserTasks extends RemoteService {
	public List<CloudTask> getUserTaks(String userEmail);
}
