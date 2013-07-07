package com.tudor.ctm.ui.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageBox extends DialogBox {

	public MessageBox(String message) {
		super();
		
		final DialogBox db = this;
		VerticalPanel panel = new VerticalPanel();
		
		db.setGlassEnabled(true);
		Button b = new Button("Close");
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				db.hide();
			}
		});
		panel.add(new HTML(message));
		panel.add(b);
		this.add(panel);
	}
}
