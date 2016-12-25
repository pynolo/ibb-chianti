package net.tarine.ibbchianti.client.frame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.LocaleConstants;
import net.tarine.ibbchianti.client.UiSingleton;
import net.tarine.ibbchianti.client.UriBuilder;
import net.tarine.ibbchianti.client.UriDispatcher;
import net.tarine.ibbchianti.client.WaitSingleton;
import net.tarine.ibbchianti.client.WizardSingleton;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.entity.WebSession;

public class JoinStart1Frame extends FramePanel {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private int queuePosition = 1000;
	private int confirmed = 1000;
	
	private InlineHTML countLabel = new InlineHTML();
	
	public JoinStart1Frame(UriBuilder params) {
		super();
		saveSessionCookie();
		draw();
		verifyQueueAndStart();
	}
	
	private void verifyQueueAndStart() {
		Timer t = new Timer() {
			public void run() {
				reload();
			}
		};
		// Schedule the timer to run once in 1 minute.
		t.schedule(ClientConstants.WEBSESSION_RELOAD_TIME);
	}

	private void draw() {
		VerticalPanel cp = new VerticalPanel(); //Content panel
		this.add(cp);
		cp.add(new HTML("<p><b>Verifica dei posti disponibili in corso.</b></p><p>&nbsp;</p>"));
		HorizontalPanel waitPanel = new HorizontalPanel();
		cp.add(waitPanel);
		
		waitPanel.add(new HTML("</p>Attualmente sono in coda per iscriversi altre "));
		waitPanel.add(countLabel);
		waitPanel.add(new HTML(" persone. </p><p>&nbsp;</p>"));
		cp.add(new HTML("<p>Attendi per favore e <i>non forzare l'aggiornamento della pagina</i> o perderai la tua posizione in coda.</p>"));
	}
	
	private void controller() {
		if (this.queuePosition < 1) {
			//Forward
			if (confirmed >= AppConstants.PARTICIPANT_MAX) {
				UriBuilder param = new UriBuilder();
				param.triggerUri(UriDispatcher.ERROR_CLOSED);
			} else {
				UriBuilder param = new UriBuilder();
				param.triggerUri(UriDispatcher.STEP_JOIN_START2);
			}
		} else {
			countLabel.setHTML("<b>"+this.queuePosition+"</b>");
		}
	}
	
	private void reload() {
		//Window.Location.reload();
		loadConfirmed();
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
				WizardSingleton.get().setWebSessionId(result.getId());
				Cookies.removeCookie(ClientConstants.WEBSESSION_COOKIE_NAME);
				Cookies.setCookie(ClientConstants.WEBSESSION_COOKIE_NAME, result.getId());
				draw();
				WaitSingleton.get().stop();
			}
		};
		WaitSingleton.get().start();
		dataService.createWebSession(Window.getClientHeight()+" "+Window.getClientWidth(), callback);
	}
	
	private void loadConfirmed() {
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Integer result) {
				setConfirmed(result);
				loadQueuePosition();
				WaitSingleton.get().stop();
			}
		};
		WaitSingleton.get().start();
		dataService.countConfirmed(callback);;
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
		dataService.getQueuePosition(WizardSingleton.get().getWebSessionId(), callback);
	}

	public void setQueuePosition(int queuePosition) {
		this.queuePosition = queuePosition;
	}
	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}
	
}
