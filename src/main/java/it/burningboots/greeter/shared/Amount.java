package it.burningboots.greeter.shared;

import java.io.Serializable;

public class Amount implements Serializable {
	private static final long serialVersionUID = 7637565767663436L;
	
	private Long amount = null;
	
	public Amount() {
		this.amount = null;
	}
	
	public Amount(Long amount) {
		setAmount(amount);
	}
	
	public Amount(Double amount) throws SystemException {
		setAmount(amount);
	}
	
	public Amount(String amount) throws SystemException {
		setAmount(amount);
	}
	
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	
	public void setAmount(Double amount) throws SystemException {
		try {
			Long intPart = new Double(Math.floor(amount)).longValue();
			Long fracPart = new Double(Math.round(amount*100)-Math.floor(amount)).longValue();
			this.amount = fracPart+100*intPart;
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	public void setAmount(String amount) throws SystemException {
		amount = amount.replaceAll(",", "\\.");//Non deve essere nel formato italiano
		try {
			String[] parts = amount.split("\\.");
			Long intPart = Long.parseLong(parts[0]);
			Long fracPart = Long.parseLong(parts[1].substring(0, 2));
			this.amount = fracPart+100*intPart;
		} catch (NumberFormatException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	public Long getAmountLong() {
		return amount;
	}
	
	public Double getAmountDouble() {
		Double amountDouble = amount/100D;
		return amountDouble;
	}

}
