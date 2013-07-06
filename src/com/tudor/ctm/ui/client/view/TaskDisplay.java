package com.tudor.ctm.ui.client.view;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.FakeData;
import com.tudor.ctm.ui.shared.UserData;

public class TaskDisplay extends Composite {

	private static TaskDisplayUiBinder uiBinder = GWT
			.create(TaskDisplayUiBinder.class);
	@UiField TextBox taskTitle;
	@UiField TextArea taskDescription;
	@UiField DateBox dueDate;
	@UiField IntegerBox timeRemaining;
	@UiField ListBox taskOwner;
	@UiField Hidden taskId;
	@UiField Button btnSaveTask;
	String errorMessage = new String();

	interface TaskDisplayUiBinder extends UiBinder<Widget, TaskDisplay> {
	}

	public TaskDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd - MMM - yyyy");
		dueDate.setFormat(new DateBox.DefaultFormat(dateFormat));
		dueDate.setValue(new Date());
		for (UserData user : FakeData.getUserList()) {
			taskOwner.addItem(user.getEmail(), user.getEmail());
		}
		
	}
	
	public TaskDisplay(CloudTask task) {
		this();
		setTask(task);
	}
	
	public void setTask(CloudTask task) {
		taskTitle.setValue(task.getTaskTitle());
		taskDescription.setValue(task.getTaskDescription());
		taskId.setValue(task.getId().toString());
		dueDate.setValue(task.getTaskDueDate());
		timeRemaining.setValue(task.getRemainingTime());
		for(int i=0; i < taskOwner.getItemCount(); i++) {
			if(task.getOwner().compareToIgnoreCase(taskOwner.getValue(i)) == 0) {
				taskOwner.setSelectedIndex(i);
				break;
			}
		}
	}
	
	public CloudTask getTask() throws Exception {
		if(!isValid()) {
			throw new Exception();
		}
		
		Long id = taskId.getValue().trim().length() == 0 ? null : Long.parseLong(taskId.getValue().trim());
		
		return new CloudTask.Builder()
			.id(id)
			.taskDescription(taskDescription.getValue().trim())
			.taskTitle(taskTitle.getValue().trim())
			.remainingTime(timeRemaining.getValue())
			.taskDueDate(dueDate.getValue())
			.owner(taskOwner.getValue(taskOwner.getSelectedIndex()))
			.build();
	}
	
	public Button getBtnSaveTask() {
		return btnSaveTask;
	}
	
	public String getErrorMessage() {
		return errorMessage.trim();
	}

	private boolean isValid() {
		boolean isValid = true;
		errorMessage = new String();
		
		if(taskTitle.getValue().trim().length() == 0) {
			isValid = false;
			errorMessage += " Task title must not be empty!";
		}
		if(taskDescription.getValue().trim().length() == 0) {
			isValid = false;
			errorMessage += " Task description must not be empty!";
		}
		if(dueDate.getValue().getTime() < System.currentTimeMillis()) {
			isValid = false;
			errorMessage += " Task due date must not be in the past!";
		}
		if(taskOwner.getSelectedIndex() == -1) {
			isValid = false;
			errorMessage += " Select a task owner!";
		}
		if(timeRemaining.getValue() < 0) {
			isValid = false;
			errorMessage += " Time remaining must be at least 0!";
		}
		
		if(isValid) {
			errorMessage = null;
		}
		return isValid;
	}
}
