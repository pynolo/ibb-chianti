package net.tarine.ibbchianti.client.frame;

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
import net.tarine.ibbchianti.shared.Amount;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.ConfigBean;
import net.tarine.ibbchianti.shared.SystemException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StepCheckoutFrame extends FramePanel implements IWizardFrame {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private UriBuilder params = null;
	private String itemNumber = null;
	private VerticalPanel cp = null; // Content panel
	
	private TextBox amountText;
	private TextBox cardNumberText;
	private TextBox expMonthText;
	private TextBox expYearText;
	
	public StepCheckoutFrame(UriBuilder params) {
		super();
		if (params != null) {
			this.params = params;
		} else {
			this.params = new UriBuilder();
		}
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
		
		//TITLE
		setTitle(constants.checkoutTitle());
		
		cp.add(new HTML("<p>"+constants.checkoutIntro()+"</p>"));

		//EMAIL
		cp.add(new HTML("<p>"+constants.checkoutDonationAmount()+"</p>"));
		HorizontalPanel emailPanel = new HorizontalPanel();
		amountText = new ExtendedTextBox();
		amountText.setValue(ClientConstants.FORMAT_CURRENCY.format(cb.getDonationMin()));
		emailPanel.add(amountText);
		emailPanel.add(new InlineHTML("&nbsp;&nbsp;"));
		cp.add(emailPanel);
		cp.add(new HTML("<p><i>"+constants.checkoutDonationMinimumDesc()+" &euro;"+
				ClientConstants.FORMAT_CURRENCY.format(cb.getDonationMin())+"</i></p>"));
		
		HorizontalPanel cardPanel = new HorizontalPanel();
		cp.add(cardPanel);

		FlowPanel cardNumberPanel = new FlowPanel();
		cardNumberPanel.add(new HTML(constants.checkoutCardNumber()));
		cardNumberText = new TextBox();
		cardNumberText.setMaxLength(64);
		cardNumberText.setValue("");
		cardNumberPanel.add(cardNumberText);
		cardPanel.add(cardNumberPanel);
		
//		cardPanel.add(new InlineHTML("&nbsp;"));
//		
//		FlowPanel lastNamePanel = new FlowPanel();
//		lastNamePanel.add(new HTML(constants.personalLastName()));
//		lastNameText = new TextBox();
//		lastNameText.setMaxLength(64);
//		lastNameText.setValue(participant.getLastName());
//		lastNamePanel.add(lastNameText);
//		cardPanel.add(lastNamePanel);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		HorizontalPanel expiryPanel = new HorizontalPanel();
		cp.add(expiryPanel);
		
		expiryPanel.add(new HTML("<p>"+constants.checkoutExpiration()+"</p>"));
		expMonthText = new TextBox();
		expMonthText.setMaxLength(2);
		expMonthText.setWidth("2em");
		expMonthText.setValue("00");
		expiryPanel.add(expMonthText);
		
		expiryPanel.add(new InlineHTML("/"));
		
		expYearText = new TextBox();
		expYearText.setMaxLength(2);
		expYearText.setWidth("2em");
		expYearText.setValue("00");
		expiryPanel.add(expYearText);
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		cp.add(new HTML("<p>"+constants.checkoutContactUs()+"</p>"));
		
		cp.add(new HTML("<p>&nbsp;</p>"));
		
		//Wizard panel
		ForwardButton wb = new ForwardButton(this);
		cp.add(wb);
	}
	
	public void goForward() {
		//Validation
		String error = "";
		Amount amount = null;
		try {
			String amountString = amountText.getValue();
			amount = new Amount(amountString);
		} catch (NumberFormatException|SystemException e) {
			error += constants.checkoutErrorAmountFormat()+"<br/>";
		}
		if (amount.getAmountDouble() > WizardSingleton.get().getConfigBean().getDonationMax() ||
				amount.getAmountDouble() < WizardSingleton.get().getConfigBean().getDonationMin())
			error += constants.checkoutErrorAmountLimit();
		if (cardNumberText.getValue().length() < 10 || cardNumberText.getValue().length() > 18)
			error += constants.checkoutErrorCard()+" <br/>";
		if (expMonthText.getValue().length() != 2 || expYearText.getValue().length() != 2)
			error += constants.checkoutErrorExp()+"<br/>";
		//Get data from textBoxes
		if (error.length() > 0) {
			UiSingleton.get().addWarning(error);
		} else {
			attemptPayment(itemNumber, amount, cardNumberText.getValue(),
					expMonthText.getValue(), expYearText.getValue());
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
				UriBuilder param = new UriBuilder();
				param.add(AppConstants.PARAM_ID, fItemNumber);
				param.triggerUri(UriDispatcher.STEP_THANK_YOU);
			}
		};
		WaitSingleton.get().start();
		dataService.payWithStripe(itemNumber, amount, cardNumber, expMonth, expYear, callback);
	}
	
}
