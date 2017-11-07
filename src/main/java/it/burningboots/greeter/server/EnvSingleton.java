package it.burningboots.greeter.server;

import it.burningboots.greeter.shared.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class EnvSingleton {

	//private static final Logger LOG = LoggerFactory.getLogger(PropertyReader.class);
	private static EnvSingleton instance = null;
	
	private String mysqlUsername = System.getenv(ServerConstants.MYSQL_DB_USERNAME);
	private String mysqlPassword = System.getenv(ServerConstants.MYSQL_DB_PASSWORD);
	private String mysqlHost = System.getenv(ServerConstants.MYSQL_DB_HOST);
	private String mysqlPort = System.getenv(ServerConstants.MYSQL_DB_PORT);
	private String mysqlDbName = System.getenv(ServerConstants.MYSQL_DB_NAME);
	//private String accessKey = null;
	
	private static Properties appProps = null;
	
	private EnvSingleton() throws IOException {
		//URL appPropertyUrl = this.getClass().getResource(AppConstants.APP_PROPERTY_FILE);
		//File appPropertyFile = new File(appPropertyUrl.getPath());
		//appProps = new Properties();
		//appProps.load(new FileInputStream(appPropertyFile));
		URL dbPropertyUrl = this.getClass().getResource(AppConstants.CUSTOM_PROPERTY_FILE);
		if (dbPropertyUrl != null) {
			File dbPropertyFile = new File(dbPropertyUrl.getPath());
			if (dbPropertyFile.exists()) {
				appProps = new Properties();
				appProps.load(new FileInputStream(dbPropertyFile));
			}
		} else {
			URL appPropertyUrl = this.getClass().getResource(AppConstants.APP_PROPERTY_FILE);
			File appPropertyFile = new File(appPropertyUrl.getPath());
			if (appPropertyFile.exists()) {
				appProps = new Properties();
				appProps.load(new FileInputStream(appPropertyFile));
			}
		}
		
		if (mysqlUsername == null)
			mysqlUsername=readProperty(ServerConstants.MYSQL_DB_USERNAME);
		if (mysqlPassword == null)
			mysqlPassword=readProperty(ServerConstants.MYSQL_DB_PASSWORD);
		if (mysqlHost == null)
			mysqlHost=readProperty(ServerConstants.MYSQL_DB_HOST);
		if (mysqlPort == null)
			mysqlPort=readProperty(ServerConstants.MYSQL_DB_PORT);
		if (mysqlDbName == null)
			mysqlDbName=readProperty(ServerConstants.MYSQL_DB_NAME);
//		if (accessKey == null)
//			accessKey=readProperty(ServerConstants.ACCESS_KEY);
	}
	
	public static EnvSingleton get() throws IOException {
		if (instance == null) instance = new EnvSingleton();
		return instance;
	}
	
	public String readProperty(String propertyName) throws IOException {
		String value = null;
		if (appProps != null) {
			value = appProps.getProperty(propertyName);
		} else {
			throw new IOException("Couldn't find '"+AppConstants.APP_PROPERTY_FILE+"'");
		}
		return value;
	}

	public String getMysqlUsername() {
		return mysqlUsername;
	}

	public String getMysqlPassword() {
		return mysqlPassword;
	}

	public String getMysqlHost() {
		return mysqlHost;
	}

	public String getMysqlPort() {
		return mysqlPort;
	}

	public String getMysqlDbName() {
		return mysqlDbName;
	}
	
//	public String getAccessKey() {
//		return accessKey;
//	}
}
