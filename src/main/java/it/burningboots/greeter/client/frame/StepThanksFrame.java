package it.burningboots.greeter.client.frame;

import it.burningboots.greeter.client.ClientConstants;
import it.burningboots.greeter.client.CookieSingleton;
import it.burningboots.greeter.client.LocaleConstants;
import it.burningboots.greeter.client.UiSingleton;
import it.burningboots.greeter.client.UriBuilder;
import it.burningboots.greeter.client.UriDispatcher;
import it.burningboots.greeter.client.WaitSingleton;
import it.burningboots.greeter.client.WizardSingleton;
import it.burningboots.greeter.client.service.DataService;
import it.burningboots.greeter.client.service.DataServiceAsync;
import it.burningboots.greeter.shared.AppConstants;
import it.burningboots.greeter.shared.entity.Participant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StepThanksFrame extends FramePanel {
	
	private static final int ATTEMPT_DELAY = 4000;// 4 seconds
	private static final int ATTEMPT_MAX = 4;// 4 attempts
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private int attemptCount = 0;
	private UriBuilder params = null;
	private VerticalPanel cp = null; // Content panel
	
	public StepThanksFrame(UriBuilder params) {
		super();
		if (params != null) {
			this.params = params;
		} else {
			this.params = new UriBuilder();
		}
		String itemNumber = CookieSingleton.get().getCookie(ClientConstants.COOKIE_ITEM_NUMBER);
		if (itemNumber == null)
			itemNumber = this.params.getValue(AppConstants.PARAM_ID);
		if (itemNumber == null)
			itemNumber = "";
		if (itemNumber.length() > AppConstants.ITEM_NUMBER_LENGHT) {
			itemNumber = itemNumber.substring(0, AppConstants.ITEM_NUMBER_LENGHT);
		}
		cp = new VerticalPanel();
		this.add(cp);
		loadAsyncData(itemNumber);
	}
	
	private void draw() {
		Participant participant = WizardSingleton.get().getParticipantBean();
		String amountString = "##";
		if (participant.getPaymentAmount() != null) {
			amountString = ClientConstants.FORMAT_CURRENCY.format(participant.getPaymentAmount());
		} else {
			//Payment not registered
			UriBuilder param = new UriBuilder();
			param.add(AppConstants.PARAM_ID, participant.getItemNumber());
			param.triggerUri(UriDispatcher.ERROR_PAYMENT);
		}
		//TITLE
		setTitle(constants.thanksTitle());
		
		cp.add(new HTML("<p>"+constants.thanksDonation()+" &euro;"+amountString+
				". "+constants.thanksConfirmed()+"</p>"));
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		cp.add(new HTML("<p style='text-align: center; font-size: 1.5em; color: #e32077;'>"+
				constants.itemNumber()+"</p>"));
		
		cp.add(new HTML("<p style='text-align: center; font-size: 4.5em; color: #e32077;'>"+
				participant.getItemNumber().toUpperCase()+"</p>"));
		cp.add(new HTML("<p style='text-align: center; font-size: 1em;'>"+
				constants.thanksTakeNote()+"</p>"));
		
		cp.add(new HTML("<p>"+constants.thanksWhatIs()+"<br />"+
				constants.thanksTellSomeone()+"</p>"));
		cp.add(new HTML("<p>&nbsp;</p>"));

		cp.add(new HTML("<p>"+constants.thanksReceiveEmail1()+" <b>"+
				participant.getEmail()+"</b> "+constants.thanksReceiveEmail2()+"</p>"));
		
		cp.add(new HTML("<p>&nbsp;</p>"));
				
		cp.add(new HTML("<h3><a href='"+AppConstants.EVENT_URL+"'><i class='fa fa-hand-o-left'></i> <b>Italian Burning Boots</b></a></h3>"));
	}
	
		
	
	//Async methods
	
	
	private void loadAsyncData(String itemNumber) {
		final String fItemNumber = itemNumber;
		AsyncCallback<Participant> callback = new AsyncCallback<Participant>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Participant result) {
				WaitSingleton.get().stop();
				if (result == null) {
					//IPN not yet received
					if (attemptCount < ATTEMPT_MAX) {
						attemptCount++;
						loadAsyncData(fItemNumber);
					} else {
						UiSingleton.get().addWarning("Couldn't find participant with id = "+fItemNumber);
						UriBuilder param = new UriBuilder();
						param.add(AppConstants.PARAM_ID, fItemNumber);
						param.triggerUri(UriDispatcher.ERROR_PAYMENT);
					}
				} else {
					//IPN received
					WizardSingleton.get().setParticipantBean(result);
					draw();
					removeWebSession();
				}
			}
		};
		
		if (itemNumber.length() > 0) {
			WaitSingleton.get().start();
			dataService.findParticipantByItemNumber(itemNumber, ATTEMPT_DELAY, callback);
		}
	}
	
	private void removeWebSession() {
		String idWebSession = CookieSingleton.get().getCookie(ClientConstants.WEBSESSION_COOKIE_NAME);
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Boolean result) {
				WaitSingleton.get().stop();
				CookieSingleton.get().removeCookie(ClientConstants.WEBSESSION_COOKIE_NAME);
			}
		};
		WaitSingleton.get().start();
		dataService.deleteWebSession(idWebSession, callback);
	}
}
