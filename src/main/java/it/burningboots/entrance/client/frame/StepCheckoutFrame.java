package it.burningboots.entrance.client.frame;

import java.util.Date;

import it.burningboots.entrance.client.ClientConstants;
import it.burningboots.entrance.client.IWizardFrame;
import it.burningboots.entrance.client.LocaleConstants;
import it.burningboots.entrance.client.UiSingleton;
import it.burningboots.entrance.client.UriBuilder;
import it.burningboots.entrance.client.UriDispatcher;
import it.burningboots.entrance.client.WaitSingleton;
import it.burningboots.entrance.client.WizardSingleton;
import it.burningboots.entrance.client.service.DataService;
import it.burningboots.entrance.client.service.DataServiceAsync;
import it.burningboots.entrance.client.widgets.ExtendedTextBox;
import it.burningboots.entrance.client.widgets.ForwardButton;
import it.burningboots.entrance.client.widgets.HeartbeatWidget;
import it.burningboots.entrance.shared.Amount;
import it.burningboots.entrance.shared.AppConstants;
import it.burningboots.entrance.shared.ConfigBean;
import it.burningboots.entrance.shared.SystemException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StepCheckoutFrame extends FramePanel implements IWizardFrame {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private UriBuilder params = null;
	private String itemNumber = null;
	private VerticalPanel cp = null; // Content panel
	
	private HeartbeatWidget heartbeat = null;
	private TextBox amountText;
	private TextBox cardNumberText;
	private ListBox expMonthList;
	private ListBox expYearList;
	
	public StepCheckoutFrame(UriBuilder params) {
		super();
		//Params
		if (params != null) {
			this.params = params;
		} else {
			this.params = new UriBuilder();
		}
		//Item number
		itemNumber = this.params.getValue(AppConstants.PARAM_ID);
		if (itemNumber == null) itemNumber = "";
		if (itemNumber.equals("")) {
			UiSingleton.get().addWarning("Your data haven't been correctly saved");
		} else {
			cp = new VerticalPanel();
			this.add(cp);
			draw();
		}
	}
	
	private void draw() {
		ConfigBean cb = WizardSingleton.get().getConfigBean();
		Date now = new Date();
		
		//TITLE
		setTitle(constants.checkoutTitle());
		
		cp.add(new HTML("<p>"+constants.checkoutIntro()+"</p>"));

		//EMAIL
		cp.add(new HTML(constants.checkoutDonationAmount()));
		HorizontalPanel emailPanel = new HorizontalPanel();
		amountText = new ExtendedTextBox();
		amountText.setValue(ClientConstants.FORMAT_CURRENCY.format(cb.getDonationMin(now)));
		emailPanel.add(amountText);
		emailPanel.add(new InlineHTML("&nbsp;&nbsp;"));
		cp.add(emailPanel);
		cp.add(new HTML("<p><i>"+constants.checkoutDonationMinimumDesc()+" &euro;"+
				ClientConstants.FORMAT_CURRENCY.format(cb.getDonationMin(now))+"</i></p>"));
		
		HorizontalPanel cardPanel = new HorizontalPanel();
		cp.add(cardPanel);

		FlowPanel cardNumberPanel = new FlowPanel();
		cardNumberPanel.add(new HTML(constants.checkoutCardNumber()));
		cardNumberText = new TextBox();
		cardNumberText.setMaxLength(64);
		cardNumberText.setValue("");
		cardNumberPanel.add(cardNumberText);
		cardPanel.add(cardNumberPanel);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		HorizontalPanel expiryPanel = new HorizontalPanel();
		cp.add(expiryPanel);
		
		expiryPanel.add(new HTML("<p>"+constants.checkoutExpiration()+"</p>"));
		expMonthList = new ListBox();
		expMonthList.addItem("01");
		expMonthList.addItem("02");
		expMonthList.addItem("03");
		expMonthList.addItem("04");
		expMonthList.addItem("05");
		expMonthList.addItem("06");
		expMonthList.addItem("07");
		expMonthList.addItem("08");
		expMonthList.addItem("09");
		expMonthList.addItem("10");
		expMonthList.addItem("11");
		expMonthList.addItem("12");
		expiryPanel.add(expMonthList);
		
		expiryPanel.add(new InlineHTML("/"));
		
		expYearList = new ListBox();
		expYearList.addItem("17");
		expYearList.addItem("18");
		expYearList.addItem("19");
		expYearList.addItem("20");
		expYearList.addItem("21");
		expYearList.addItem("22");
		expYearList.addItem("23");
		expYearList.addItem("24");
		expYearList.addItem("25");
		expYearList.addItem("26");
		expYearList.addItem("27");
		expiryPanel.add(expYearList);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		cp.add(new HTML("<p>"+constants.checkoutSecurity()+" &nbsp;&nbsp; <img src='img/powered_by_stripe.png' /><br/>"+
				constants.checkoutContactUs()+"</p>"));
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		//Wizard panel
		ForwardButton wb = new ForwardButton(this);
		cp.add(wb);
		
		heartbeat = new HeartbeatWidget();
		cp.add(heartbeat);
	}
	
	public void goForward() {
		//Validation
		String error = "";
		Amount amount = null;
		Date now = new Date();
		try {
			String amountString = amountText.getValue();
			amount = new Amount(amountString);
		} catch (NumberFormatException|SystemException e) {
			error += constants.checkoutErrorAmountFormat()+"<br/>";
		}
		Double amountDouble = amount.getAmountDouble();
		if (amountDouble > WizardSingleton.get().getConfigBean().getDonationMax() ||
				amountDouble < WizardSingleton.get().getConfigBean().getDonationMin(now))
			error += constants.checkoutErrorAmountLimit()+
			" (min &euro;"+ClientConstants.FORMAT_CURRENCY.format(
					WizardSingleton.get().getConfigBean().getDonationMin(now))+
			" max &euro;"+ClientConstants.FORMAT_CURRENCY.format(
					WizardSingleton.get().getConfigBean().getDonationMax())+")";
		if (cardNumberText.getValue().length() < 10 || cardNumberText.getValue().length() > 18)
			error += constants.checkoutErrorCard()+" <br/>";
		//Get data from textBoxes
		if (error.length() > 0) {
			UiSingleton.get().addWarning(error);
		} else {
			attemptPayment(itemNumber, amount, cardNumberText.getValue(),
					expMonthList.getSelectedValue(), expYearList.getSelectedValue());
		}
	}
	
	
	//Async methods
	
	private void attemptPayment(String itemNumber, Amount amount, String cardNumber,
			String expMonth, String expYear) {
		final String fItemNumber = itemNumber;
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(String stripeResult) {
				UiSingleton.get().addWarning(stripeResult);
				WaitSingleton.get().stop();
				
				heartbeat.cancelHeartbeatTimer();
				UriBuilder param = new UriBuilder();
				param.add(AppConstants.PARAM_ID, fItemNumber);
				param.triggerUri(UriDispatcher.STEP_THANK_YOU);
			}
		};
		WaitSingleton.get().start();
		dataService.payWithStripe(itemNumber, amount, cardNumber, expMonth, expYear, callback);
	}
	
}
