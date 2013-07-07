package com.tudor.ctm.ui.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tudor.ctm.ui.shared.CloudUser;

public interface GetUserDataAsync {
	void getUserData(String URL, AsyncCallback<CloudUser> callback);

	void getAllUsers(AsyncCallback<List<CloudUser>> callback);
}
