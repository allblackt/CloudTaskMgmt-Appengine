package com.tudor.ctm.ui.server;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tudor.ctm.endpoint.CloudProjectEndpoint;
import com.tudor.ctm.endpoint.CloudUserEndpoint;
import com.tudor.ctm.ui.client.GetUserData;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudUser;

public class GetUserDataImpl extends RemoteServiceServlet implements
		GetUserData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6583038997404904006L;

	@Override
	public CloudUser getUserData(String URL) {
		UserService us = UserServiceFactory.getUserService();
		if(us.isUserLoggedIn())
		{
			User u = us.getCurrentUser();
			CloudUserEndpoint endpoint = new CloudUserEndpoint();
			CloudUser cloudUser = endpoint.getCloudUserByEmail(u.getEmail());
			if(cloudUser == null) {
				cloudUser = new CloudUser.Builder().email(u.getEmail()).build();
				cloudUser = endpoint.insertCloudUser(cloudUser); 
			}
			cloudUser.setIsLoggedIn(true);
			cloudUser.setLogoutURL(us.createLogoutURL(URL));
			cloudUser.setIsAdmin(us.isUserAdmin());
			return cloudUser;
		}
		else
		{
			return new CloudUser("error@error.com", us.createLoginURL(URL), false, false);
		}
	}

	@Override
	public List<CloudUser> getAllUsers() {
		return new CloudUserEndpoint().getAllUsers();
	}

	@Override
	public List<CloudProject> getUserProjects(CloudUser user) {
		return new CloudProjectEndpoint().getUserProjects(user);
	}

}
