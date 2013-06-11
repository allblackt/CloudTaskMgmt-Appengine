package com.tudor.ctm.ui.shared;

import java.io.Serializable;
import java.util.Date;

public class CloudTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4867959407964935026L;
	
	private String taskTitle;
	private String taskDescription;
	private Date taskDueDate;
	private CTMUser owner;
	
	public CloudTask(String taskTitle, String taskDescription,
			Date taskDueDate, CTMUser owner) {
		this.taskTitle = taskTitle;
		this.taskDescription = taskDescription;
		this.taskDueDate = taskDueDate;
		this.owner = owner;
	}
	
	public CloudTask() {}
	
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public Date getTaskDueDate() {
		return taskDueDate;
	}
	public void setTaskDueDate(Date taskDueDate) {
		this.taskDueDate = taskDueDate;
	}
	public CTMUser getOwner() {
		return owner;
	}
	public void setOwner(CTMUser owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "CloudTask [taskTitle=" + taskTitle + ", taskDescription="
				+ taskDescription + ", taskDueDate=" + taskDueDate + ", owner="
				+ owner + "]";
	}

	
}
