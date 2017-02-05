package net.tarine.ibbchianti.shared;

public class AppConstants {

	//MAIN CONFIG
	public static final int ITEM_NUMBER_LENGHT = 6;
	public static final String EVENT_URL = "https://burningboots.it";
	
	//WebSession
	public static long WEBSESSION_TTL = 1000*60*15; //15 minutes
	public static int QUEUE_RELOAD_TIME = 1000*30; //30 seconds
	public static int QUEUE_MAX_LENGTH = 10; //30 seconds
	public static long HEARTBEAT_TTL = 1000*60*5; //5 minutes
	public static int HEARTBEAT_RELOAD_TIME = 1000*20; //20 seconds
	
//	// IPN
//	// GUIDE https://developer.paypal.com/docs/classic/ipn/integration-guide/IPNSetup/
//	public static final String BASE_URL = "https://burningboots.it/appennino";
//	public static final String IPN_URL = "https://burningboots.it/appennino/ipn";//TO BE SET ON PAYPAL TOO https://www.paypal.com/cgi-bin/customerprofileweb?cmd=_profile-ipn-notify
//	public static final String PAYPAL_URL = "https://www.paypal.com/cgi-bin/webscr"; //PRODUCTION
//	//public static final String PAYPAL_URL= "https://www.sandbox.paypal.com/cgi-bin/webscr";//SANDBOX
//	public static final String PAYPAL_ACCOUNT = "dragolar@gmail.com"; //PRODUCTION
//	//public static final String PAYPAL_ACCOUNT = "wave-facilitator@tarine.net";//SANDBOX pw low level
//	//BUYER TEST ACCOUNT paolo-buyer@tarine.net pw low level
	
	// CONFIG TABLE
	public static final String CONFIG_ACCESS_KEY = "accessKey";
	public static final String CONFIG_TICKET_LIMIT = "ticketLimit";
	public static final String CONFIG_DONATION_MIN = "donationMin";
	public static final String CONFIG_DONATION_MAX = "donationMax";
	public static final String CONFIG_STRIPE_SECRET_KEY = "stripeSecretKey";
	public static final String CONFIG_STRIPE_PUBLIC_KEY = "stripePublicKey";
	
	// CONFIG FILES
	public static final String HIBERNATE_CONFIG_FILE="/hibernate.cfg.xml";
	public static final String APP_PROPERTY_FILE = "/app.version";
	public static final String CUSTOM_PROPERTY_FILE = "/custom.properties";
	
	// PARAMS
	public static final String PARAM_ID = "id";
	
	// FORMATS
	public static final String PATTERN_TIMESTAMP = "dd/MM/yyyy HH:mm:ss";//"dd/MM/yyyy HH:mm";
	public static final String PATTERN_DAY = "dd/MM/yyyy";
	public static final String PATTERN_MONTH = "MM/yyyy";
	public static final String PATTERN_CURRENCY = "#0.00";
	public static final long HOUR = 3600000L;
	public static final long DAY = HOUR*24;
	public static final long MONTH = DAY*30; //millisecondi in 30 giorni 1000 * 60 * 60 * 24 * 30;
	public static final long YEAR = DAY*365; 

}
