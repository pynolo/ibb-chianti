package net.tarine.ibbchianti.client.widgets;

import java.util.Date;

import net.tarine.ibbchianti.client.ClientConstants;
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
	
	public HeartbeatWidget(String idWebSession) {
		super();
		this.idWebSession=idWebSession;
		startHeartbeatTimer();
	}
	
	private void startHeartbeatTimer() {
		sendHeartbeat();
		Timer t = new Timer() {
			public void run() {
				sendHeartbeat();
			}
		};
		// Schedule the timer to run once in 1 minute.
		t.schedule(AppConstants.HEARTBEAT_RELOAD_TIME);
	}
	
	private void updateValue(Date date) {
		this.setHTML(ClientConstants.FORMAT_TIMESTAMP.format(date));
	}
	
	private void sendHeartbeat() {
		AsyncCallback<Date> callback = new AsyncCallback<Date>() {
			@Override
			public void onFailure(Throwable e) {
				UiSingleton.get().addError(e);
				UiSingleton.get().addWarning("Try to reload the page please");
			}
			@Override
			public void onSuccess(Date result) {
				updateValue(result);
			}
		};
		dataService.updateHeartbeat(idWebSession, callback);
	}
	
}
