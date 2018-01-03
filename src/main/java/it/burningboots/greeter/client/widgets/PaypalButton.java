package it.burningboots.greeter.client.widgets;

import it.burningboots.greeter.client.UiSingleton;
import it.burningboots.greeter.client.UriDispatcher;
import it.burningboots.greeter.client.service.DataService;
import it.burningboots.greeter.client.service.DataServiceAsync;
import it.burningboots.greeter.shared.AppConstants;
import it.burningboots.greeter.shared.PaypalButtonConfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

public class PaypalButton extends HTML {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	
	private String BUTTON_IMG_URL_IT = "https://www.paypalobjects.com/it_IT/IT/i/btn/btn_buynowCC_LG.gif";
	private String BUTTON_IMG_URL_EN = "https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif";
	private NumberFormat NF = NumberFormat.getFormat("0.00");
	
	private String languageCode = null;
	private String itemNumber = null;
	private Double amount = null;
	private PaypalButtonConfig buttonConfig = new PaypalButtonConfig();
	
	//Parametri
	//https://developer.paypal.com/docs/classic/paypal-payments-standard/integration-guide/Appx_websitestandard_htmlvariables/#individual-items-variables

	public PaypalButton(String languageCode, String itemNumber, Double amount) {
		super();
		if (languageCode == null) languageCode = "en";
		this.languageCode = languageCode;
		this.itemNumber = itemNumber;
		this.amount = amount;
		loadButtonConfig();
	}

	private void draw() {
		String imgUrl = BUTTON_IMG_URL_EN;
		if (languageCode.equalsIgnoreCase("it")) imgUrl = BUTTON_IMG_URL_IT;
		String amountString = NF.format(amount);
		amountString = amountString.replace(',','.');//Must be in english format

		String html = "<form action=\""+buttonConfig.getPaymentUrl()+"\" method=\"post\" target=\"_top\">";
		html += "<input type=\"hidden\" name=\"cmd\" value=\"_xclick\" />";
		html += "<input type=\"hidden\" name=\"business\" value=\""+buttonConfig.getAccount()+"\" />";
		//Descrizione e importo articolo
		html += "<input type=\"hidden\" name=\"item_name\" value=\"Italian Burning Boots "+itemNumber+"\" />";
		html += "<input type=\"hidden\" name=\"item_number\" value=\""+itemNumber+"\" />";
		html += "<input type=\"hidden\" name=\"amount\" value=\""+amountString+"\" />";
		html += "<input type=\"hidden\" name=\"no_shipping\" value=\"1\" />";
		html += "<input type=\"hidden\" name=\"no_note\" value=\"1\" />";
		html += "<input type=\"hidden\" name=\"currency_code\" value=\"EUR\" />";
		//Notify and return
		html += "<input type=\"hidden\" name=\"notify_url\" value=\""+buttonConfig.getNotifyUrl()+"\" />";
		html += "<input type=\"hidden\" name=\"return\" value=\""+buttonConfig.getReturnUrl()+
				UriDispatcher.SEPARATOR_TOKEN+AppConstants.PARAM_ID+
				UriDispatcher.SEPARATOR_VALUES+itemNumber+"\" />";
		//Configurazione pagina paypal
		html += "<input type=\"hidden\" name=\"image_url\" value=\""+buttonConfig.getLogoImgUrl()+"\" />";
		html += "<input type=\"hidden\" name=\"lc\" value=\""+languageCode+"\" />";
		html += "<input type=\"image\" src=\""+imgUrl+"\" border=\"0\" name=\"submit\" />";
		html += "<img border=\"0\" src=\"https://www.paypalobjects.com/it_IT/i/scr/pixel.gif\" width=\"1\" height=\"1\" />";
		html += "</form>";
		this.setHTML(html);
	}
    
	public void updateAmount(Double amount) {
		this.amount = amount;
		draw();
	}
	
	public void updateItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
		draw();
	}
		
	private void loadButtonConfig() {
		AsyncCallback<PaypalButtonConfig> callback = new AsyncCallback<PaypalButtonConfig>() {
			@Override
			public void onFailure(Throwable e) {
				UiSingleton.get().addError(e);
			}
			@Override
			public void onSuccess(PaypalButtonConfig result) {
				buttonConfig = result;
				draw();
			}
		};
		dataService.getPaypalButtonConfig(callback);
	}
}
