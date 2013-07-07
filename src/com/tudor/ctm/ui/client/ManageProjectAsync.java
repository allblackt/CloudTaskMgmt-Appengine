package com.tudor.ctm.ui.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tudor.ctm.ui.shared.CloudProject;

public interface ManageProjectAsync {
	void getCloudProjects(AsyncCallback<List<CloudProject>> callback);

	void addCloudProject(CloudProject project, AsyncCallback<Boolean> callback);

	void saveCloudProject(CloudProject project, AsyncCallback<Boolean> callback);
}
