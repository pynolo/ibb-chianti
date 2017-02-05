package net.tarine.ibbchianti.client.frame;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.LocaleConstants;
import net.tarine.ibbchianti.client.UiSingleton;
import net.tarine.ibbchianti.client.UriBuilder;
import net.tarine.ibbchianti.client.UriDispatcher;
import net.tarine.ibbchianti.client.WaitSingleton;
import net.tarine.ibbchianti.client.WizardSingleton;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.client.widgets.HeartbeatWidget;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.entity.WebSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StepStartFrame extends FramePanel {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private String idWebSession = null;
	private int queuePosition = 1000;
	private int confirmed = 1000;
	
	private InlineHTML countLabel = new InlineHTML();
	
	public StepStartFrame(UriBuilder params) {
		super();
		saveSessionCookie();
	}
	
	private void verifyQueueAndStart() {
		reload();
		Timer t = new Timer() {
			public void run() {
				reload();
			}
		};
		// Schedule the timer to run once in 1 minute.
		t.schedule(AppConstants.QUEUE_RELOAD_TIME);
	}

	private void draw() {
		VerticalPanel cp = new VerticalPanel(); //Content panel
		this.add(cp);
		cp.add(new HTML("<p><b>"+constants.queueVerify()+"</b></p><p>&nbsp;</p>"));
		HorizontalPanel waitPanel = new HorizontalPanel();
		cp.add(waitPanel);
		
		waitPanel.add(new HTML(constants.queueCurrentlyOnline()));
		waitPanel.add(countLabel);
		waitPanel.add(new HTML(constants.queuePersons()+". </p><p>&nbsp;</p>"));

		HeartbeatWidget heartbeat = new HeartbeatWidget(idWebSession);
		cp.add(heartbeat);
	}
	
	private void controller() {
		if (this.queuePosition < 1) {
			//Forward
			if (confirmed >= WizardSingleton.get().getConfigBean().getTicketLimit()) {
				UriBuilder param = new UriBuilder();
				param.triggerUri(UriDispatcher.ERROR_CLOSED);
			} else {
				UriBuilder param = new UriBuilder();
				param.triggerUri(UriDispatcher.STEP_PERSONAL);
			}
		} else {
			countLabel.setHTML("<b>"+this.queuePosition+"</b>");
		}
	}
	
	private void reload() {
		//Window.Location.reload();
		loadConfirmedParticipants();
	}

	
	//Async methods
	
	private void saveSessionCookie() {
		AsyncCallback<WebSession> callback = new AsyncCallback<WebSession>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(WebSession result) {
				idWebSession = result.getId();
				Cookies.removeCookie(ClientConstants.WEBSESSION_COOKIE_NAME);
				Cookies.setCookie(ClientConstants.WEBSESSION_COOKIE_NAME, result.getId());
				WaitSingleton.get().stop();
				
				draw();
				verifyQueueAndStart();
			}
		};
		WaitSingleton.get().start();
		dataService.createWebSession(Window.getClientHeight()+" "+Window.getClientWidth(), callback);
	}
	
	private void loadConfirmedParticipants() {
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Integer result) {
				setConfirmedParticipants(result);
				loadQueuePosition();
				WaitSingleton.get().stop();
			}
		};
		WaitSingleton.get().start();
		dataService.countConfirmed(callback);
	}
	
	private void loadQueuePosition() {
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Integer result) {
				setQueuePosition(result);
				controller();
				WaitSingleton.get().stop();
			}
		};
		WaitSingleton.get().start();
		dataService.getQueuePosition(idWebSession, callback);
	}

	public void setQueuePosition(int queuePosition) {
		this.queuePosition = queuePosition;
	}
	public void setConfirmedParticipants(int confirmed) {
		this.confirmed = confirmed;
	}
	
}
