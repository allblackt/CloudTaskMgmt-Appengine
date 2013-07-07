package com.tudor.ctm.ui.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.tudor.ctm.ui.client.GetUserData;
import com.tudor.ctm.ui.client.GetUserDataAsync;
import com.tudor.ctm.ui.client.ManageProject;
import com.tudor.ctm.ui.client.ManageProjectAsync;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudUser;

public class AdminUi extends Composite {

	@UiField
	TabPanel tabPanel;
	@UiField
	VerticalPanel vPanelProjects;
	@UiField
	VerticalPanel vPanelR;
	@UiField
	HorizontalPanel hPanel;
	@UiField
	ListBox coordinator;
	@UiField
	ListBox members;
	@UiField Button btnSave;
	@UiField Button btnAddProject;
	
	
	private static AdminUiUiBinder uiBinder = GWT.create(AdminUiUiBinder.class);
	private ManageProjectAsync manageProject = GWT.create(ManageProject.class);
	private GetUserDataAsync userData = GWT.create(GetUserData.class);
	private CellTable<CloudProject> table;
	private CloudUser user;
	private List<CloudUser> allUsers;
	
	@UiTemplate("AdminUi.ui.xml")
	interface AdminUiUiBinder extends UiBinder<Widget, AdminUi> {
	}

	public AdminUi(CloudUser user) {
		initWidget(uiBinder.createAndBindUi(this));
		
		tabPanel.selectTab(0);
		
		this.user = user;
		
		final ProvidesKey<CloudProject> KEY_PROVIDER = new ProvidesKey<CloudProject>() {
			@Override
			public Object getKey(CloudProject item) {
				return item.getId();
			}
		};
		
		table = new CellTable<CloudProject>(KEY_PROVIDER);
		
		final SingleSelectionModel<CloudProject> selectionModel = new SingleSelectionModel<CloudProject>(KEY_PROVIDER);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				setOptions();
				for (CloudUser member : selectionModel.getSelectedObject().getMembers()) {
					members.setItemSelected(allUsers.indexOf(member), true);
				}
				coordinator.setSelectedIndex(allUsers.indexOf(selectionModel.getSelectedObject().getOwner()));
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
	    
	    
	    //table.addColumn(nameColumn, "Project name");
	    table.addColumn(nameColumn);

	    nameColumn.setFieldUpdater(new FieldUpdater<CloudProject, String>() {
			@Override
			public void update(int index, CloudProject object, String value) {
				if(isProjectNameValid(value, 1)){
					object.setName(value);
					table.getSelectionModel().setSelected(object, true);
				} else {
					nameCell.clearViewData(KEY_PROVIDER.getKey(object));
					new MessageBox("The project name you provided is invalid. A project with the same name already exists.").center();
					
				}
				table.redraw();
			}
		});

	    vPanelProjects.add(table);
	    
	    displayProjects();
	}
	
	private void displayProjects() {
		manageProject.getCloudProjects(new AsyncCallback<List<CloudProject>>() {
			@Override
			public void onSuccess(final List<CloudProject> projects) {
			    /* Populate the table */
			    if(projects != null && projects.size() > 0) {
			    	vPanelProjects.clear();
			    	vPanelProjects.add(table);
			    	vPanelR.setVisible(true);
			    	table.setVisible(true);
			    	table.setRowData(projects);
			    	table.redraw();
			    	userData.getAllUsers(new AsyncCallback<List<CloudUser>>() {
						@Override
						public void onFailure(Throwable caught) {
							new MessageBox(caught.getMessage()).center();
						}
						@Override
						public void onSuccess(List<CloudUser> result) {
							if( result!=null && result.size() > 0) {
								allUsers = result;
								setOptions();
								table.getSelectionModel().setSelected(projects.get(0), true);
							}
						}
					});
			    } else {
			    	vPanelProjects.add(new HTML("No projects defined"));
			    	vPanelR.setVisible(false);
			    	table.setVisible(false);
			    }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				new MessageBox(caught.getMessage());
			}
		});
	}
	
	private void setOptions() {
		members.clear();
		coordinator.clear();
		for (CloudUser user : allUsers) {
			members.addItem(user.getEmail());
			coordinator.addItem(user.getEmail());
		}
	}
	
	private boolean isProjectNameValid(String name, int max) {
		for (CloudProject project : table.getVisibleItems()) {
			if(project.getName().trim().compareToIgnoreCase(name) == 0) {
				max--;
			}
			if(max == 0) {
				return false;
			}
		}
		return true;
	}

	@UiHandler("btnAddProject")
	void onBtnAddProjectClick(ClickEvent event) {
		final DialogBox box = new DialogBox();
		final AddProject ap = new AddProject();
		box.add(ap);
		ap.btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				box.hide();
			}
		});
		ap.btnSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(ap.txtProjectName.getValue().trim().length() == 0) {
					new MessageBox("Please enter a project name").center();
				} else if (!isProjectNameValid(ap.txtProjectName.getValue().trim(), 1)) {
					new MessageBox("The project name you provided is invalid. A project with the same name already exists.").center();
				} else {
					box.hide();
					
					CloudProject cp = new CloudProject.Builder().name(ap.txtProjectName.getValue()).owner(user).members(Arrays.asList(user)).build();
					
					manageProject.addCloudProject(cp, new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							displayProjects();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							new MessageBox(caught.getMessage());
						}
					});
					
				}
			}
		});
		box.setGlassEnabled(true);
		box.center();
	}
	
	@SuppressWarnings("unchecked")
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		CloudProject project = ((SingleSelectionModel<CloudProject>)table.getSelectionModel()).getSelectedObject();
		List<CloudUser> selectedMembers = new ArrayList<CloudUser>();
		for (int i = 0 ; i < members.getItemCount() ; i++) {
			if(members.isItemSelected(i)) {
				selectedMembers.add(allUsers.get(i));
			}
		}
		project.setMembers(selectedMembers);
		project.setOwner(allUsers.get(coordinator.getSelectedIndex()));
		
		manageProject.saveCloudProject(project, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				new MessageBox(caught.getMessage()).center();
			}

			@Override
			public void onSuccess(Boolean result) {
				new MessageBox("Project saved.").center();
			}
		});
	}
}
