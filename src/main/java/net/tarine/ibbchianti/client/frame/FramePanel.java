package net.tarine.ibbchianti.client.frame;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.UiSingleton;

public class FramePanel extends FlowPanel {

	public FramePanel() {
		super();
		setTitle(ClientConstants.DEFAULT_FRAME_TITLE);
	}
	
	public void setTitle(String frameTitle) {
		UiSingleton.get().setApplicationTitle(frameTitle);
	    SimplePanel headerPanel = UiSingleton.get().getHeaderPanel();
	    headerPanel.clear();
	    String title = ClientConstants.DEFAULT_FRAME_TITLE;
		if (frameTitle != null) {
			title = frameTitle;
		}
		headerPanel.add(new HTML("<h1>"+title+"</h1>"));
	}
	
	//public void forwardIfJoinNotPossible() {
	//	ConfigBean p = WizardSingleton.get().getPropertyBean();
	//	Boolean isRegisterWizard = WizardSingleton.get().getWizardType()
	//			.equals(AppConstants.WIZARD_REGISTER);
	//	//Check if joining wizard can be active
	//	if (isRegisterWizard) {
	//		if ( p.getClosed() ) {
	//			UriBuilder param = new UriBuilder();
	//			param.triggerUri(UriDispatcher.ERROR_CLOSED);
	//		}
	//		if (	(p.getHutCount()+p.getTentCount() >= p.getTotalMax())
	//				||
	//				((p.getHutCount() >= p.getHutMax()) && (p.getTentCount() >= p.getTentMax())) ) {
	//			UriBuilder param = new UriBuilder();
	//			param.triggerUri(UriDispatcher.ERROR_FULL);
	//		}
	//	}
	//}
	
}
