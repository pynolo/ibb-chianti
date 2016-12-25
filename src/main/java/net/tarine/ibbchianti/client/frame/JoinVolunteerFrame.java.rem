package net.tarine.ibbchianti.client.frame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
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

public class JoinVolunteerFrame extends FramePanel implements IWizardPanel {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private UriBuilder params = null;
	private VerticalPanel cp = null; // Content panel
	
	private CheckBox lntCheck;
	private CheckBox kitchenCheck;
	private CheckBox woodCheck;
	private CheckBox greeterCheck;
	private CheckBox decoCheck;
	
	public JoinVolunteerFrame(UriBuilder params) {
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
		
		String volunteer = participant.getVolunteering();
		if (volunteer == null) volunteer = "";
		
		//TITLE
		setTitle(constants.joinVolunteerTitle());
		
		cp.add(new HTML("<p>"+constants.joinVolunteerIntro()+"</p>"));
		
		lntCheck = new CheckBox(constants.joinVolunteerLntName(), true);
		lntCheck.setValue(volunteer.contains(AppConstants.VOLUNTEER_LNT));
		cp.add(lntCheck);
		cp.add(new HTML(constants.joinVolunteerLntDescr()+"<br/>&nbsp;"));
		
		kitchenCheck = new CheckBox(constants.joinVolunteerKitchenName(), true);
		kitchenCheck.setValue(volunteer.contains(AppConstants.VOLUNTEER_KITCHEN));
		cp.add(kitchenCheck);
		cp.add(new HTML(constants.joinVolunteerKitchenDescr()+"<br/>&nbsp;"));
		
		greeterCheck = new CheckBox(constants.joinVolunteerGreeterName(), true);
		greeterCheck.setValue(volunteer.contains(AppConstants.VOLUNTEER_GREETER));
		cp.add(greeterCheck);
		cp.add(new HTML(constants.joinVolunteerGreeterDescr()+"<br/>&nbsp;"));
		
		woodCheck = new CheckBox(constants.joinVolunteerWoodName(), true);
		woodCheck.setValue(volunteer.contains(AppConstants.VOLUNTEER_WOOD));
		cp.add(woodCheck);
		cp.add(new HTML(constants.joinVolunteerWoodDescr()+"<br/>&nbsp;"));
		
		decoCheck = new CheckBox(constants.joinVolunteerDecoName(), true);
		decoCheck.setValue(volunteer.contains(AppConstants.VOLUNTEER_DECO));
		cp.add(decoCheck);
		cp.add(new HTML(constants.joinVolunteerDecoDescr()+"<br/>&nbsp;"));
		
		//Wizard panel
		WizardButtons wb = new WizardButtons(this, true, true);
		cp.add(wb);
	}
	
	private void storeInBean() {
		Participant participant = WizardSingleton.get().getParticipantBean();
		String volunteer = "";
		if (lntCheck.getValue()) volunteer += AppConstants.VOLUNTEER_LNT+" ";
		if (kitchenCheck.getValue()) volunteer += AppConstants.VOLUNTEER_KITCHEN+" ";
		if (greeterCheck.getValue()) volunteer += AppConstants.VOLUNTEER_GREETER+" ";
		if (woodCheck.getValue()) volunteer += AppConstants.VOLUNTEER_WOOD+" ";
		if (decoCheck.getValue()) volunteer += AppConstants.VOLUNTEER_DECO+" ";
		participant.setVolunteering(volunteer);
	}
	
	@Override
	public void goBackward() {
		storeInBean();
		Participant participant = WizardSingleton.get().getParticipantBean();
		//Backward
		UriBuilder param = new UriBuilder();
		param.add(AppConstants.PARAMS_ITEM_NUMBER, participant.getItemNumber());
		if (WizardSingleton.get().getWizardType() == AppConstants.WIZARD_REGISTER) {
			// REGISTER
			param.triggerUri(UriDispatcher.STEP_JOIN_BASE);
		} else {
			// TRANSFER
			param.triggerUri(UriDispatcher.STEP_REPLACE_BASE);
		}
		
	}
	
	@Override
	public void goForward() {
		storeInBean();
		Participant participant = WizardSingleton.get().getParticipantBean();
		//Forward
		UriBuilder param = new UriBuilder();
		param.add(AppConstants.PARAMS_ITEM_NUMBER, participant.getItemNumber());
		param.triggerUri(UriDispatcher.STEP_JOIN_FOOD);
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
