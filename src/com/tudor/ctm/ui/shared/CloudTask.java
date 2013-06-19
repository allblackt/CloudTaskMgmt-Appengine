package com.tudor.ctm.ui.shared;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.datanucleus.annotations.Unowned;


@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class CloudTask implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9127970229764340390L;
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;
	private String taskTitle;
	private String taskDescription;
	private Date taskDueDate;
	@Persistent(defaultFetchGroup="true")
	@Unowned
	private String owner;
	
	public CloudTask(String taskTitle, String taskDescription,
			Date taskDueDate, String owner) {
		this.taskTitle = taskTitle;
		this.taskDescription = taskDescription;
		this.taskDueDate = taskDueDate;
		this.owner = owner;
	}
	
	public CloudTask() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "CloudTask [taskTitle=" + taskTitle + ", taskDescription="
				+ taskDescription + ", taskDueDate=" + taskDueDate + ", owner="
				+ owner + "]";
	}
}
