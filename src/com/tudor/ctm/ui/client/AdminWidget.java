package com.tudor.ctm.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.tudor.ctm.ui.shared.CloudProject;

public class AdminWidget extends VerticalPanel {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AdminWidget(){
		
		super();
		
		List<CloudProject> projects = new ArrayList<CloudProject>();
		
		projects.add(new CloudProject((long) 1, "Cloud project 1"));
		projects.add(new CloudProject((long) 2, "Cloud project 2"));
		
		
		 /* The key provider that allows us to identify Contacts even if a field
		   * changes. We identify contacts by their unique ID.
		   */
		ProvidesKey<CloudProject> KEY_PROVIDER = new ProvidesKey<CloudProject>() {
			@Override
			public Object getKey(CloudProject item) {
				return item.getId();
			}
		};
		
		final CellTable table = new CellTable(KEY_PROVIDER);
		
		final SingleSelectionModel<CloudProject> selectionModel = new SingleSelectionModel<CloudProject>(KEY_PROVIDER);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Window.alert(selectionModel.getSelectedObject().getName());
			}
		});
		
		final TextInputCell nameCell = new TextInputCell();
		
		table.setSelectionModel(selectionModel);
		
	    Column nameColumn = new Column<CloudProject, String>(nameCell) {
			@Override
			public String getValue(CloudProject object) {
				return object.getName();
			}
	    };
	    
	    table.addColumn(nameColumn, "Name");
		
	    nameColumn.setFieldUpdater(new FieldUpdater<CloudProject, String>() {

			@Override
			public void update(int index, CloudProject object, String value) {
				Window.alert("You changed the name of " + object.getName() + " to " + value);
				// Push the changes into the Contact. At this point, you could send an
				// asynchronous request to the server to update the database.
				table.redraw();
			}
		});

	      // Push the data into the widget.
	    table.setRowData(projects);
	      
	    this.add(table);
	    
	    Button b = new Button("Buton nou");
	    this.add(b);
	}
}
