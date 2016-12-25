package net.tarine.ibbchianti.client.frame;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

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
import net.tarine.ibbchianti.shared.StringValidator;
import net.tarine.ibbchianti.shared.ValidationException;
import net.tarine.ibbchianti.shared.entity.Participant;

public class JoinLegalFrame extends FramePanel implements IWizardPanel {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private DateTimeFormat DTF = DateTimeFormat.getFormat("dd/MM/yyyy");
	
	private UriBuilder params = null;
	private VerticalPanel cp = null; // Content panel
	
	private TextBox firstNameText;
	private TextBox lastNameText;
	private DateBox birthDate;
	private TextBox birthCityText;
	private CheckBox ibbCheck;
	private CheckBox burnerCheck;
	
	public JoinLegalFrame(UriBuilder params) {
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
		setTitle(constants.joinLegalTitle());
		
		
		cp.add(new HTML("<p>"+constants.joinLegalItalianLaw()+"</p>"));
		
		HorizontalPanel namePanel = new HorizontalPanel();
		cp.add(namePanel);

		FlowPanel firstNamePanel = new FlowPanel();
		firstNamePanel.add(new HTML(constants.joinLegalFirstName()));
		firstNameText = new TextBox();
		firstNameText.setMaxLength(64);
		firstNameText.setValue(participant.getFirstName());
		firstNamePanel.add(firstNameText);
		namePanel.add(firstNamePanel);
		
		namePanel.add(new InlineHTML("&nbsp;"));
		
		FlowPanel lastNamePanel = new FlowPanel();
		lastNamePanel.add(new HTML(constants.joinLegalLastName()));
		lastNameText = new TextBox();
		lastNameText.setMaxLength(64);
		lastNameText.setValue(participant.getLastName());
		lastNamePanel.add(lastNameText);
		namePanel.add(lastNamePanel);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		HorizontalPanel birthPanel = new HorizontalPanel();
		cp.add(birthPanel);
		
		FlowPanel birthCityPanel = new FlowPanel();
		birthCityPanel.add(new HTML("<p>"+constants.joinLegalBirthCity()+"</p>"));
		birthCityText = new TextBox();
		birthCityText.setMaxLength(128);
		birthCityText.setValue(participant.getBirthCity());
		birthCityPanel.add(birthCityText);
		birthPanel.add(birthCityPanel);
		
		birthPanel.add(new InlineHTML("&nbsp;"));
		
		FlowPanel birthDatePanel = new FlowPanel();
		birthDatePanel.add(new HTML("<p>"+constants.joinLegalBirthDate()+"</p>"));
		birthDate = new DateBox();
		DateBox.Format BOX_FORMAT_TIMESTAMP = new DateBox.DefaultFormat(DTF);
		birthDate.setFormat(BOX_FORMAT_TIMESTAMP);
		birthDate.setValue(participant.getBirthDt());
		birthDatePanel.add(birthDate);
		birthPanel.add(birthDatePanel);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		
		cp.add(new HTML("<p>"+constants.joinLegalParticipation()+"</p>"));
		VerticalPanel hp = new VerticalPanel();
		cp.add(hp);
		ibbCheck = new CheckBox("&nbsp; "+constants.joinLegalParticipationIbb(), true);
		ibbCheck.setValue(participant.getAlreadyIbb());
		hp.add(ibbCheck);
		burnerCheck = new CheckBox("&nbsp; "+constants.joinLegalParticipationBurns(), true);
		burnerCheck.setValue(participant.getAlreadyBurner());
		hp.add(burnerCheck);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		//Wizard panel
		WizardButtons wb = new WizardButtons(this, true, true);
		cp.add(wb);
	}
	
	
	private boolean storeInBean() {
		String errorMessage = "";
		String firstName = firstNameText.getValue();
		try {
			StringValidator.validateName(firstName);
		} catch (ValidationException e) {
			errorMessage += e.getMessage();
		}
		String lastName = lastNameText.getValue();
		try {
			StringValidator.validateName(lastName);
		} catch (ValidationException e) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += e.getMessage();
		}
		String birthCity = birthCityText.getValue();
		if (birthCity.length() < 3) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += constants.joinLegalErrorCity();
		}
		Date birthDt = birthDate.getValue();
		if (birthDt == null) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += constants.joinLegalErrorDate();
		}
		
		boolean isError = (errorMessage.length() > 0);
		if (isError) {
			UiSingleton.get().addWarning(errorMessage);
		} else {
			//Store in bean
			Participant participant = WizardSingleton.get().getParticipantBean();
			participant.setFirstName(firstName);
			participant.setLastName(lastName);
			participant.setBirthCity(birthCity);
			participant.setBirthDt(birthDt);
			participant.setAlreadyIbb(ibbCheck.getValue());
			participant.setAlreadyBurner(burnerCheck.getValue());
		}
		return isError;
	}
	
	@Override
	public void goBackward() {
		boolean isError = storeInBean();
		if (!isError) {
			Participant participant = WizardSingleton.get().getParticipantBean();
			//Forward
			UriBuilder param = new UriBuilder();
			param.add(AppConstants.PARAMS_ITEM_NUMBER, participant.getItemNumber());
			param.triggerUri(UriDispatcher.STEP_JOIN_FOOD);
		}
	}
	
	@Override
	public void goForward() {
		boolean isError = storeInBean();
		if (!isError) {
			Participant participant = WizardSingleton.get().getParticipantBean();
			//Forward
			UriBuilder param = new UriBuilder();
			param.add(AppConstants.PARAMS_ITEM_NUMBER, participant.getItemNumber());
			if (WizardSingleton.get().getWizardType() == AppConstants.WIZARD_REGISTER) {
				// REGISTER
				param.triggerUri(UriDispatcher.STEP_JOIN_CHECKOUT);
			} else {
				// TRANSFER
				param.triggerUri(UriDispatcher.STEP_REPLACE_SAVE);
			}
		}
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
