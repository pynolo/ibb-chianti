package net.tarine.ibbchianti.client;

import com.google.gwt.user.client.ui.SimplePanel;

import net.tarine.ibbchianti.client.frame.ErrorClosedFrame;
import net.tarine.ibbchianti.client.frame.ErrorPaymentFrame;
import net.tarine.ibbchianti.client.frame.JoinCheckoutFrame;
import net.tarine.ibbchianti.client.frame.StepQueueFrame;
import net.tarine.ibbchianti.client.frame.StepPersonalFrame;
import net.tarine.ibbchianti.client.frame.JoinThankYouFrame;
import net.tarine.ibbchianti.client.frame.WarningFrame;
import net.tarine.ibbchianti.client.frame.ParticipantFrame;

public class UriDispatcher {
	
	public static final String SEPARATOR_TOKEN = "!";// Â£
	public static final String SEPARATOR_PARAMS = "/"; // /
	public static final String SEPARATOR_VALUES = "=";
	
	public static final String STEP_JOIN_INDEX = "index";
	public static final String STEP_JOIN_WARNING = "warning";
	public static final String STEP_JOIN_PERSONAL = "legal";
	public static final String STEP_JOIN_CHECKOUT = "checkout";
	public static final String ERROR_CLOSED = "errClosed";
	public static final String ERROR_PAYMENT = "errPayment";
	public static final String STEP_THANK_YOU = "thankyou";
	public static final String PARTICIPANTS = "participants";
	
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
			if (STEP_JOIN_INDEX.equals(token)) {
				contentPanel.add(new StepQueueFrame(params));
			}
			if (STEP_JOIN_WARNING.equals(token)) {
				contentPanel.add(new WarningFrame(params));
			}
			if (STEP_JOIN_PERSONAL.equals(token)) {
				contentPanel.add(new StepPersonalFrame(params));
			}
			if (STEP_JOIN_CHECKOUT.equals(token)) {
				contentPanel.add(new JoinCheckoutFrame(params));
			}
			if (ERROR_CLOSED.equals(token)) {
				contentPanel.add(new ErrorClosedFrame(params));
			}
			if (ERROR_PAYMENT.equals(token)) {
				contentPanel.add(new ErrorPaymentFrame(params));
			}
			if (STEP_THANK_YOU.equals(token)) {
				contentPanel.add(new JoinThankYouFrame(params));
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
