package com.tudor.ctm.ui.client.view;

import java.util.List;

import sun.awt.CausedFocusEvent.Cause;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.tudor.ctm.ui.client.GetUserData;
import com.tudor.ctm.ui.client.GetUserDataAsync;
import com.tudor.ctm.ui.client.ManageTaskService;
import com.tudor.ctm.ui.client.ManageTaskServiceAsync;
import com.tudor.ctm.ui.client.Welcome;
import com.tudor.ctm.ui.client.res.CTMRes;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.CloudUser;
import com.google.gwt.user.client.ui.Label;

public class UserUI extends Composite{

	private static UserViewUiBinder uiBinder = GWT
			.create(UserViewUiBinder.class);
	
	private static CTMRes ctmresources = GWT.create(CTMRes.class);
	private static GetUserDataAsync getUserData = GWT.create(GetUserData.class);
	private static ManageTaskServiceAsync manageTaskService = GWT.create(ManageTaskService.class);
	private CloudUser user;
	private CloudProject selectedProject;
	
	@UiField(provided=true) 
	CellList<CloudProject> cellList = new CellList<CloudProject>(new AbstractCell<CloudProject>(){
		@Override
		public void render(Context context, CloudProject value, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<table>");
		    sb.appendHtmlConstant("<tr><td rowspan='3'>");
		    sb.appendHtmlConstant(AbstractImagePrototype.create(ctmresources.projectIcon()).getHTML());
		    sb.appendHtmlConstant("</td>");
		    sb.appendHtmlConstant("<td style='font-size:95%;'>");
		    sb.appendEscaped(value.getName());
		    sb.appendHtmlConstant("</td></tr><tr><td>");
		    sb.appendEscaped(value.getOwner().getName() + " (" + value.getOwner().getEmail() + ")");
		    sb.appendHtmlConstant("</td></tr></table>");
		}
	});
	@UiField VerticalPanel tasksContainer;
	@UiField Button btnNewTask;
	@UiField HTML noTasks;
	@UiField Label lblWarning;

	interface UserViewUiBinder extends UiBinder<Widget, UserUI> {
	}

	public UserUI(CloudUser user) {
		initWidget(uiBinder.createAndBindUi(this));
		this.user = user;
		btnNewTask.addClickHandler(btnNewTaskClick);
		cellList.setSelectionModel(getUserProjectSelectionModel());;
		loadUserProjects(user);
		tasksContainer.addStyleName("tasksPadding");
	}
	
	private void loadUserProjects(CloudUser user) {
		Welcome.showLoading();
		getUserData.getUserProjects(user, new AsyncCallback<List<CloudProject>>() {
			
			@Override
			public void onSuccess(List<CloudProject> projects) {
				Welcome.hideLoading();
				if(projects != null)
					System.out.println(projects.toString());
				if(projects == null || projects.size() == 0) {
					btnNewTask.setVisible(false);
					lblWarning.setVisible(true);
					return;
				}
				
				btnNewTask.setVisible(true);
				lblWarning.setVisible(false);
				
				cellList.setRowData(projects);
				cellList.getSelectionModel().setSelected(projects.get(0), true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Welcome.hideLoading();
				caught.printStackTrace();
				new MessageBox(caught.getMessage()).center();				
			}
		});
	}
	
	private void loadTasks (List<CloudTask> tasks) {
		System.out.println("Should start loading tasks...");
		tasksContainer.clear();
		if(tasks == null || tasks.size() == 0) {
			noTasks.setVisible(true);
			tasksContainer.add(noTasks);
		} else {
			System.out.println(tasks.size() + " tasks in current project");
			for (CloudTask ct : tasks) {
				addTaskToContainer(ct);					
			}
		}
	}
	
	private void addTaskToContainer(final CloudTask ct) {
		/* Skip adding the task in view if the user isn't the owner of the project / task */
		if( !ct.getOwner().equals(user) && !ct.getProject().getOwner().equals(user)) {
			return;
		}
		
		final TaskDisplay td = new TaskDisplay(ct, ct.getProject());
		final DisclosurePanel dc = new DisclosurePanel(ct.getTaskTitle() + " - " + ct.getRemainingTime() + " hours left.");
		
		if(selectedProject.getOwner().equals(user)) {
			td.btnDelete.setVisible(true);
			td.btnDelete.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					new ConfirmBox("Are you sure you want to delete this task?").yesHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							manageTaskService.removeTask(ct, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									new MessageBox(caught.getMessage()).center();
								}

								@Override
								public void onSuccess(Boolean result) {
									new MessageBox("Task was deleted.").center();
									dc.removeFromParent();
								}
							});
							
						}
					}).center();
					
				}
			});
		}
		
		dc.setOpen(false);
		dc.setAnimationEnabled(true);
		dc.setContent(td);
		
		/* Prepare task for being edited */
		td.btnSaveTask.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					Welcome.showLoading();
					CloudTask savedTask = td.getTask();
					manageTaskService.editTask(savedTask, new AsyncCallback<CloudTask>() {
						@Override
						public void onSuccess(CloudTask savedTask) {
							if(savedTask.getOwner().equals(user) || savedTask.getProject().getOwner().equals(user)) {
								dc.getHeaderTextAccessor().setText(savedTask.getTaskTitle() + " - " + savedTask.getRemainingTime() + " hours left.");
								dc.setOpen(false);
							} else {
								/* Remove if task was reassigned */
								dc.removeFromParent();
							}
							Welcome.hideLoading();
						}
						@Override
						public void onFailure(Throwable caught) {
							new MessageBox(caught.getMessage()).center();
							Welcome.hideLoading();
						}
					});
					
				} catch (Exception e) {
					Welcome.hideLoading();
					new MessageBox(td.getErrorMessage()).center();;
				}
			}
		});
		tasksContainer.add(dc);
	}
	
	private void getProjectTasksForUser(CloudProject project) {
		Welcome.showLoading();
		manageTaskService.getProjectTasks(project, user, new AsyncCallback<List<CloudTask>>() {

			@Override
			public void onFailure(Throwable caught) {
				Welcome.hideLoading();
				caught.printStackTrace();
				new MessageBox(caught.getMessage()).center();
			}

			@Override
			public void onSuccess(List<CloudTask> tasks) {
				Welcome.hideLoading();
				System.out.println("Loading tasks...");
				loadTasks(tasks);
			}
		});
	}
	
	private SingleSelectionModel<CloudProject> getUserProjectSelectionModel() {

		ProvidesKey<CloudProject> KEY_PROVIDER = new ProvidesKey<CloudProject>() {
			@Override
			public Object getKey(CloudProject item) {
				return item.getId();
			}
		};
		
		final SingleSelectionModel<CloudProject> selectionModel = new SingleSelectionModel<CloudProject>(KEY_PROVIDER);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Welcome.showLoading();
				System.out.println("Triggered selection change for " + selectionModel.getSelectedObject().getName());
				selectedProject = selectionModel.getSelectedObject();
				getProjectTasksForUser(selectionModel.getSelectedObject());
			}
		});
		return selectionModel;
	}
	
	
	/* When a new task is addded */
	private ClickHandler btnNewTaskClick = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			//final CloudProject cp = ((SingleSelectionModel<CloudProject>)cellList.getSelectionModel()).getSelectedObject();
			final DialogBox box = new DialogBox();
			final TaskDisplay td = new TaskDisplay(selectedProject);
			
			if(!selectedProject.getOwner().equals(user)) {
				td.forceOwner(user);
			}
			
			td.btnCancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					box.hide();
				}
			});
			
			/* Insert tasks */
			td.btnSaveTask.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					try {
						Welcome.showLoading();
						CloudTask savedTask = td.getTask();
						manageTaskService.addTask(savedTask, new AsyncCallback<CloudTask>() {
							@Override
							public void onSuccess(CloudTask savedTask) {
								Welcome.hideLoading();
								if(savedTask.getId() != null) {
									noTasks.setVisible(false);
									addTaskToContainer(savedTask);
								}
							}
							@Override
							public void onFailure(Throwable caught) {
								Welcome.hideLoading();
								new MessageBox(caught.getMessage()).center();
							}
						});
						box.hide();
					} catch (Exception e) {
						Welcome.hideLoading();
						if(td.getErrorMessage() == null) {
							e.printStackTrace();
						} else { 
							new MessageBox(td.getErrorMessage()).center();
						}
					}
				}
			});
			box.add(td);
			box.setGlassEnabled(true);
			box.center();
		}
	};
}
