package com.tudor.ctm.ui.client.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
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
import com.tudor.ctm.ui.client.res.CTMRes;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.FakeData;
import com.tudor.ctm.ui.shared.CloudUser;
import com.google.gwt.user.client.ui.Button;

public class UserUI extends Composite{

	private static UserViewUiBinder uiBinder = GWT
			.create(UserViewUiBinder.class);
	
	private static CTMRes ctmresources = GWT.create(CTMRes.class);
	private static GetUserDataAsync getUserData = GWT.create(GetUserData.class);
	private static ManageTaskServiceAsync manageTaskService = GWT.create(ManageTaskService.class);
	
	private CloudUser user;
	
	@UiField(provided=true) 
	CellList<CloudProject> cellList = new CellList<CloudProject>(new AbstractCell<CloudProject>(){
		@Override
		public void render(Context context, CloudProject value, SafeHtmlBuilder sb) {
			//sb.appendEscaped(value.getName());
			sb.appendHtmlConstant("<table>");
		    sb.appendHtmlConstant("<tr><td rowspan='3'>");
		    sb.appendHtmlConstant(AbstractImagePrototype.create(ctmresources.projectIcon()).getHTML());
		    sb.appendHtmlConstant("</td>");
		    sb.appendHtmlConstant("<td style='font-size:95%;'>");
		    sb.appendEscaped(value.getName());
		    sb.appendHtmlConstant("</td></tr><tr><td>");
		    sb.appendEscaped(value.getOwner().getEmail());
		    sb.appendHtmlConstant("</td></tr></table>");
		}
	});
	@UiField VerticalPanel tasksContainer;
	@UiField Button btnNewTask;

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
		
		getUserData.getUserProjects(user, new AsyncCallback<List<CloudProject>>() {
			
			@Override
			public void onSuccess(List<CloudProject> projects) {
				if(projects != null)
					System.out.println(projects.toString());
					if(projects == null || projects.size() == 0) {
					return;
				}
				cellList.setRowData(projects);
				cellList.getSelectionModel().setSelected(projects.get(0), true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				new MessageBox(caught.getMessage()).center();				
			}
		});
	}
	
	private void loadTasks (List<CloudTask> tasks) {
		tasksContainer.clear();
		if(tasks == null || tasks.size() == 0) {
			tasksContainer.add(new HTML("No tasks to display"));
		} else {
			for (CloudTask ct : tasks) {
				final TaskDisplay td = new TaskDisplay(ct, ct.getProject());
				final DisclosurePanel dc = new DisclosurePanel(ct.getTaskTitle() + " - " + ct.getRemainingTime() + " hours left.");
				dc.setOpen(false);
				dc.setAnimationEnabled(true);
				dc.setContent(td);
				td.btnSaveTask.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						try {
							CloudTask savedTask = td.getTask();
							dc.getHeaderTextAccessor().setText(savedTask.getTaskTitle() + " - " + savedTask.getRemainingTime() + " hours left.");
							dc.setOpen(false);
						} catch (Exception e) {
							Window.alert(td.getErrorMessage());
						}
					}
				});
				tasksContainer.add(dc);
			}
		}
	}
	
	private void getProjectTasksForUser(CloudProject project) {
//			List<CloudTask> tasks = null;
//			System.out.println("Owner:" + project.getOwner().getEmail());
//			System.out.println("Current user:" + user.getEmail());
//			//if(project.getOwner().getEmail().compareToIgnoreCase(user.getEmail()) == 0) {
//				tasks = FakeData.getCloudTasks();
//			//}
		manageTaskService.getProjectTasks(project, user, new AsyncCallback<List<CloudTask>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				new MessageBox(caught.getMessage()).center();
			}

			@Override
			public void onSuccess(List<CloudTask> tasks) {
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
				getProjectTasksForUser(selectionModel.getSelectedObject());
			}
		});
		return selectionModel;
	}
	
	private ClickHandler btnNewTaskClick = new ClickHandler() {
		@SuppressWarnings("unchecked")
		@Override
		public void onClick(ClickEvent event) {
			final CloudProject cp = ((SingleSelectionModel<CloudProject>)cellList.getSelectionModel()).getSelectedObject();
			final DialogBox box = new DialogBox();
			final TaskDisplay td = new TaskDisplay(cp);
			
			td.btnCancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					box.hide();
				}
			});
			
			td.btnSaveTask.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					try {
						CloudTask savedTask = td.getTask();
						System.out.println(savedTask.toString());
						cellList.getSelectionModel().setSelected(cp, true);
						box.hide();
					} catch (Exception e) {
						if(td.getErrorMessage() == null) {
							e.printStackTrace();
						} else { 
							Window.alert(td.getErrorMessage());
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
