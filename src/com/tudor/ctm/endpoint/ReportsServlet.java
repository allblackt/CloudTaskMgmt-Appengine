package com.tudor.ctm.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tudor.ctm.ui.server.NotificationManager;
import com.tudor.ctm.ui.server.NotificationTask;
import com.tudor.ctm.ui.shared.CloudProject;

public class ReportsServlet extends HttpServlet {

	private static final long serialVersionUID = 6761963707694507526L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<CloudProject> projects = (List<CloudProject>) new CloudProjectEndpoint().listCloudProject(null, null).getItems();
		for (CloudProject cloudProject : projects) {
			try {
				NotificationManager.queueProjectForReport(cloudProject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
