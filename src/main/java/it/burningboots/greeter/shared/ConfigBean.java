package it.burningboots.greeter.shared;

import java.io.Serializable;

public class ConfigBean implements Serializable {
	private static final long serialVersionUID = 5717993261825827825L;
	
	private String basePassword = null;
	private String adminPassword = null;
	//private String stripeTestSecretKey = "x";
	//private String stripeTestPublicKey = "x";
	//private String stripeSecretKey = "x";
	private String stripePublicKey = "x";
	
	public ConfigBean() {
	}
	
	public String getBasePassword() {
		return basePassword;
	}

	public void setBasePassword(String basePassword) {
		this.basePassword = basePassword;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getStripePublicKey() {
		return stripePublicKey;
	}

	public void setStripePublicKey(String stripePublicKey) {
		this.stripePublicKey = stripePublicKey;
	}
	
}
