package net.tarine.ibbchianti.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineHTML;

import net.tarine.ibbchianti.client.ClientConstants;
import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.client.service.DataServiceAsync;
import net.tarine.ibbchianti.shared.entity.Participant;

public class ParticipantTable extends PagingTable<Participant> {
	
	private static final int TABLE_ROWS = 1000;
	private int bedCount = 0;
	private int tentCount = 0;
	private int disCount = 0;
	private double paymentTotal = 0D;
	
	private AsyncCallback<List<Participant>> callback = new AsyncCallback<List<Participant>>() {
		@Override
		public void onFailure(Throwable caught) {
			setTableRows(new ArrayList<Participant>());
			//WaitSingleton.get().stop();
		}
		@Override
		public void onSuccess(List<Participant> result) {
			setTableRows(result);
			//WaitSingleton.get().stop();
		}
	};
	
	public ParticipantTable(DataModel<Participant> model) {
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
	protected void addTableRow(int rowNum, Participant rowObj) {
		if (rowObj.getPaymentAmount() != null) paymentTotal += rowObj.getPaymentAmount();
		final Participant rowFinal = rowObj;
		//EMAIL
		getInnerTable().setHTML(rowNum, 1, "<b>"+rowFinal.getEmail()+"</b> ");
		//COGNOME
		String cognome = "";
		if (rowFinal.getLastName() != null) cognome = rowFinal.getLastName();
		getInnerTable().setHTML(rowNum, 2, cognome);
		//NOME
		String nome = "";
		if (rowFinal.getFirstName() != null) nome = rowFinal.getFirstName();
		getInnerTable().setHTML(rowNum, 3, nome);
		//NASCITA
		String nascita = "";
		nascita += ClientConstants.FORMAT_DAY.format(rowFinal.getBirthDt())+" ";
		if (rowFinal.getBirthCity() != null) nascita += rowFinal.getBirthCity()+" ";
		getInnerTable().setHTML(rowNum, 5, nascita);
		//PAGAMENTO
		String pag = "";
		if (rowFinal.getPaymentAmount() != null)
				pag += "<b>&euro;"+ClientConstants.FORMAT_CURRENCY.format(rowFinal.getPaymentAmount())+"</b> ";
		if (rowFinal.getPaymentDt() != null)
				pag += ClientConstants.FORMAT_TIMESTAMP.format(rowFinal.getPaymentDt())+" ";
		InlineHTML paymentHtml = new InlineHTML(pag);
		paymentHtml.setTitle(rowFinal.getItemNumber());
		getInnerTable().setWidget(rowNum, 9, paymentHtml);
		//TRANSFER
		String repl = "";
		if (rowFinal.getUpdateDt().after(rowFinal.getCreationDt())) {
			if (rowFinal.getEmailOriginal() != null)
					repl += rowFinal.getEmailOriginal()+" ";
			repl += ClientConstants.FORMAT_DAY.format(rowFinal.getUpdateDt())+" ";
		}
		getInnerTable().setHTML(rowNum, 10, repl);
	}
	
	@Override
	protected void addHeader() {
		// Set the data in the current row
		getInnerTable().setHTML(0, 1, "Email");
		getInnerTable().setHTML(0, 2, "Last name");
		getInnerTable().setHTML(0, 3, "First name");
		getInnerTable().setHTML(0, 5, "Birth");
		getInnerTable().setHTML(0, 9, "Payment");
		getInnerTable().setHTML(0, 10, "Transfer");
	}
	
	@Override
	protected void addFooter(int rowNum) {
		getInnerTable().setHTML(rowNum, 0,
				ClientConstants.ICON_HUT+" Total hut <b>"+bedCount+"</b><br />"+
						ClientConstants.ICON_TENT+" Total tent <b>"+tentCount+"</b>");
		getInnerTable().getFlexCellFormatter().setColSpan(rowNum, 0, 2);
		getInnerTable().setHTML(rowNum, 1,
				"<i class='fa fa-scissors'></i> Total discount <b>"+disCount+"</b><br />"+
				"<b>TOTAL participants "+(bedCount+tentCount)+"</b>");
		getInnerTable().getFlexCellFormatter().setColSpan(rowNum, 1, 2);
		//PAGAMENTO
		getInnerTable().setHTML(rowNum, 7, "<b>TOTAL &euro;"+
				ClientConstants.FORMAT_CURRENCY.format(paymentTotal)+"</b>");
		getInnerTable().setHTML(rowNum, 8, "&nbsp;");
	}
	
	@Override
	protected void onEmptyResult() {}
	
	
	
	//Inner classes
	
	
	
	public static class ParticipantModel implements DataModel<Participant> {
		private DataServiceAsync dataService = GWT.create(DataService.class);
		private boolean confirmed = true;
		
		public ParticipantModel(boolean confirmed) {
			this.confirmed=confirmed;
		}

		@Override
		public void find(int offset, int pageSize, AsyncCallback<List<Participant>> callback) {
			//WaitSingleton.get().start();
			dataService.findParticipants(confirmed, callback);
		}
	}

	
}
