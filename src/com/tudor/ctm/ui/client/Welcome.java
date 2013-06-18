package com.tudor.ctm.ui.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.tudor.ctm.ui.shared.CloudTask;

public class Welcome implements EntryPoint {

	private RootPanel rootPanel = RootPanel.get("mainLayout");
	private TextBox taskTitle;
	private TextArea taskDescription;
	private VerticalPanel vPanel; 
	private DateBox dateBox;
	
	private final AddTaskServiceAsync addTaskService = GWT.create(AddTaskService.class);
	
	/* Initialize visual components */
	private void Init(){
		
		vPanel = new VerticalPanel();
		taskTitle = new TextBox();
		dateBox = new DateBox();
		taskDescription = new TextArea();
		
		taskTitle.setWidth("50em");
		
		taskDescription.setWidth("50em");
		taskDescription.setHeight("10em");
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd - MMM - yyyy");
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dateBox.setValue(new Date());
		
		/* Do visual magic here */
//		taskTitle.setText("Task title...");
//		taskTitle.addClickHandler(taskTitleOnClick);
		
		
		rootPanel.add(taskTitle);
		rootPanel.add(dateBox);
		rootPanel.add(taskDescription);
		
		
		rootPanel.add(vPanel);
		
		Button btnAddItem = new Button("Adauga");
		btnAddItem.addClickHandler(btnAddThing_onClick);
		rootPanel.add(btnAddItem);
	}
	
	private ClickHandler taskTitleOnClick = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			/*  Insert logic here */
		}
	};
		

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		Init();
	}
	
	ClickHandler btnAddThing_onClick = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			//showLoading();
		    addTask(taskTitle.getValue(), taskDescription.getValue(), dateBox.getValue(), "ttabacel@ea.com");
		}
	};
	
	private void addTask(String taskTitle, String taskDescription, Date taskDueDate,
				String ownerEmail) {
		addTaskService.addTask(taskTitle, taskDescription, taskDueDate, ownerEmail, new AsyncCallback<CloudTask>() {

			@Override
			public void onFailure(Throwable caught) {
				//hideLoading();
				alertWidget(caught.getMessage(), caught.getStackTrace().toString());
			}

			@Override
			public void onSuccess(CloudTask result) {
				//hideLoading();
				showAddedTask(result);
			}
			
		});
	}
	
	private void showAddedTask(CloudTask result){
		TextArea ta = new TextArea();
		TextBox tb = new TextBox();
		DateBox db = new DateBox();
		Button btnSalveaza = new Button("Salveaza");
		VerticalPanel vp = new VerticalPanel();
		DecoratorPanel decPanel = new DecoratorPanel();
		
		ta.setValue(result.getTaskDescription());
		tb.setValue(result.getTaskTitle());
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd - MMM - yyyy");
		db.setFormat(new DateBox.DefaultFormat(dateFormat));
		db.setValue(result.getTaskDueDate());
		
		vp.add(tb);
		vp.add(ta);
		vp.add(db);
		vp.add(btnSalveaza);
		
		DisclosurePanel advancedDisclosure = new DisclosurePanel(tb.getText());
					
		decPanel.add(vp);
		
		advancedDisclosure.setAnimationEnabled(true);
	    advancedDisclosure.setContent(decPanel);
	    advancedDisclosure.setWidth("60em");
	    
	    vPanel.insert(advancedDisclosure, 0);
	}
	
	public static DialogBox alertWidget(final String header, final String content) {
        final DialogBox box = new DialogBox();
        final VerticalPanel panel = new VerticalPanel();
        box.setText(header);
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
