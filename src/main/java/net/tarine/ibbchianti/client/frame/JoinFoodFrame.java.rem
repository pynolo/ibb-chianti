package net.tarine.ibbchianti.client.frame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import net.tarine.ibbchianti.client.LocaleConstants;
import net.tarine.ibbchianti.client.UiSingleton;
import net.tarine.ibbchianti.client.UriBuilder;
import net.tarine.ibbchianti.client.UriDispatcher;
import net.tarine.ibbchianti.client.WaitSingleton;
import net.tarine.ibbchianti.client.WizardSingleton;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.client.widgets.WizardButtons;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.entity.Participant;

public class JoinFoodFrame extends FramePanel implements IWizardPanel {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private UriBuilder params = null;
	private VerticalPanel cp = null; // Content panel
	
	private TextArea foodArea;
	
	public JoinFoodFrame(UriBuilder params) {
		super();
		if (params != null) {
			this.params = params;
		} else {
			this.params = new UriBuilder();
		}
		String itemNumber = this.params.getValue(AppConstants.PARAMS_ITEM_NUMBER);
		if (itemNumber == null) itemNumber = "";
		cp = new VerticalPanel();
		this.add(cp);
		loadAsyncData(itemNumber);
	}
	
	private void draw() {
		if (WizardSingleton.get().getWizardType().equals(AppConstants.WIZARD_REGISTER))
				forwardIfJoinNotPossible();
		Participant participant = WizardSingleton.get().getParticipantBean();
		
		//TITLE
		setTitle(constants.joinFoodTitle());
		
		cp.add(new HTML("<p>"+constants.joinFoodTellUs()+"</p><br/>"));
		foodArea = new TextArea();
		foodArea.setHeight("8em");
		foodArea.setWidth("100%");
		foodArea.setValue(participant.getFoodRestrictions());
		cp.add(foodArea);
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		//Wizard panel
		WizardButtons wb = new WizardButtons(this, true, true);
		cp.add(wb);
	}
	
	
	private void storeInBean() {
		if (foodArea.getValue().length() > 0) {
			Participant participant = WizardSingleton.get().getParticipantBean();
			participant.setFoodRestrictions(foodArea.getValue());
		}
	}
	
	@Override
	public void goBackward() {
		storeInBean();
		Participant participant = WizardSingleton.get().getParticipantBean();
		//Forward
		UriBuilder param = new UriBuilder();
		param.add(AppConstants.PARAMS_ITEM_NUMBER, participant.getItemNumber());
		param.triggerUri(UriDispatcher.STEP_JOIN_VOLUNTEER);
	}
	
	@Override
	public void goForward() {
		storeInBean();
		Participant participant = WizardSingleton.get().getParticipantBean();
		//Forward
		UriBuilder param = new UriBuilder();
		param.add(AppConstants.PARAMS_ITEM_NUMBER, participant.getItemNumber());
		param.triggerUri(UriDispatcher.STEP_JOIN_LEGAL);
	}

	
	
	//Async methods
	
	
	private void loadAsyncData(String itemNumber) {
		AsyncCallback<Participant> callback = new AsyncCallback<Participant>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Participant result) {
				WizardSingleton.get().setParticipantBean(result);
				draw();
				WaitSingleton.get().stop();
			}
		};
		
		if (itemNumber.length() == 0) {
			//No itemNumber passed => brand new participant
			WaitSingleton.get().start();
			dataService.createTransientParticipant(callback);
		} else {
			//itemNumberKey passed => check participant in WizardSingleton and load it from DB if empty
			Participant prt = WizardSingleton.get().getParticipantBean();
			if (prt == null) {
				WaitSingleton.get().start();
				dataService.findParticipantByItemNumber(itemNumber, 0, callback);
			} else {
				draw();
			}
		}
	}
	
}
