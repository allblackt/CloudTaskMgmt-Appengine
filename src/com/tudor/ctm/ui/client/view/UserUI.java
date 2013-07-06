package com.tudor.ctm.ui.client.view;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.ui.shared.FakeData;

public class UserUI extends Composite {

	private static UserViewUiBinder uiBinder = GWT
			.create(UserViewUiBinder.class);
	@UiField(provided=true) 
	CellList<CloudProject> cellList = new CellList<CloudProject>(new AbstractCell<CloudProject>(){
		@Override
		public void render(Context context, CloudProject value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	});
	@UiField VerticalPanel tasksContainer;

	interface UserViewUiBinder extends UiBinder<Widget, UserUI> {
	}

	public UserUI() {
		initWidget(uiBinder.createAndBindUi(this));
		
		List<CloudProject> projects = new ArrayList<CloudProject>();
		
		projects.add(new CloudProject((long) 1, "Cloud project 1"));
		projects.add(new CloudProject((long) 2, "Cloud project 2"));
		
		cellList.setSelectionModel(getUserProjectSelectionModel());;
		
		cellList.setRowData(projects);
		
		cellList.redraw();
		
		for (CloudTask ct : FakeData.getCloudTasks()) {
			final TaskDisplay td = new TaskDisplay(ct);
			final DisclosurePanel dc = new DisclosurePanel(ct.getTaskTitle() + " - " + ct.getRemainingTime() + " hours left.");
			dc.setOpen(false);
			dc.setAnimationEnabled(true);
			dc.setContent(td);
			
			td.getBtnSaveTask().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					try {
						CloudTask savedTask = td.getTask();
						//td.setTask(savedTask);
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
				Window.alert(selectionModel.getSelectedObject().getName());
			}
		});
		
		return selectionModel;
	}

}
