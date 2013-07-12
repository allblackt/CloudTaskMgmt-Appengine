package com.tudor.ctm.ui.shared;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class CloudTask implements Serializable {

	private static final long serialVersionUID = 9127970229764340390L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String taskTitle;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String taskDescription;
	private Date taskDueDate;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private int remainingTime;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private int totalTime;

	@Persistent(defaultFetchGroup = "true")
	@Unowned
	private CloudUser owner;

	@Persistent(defaultFetchGroup = "true")
	@Unowned
	private CloudProject project;

	public CloudTask(String taskTitle, String taskDescription,
			Date taskDueDate, CloudUser owner) {
		this.taskTitle = taskTitle;
		this.taskDescription = taskDescription;
		this.taskDueDate = taskDueDate;
		this.owner = owner;
	}

	public CloudTask() {
	}

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

	public CloudUser getOwner() {
		return owner;
	}

	public void setOwner(CloudUser owner) {
		this.owner = owner;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	@Override
	public String toString() {
		return "CloudTask [taskTitle=" + taskTitle + ", taskDescription="
				+ taskDescription + ", taskDueDate=" + taskDueDate + ", owner="
				+ owner + ", project=" + project + "]";
	}

	public CloudProject getProject() {
		return project;
	}

	public void setProject(CloudProject project) {
		this.project = project;
	}

	public static class Builder {
		private Long id;
		private String taskTitle;
		private String taskDescription;
		private Date taskDueDate;
		private int remainingTime;
		private int totalTime;
		private CloudUser owner;
		private CloudProject project;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder taskTitle(String taskTitle) {
			this.taskTitle = taskTitle;
			return this;
		}

		public Builder taskDescription(String taskDescription) {
			this.taskDescription = taskDescription;
			return this;
		}

		public Builder taskDueDate(Date taskDueDate) {
			this.taskDueDate = taskDueDate;
			return this;
		}

		public Builder remainingTime(int remainingTime) {
			this.remainingTime = remainingTime;
			return this;
		}

		public Builder totalTime(int totalTime) {
			this.totalTime = totalTime;
			return this;
		}

		public Builder owner(CloudUser owner) {
			this.owner = owner;
			return this;
		}

		public Builder project(CloudProject project) {
			this.project = project;
			return this;
		}

		public CloudTask build() {
			return new CloudTask(this);
		}
	}

	private CloudTask(Builder builder) {
		this.id = builder.id;
		this.taskTitle = builder.taskTitle;
		this.taskDescription = builder.taskDescription;
		this.taskDueDate = builder.taskDueDate;
		this.remainingTime = builder.remainingTime;
		this.totalTime = builder.totalTime;
		this.owner = builder.owner;
		this.project = builder.project;
	}
}
