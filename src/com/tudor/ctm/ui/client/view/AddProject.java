package com.tudor.ctm.ui.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;

public class AddProject extends Composite {

	private static AddProjectUiBinder uiBinder = GWT
			.create(AddProjectUiBinder.class);
	@UiField TextBox txtProjectName;
	@UiField Button btnCancel;
	@UiField Button btnSave;

	interface AddProjectUiBinder extends UiBinder<Widget, AddProject> {
	}

	public AddProject() {
		initWidget(uiBinder.createAndBindUi(this));
		txtProjectName.setFocus(true);
	}

}
