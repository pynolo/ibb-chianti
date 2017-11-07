package it.burningboots.greeter.client.widgets;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataModel<T> {

	public void find(int offset, int pageSize, AsyncCallback<List<T>> callback);
	
}
