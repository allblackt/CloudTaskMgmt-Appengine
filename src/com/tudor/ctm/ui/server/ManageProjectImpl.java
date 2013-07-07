package com.tudor.ctm.ui.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tudor.ctm.endpoint.CloudProjectEndpoint;
import com.tudor.ctm.ui.client.ManageProject;
import com.tudor.ctm.ui.shared.CloudProject;

public class ManageProjectImpl extends RemoteServiceServlet implements
		ManageProject {

	private static final long serialVersionUID = -4542553031852307444L;

	@Override
	public List<CloudProject> getCloudProjects() {
		return (List<CloudProject>) new CloudProjectEndpoint().listCloudProject(null, null).getItems();
	}

	@Override
	public boolean addCloudProject(CloudProject project) {
		new CloudProjectEndpoint().insertCloudProject(project);
		return true;
	}

	@Override
	public boolean saveCloudProject(CloudProject project) {
		new CloudProjectEndpoint().updateCloudProject(project);
		return true;
	}

}
