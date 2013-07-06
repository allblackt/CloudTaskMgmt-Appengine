package com.tudor.ctm.ui.shared;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class FakeData {
		
		public static List<CloudProject> getCloudProjects() {
			
			return Arrays.asList(
					new CloudProject.Builder().id((long) 1).name("Cloud Project 1").owner(new UserData("ttabace@gmail.com", "LogoutURL", false)).build(),
					new CloudProject.Builder().id((long) 2).name("Cloud Project 2").owner(new UserData("ttabace@gmail.com", "LogoutURL", false)).build()
					);
		}
		
		public static List<UserData> getProjectMembers(long projectId) {
			return getUserList();
		}
		
		public static List<UserData> getUserList() {
			return Arrays.asList(
					new UserData("tudorsmt@gmail.com", "LogoutURL", false),
					new UserData("allblack007@gmail.com", "LogoutURL", false)
				);
		}
		
		public static List<CloudTask> getCloudTasks() {
			return Arrays.asList(
					new CloudTask.Builder().id(1L).taskDescription("Task 1 description").taskTitle("Task 1 title").owner(getUserList().get(0).getEmail()).taskDueDate(new Date()).remainingTime(100).build(),
					new CloudTask.Builder().id(2L).taskDescription("Task 2 description").taskTitle("Task 2 title").owner(getUserList().get(0).getEmail()).taskDueDate(new Date()).remainingTime(200).build(),
					new CloudTask.Builder().id(3L).taskDescription("Task 3 description").taskTitle("Task 3 title").owner(getUserList().get(0).getEmail()).taskDueDate(new Date()).remainingTime(300).build(),
					new CloudTask.Builder().id(4L).taskDescription("Task 4 description").taskTitle("Task 4 title").owner(getUserList().get(1).getEmail()).taskDueDate(new Date()).remainingTime(400).build()
					);
		}
}
