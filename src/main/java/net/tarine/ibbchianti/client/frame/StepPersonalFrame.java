package net.tarine.ibbchianti.client.frame;

import java.util.Date;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.IWizardFrame;
import net.tarine.ibbchianti.client.LocaleConstants;
import net.tarine.ibbchianti.client.UiSingleton;
import net.tarine.ibbchianti.client.UriBuilder;
import net.tarine.ibbchianti.client.UriDispatcher;
import net.tarine.ibbchianti.client.WaitSingleton;
import net.tarine.ibbchianti.client.WizardSingleton;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.client.widgets.ExtendedTextBox;
import net.tarine.ibbchianti.client.widgets.ForwardButton;
import net.tarine.ibbchianti.client.widgets.HeartbeatWidget;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.StringValidator;
import net.tarine.ibbchianti.shared.ValidationException;
import net.tarine.ibbchianti.shared.entity.Participant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class StepPersonalFrame extends FramePanel implements IWizardFrame {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private VerticalPanel cp = null; // Content panel
	
	private HeartbeatWidget heartbeat = null;
	private ExtendedTextBox emailText;
	private TextBox firstNameText;
	private TextBox lastNameText;
	private DateBox birthDate;
	private TextBox birthCityText;
	
	public StepPersonalFrame(UriBuilder params) {
		super();
		cp = new VerticalPanel();
		this.add(cp);
		draw();
	}
	
	private void draw() {
		Participant participant = WizardSingleton.get().getParticipantBean();
		cp.clear();

		//TITLE
		setTitle(constants.personalTitle());
		
		cp.add(new HTML("<p>"+constants.personalIntro1()+
				" <b>&euro;"+ClientConstants.FORMAT_CURRENCY.format(WizardSingleton.get().getConfigBean().getDonationMin())+
				"</b> "+constants.personalIntro2()+"</p>"));
		cp.add(new HTML("<p>"+constants.personalIntroWarning()+"</p>"));
		//EMAIL
		cp.add(new HTML(constants.personalEmail()));
		HorizontalPanel emailPanel = new HorizontalPanel();
		emailText = new ExtendedTextBox();
		emailText.setValue(participant.getEmail());
		emailPanel.add(emailText);
		emailPanel.add(new InlineHTML("&nbsp;&nbsp;"));
		cp.add(emailPanel);
		cp.add(new HTML("<p><i>"+constants.personalEmailWarning()+"</i></p>"));
		
		HorizontalPanel namePanel = new HorizontalPanel();
		cp.add(namePanel);

		FlowPanel firstNamePanel = new FlowPanel();
		firstNamePanel.add(new HTML(constants.personalFirstName()));
		firstNameText = new TextBox();
		firstNameText.setMaxLength(64);
		firstNameText.setValue(participant.getFirstName());
		firstNamePanel.add(firstNameText);
		namePanel.add(firstNamePanel);
		
		namePanel.add(new InlineHTML("&nbsp;"));
		
		FlowPanel lastNamePanel = new FlowPanel();
		lastNamePanel.add(new HTML(constants.personalLastName()));
		lastNameText = new TextBox();
		lastNameText.setMaxLength(64);
		lastNameText.setValue(participant.getLastName());
		lastNamePanel.add(lastNameText);
		namePanel.add(lastNamePanel);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		HorizontalPanel birthPanel = new HorizontalPanel();
		cp.add(birthPanel);
		
		FlowPanel birthCityPanel = new FlowPanel();
		birthCityPanel.add(new HTML("<p>"+constants.personalBirthCity()+"</p>"));
		birthCityText = new TextBox();
		birthCityText.setMaxLength(128);
		birthCityText.setValue(participant.getBirthCity());
		birthCityPanel.add(birthCityText);
		birthPanel.add(birthCityPanel);
		
		birthPanel.add(new InlineHTML("&nbsp;"));
		
		FlowPanel birthDatePanel = new FlowPanel();
		birthDatePanel.add(new HTML("<p>"+constants.personalBirthDate()+"</p>"));
		birthDate = new DateBox();
		DateBox.Format BOX_FORMAT_TIMESTAMP = new DateBox.DefaultFormat(ClientConstants.FORMAT_DAY);
		birthDate.setFormat(BOX_FORMAT_TIMESTAMP);
		birthDate.setValue(participant.getBirthDt());
		birthDatePanel.add(birthDate);
		birthPanel.add(birthDatePanel);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		cp.add(new HTML("<b>"+constants.personalPrivacy()+"</b>"));
		//Iubenda
		cp.add(new HTML("<a href='//www.iubenda.com/privacy-policy/8035880' "+
				"class='iubenda-white iubenda-embed' title='Privacy Policy'>Privacy Policy</a>"+
				"<script type='text/javascript'>(function (w,d) {var loader = function () {var s = d.createElement('script'), tag = d.getElementsByTagName('script')[0]; s.src = '//cdn.iubenda.com/iubenda.js'; tag.parentNode.insertBefore(s,tag);}; if(w.addEventListener){w.addEventListener('load', loader, false);}else if(w.attachEvent){w.attachEvent('onload', loader);}else{w.onload = loader;}})(window, document);</script>"));

		cp.add(new HTML("<p>&nbsp;</p>"));
		
		//Wizard panel
		ForwardButton wb = new ForwardButton(this);
		cp.add(wb);

		heartbeat = new HeartbeatWidget();
		cp.add(heartbeat);
	}
	
	@Override
	public void goForward() {
		//Verification
		String errorMessage = "";
		String email = emailText.getValue();
		try {
			StringValidator.validateEmail(email);
		} catch (ValidationException e) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += e.getMessage();
		}
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
			errorMessage += constants.personalErrorCity();
		}
		Date birthDt = birthDate.getValue();
		if (birthDt == null) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += constants.personalErrorDate();
		}
		
		boolean isError = (errorMessage.length() > 0);
		if (isError) {
			UiSingleton.get().addWarning(errorMessage);
		} else {
			//Store in bean
			Participant transPrt = new Participant();
			transPrt.setEmail(email);
			transPrt.setFirstName(firstName);
			transPrt.setLastName(lastName);
			transPrt.setBirthCity(birthCity);
			transPrt.setBirthDt(birthDt);
			
			AsyncCallback<Participant> callback = new AsyncCallback<Participant>() {
				@Override
				public void onFailure(Throwable caught) {
					UiSingleton.get().addError(caught);
					WaitSingleton.get().stop();
				}
				@Override
				public void onSuccess(Participant prt) {
					WaitSingleton.get().stop();
					
					heartbeat.cancelHeartbeatTimer();
					UriBuilder param = new UriBuilder();
					param.add(AppConstants.PARAM_ID, prt.getItemNumber());
					param.triggerUri(UriDispatcher.STEP_CHECKOUT);
				}
			};
			WaitSingleton.get().start();
			dataService.saveOrUpdateParticipant(transPrt, callback);
			

		}
	}
	
}
