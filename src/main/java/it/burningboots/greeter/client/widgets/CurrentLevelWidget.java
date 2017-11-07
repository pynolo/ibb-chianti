package it.burningboots.greeter.client.widgets;

import it.burningboots.greeter.client.ClientConstants;
import it.burningboots.greeter.client.ILevelHandler;
import it.burningboots.greeter.client.LocaleConstants;
import it.burningboots.greeter.client.UiSingleton;
import it.burningboots.greeter.client.WaitSingleton;
import it.burningboots.greeter.client.service.DataService;
import it.burningboots.greeter.client.service.DataServiceAsync;
import it.burningboots.greeter.shared.entity.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineHTML;

public class CurrentLevelWidget extends InlineHTML {

	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	private int SCHEDULER_DELAY = 10000;
	
	private ILevelHandler parent = null;
	private Level level = null;
	
	public CurrentLevelWidget() {
		this(null);
	}
	public CurrentLevelWidget(ILevelHandler parent) {
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
	
	public Level getLevel() {
		return level;
	}
	
	private void setLevel(Level value) {
		this.level = value;
		if (parent != null) parent.updateLevel(level);
		this.setHTML("<b>&euro;"+ClientConstants.FORMAT_CURRENCY.format(level.getPrice())+"</b> ("+
				constants.level()+" "+level.getId()+") ");
	}
	
	private void amountUpdater() {
		AsyncCallback<Level> callback = new AsyncCallback<Level>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Level value) {
				WaitSingleton.get().stop();
				setLevel(value);
			}
		};
		WaitSingleton.get().start();
		dataService.getCurrentLevel(callback);
	}
}
