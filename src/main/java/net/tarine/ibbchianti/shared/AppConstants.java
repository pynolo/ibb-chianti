package net.tarine.ibbchianti.shared;

import java.util.HashMap;
import java.util.Map;



public class AppConstants {

	//WebSession
	public static long WEBSESSION_TTL = 1000*60*30; //30 minutes

	// IPN
	// GUIDE https://developer.paypal.com/docs/classic/ipn/integration-guide/IPNSetup/
	public static final String BASE_URL = "https://burningboots.it/appennino";
	public static final String IPN_URL = "https://burningboots.it/appennino/ipn";//TO BE SET ON PAYPAL TOO https://www.paypal.com/cgi-bin/customerprofileweb?cmd=_profile-ipn-notify
	public static final String EVENT_URL = "https://burningboots.it";
	public static final String PAYPAL_URL = "https://www.paypal.com/cgi-bin/webscr"; //PRODUCTION
	//public static final String PAYPAL_URL= "https://www.sandbox.paypal.com/cgi-bin/webscr";//SANDBOX
	public static final String PAYPAL_ACCOUNT = "dragolar@gmail.com"; //PRODUCTION
	//public static final String PAYPAL_ACCOUNT = "wave-facilitator@tarine.net";//SANDBOX pw low level
	//BUYER TEST ACCOUNT paolo-buyer@tarine.net pw low level
	public static final int ITEM_NUMBER_LENGHT = 6;
	
	public static final String CONFIG_MAX_TICKET_COUNT = "maxTicketCount";
	public static final String CONFIG_MAX_FOREIGNER_TICKET_COUNT = "maxForeignTicketCount";
	public static final String CONFIG_PRICE_TICKET = "priceFull";
	public static final String CONFIG_PRICE_FOREIGNER_TICKET = "priceForeign";
	public static final String CONFIG_PRICE_REDUCED_TICKET = "priceReduced";
	public static final String CONFIG_SERVICE_OPEN = "serviceOpen";
	public static final String CONFIG_ACCESS_KEY = "accessKey";
	
	// CONFIG FILES
	public static final String HIBERNATE_CONFIG_FILE="/hibernate.cfg.xml";
	public static final String APP_PROPERTY_FILE = "/app.properties";
	public static final String CUSTOM_PROPERTY_FILE = "/custom.properties";
	
	// PARAMS
	public static final String PARAM_ID = "id";
	public static final String PARAMS_ITEM_NUMBER = "itemNumber";
	
	// VALUES
	//WIZARD
	public static final Integer WIZARD_REGISTER = 1;
	public static final Integer WIZARD_TRANSFER = 2;
	//ACCOMMODATION
	public static final Integer ACCOMMODATION_HUT = 1;
	public static final Integer ACCOMMODATION_TENT = 2;
	public static final Map<Integer, String> ACCOMMODATION_DESC = new HashMap<Integer, String>();
	static {
		ACCOMMODATION_DESC.put(ACCOMMODATION_HUT, "Hut / Rifugio");
		ACCOMMODATION_DESC.put(ACCOMMODATION_TENT, "Tent / Tenda");};
	//VOLUNTEER
	public static final String VOLUNTEER_KITCHEN = "Kitchen";
	public static final String VOLUNTEER_GREETER = "Greeter";
	public static final String VOLUNTEER_WOOD = "Wood";
	public static final String VOLUNTEER_LNT = "LNTrace";
	public static final String VOLUNTEER_DECO = "Deco";
	
	// FORMATS
	public static final String PATTERN_TIMESTAMP = "dd/MM/yyyy HH:mm";//"dd/MM/yyyy HH:mm";
	public static final String PATTERN_DAY = "dd/MM/yyyy";
	public static final String PATTERN_MONTH = "MM/yyyy";
	public static final String PATTERN_CURRENCY = "#0.00";
	public static final long HOUR = 3600000L;
	public static final long DAY = HOUR*24;
	public static final long MONTH = DAY*30; //millisecondi in 30 giorni 1000 * 60 * 60 * 24 * 30;
	public static final long YEAR = DAY*365; 

	// LOOKUP: RESALE TYPE
	public static final String RESALE_TYPE_SELLING = "SELL";
	public static final String RESALE_TYPE_BUYING = "BUY";

}
