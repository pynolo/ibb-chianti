package net.tarine.ibbchianti.client;

import com.google.gwt.i18n.client.Constants;

public interface LocaleConstants extends Constants {
	String locale();
	String pleaseWait();
	String forward();
	String warning();
	String error();
	
	String queueVerify();
	String queueCurrentlyOnline();
	String queuePersons();
	
	String personalTitle();
	String personalIntro();
	String personalEmail();
	String personalEmailWarning();
	String personalFirstName();
	String personalLastName();
//	String personalBirthCity();
//	String personalBirthDate();
	String personalErrorCity();
	String personalErrorDate();
	
	
	String joinBaseTitle();
	String joinBaseWelcome();

	String joinBaseAccommodation();
	String joinBaseToSoldOut();
	String joinBaseBedFeatures();
	String joinBaseTentFeatures();

	String joinCheckoutTitle();
	String joinCheckoutOneMoreStep();
	String joinCheckoutPleaseConfirm();
	String joinCheckoutMinimumAmount();
	String joinCheckoutContactUs();
	String joinCheckoutDonateButton();
	String joinCheckoutMinimum();


	String joinThankYouTitle();
	String joinThankYouDonation();
	String joinThankYouConfirmed();
	String joinThankYouTakeNote();
	String joinThankYouWhatIs();
	String joinThankYouTellSomeone();
	String joinThankYouGoToForum();
	String joinThankYouReceiveEmail();

	String errorPaymentTitle();
	String errorPaymentInfo();
	String errorPaymentDontWorry();
	String errorFullTitle();
	String errorFullInfo();
	String errorFullDontWorry();
	String errorClosedTitle();
	String errorClosedInfo();
	String errorClosedAsk();

}
