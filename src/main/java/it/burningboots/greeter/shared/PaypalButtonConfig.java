package it.burningboots.greeter.shared;

import java.io.Serializable;

public class PaypalButtonConfig implements Serializable {
	private static final long serialVersionUID = -2957201121881019099L;
	
	private String account = null;
	private String paymentUrl = null;
	private String notifyUrl = null;
	private String returnUrl = null;
	private String logoImgUrl = null;
	
	public PaypalButtonConfig() {
		this.account="";
		this.paymentUrl = "";
		this.notifyUrl = "";
		this.returnUrl = "";
		this.logoImgUrl = "";
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPaymentUrl() {
		return paymentUrl;
	}
	
	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public String getReturnUrl() {
		return returnUrl;
	}
	
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
	public String getLogoImgUrl() {
		return logoImgUrl;
	}
	
	public void setLogoImgUrl(String logoImgUrl) {
		this.logoImgUrl = logoImgUrl;
	}
	
}
