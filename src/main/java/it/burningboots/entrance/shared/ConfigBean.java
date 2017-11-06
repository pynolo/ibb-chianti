package it.burningboots.entrance.shared;

import java.io.Serializable;

public class ConfigBean implements Serializable {
	private static final long serialVersionUID = 5717993261825827825L;
	
	//private String version = null;
	private String accessKey = null;
	//private String stripeTestSecretKey = "x";
	//private String stripeTestPublicKey = "x";
	//private String stripeSecretKey = "x";
	private String stripePublicKey = "x";
	
	public ConfigBean() {
	}
	
//	public String getVersion() {
//		return version;
//	}
//	
//	public void setVersion(String version) {
//		this.version = version;
//	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getStripePublicKey() {
		return stripePublicKey;
	}

	public void setStripePublicKey(String stripePublicKey) {
		this.stripePublicKey = stripePublicKey;
	}
	
}
