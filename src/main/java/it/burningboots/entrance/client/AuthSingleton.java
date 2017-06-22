package it.burningboots.entrance.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

public class AuthSingleton {
		
	private static AuthSingleton instance = null;
	private List<IAuthenticatedWidget> widgetList = null;
	private String verifiedAccessKey = null;
	
	private TextBox accessKeyText = null;
	
	private AuthSingleton() {
		widgetList = new ArrayList<IAuthenticatedWidget>();
	}
	
	public static final AuthSingleton get() {
		if (instance == null) {
			instance = new AuthSingleton();
		}
		return instance;
	}

	public void logout(IAuthenticatedWidget widget) {
		deleteCookie();
		queueForAuthentication(widget);
	}
	
	public void queueForAuthentication(IAuthenticatedWidget widget) {
		if (verifiedAccessKey == null) verifiedAccessKey = "";
		if (!verifiedAccessKey.equals("")) {
			widget.onSuccessfulAuthentication();
		} else {
			widgetList.add(widget);
			if (widgetList.size() <= 1) {
				autorizeByCookieOrPopUp(widgetList);
			}
		}
	}
	
	private void unlockWidgets(List<IAuthenticatedWidget> widgetList) {
		for (IAuthenticatedWidget widget:widgetList) {
			widget.onSuccessfulAuthentication();
		}
		widgetList.clear();
	}
	
	
	//Cookie
	private void autorizeByCookieOrPopUp(List<IAuthenticatedWidget> widgetList) {
		String ak = CookieSingleton.get().getCookie(ClientConstants.COOKIE_ACCESS_KEY);
		authenticateOrPopUp(ak, widgetList);
	}
	private void saveCookie(String ak) {
		CookieSingleton.get().setCookie(ClientConstants.COOKIE_ACCESS_KEY, ak);
	}
	private void deleteCookie() {
		CookieSingleton.get().removeCookie(ClientConstants.COOKIE_ACCESS_KEY);
	}
	
	
	
	// METODI ASINCRONI


	private void authenticateOrPopUp(String userAccessKey,
			List<IAuthenticatedWidget> widgetList) {
		try {
			if (userAccessKey != null) {
				String dbAccessKey = WizardSingleton.get().getConfigBean().getAccessKey();
				verifiedAccessKey = null;
				if (userAccessKey.equals(dbAccessKey)) {
					verifiedAccessKey = userAccessKey;
					saveCookie(userAccessKey);
					unlockWidgets(widgetList);
				} else {
					new AuthPopUp("", widgetList);
				}
			} else {
				new AuthPopUp("", widgetList);
			}
		} catch (Exception e) {
			//Will never be called because Exceptions will be caught by callback
			e.printStackTrace();
		}
	}
	
	
	
	// Inner Classes
	
	
	
	private class AuthPopUp extends PopupPanel {
		
		private List<IAuthenticatedWidget> widgetList = null;
		public String msg = "";
		
		public AuthPopUp(String msg, List<IAuthenticatedWidget> widgetList) {
			super(false);
			this.widgetList = widgetList;
			this.msg=msg;
			init();
		}
		
		private void init() {
			//UI
			this.setModal(true);
			this.setGlassEnabled(true);
			drawForm();
		}
		
		private void drawForm() {
			final FormPanel form = new FormPanel();
			FlexTable table = new FlexTable();
			int r=0;
			
			Image bannerImg = new Image("img/ibb_banner.png");
			table.setWidget(r, 0, bannerImg);
			table.getFlexCellFormatter().setColSpan(r, 0, 2);
			r++;
			
			HTML title = new HTML();
			title.setHTML("<h3>Autenticazione</h3>");
			table.setWidget(r, 0, title);
			table.getFlexCellFormatter().setColSpan(r, 0, 2);
			r++;
			
			//Messaggio eventuale
			HTML message = new HTML(msg);
			message.setStyleName("message-error");
			table.setWidget(r, 0, message);
			table.getFlexCellFormatter().setColSpan(r, 0, 2);
			r++;
			
			//Tipo Anagrafica
			table.setHTML(r, 0, "Access&nbsp;key&nbsp;&nbsp;");
			accessKeyText = new TextBox();
			accessKeyText.setWidth("12em");
			table.setWidget(r, 1, accessKeyText);
			r++;
			
			HorizontalPanel buttonPanel = new HorizontalPanel();
			// Bottone SALVA
			Button submitButton = new Button("Login");
			submitButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					form.submit();
				}
			});
			buttonPanel.add(submitButton);
			
			table.setWidget(r,0,buttonPanel);
			table.getFlexCellFormatter().setColSpan(r, 0, 2);
			
			form.add(table);
			this.add(form);
			form.addSubmitHandler(new SubmitHandler() {
				@Override
				public void onSubmit(SubmitEvent event) {
					close();
					authenticateOrPopUp(accessKeyText.getValue(), widgetList);
				}
			});
			this.center();
			this.show();
		}
		
		public void close() {
			this.hide();
			this.removeFromParent();
		}

	}
	
}
