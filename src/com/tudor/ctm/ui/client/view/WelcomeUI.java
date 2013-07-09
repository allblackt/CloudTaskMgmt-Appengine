package com.tudor.ctm.ui.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabBar.Tab;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.TabPanel;

public class WelcomeUI extends Composite {

	private static WelcomeUiBinder uiBinder = GWT.create(WelcomeUiBinder.class);
	
	@UiField TabPanel tabPanel;
	
//	@UiField
//	FlowPanel adminPanel;
//	@UiField
//	FlowPanel projectsPanel;
//	@UiField
//	Tab adminTab;

	@UiTemplate("WelcomeUI.ui.xml")
	interface WelcomeUiBinder extends UiBinder<Widget, WelcomeUI> {
	}

	public WelcomeUI() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TabPanel getTabPanel() {
		return tabPanel;
	}

//	public FlowPanel getAdminPanel() {
//		return adminPanel;
//	}
//
//	public FlowPanel getProjectsPanel() {
//		return projectsPanel;
//	}
//
//	public Tab getAdminTab() {
//		return adminTab;
//	}
	
}
