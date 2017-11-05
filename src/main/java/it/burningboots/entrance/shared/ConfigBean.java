package it.burningboots.entrance.shared;

import it.burningboots.entrance.shared.entity.Level;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ConfigBean implements Serializable {
	private static final long serialVersionUID = 5717993261825827825L;
	
	//private String version = null;
	private String accessKey = null;
	//private int ticketLimit = -1;
	//private double donationMin = -1D;
	private double donationMax = -1D;
	//private String stripeTestSecretKey = "x";
	//private String stripeTestPublicKey = "x";
	//private String stripeSecretKey = "x";
	private String stripePublicKey = "x";
	private List<Level> levelList = null;
	
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

	public double getDonationMax() {
		return donationMax;
	}

	public void setDonationMax(double donationMax) {
		this.donationMax = donationMax;
	}

	public double getDonationMin(Date dt) {
		
	}
	
	public List<Level> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<Level> levelList) {
		this.levelList = levelList;
	}

	public String getStripePublicKey() {
		return stripePublicKey;
	}

	public void setStripePublicKey(String stripePublicKey) {
		this.stripePublicKey = stripePublicKey;
	}
	
}
