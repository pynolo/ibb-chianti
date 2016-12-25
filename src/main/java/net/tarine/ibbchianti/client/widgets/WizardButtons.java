package net.tarine.ibbchianti.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

import net.tarine.ibbchianti.client.LocaleConstants;
import net.tarine.ibbchianti.client.frame.IWizardPanel;

public class WizardButtons extends FlowPanel {

	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private IWizardPanel parent;
	boolean prevEnabled = false;
	boolean nextEnabled = false;
	private Button prevButton;
	private Button nextButton;
	
	public WizardButtons(IWizardPanel parent, boolean prevEnabled, boolean nextEnabled) {
		this.parent = parent;
		this.prevEnabled = prevEnabled;
		this.nextEnabled = nextEnabled;
		draw();
	}
	
	private void draw() {
		this.add(new HTML("<p>&nbsp;</p>"));
		this.setStyleName("row");
		//PREV
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.setStyleName("col-xs-2");
		prevButton = new Button();
		prevButton.setHTML("&nbsp;<i class='glyphicon glyphicon-chevron-left'></i>"+
				"<i class='glyphicon glyphicon-chevron-left'></i> "+constants.prev());
		prevButton.setEnabled(prevEnabled);
		prevButton.setStyleName("btn btn-primary");
		prevButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.goBackward();
			}
		});
		leftPanel.add(prevButton);
		this.add(leftPanel);
		
		SimplePanel centerPanel = new SimplePanel();
		centerPanel.setStyleName("col-xs-8");
		this.add(centerPanel);
		
		//NEXT
		SimplePanel rightPanel = new SimplePanel();
		rightPanel.setStyleName("col-xs-2");
		nextButton = new Button();
		nextButton.setHTML(constants.next()+" <i class='glyphicon glyphicon-chevron-right'></i>"+
		"<i class='glyphicon glyphicon-chevron-right'></i>&nbsp;");
		nextButton.setEnabled(nextEnabled);
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
