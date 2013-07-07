package com.tudor.ctm.ui.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tudor.ctm.ui.shared.CloudProject;

@RemoteServiceRelativePath("manageproject")
public interface ManageProject extends RemoteService {
	public List<CloudProject> getCloudProjects();
	public boolean addCloudProject(CloudProject project);
	public boolean saveCloudProject(CloudProject project);
}
