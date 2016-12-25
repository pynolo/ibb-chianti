package net.tarine.ibbchianti.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.shared.entity.Discount;

public class DiscountTable extends PagingTable<Discount> {
	
	private static final int TABLE_ROWS = 1000;
	
	private AsyncCallback<List<Discount>> callback = new AsyncCallback<List<Discount>>() {
		@Override
		public void onFailure(Throwable caught) {
			setTableRows(new ArrayList<Discount>());
			//WaitSingleton.get().stop();
		}
		@Override
		public void onSuccess(List<Discount> result) {
			setTableRows(result);
			//WaitSingleton.get().stop();
		}
	};
	
	public DiscountTable(DataModel<Discount> model) {
		super(model, TABLE_ROWS);
		drawPage(0);
	}

	@Override
	public void drawPage(int page) {
		clearInnerTable();
		getInnerTable().setHTML(0, 0, ClientConstants.LABEL_LOADING);
		getModel().find(page*TABLE_ROWS,
				TABLE_ROWS,
				callback);
	}
	
	public void refresh() {
		drawPage(0);
	}
	
	@Override
	protected void addTableRow(int rowNum, Discount rowObj) {
		final Discount rowFinal = rowObj;
		//EMAIL
		getInnerTable().setHTML(rowNum, 0, "<b>"+rowFinal.getEmail()+"</b> ");
		//Tickets
		String tks = "";
		if (rowFinal.getTickets() != null) tks = rowFinal.getTickets().toString();
		getInnerTable().setHTML(rowNum, 1, "<b>"+tks+"</b>");
		//NOME
		String note = "";
		if (rowFinal.getNote() != null) note = rowFinal.getNote();
		getInnerTable().setHTML(rowNum, 2, note);
	}
	
	@Override
	protected void addHeader() {
		// Set the data in the current row
		getInnerTable().setHTML(0, 0, "Email");
		getInnerTable().setHTML(0, 1, "Tickets");
		getInnerTable().setHTML(0, 2, "Note");
	}
	
	@Override
	protected void addFooter(int rowNum) {
	}
	
	@Override
	protected void onEmptyResult() {}
	
	
	
	//Inner classes
	
	
	
	public static class DiscountModel implements DataModel<Discount> {
		private DataServiceAsync dataService = GWT.create(DataService.class);

		public DiscountModel() {
		}

		@Override
		public void find(int offset, int pageSize, AsyncCallback<List<Discount>> callback) {
			//WaitSingleton.get().start();
			dataService.findDiscounts(callback);
		}
	}

	
}
