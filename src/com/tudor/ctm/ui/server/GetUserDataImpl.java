package com.tudor.ctm.ui.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tudor.ctm.ui.client.GetUserData;
import com.tudor.ctm.ui.shared.UserData;

public class GetUserDataImpl extends RemoteServiceServlet implements
		GetUserData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6583038997404904006L;

	@Override
	public UserData getUserData(String URL) {
		UserService us = UserServiceFactory.getUserService();
		if(us.isUserLoggedIn())
		{
			User u = us.getCurrentUser();
			return new UserData(u.getEmail(), us.createLogoutURL(URL), us.isUserAdmin());
		}
		else
		{
			return new UserData("error@error.com", us.createLoginURL(URL), false, false);
		}
	}

}
