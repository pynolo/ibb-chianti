package it.burningboots.entrance.client.frame;

import it.burningboots.entrance.client.LocaleConstants;
import it.burningboots.entrance.client.UriBuilder;
import it.burningboots.entrance.shared.AppConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ErrorSystemFrame extends FramePanel {
	
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private VerticalPanel cp = null; // Content panel
		
	public ErrorSystemFrame(UriBuilder params) {
		super();
		cp = new VerticalPanel();
		this.add(cp);
		draw();
	}
	
	private void draw() {
		//TITLE
		setTitle(constants.errorSystemTitle());
		
		cp.add(new HTML("<p>"+constants.errorSystemInfo()+"</p>"));
		cp.add(new HTML("<p>&nbsp;</p>"));
	
		cp.add(new HTML("<h3><a href='"+AppConstants.EVENT_URL+"'><i class='fa fa-hand-o-left'></i> <b>Italian Burning Boots</b></a></h3>"));
	}
	
}
