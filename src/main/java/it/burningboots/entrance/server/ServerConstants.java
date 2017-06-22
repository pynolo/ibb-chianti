package it.burningboots.entrance.server;

import it.burningboots.entrance.shared.AppConstants;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerConstants {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServerConstants.class);

	
	// ENV
	public static final String MYSQL_DB_USERNAME = "MYSQL_DB_USERNAME";
	public static final String MYSQL_DB_PASSWORD = "MYSQL_DB_PASSWORD";
	public static final String MYSQL_DB_HOST = "MYSQL_DB_HOST";
	public static final String MYSQL_DB_PORT = "MYSQL_DB_PORT";
	public static final String MYSQL_DB_NAME = "APP_NAME";
	public static final String ACCESS_KEY = "ACCESS_KEY";
	
	public static final String PROPERTY_FILE = "/custom.properties";
	public static final String UPLOAD_DIRECTORY = System.getProperty("java.io.tmpdir");
	
	//public static final Charset DEFAULT_FILE_CHARSET = Charset.forName("ISO-8859-15");//Now UTF-8

	public static final SimpleDateFormat FORMAT_DAY = new SimpleDateFormat(AppConstants.PATTERN_DAY);
	public static final SimpleDateFormat FORMAT_YEAR = new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat FORMAT_TIMESTAMP = new SimpleDateFormat(AppConstants.PATTERN_TIMESTAMP);
	public static final SimpleDateFormat FORMAT_FILE_NAME_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	public static final SimpleDateFormat FORMAT_FILE_NAME_DAY = new SimpleDateFormat("yyyy-MM-dd");
	public static final DecimalFormat FORMAT_CURRENCY = new DecimalFormat(AppConstants.PATTERN_CURRENCY);
	public static Date DATE_FAR_PAST;
	public static Date DATE_FAR_FUTURE;
	static {
		try {
			DATE_FAR_PAST = FORMAT_DAY.parse("01/01/1000");
			DATE_FAR_FUTURE = FORMAT_DAY.parse("01/01/3000");
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	//SMTP
	public static final int SMTP_PORT = 587;
	public static final String SMTP_HOST = "smtp.gmx.com";
	public static final String SMTP_FROM = "burningboots@gmx.com";
	public static final boolean SMTP_AUTH = true;
	public static final String SMTP_USERNAME = "burningboots@gmx.com";
	public static final String SMTP_PASSWORD = "boots1234";
	public static final String SMTP_PROTOCOL = "TLS";
	public static final boolean SMTP_DEBUG = true;
	
}
