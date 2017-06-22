package it.burningboots.entrance.client.widgets;

import it.burningboots.entrance.client.IWizardFrame;
import it.burningboots.entrance.client.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class ForwardButton extends FlowPanel {

	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private Button nextButton;
	private IWizardFrame parent;
	
	public ForwardButton(IWizardFrame parent) {
		this.parent = parent;
		draw();
	}
	
	private void draw() {
		this.add(new HTML("<p>&nbsp;</p>"));
		this.setStyleName("row");
		//PREV
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.setStyleName("col-xs-2");
		this.add(leftPanel);
		
		SimplePanel centerPanel = new SimplePanel();
		centerPanel.setStyleName("col-xs-8");
		this.add(centerPanel);
		
		//NEXT
		SimplePanel rightPanel = new SimplePanel();
		rightPanel.setStyleName("col-xs-2");
		nextButton = new Button();
		nextButton.setHTML(constants.forward()+" <i class='glyphicon glyphicon-chevron-right'></i>"+
		"<i class='glyphicon glyphicon-chevron-right'></i>&nbsp;");
		nextButton.setStyleName("btn btn-primary");
		nextButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.goForward();
			}
		});
		rightPanel.add(nextButton);
		this.add(rightPanel);
	}
	
//<div class="row">
//	<div class="col-sm-2">
// 		<button type="button" class="btn btn-primary" disabled="true">&nbsp;<i class="glyphicon glyphicon-chevron-left"></i><i class="glyphicon glyphicon-chevron-left"></i> Prev</button>
//	</div>
//	<div class="col-sm-8"></div>
//	<div class="col-sm-2">
//		<button type="button" class="btn btn-primary" onclick="forwardForm0(); return true;">Next <i class="glyphicon glyphicon-chevron-right"></i><i class="glyphicon glyphicon-chevron-right"></i>&nbsp;</button>
//	</div>
//</div>
}
