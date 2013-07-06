package com.tudor.ctm.ui.client;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.tudor.ctm.ui.client.view.AdminUi;
import com.tudor.ctm.ui.client.view.UserUI;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.UserData;

public class Welcome implements EntryPoint {

	private RootPanel rootPanel = RootPanel.get("mainLayout");
	private TextBox taskTitle;
	private TextArea taskDescription;
	private VerticalPanel vPanel; 
	private DateBox dateBox;
	private UserData loggedUser;
	private DialogBox addTaskBox;
	
	private final ManageTaskServiceAsync manageTaskService = GWT.create(ManageTaskService.class);
	private final GetUserTasksAsync getUserTasks = GWT.create(GetUserTasks.class);
	private final GetUserDataAsync getUserData = GWT.create(GetUserData.class);
	
	
	@Override
	public void onModuleLoad() {
		
		showLoading();
		
		/* Get the data associated with google login */
		getUserData.getUserData(Window.Location.getPath() + Window.Location.getQueryString(), new AsyncCallback<UserData>() {
			
			@Override
			public void onSuccess(UserData result) {
				
				loggedUser = result;
				
				/* If logged in initialize the application and display the user data. */
				if (loggedUser.getIsLoggedIn())
				{
					
					RootPanel.get("logoutLink").getElement().setPropertyString("href", loggedUser.getLogoutURL());
					RootPanel.get("spnUsername").getElement().setInnerText(loggedUser.getEmail());
					
					if(result.getIsAdmin())
					{
						RootPanel.get("spnUsername").getElement().setInnerText(loggedUser.getEmail() + "(admin)");
						InitAdmin();
					}
					else
					{
						Init();
						RootPanel.get("spnUsername").getElement().setInnerText(loggedUser.getEmail());
					}

				}
				else
				{
					Window.Location.replace(result.getLogoutURL());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				alertWidget(caught.getMessage(), caught.getStackTrace().toString());
			}
		});
		
		
	}
	
	/* Initialize visual components for normal users */
	private void Init(){
		
		vPanel = new VerticalPanel();
		
		vPanel.add(new UserUI());
		
		rootPanel.add(vPanel);
		
		hideLoading();
		
		if(1==1)
			return;
		
		Button addWithPopup = new Button("Add new task...");
		addWithPopup.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addTaskBox = addNewTask();
				addTaskBox.center();
			}
		});
		
		rootPanel.add(addWithPopup);
		rootPanel.add(vPanel);
		
		/* Get all the tasks which already exist in the database for the current user */
		getUserTasks.getUserTaks(loggedUser.getEmail(), new AsyncCallback<List<CloudTask>>() {
			
			@Override
			public void onSuccess(List<CloudTask> result) {
				for (CloudTask cloudTask : result) {
					showAddedTask(cloudTask);
				}
				hideLoading();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				hideLoading();
				alertWidget(caught.getMessage(), caught.getStackTrace().toString());
			}
		});
	}
	
	
	/* Initialize visual components for administrators */
	private void InitAdmin() {
		
		AdminUi adminui = new AdminUi();
		
		rootPanel.add(adminui);
		
//		TabPanel tabPanel = new TabPanel();
//		
//	    AdminWidget aw = new AdminWidget();
//	    tabPanel.add(aw, "Admin");
//	    tabPanel.selectTab(0);
//	    rootPanel.add(tabPanel);
	    hideLoading();
	}
	
	ClickHandler btnAddThing_onClick = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			showLoading();
		    boolean ok = true;
		    String errMsg = "";
		    long now = new Date().getTime();
		    long taskTime = dateBox.getValue().getTime();
		    try {
				if(taskTitle.getValueOrThrow() == null){
					errMsg += "The task must have a title! ";
					ok = false;
				} 
				if(taskDescription.getValueOrThrow() == null){
					errMsg += "The task must have a description! ";
					ok = false;
				}
				
				if(taskTime < now){
					System.out.println(taskTime);
					System.out.println(now);
					errMsg += "The task due date must be in the future!";
					ok = false;
				}
			} catch (ParseException e) {
				ok = false;
				errMsg = e.getMessage();
			}
		    if(ok){
		    	addTask(taskTitle.getValue(), taskDescription.getValue(), dateBox.getValue(), loggedUser.getEmail());
		    } else {
		    	hideLoading();
		    	alertWidget("Error", errMsg).center();
		    }
			
		}
	};
	
	ClickHandler btnSalveaza_onClick = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			Button sender = (Button) event.getSource();
			VerticalPanel vp = (VerticalPanel) sender.getParent();
			TextArea ta = (TextArea)vp.getWidget(2);
			TextBox tb = (TextBox) vp.getWidget(1);
			DateBox db = (DateBox) vp.getWidget(3);
			Hidden h = (Hidden) vp.getWidget(0);
			editTask(Long.parseLong(h.getValue()), tb.getValue(), ta.getValue(), db.getValue(), loggedUser.getEmail());
		}
	};
	
	/* Wrapper over the async service call for addTask */
	private void addTask(String taskTitle, String taskDescription, Date taskDueDate,
				String ownerEmail) {
		manageTaskService.addTask(taskTitle, taskDescription, taskDueDate, ownerEmail, new AsyncCallback<CloudTask>() {

			@Override
			public void onFailure(Throwable caught) {
				hideLoading();
				addTaskBox.hide();
				alertWidget(caught.getMessage(), caught.getStackTrace().toString()).center();
			}

			@Override
			public void onSuccess(CloudTask result) {
				hideLoading();
				addTaskBox.hide();
				showAddedTask(result);
			}
			
		});
	}
	
	/* Wrapper over the async service call for editTask */
	private void editTask(Long taskId, String taskTitle, String taskDescription, Date taskDueDate, String ownerEmail){
		manageTaskService.editTask(taskId, taskTitle, taskDescription, taskDueDate, ownerEmail, new AsyncCallback<CloudTask>() {
					
					@Override
					public void onSuccess(CloudTask result) {
						alertWidget("Edit Task", "The task has been updated.").center();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Task edit error:" + caught.getMessage());
					}
				});
	}
	
	/* Displays the task in a collapsible control under the  */
	private void showAddedTask(CloudTask result){
		TextArea ta = new TextArea();
		TextBox tb = new TextBox();
		DateBox db = new DateBox();
		Hidden h = new Hidden();
		
		Button btnSalveaza = new Button("Save");
		VerticalPanel vp = new VerticalPanel();
		DecoratorPanel decPanel = new DecoratorPanel();
		
		ta.setValue(result.getTaskDescription());
		tb.setValue(result.getTaskTitle());
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd - MMM - yyyy");
		db.setFormat(new DateBox.DefaultFormat(dateFormat));
		db.setValue(result.getTaskDueDate());
		h.setValue(result.getId().toString());
		btnSalveaza.addClickHandler(btnSalveaza_onClick);
		
		vp.add(h);
		vp.add(tb);
		vp.add(ta);
		vp.add(db);
		vp.add(btnSalveaza);
		
		DisclosurePanel advancedDisclosure = new DisclosurePanel(tb.getText());
					
		decPanel.add(vp);
		
		advancedDisclosure.setAnimationEnabled(true);
	    advancedDisclosure.setContent(decPanel);
	    advancedDisclosure.setWidth("60em");
	    
	    //RootPanel.get("taskList").insert(advancedDisclosure, 0);
	    
	    vPanel.insert(advancedDisclosure, 0);
	}
	
	public DialogBox addNewTask() {
		final DialogBox box = new DialogBox();
		VerticalPanel mainControls = new VerticalPanel();
		taskTitle = new TextBox();
		dateBox = new DateBox();
		taskDescription = new TextArea();
		
		taskTitle.setWidth("50em");
		
		taskDescription.setWidth("50em");
		taskDescription.setHeight("10em");
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd - MMM - yyyy");
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dateBox.setValue(new Date());
		
		mainControls.add(taskTitle);
		mainControls.add(dateBox);
		mainControls.add(taskDescription);
		
		Button btnAddItem = new Button("Add");
		btnAddItem.addClickHandler(btnAddThing_onClick);
		mainControls.add(btnAddItem);
		
		box.add(mainControls);
		box.setGlassEnabled(true);
		return box;
	}
	
	public static DialogBox alertWidget(final String header, final String content) {
        final DialogBox box = new DialogBox();
        final VerticalPanel panel = new VerticalPanel();
        box.setText(header);
        box.setGlassEnabled(true);
        box.setAnimationEnabled(true);
        panel.add(new Label(content));
        final Button buttonClose = new Button("Close",new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                box.hide();
            }
        });
        // few empty labels to make widget larger
        final Label emptyLabel = new Label("");
        emptyLabel.setSize("auto","25px");
        panel.add(emptyLabel);
        panel.add(emptyLabel);
        buttonClose.setWidth("90px");
        panel.add(buttonClose);
        panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);
        box.add(panel);
        return box;
    }
	

	private void showLoading(){
		DOM.setStyleAttribute(RootPanel.get("loading").getElement(), "display", "block");
	}
	
	private void hideLoading(){
		DOM.setStyleAttribute(RootPanel.get("loading").getElement(), "display", "none");
	}
}
