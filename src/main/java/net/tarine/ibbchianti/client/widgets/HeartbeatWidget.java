package net.tarine.ibbchianti.client.widgets;

import java.util.Date;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.CookieSingleton;
import net.tarine.ibbchianti.client.UiSingleton;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.shared.AppConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineHTML;

public class HeartbeatWidget extends InlineHTML {

	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private String idWebSession = null;
	Timer timer = null;
	
	public HeartbeatWidget() {
		super();
		this.idWebSession=CookieSingleton.get().getCookie(ClientConstants.WEBSESSION_COOKIE_NAME);
		startHeartbeatTimer();
	}
	
	private void startHeartbeatTimer() {
		sendHeartbeat();
		timer = new Timer() {
			public void run() {
				sendHeartbeat();
			}
		};
		// Schedule the timer to run once in 1 minute.
		timer.scheduleRepeating(AppConstants.HEARTBEAT_RELOAD_TIME);
	}
	
	public void cancelHeartbeatTimer() {
		if (timer != null) {
			if (timer.isRunning()) {
				timer.cancel();
				if (timer.isRunning())
					UiSingleton.get().addWarning("I couldn't cancel Heartbeat timer");
			}
		}
	}
	
	private void updateValue(Date date) {
		this.setHTML(ClientConstants.FORMAT_TIMESTAMP.format(date));
	}
	
	private void sendHeartbeat() {
		AsyncCallback<Date> callback = new AsyncCallback<Date>() {
			@Override
			public void onFailure(Throwable e) {
				UiSingleton.get().addError(e);
			}
			@Override
			public void onSuccess(Date result) {
				updateValue(result);
			}
		};
		dataService.updateHeartbeat(this.idWebSession, callback);
	}
	
}
