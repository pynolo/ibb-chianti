package net.tarine.ibbchianti.shared;

import java.io.Serializable;

public class ConfigBean implements Serializable {
	private static final long serialVersionUID = 5717993261825827825L;
	
	private String version = null;
	private String accessKey = null;
	private int ticketMax = -1;
	private double ticketPrice = -1D;
	private String stripeKey = "x";
	
	public ConfigBean() {
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getStripeKey() {
		return stripeKey;
	}

	public void setStripeKey(String stripeKey) {
		this.stripeKey = stripeKey;
	}

	public int getTicketMax() {
		return ticketMax;
	}

	public void setTicketMax(int ticketMax) {
		this.ticketMax = ticketMax;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
}
