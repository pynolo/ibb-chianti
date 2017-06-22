package it.burningboots.entrance.client;

import com.google.gwt.i18n.client.Constants;

public interface LocaleConstants extends Constants {
	String locale();
	String pleaseWait();
	String forward();
	String warning();
	String error();
	String itemNumber();
	
	String queueTitle();
	String queueVerify();
	String queueCurrentlyOnline1();
	String queueCurrentlyOnline2();
	String queuePleaseWait();
	
	String personalTitle();
	String personalIntro1();
	String personalIntro2();
	String personalIntroWarning();
	String personalEmail();
	String personalEmailWarning();
	String personalFirstName();
	String personalLastName();
	String personalBirthCity();
	String personalBirthDate();
	String personalErrorCity();
	String personalErrorDate();
	String personalPrivacy();

	String checkoutTitle();
	String checkoutIntro();
	String checkoutDonationAmount();
	String checkoutDonationMinimumDesc();
	String checkoutCardNumber();
	String checkoutExpiration();
	String checkoutSecurity();
	String checkoutContactUs();
	String checkoutErrorAmountFormat();
	String checkoutErrorAmountLimit();
	String checkoutErrorCard();
	String checkoutErrorExp();
	
	String thanksTitle();
	String thanksDonation();
	String thanksConfirmed();
	String thanksTakeNote();
	String thanksWhatIs();
	String thanksTellSomeone();
	String thanksGoToForum();
	String thanksReceiveEmail1();
	String thanksReceiveEmail2();

	String errorPaymentTitle();
	String errorPaymentInfo();
	String errorPaymentDontWorry();
	String errorClosedTitle();
	String errorClosedInfo();
	String errorClosedAsk();
	String errorSystemTitle();
	String errorSystemInfo();
	
}
