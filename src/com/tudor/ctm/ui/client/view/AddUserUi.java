package com.tudor.ctm.ui.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tudor.ctm.ui.shared.CloudUser;
import com.google.gwt.user.client.ui.SimpleCheckBox;

public class AddUserUi extends Composite {

	private static AddUserUiUiBinder uiBinder = GWT
			.create(AddUserUiUiBinder.class);
	@UiField TextBox txtName;
	@UiField TextBox txtEmail;
	@UiField HTMLPanel cbAdmin;
	@UiField Button btnSave;
	@UiField Button btnCancel;
	@UiField ListBox lstUsers;
	@UiField SimpleCheckBox checkAdmin;
	private List<CloudUser> users;

	interface AddUserUiUiBinder extends UiBinder<Widget, AddUserUi> {
	}

	public AddUserUi(List<CloudUser> users) {
		initWidget(uiBinder.createAndBindUi(this));
		this.users = users;
		for (CloudUser cloudUser : users) {
			lstUsers.addItem(cloudUser.getName(), cloudUser.getId().toString());
		}
		lstUsers.setSelectedIndex(0);
		lstUsersOnChange(null);
	}
	
	@UiHandler("lstUsers")
	void lstUsersOnChange(ChangeEvent event) {
		int index = lstUsers.getSelectedIndex();
		CloudUser selectedUser = users.get(index);
		txtName.setText(selectedUser.getName());
		txtEmail.setText(selectedUser.getEmail());
		if(selectedUser.getIsAdmin() != null && selectedUser.getIsAdmin() == true) {
			checkAdmin.setValue(true);
		}
	}
	
	public CloudUser getUpdatedUser() {
		int index = lstUsers.getSelectedIndex();
		CloudUser selectedUser = users.get(index);
		selectedUser.setEmail(txtEmail.getValue());
		selectedUser.setName(txtName.getValue());
		if(checkAdmin.getValue()!= null && checkAdmin.getValue() == true) {
			selectedUser.setIsAdmin(true);
		}
		return selectedUser;
	}
	
	public boolean isValid(){
		boolean isValid = true;
		String message = "";
		
		if(txtName.getValue().trim().length() == 0 ) {
			isValid = false;
			message += " User name must not be empty!";
		}
		
		if(txtEmail.getValue().trim().length() == 0 ) {
			isValid = false;
			message += " User email must not be empty!";
		}
		if(!isValid) {
			new MessageBox(message.toString()).center();
		}
		return isValid;
	}
}
