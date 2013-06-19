package com.tudor.ctm.ui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tudor.ctm.ui.shared.UserData;

public interface GetUserDataAsync {
	void getUserData(String URL, AsyncCallback<UserData> callback);
}
