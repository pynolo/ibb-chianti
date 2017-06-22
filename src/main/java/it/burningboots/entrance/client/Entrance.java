package it.burningboots.entrance.client;

import it.burningboots.entrance.client.service.DataService;
import it.burningboots.entrance.client.service.DataServiceAsync;
import it.burningboots.entrance.shared.ConfigBean;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Entrance implements EntryPoint {
	
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		loadConfigBean();
	}
	
	//AsyncMethods
	
	private void loadConfigBean() {
		AsyncCallback<ConfigBean> callback = new AsyncCallback<ConfigBean>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(ConfigBean result) {
				WizardSingleton.get().setConfigBean(result);
				WaitSingleton.get().stop();
				UiSingleton.get().drawUi();
			}
		};
		dataService.getConfigBean(callback);
	}
}
