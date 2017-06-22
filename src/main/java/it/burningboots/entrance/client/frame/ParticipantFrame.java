package it.burningboots.entrance.client.frame;

import it.burningboots.entrance.client.AuthSingleton;
import it.burningboots.entrance.client.ClientConstants;
import it.burningboots.entrance.client.CookieSingleton;
import it.burningboots.entrance.client.IAuthenticatedWidget;
import it.burningboots.entrance.client.UriBuilder;
import it.burningboots.entrance.client.widgets.DataModel;
import it.burningboots.entrance.client.widgets.ParticipantTable;
import it.burningboots.entrance.shared.entity.Participant;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ParticipantFrame extends FramePanel implements IAuthenticatedWidget {
	
	private String filterCookie = "1";
	private String orderCookie = "id";
	
	private VerticalPanel panel = null;
	private FlowPanel resultPanel = null;
	
	public ParticipantFrame(UriBuilder params) {
		super();
		if (params == null) {
			params = new UriBuilder();
		}
		String fc = CookieSingleton.get().getCookie(ClientConstants.COOKIE_FILTER_CONFIRMED);
		if (fc != null) {
			if (!fc.equals(""))	filterCookie = fc;
		}
		String oc = CookieSingleton.get().getCookie(ClientConstants.COOKIE_ORDER_BY);
		if (oc != null) {
			if (!oc.equals(""))	orderCookie = oc;
		}
		this.setWidth("100%");
		AuthSingleton.get().queueForAuthentication(this);
	}
	
	@Override
	public void onSuccessfulAuthentication() {
		draw();
	}
	
	private void draw() {
		setTitle("Participants");
		panel = new VerticalPanel();
		this.add(panel);
		// Select
		HorizontalPanel topPanel = new HorizontalPanel();
		// FILTER
		topPanel.add(new HTML("Show:&nbsp;"));
		final ListBox confirmList = new ListBox();
		confirmList.addItem("All","0");
		confirmList.addItem("Confirmed only", "1");
		if (filterCookie.equals("0")) {
			confirmList.setSelectedIndex(0);
		} else {
			confirmList.setSelectedIndex(1);
		}
		confirmList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				filterCookie = confirmList.getSelectedValue();
				CookieSingleton.get().setCookie(ClientConstants.COOKIE_FILTER_CONFIRMED,
						filterCookie);
				refreshTable();
			}
		});
		confirmList.setEnabled(true);
		topPanel.add(confirmList);
		//ORDER BY
		topPanel.add(new HTML("&nbsp;Order:&nbsp;"));
		final ListBox orderList = new ListBox();
		orderList.addItem("Registration date","id");
		orderList.addItem("Last name", "lastName");
		if (orderCookie.equals("lastName")) {
			orderList.setSelectedIndex(1);
		} else {
			orderList.setSelectedIndex(0);
		}
		orderList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				orderCookie = orderList.getSelectedValue();
				CookieSingleton.get().setCookie(ClientConstants.COOKIE_ORDER_BY,
						orderCookie);
				refreshTable();
			}
		});
		orderList.setEnabled(true);
		topPanel.add(orderList);
		
		panel.add(topPanel);
		//Tabella partecipanti
		resultPanel = new FlowPanel();
		panel.add(resultPanel);
		refreshTable();
	}
	
	private void refreshTable() {
		resultPanel.clear();
		//Tabella
		DataModel<Participant> model = new ParticipantTable.ParticipantModel(filterCookie.equals("1"), orderCookie);
		ParticipantTable partTable = new ParticipantTable(model);
		resultPanel.add(partTable);
	}
	
}
