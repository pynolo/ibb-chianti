package net.tarine.ibbchianti.client.frame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.LocaleConstants;
import net.tarine.ibbchianti.client.UiSingleton;
import net.tarine.ibbchianti.client.UriBuilder;
import net.tarine.ibbchianti.client.UriDispatcher;
import net.tarine.ibbchianti.client.WaitSingleton;
import net.tarine.ibbchianti.client.WizardSingleton;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.entity.Participant;

public class StepThanksFrame extends FramePanel {
	
	private static final int DELAY = 6000;// 6 seconds
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private UriBuilder params = null;
	private VerticalPanel cp = null; // Content panel
	
	public StepThanksFrame(UriBuilder params) {
		super();
		if (params != null) {
			this.params = params;
		} else {
			this.params = new UriBuilder();
		}
		String itemNumber = this.params.getValue(AppConstants.PARAM_ID);
		if (itemNumber == null) itemNumber = "";
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
				constants.thanksTellSomeone()+"<br/>"+
				"("+constants.thanksGoToForum()+")</p>"));
		cp.add(new HTML("<p>&nbsp;</p>"));

		cp.add(new HTML("<p>"+constants.thanksReceiveEmail()+" <b>"+
				participant.getEmail()+"</b></p>"));
		
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
				if (result == null) {
					UiSingleton.get().addWarning("Couldn't find participant with id = "+fItemNumber);
					UriBuilder param = new UriBuilder();
					param.add(AppConstants.PARAM_ID, fItemNumber);
					param.triggerUri(UriDispatcher.ERROR_PAYMENT);
				} else {
					WizardSingleton.get().setParticipantBean(result);
					draw();
				}
				WaitSingleton.get().stop();
			}
		};
		
		if (itemNumber.length() > 0) {
			WaitSingleton.get().start();
			dataService.findParticipantByItemNumber(itemNumber, DELAY, callback);
		}
	}
	
}