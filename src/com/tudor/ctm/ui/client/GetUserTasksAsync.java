package com.tudor.ctm.ui.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tudor.ctm.ui.shared.CloudTask;

public interface GetUserTasksAsync {
	void getUserTaks(String userEmail, AsyncCallback<List<CloudTask>> callback);
}
