package it.burningboots.entrance.client.widgets;


import it.burningboots.entrance.client.ClientConstants;
import it.burningboots.entrance.client.IAmountHandler;
import it.burningboots.entrance.client.UiSingleton;
import it.burningboots.entrance.client.WaitSingleton;
import it.burningboots.entrance.client.service.DataService;
import it.burningboots.entrance.client.service.DataServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineHTML;

public class DonationAmountWidget extends InlineHTML {

	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private int SCHEDULER_DELAY = 10000;
	
	private IAmountHandler parent = null;
	private double amount = 0D;
	
	public DonationAmountWidget() {
		this(null);
	}
	public DonationAmountWidget(IAmountHandler parent) {
		this.parent = parent;
		// Create a new timer that calls Window.alert().
		Timer t = new Timer() {
			public void run() {
				amountUpdater();
			}
		};
		// Schedule the timer to run once in 5 seconds.
		t.schedule(SCHEDULER_DELAY);
		amountUpdater();
	}
	
	public double getAmount() {
		return amount;
	}
	
	private void setAmount(Double value) {
		this.amount = value;
		if (parent != null) parent.updateAmount(amount);
		this.setHTML("<b>&euro;"+ClientConstants.FORMAT_CURRENCY.format(amount)+"</b>");
	}
	
	private void amountUpdater() {
		AsyncCallback<Double> callback = new AsyncCallback<Double>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Double value) {
				WaitSingleton.get().stop();
				setAmount(value);
			}
		};
		WaitSingleton.get().start();
		dataService.getDonationMin(callback);
	}
}
