package net.tarine.ibbchianti.shared;

import java.io.Serializable;

public class ConfigBean implements Serializable {
	private static final long serialVersionUID = 5717993261825827825L;
	
	//private String version = null;
	private String accessKey = null;
	private int ticketLimit = -1;
	private double donationMin = -1D;
	private double donationMax = -1D;
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

	public int getTicketLimit() {
		return ticketLimit;
	}

	public void setTicketLimit(int ticketLimit) {
		this.ticketLimit = ticketLimit;
	}

	public double getDonationMin() {
		return donationMin;
	}

	public void setDonationMin(double donationMin) {
		this.donationMin = donationMin;
	}

	public double getDonationMax() {
		return donationMax;
	}

	public void setDonationMax(double donationMax) {
		this.donationMax = donationMax;
	}

	public String getStripePublicKey() {
		return stripePublicKey;
	}

	public void setStripePublicKey(String stripePublicKey) {
		this.stripePublicKey = stripePublicKey;
	}

}
