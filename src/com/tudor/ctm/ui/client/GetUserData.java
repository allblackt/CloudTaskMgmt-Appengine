package com.tudor.ctm.ui.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudUser;

@RemoteServiceRelativePath("getUserData")
public interface GetUserData extends RemoteService {
	CloudUser getUserData(String URL);
	List<CloudUser> getAllUsers();
	List<CloudProject> getUserProjects(CloudUser user);
	void saveUser(CloudUser user);
}
