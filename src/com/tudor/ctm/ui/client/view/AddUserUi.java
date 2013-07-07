package com.tudor.ctm.ui.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Button;

public class AddUserUi extends Composite {

	private static AddUserUiUiBinder uiBinder = GWT
			.create(AddUserUiUiBinder.class);
	@UiField TextBox txtName;
	@UiField TextBox txtEmail;
	@UiField HTMLPanel cbAdmin;
	@UiField Button btnSave;
	@UiField Button btnCancel;

	interface AddUserUiUiBinder extends UiBinder<Widget, AddUserUi> {
	}

	public AddUserUi() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
