package com.tudor.ctm.ui.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class FakeData {
		
		public static List<CloudProject> getCloudProjects() {
			
			return Arrays.asList(
					new CloudProject.Builder().id(1L).name("Cloud Project 1").owner(getUserList().get(0)).members(getUserList()).build(),
					new CloudProject.Builder().id(2L).name("Cloud Project 2").owner(getUserList().get(1)).members(getUserList()).build(),
					new CloudProject.Builder().id(3L).name("Cloud Project 3").owner(getUserList().get(2)).members(getUserList()).build()
					);
		}
		
		public static List<CloudUser> getProjectMembers(long projectId) {
			return getUserList();
		}
		
		public static List<CloudUser> getUserList() {
			return Arrays.asList(
					new CloudUser("tudorsmt@gmail.com", "LogoutURL", false),
					new CloudUser("allblack007@gmail.com", "LogoutURL", false),
					new CloudUser("test@example.com", "LogoutURL", false)
				);
		}
		
		public static List<CloudTask> getCloudTasks() {
			return Arrays.asList(
					new CloudTask.Builder().id(1L).taskDescription("Task 1 description").taskTitle("Task 1 title").project(getCloudProjects().get(0)).owner(getUserList().get(0)).taskDueDate(new Date(1357020840000L)).remainingTime(100).build(),
					new CloudTask.Builder().id(2L).taskDescription("Task 2 description").taskTitle("Task 2 title").project(getCloudProjects().get(0)).owner(getUserList().get(0)).taskDueDate(new Date(1359785640000L)).remainingTime(200).build(),
					new CloudTask.Builder().id(3L).taskDescription("Task 3 description").taskTitle("Task 3 title").project(getCloudProjects().get(0)).owner(getUserList().get(0)).taskDueDate(new Date(1362291240000L)).remainingTime(300).build(),
					new CloudTask.Builder().id(4L).taskDescription("Task 4 description").taskTitle("Task 4 title").project(getCloudProjects().get(0)).owner(getUserList().get(2)).taskDueDate(new Date(1365056040000L)).remainingTime(400).build()
					);
		}
		
		public static List<CloudTask> getUserTasks(CloudUser user) {
			List<CloudTask> userTasks = new ArrayList<CloudTask>();
			for (CloudTask cloudTask : getCloudTasks()) {
				if(cloudTask.getOwner().getEmail() == user.getEmail()) {
					userTasks.add(cloudTask);
				}
			}
			return userTasks;
		}
}
