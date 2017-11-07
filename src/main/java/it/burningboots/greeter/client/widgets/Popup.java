package it.burningboots.greeter.client.widgets;

import it.burningboots.greeter.client.ClientConstants;
import it.burningboots.greeter.client.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Popup extends PopupPanel {
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	private Button button = null;
	
	public Popup(String message) {
		// PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
		// If this is set, the panel closes itself automatically when the user
		// clicks outside of it.
		super(true);
		// PopupPanel is a SimplePanel, so you have to set it's widget property
		// to
		// whatever you want its contents to be.
		final PopupPanel pop = this;
		VerticalPanel vp = new VerticalPanel();
		vp.add(new HTML(ClientConstants.MSG_ICON_WARN+" <b>"+constants.warning()+"</b>"));
		vp.add(new HTML(message));
		button = new Button("&nbsp;OK&nbsp;");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pop.hide();
			}
		});
		vp.add(button);
		setWidget(vp);
	}

	public Popup(Throwable e) {
		super(true);
		final PopupPanel pop = this;
		VerticalPanel vp = new VerticalPanel();
		vp.add(new HTML(ClientConstants.MSG_ICON_ERROR+" <b>"+constants.error()+"</b>"));
		vp.add(new HTML(e.getMessage()));
		button = new Button("&nbsp;OK&nbsp;");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pop.hide();
			}
		});
		vp.add(button);
		setWidget(vp);
	}

	public void centerAndShow() {
		final PopupPanel pop = this;
		pop.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 2;
				pop.setPopupPosition(left, top);
			}
		});
	}
	
}
