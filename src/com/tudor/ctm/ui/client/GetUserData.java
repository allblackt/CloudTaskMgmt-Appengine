package com.tudor.ctm.ui.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tudor.ctm.ui.shared.UserData;

@RemoteServiceRelativePath("getUserData")
public interface GetUserData extends RemoteService {
	UserData getUserData(String URL);
}
