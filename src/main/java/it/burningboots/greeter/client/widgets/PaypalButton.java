package it.burningboots.greeter.client.widgets;

import it.burningboots.greeter.shared.AppConstants;

import java.text.DecimalFormat;

import com.google.gwt.user.client.ui.HTML;

public class PaypalButton extends HTML {
	
	private String BUTTON_IMG_URL_IT = "https://www.paypalobjects.com/it_IT/IT/i/btn/btn_buynowCC_LG.gif";
	private String BUTTON_IMG_URL_EN = "https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif";
	private DecimalFormat DM = new DecimalFormat("0.00");
	
	private String paypalPaymentUrl = null;
	private String languageCode = null;
	private String itemNumber = null;
	private Double amount = null;
	private String notifyUrl = null;
	private String returnUrl = null;
	
	//Parametri
	//https://developer.paypal.com/docs/classic/paypal-payments-standard/integration-guide/Appx_websitestandard_htmlvariables/#individual-items-variables

	public PaypalButton(String paypalPaymentUrl, String languageCode, String itemNumber, Double amount,
			String notifyUrl, String returnUrl) {
		super();
		if (languageCode == null) languageCode = "en";
		this.paypalPaymentUrl = paypalPaymentUrl;
		this.languageCode = languageCode;
		this.itemNumber = itemNumber;
		this.amount = amount;
		this.notifyUrl = notifyUrl;
		this.returnUrl = returnUrl;
		draw();
	}

	private void draw() {
		String imgUrl = BUTTON_IMG_URL_EN;
		if (languageCode.equalsIgnoreCase("it")) imgUrl = BUTTON_IMG_URL_IT;
		String amountString = DM.format(amount);
		amountString = amountString.replace(',','.');//Must be in english format

		String html = "<form action=\""+paypalPaymentUrl+"\" method=\"post\" target=\"_top\">";
		html += "<input type=\"hidden\" name=\"cmd\" value=\"_s-xclick\">";
		//Descrizione e importo articolo
		html += "<input type=\"hidden\" name=\"item_name\" value=\"Italian Burning Boots "+itemNumber+"\">";
		html += "<input type=\"hidden\" name=\"item_number\" value=\""+itemNumber+"\">";
		html += "<input type=\"hidden\" name=\"quantity\" value=\"1\">";
		html += "<input type=\"hidden\" name=\"amount\" value=\""+amountString+"\">";
		html += "<input type=\"hidden\" name=\"currency_code\" value=\"EUR\">";
		html += "<input type=\"hidden\" name=\"no_shipping\" value=\"1\">";
		//Notify and return
		html += "<input type=\"hidden\" name=\"address_override\" value=\"1\">";
		html += "<input type=\"hidden\" name=\"notify_url\" value=\""+notifyUrl+"\">";
		html += "<input type=\"hidden\" name=\"return\" value=\""+returnUrl+"\">";
		html += "<input type=\"hidden\" name=\"rm\" value=\"2\">";
		//Configurazione pagina paypal
		html += "<input type=\"hidden\" name=\"image_url\" value=\""+AppConstants.PAYPAL_LOGO_IMG_URL+"\">";
		html += "<input type=\"hidden\" name=\"lc\" value=\""+languageCode+"\">";
		html += "<input type=\"image\" src=\""+imgUrl+"\" border=\"0\" name=\"submit\">";
		html += "<img border=\"0\" src=\"https://www.paypalobjects.com/it_IT/i/scr/pixel.gif\" width=\"1\" height=\"1\">";
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
}
