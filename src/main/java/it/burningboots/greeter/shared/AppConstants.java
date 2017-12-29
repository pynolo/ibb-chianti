package it.burningboots.greeter.shared;

public class AppConstants {

	//MAIN CONFIG
	public static final int ITEM_NUMBER_LENGHT = 6;
	public static final String EVENT_URL = "https://burningboots.it";
	public static final double DONATION_MAX = 200D;
	
	//WebSession
	public static int QUEUE_RELOAD_TIME = 1000*30; //30 seconds
	public static int QUEUE_MAX_LENGTH = 10; //10 persons
	public static int HEARTBEAT_RELOAD_TIME = 1000*20; //20 seconds
	public static long HEARTBEAT_TTL = 1000*60*5; //5 minutes
	public static long WEBSESSION_TTL = 1000*60*15; //15 minutes
	
	// IPN
	// GUIDE https://developer.paypal.com/docs/classic/ipn/integration-guide/IPNSetup/
	public static final String PAYPAL_THANKYOU_URL = "https://burningboots.it/greeter#thankyou";
	public static final String PAYPAL_LOGO_IMG_URL = "https://burningboots.it/images/ibb_title_purple.png";
	public static final String PAYPAL_IPN_URL = "https://burningboots.it/greeter/ipn";//TO BE SET ON PAYPAL TOO https://www.paypal.com/cgi-bin/customerprofileweb?cmd=_profile-ipn-notify
	//public static final String PAYPAL_URL = "https://www.paypal.com/cgi-bin/webscr"; //PRODUCTION
	public static final String PAYPAL_URL= "https://www.sandbox.paypal.com/cgi-bin/webscr";//SANDBOX
	//public static final String PAYPAL_ACCOUNT = "itburningboots@gmail.com"; //PRODUCTION
	public static final String PAYPAL_ACCOUNT = "itburningboots-facilitator@gmail.com";//SANDBOX
	//BUYER TEST ACCOUNT paolo-buyer@tarine.net
	
	// CONFIG TABLE
	public static final String CONFIG_BASE_PASSWORD = "basePassword";
	public static final String CONFIG_ADMIN_PASSWORD = "adminPassword";
	//public static final String CONFIG_STRIPE_SECRET_KEY = "stripeSecretKey";
	//public static final String CONFIG_STRIPE_PUBLIC_KEY = "stripePublicKey";
	
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
