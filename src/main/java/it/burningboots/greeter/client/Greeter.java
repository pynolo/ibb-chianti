package it.burningboots.greeter.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Greeter implements EntryPoint {
	
	//private final DataServiceAsync dataService = GWT.create(DataService.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		UiSingleton.get().drawUi();
	}
	
	//AsyncMethods
	
	//private void loadConfigBean() {
	//	AsyncCallback<ConfigBean> callback = new AsyncCallback<ConfigBean>() {
	//		@Override
	//		public void onFailure(Throwable caught) {
	//			UiSingleton.get().addError(caught);
	//			WaitSingleton.get().stop();
	//		}
	//		@Override
	//		public void onSuccess(ConfigBean result) {
	//			WizardSingleton.get().setConfigBean(result);
	//			WaitSingleton.get().stop();
	//			UiSingleton.get().drawUi();
	//		}
	//	};
	//	dataService.findConfigByKey(key, callback);getConfigBean(callback);
	//}
}
