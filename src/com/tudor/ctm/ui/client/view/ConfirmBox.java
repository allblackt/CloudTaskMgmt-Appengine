package com.tudor.ctm.ui.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConfirmBox extends DialogBox {

	Button yes = new Button("Yes");
	
	public ConfirmBox(String message) {
		super();
		
		final DialogBox db = this;
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		
		db.setGlassEnabled(true);
		Button no = new Button("No");
		
		no.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				db.hide();
			}
		});
		
		yes.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				db.hide();
			}
		});
		
		panel.add(new HTML(message));
		hp.add(yes);
		hp.add(no);
		panel.add(hp);
		this.add(panel);
	}
	
	public ConfirmBox yesHandler(ClickHandler handler) {
		yes.addClickHandler(handler);
		return this;
	}
}
