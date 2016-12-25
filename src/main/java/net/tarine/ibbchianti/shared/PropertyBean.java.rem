package net.tarine.ibbchianti.shared;

import java.io.Serializable;

public class PropertyBean implements Serializable {
	private static final long serialVersionUID = 5717993261825827825L;
	
	private String version = null;
	private String accessKey = null;
	private boolean closed = true;
	private int totalMax = -1;
	private int hutMax = -1;
	private double hutPrice = -1D;
	private double hutPriceLow = -1D;
	private int tentMax = -1;
	private double tentPrice = -1D;
	private double tentPriceLow = -1D;
	
	private int hutCount = -1;
	private int tentCount = -1;
	
	public PropertyBean() {
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean getClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public int getTotalMax() {
		return totalMax;
	}
	public void setTotalMax(String totalMax) {
		this.totalMax = Integer.parseInt(totalMax);
	}
	public int getHutMax() {
		return hutMax;
	}
	public void setHutMax(String hutMax) {
		this.hutMax = Integer.parseInt(hutMax);
	}
	public int getTentMax() {
		return tentMax;
	}
	public void setTentMax(String tentMax) {
		this.tentMax = Integer.parseInt(tentMax);
	}
	public double getHutPrice() {
		return hutPrice;
	}
	public void setHutPrice(String hutPrice) {
		this.hutPrice = Double.parseDouble(hutPrice);
	}
	public double getTentPrice() {
		return tentPrice;
	}
	public void setTentPrice(String tentPrice) {
		this.tentPrice =  Double.parseDouble(tentPrice);
	}
	public int getHutCount() {
		return hutCount;
	}
	public void setHutCount(int hutCount) {
		this.hutCount = hutCount;
	}
	public int getTentCount() {
		return tentCount;
	}
	public void setTentCount(int tentCount) {
		this.tentCount = tentCount;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public double getHutPriceLow() {
		return hutPriceLow;
	}
	public void setHutPriceLow(String hutPriceLow) {
		this.hutPriceLow = Double.parseDouble(hutPriceLow);
	}
	public double getTentPriceLow() {
		return tentPriceLow;
	}
	public void setTentPriceLow(String tentPriceLow) {
		this.tentPriceLow = Double.parseDouble(tentPriceLow);
	}

	public int getAvailableHut() {
		int a1 = hutMax-hutCount;
		int a2 = totalMax-(hutCount+tentCount);
		if (a1 < a2) return a1;
		return a2;
	}
	
	public int getAvailableTent() {
		int a1 = tentMax-tentCount;
		int a2 = totalMax-(hutCount+tentCount);
		if (a1 < a2) return a1;
		return a2;
	}
}
