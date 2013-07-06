package com.tudor.ctm.ui.client.view;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.UserData;
import com.google.gwt.user.client.ui.Button;

public class AdminUi extends Composite {

	@UiField
	TabPanel tabPanel;
	@UiField
	VerticalPanel vPanelL;
	@UiField
	VerticalPanel vPanelR;
	@UiField
	HorizontalPanel hPanel;
	@UiField
	ListBox coordinator;
	@UiField
	ListBox members;
	@UiField Button btnSave;
	
	
	private static AdminUiUiBinder uiBinder = GWT.create(AdminUiUiBinder.class);

	@UiTemplate("AdminUi.ui.xml")
	interface AdminUiUiBinder extends UiBinder<Widget, AdminUi> {
	}

	public AdminUi() {
		initWidget(uiBinder.createAndBindUi(this));
		
		tabPanel.selectTab(0);
		
		List<CloudProject> projects = getCloudProjects();
		
		ProvidesKey<CloudProject> KEY_PROVIDER = new ProvidesKey<CloudProject>() {
			@Override
			public Object getKey(CloudProject item) {
				return item.getId();
			}
		};
		
		final CellTable<CloudProject> table = new CellTable<CloudProject>(KEY_PROVIDER);
		
		final SingleSelectionModel<CloudProject> selectionModel = new SingleSelectionModel<CloudProject>(KEY_PROVIDER);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				members.clear();
				coordinator.clear();				
				int index = 0;
				coordinator.setSelectedIndex(index);
			    for (UserData userData : getProjectMembers(selectionModel.getSelectedObject().getId())) {
					members.addItem(userData.getEmail());
					coordinator.addItem(userData.getEmail());
					
					if(selectionModel.getSelectedObject().getOwner().getEmail() != userData.getEmail()) {
						coordinator.setSelectedIndex(index);
					} 
					index++;
				}
			}
		});
		
		final TextInputCell nameCell = new TextInputCell();
		
		table.setSelectionModel(selectionModel);
		
	    Column<CloudProject, String> nameColumn = new Column<CloudProject, String>(nameCell) {
			@Override
			public String getValue(CloudProject object) {
				return object.getName();
			}
	    };
	    
	    table.addColumn(nameColumn, "Project name");
		
	    nameColumn.setFieldUpdater(new FieldUpdater<CloudProject, String>() {

			@Override
			public void update(int index, CloudProject object, String value) {
				Window.alert("You changed the name of " + object.getName() + " to " + value);
				// Push the changes into the Contact. At this point, you could send an
				// asynchronous request to the server to update the database.
				table.redraw();
			}
		});

	      // Push the data into the widget.
	    table.setRowData(projects);
	    // set the first item as selected
	    table.getSelectionModel().setSelected(projects.get(0), true);
	    vPanelL.add(table);
	    
		
	}
	

	private List<CloudProject> getCloudProjects() {
		List<CloudProject> projects = Arrays.asList(
				new CloudProject.Builder().id((long) 1).name("Cloud Project 1").owner(new UserData("ttabace@gmail.com", "LogoutURL", false)).build(),
				new CloudProject.Builder().id((long) 2).name("Cloud Project 2").owner(new UserData("ttabace@gmail.com", "LogoutURL", false)).build()
				);
		
		return projects;
	}
	
	private List<UserData> getProjectMembers(long projectId) {
		List<UserData> projectMembers = Arrays.asList(
					new UserData("tudorsmt@gmail.com", "LogoutURL", false),
					new UserData("allblack007@gmail.com", "LogoutURL", false)
				);
		return projectMembers;
	}

}
