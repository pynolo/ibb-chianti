package it.burningboots.greeter.client;

import it.burningboots.greeter.client.frame.ErrorClosedFrame;
import it.burningboots.greeter.client.frame.ErrorPaymentFrame;
import it.burningboots.greeter.client.frame.ParticipantFrame;
import it.burningboots.greeter.client.frame.StepCheckoutFrame;
import it.burningboots.greeter.client.frame.StepPersonalFrame;
import it.burningboots.greeter.client.frame.StepStartFrame;
import it.burningboots.greeter.client.frame.StepThanksFrame;
import it.burningboots.greeter.client.frame.WarningFrame;

import com.google.gwt.user.client.ui.SimplePanel;

public class UriDispatcher {
	
	public static final String SEPARATOR_TOKEN = "!";// Â£
	public static final String SEPARATOR_PARAMS = "/"; // /
	public static final String SEPARATOR_VALUES = "=";
	
	public static final String STEP_START = "start";
	public static final String STEP_WARNING = "warning";
	public static final String STEP_PERSONAL = "personal";
	public static final String STEP_CHECKOUT = "checkout";
	public static final String ERROR_CLOSED = "errClosed";
	public static final String ERROR_PAYMENT = "errPayment";
	public static final String ERROR_SYSTEM = "errSystem";
	public static final String STEP_THANK_YOU = "thankyou";
	public static final String PARTICIPANTS = "participants";
	public static final String INDEX = STEP_START;
	
	//Reloads the current page from the server reload(true) and not from cache reload(false)
	public static native void hardReload() /*-{
	  $wnd.location.reload(true);
	}-*/;
	
	public static void loadContent(String fullToken) {
		if (fullToken != null) {
			String token = tokenFromUri(fullToken);
			UriBuilder params = paramsFromUri(fullToken);
			SimplePanel contentPanel = UiSingleton.get().getContentPanel();
			contentPanel.clear();
			if (STEP_START.equals(token)) {
				contentPanel.add(new StepStartFrame(params));
			}
			if (STEP_WARNING.equals(token)) {
				contentPanel.add(new WarningFrame(params));
			}
			if (STEP_PERSONAL.equals(token)) {
				contentPanel.add(new StepPersonalFrame(params));
			}
			if (STEP_CHECKOUT.equals(token)) {
				contentPanel.add(new StepCheckoutFrame(params));
			}
			if (ERROR_CLOSED.equals(token)) {
				contentPanel.add(new ErrorClosedFrame(params));
			}
			if (ERROR_PAYMENT.equals(token)) {
				contentPanel.add(new ErrorPaymentFrame(params));
			}
			if (STEP_THANK_YOU.equals(token)) {
				contentPanel.add(new StepThanksFrame(params));
			}
			if (PARTICIPANTS.equals(token)) {
				contentPanel.add(new ParticipantFrame(params));
			}
		}
	}
	
	private static String tokenFromUri(String fullToken) {
		String result = null;
		String[] pieces = fullToken.split(SEPARATOR_TOKEN);
		if (pieces.length >= 1) {
			result = pieces[0];
		}
		return result;
	}
	
	private static UriBuilder paramsFromUri(String fullToken) {
		//Extract only the part after SEPARATOR_TOKEN
		String parameters = null;
		String[] tokenPieces = fullToken.split(SEPARATOR_TOKEN);
		if (tokenPieces.length >= 2) {
			parameters = tokenPieces[1];
		} else {
			return null;
		}
		
		//Extract parameters and put them in a map
		UriBuilder result = new UriBuilder();
		String[] pieces = parameters.split(SEPARATOR_PARAMS);
		if (pieces.length >= 1) {
			for (String piece : pieces) {
				String[] couple = piece.split(SEPARATOR_VALUES);
				if (couple.length >= 2) {
					result.add(couple[0], couple[1]);
				}
			}
		}
		return result;
	}
}
