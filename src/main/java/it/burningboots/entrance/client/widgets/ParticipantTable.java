package it.burningboots.entrance.client.widgets;

import it.burningboots.entrance.client.ClientConstants;
import it.burningboots.entrance.client.service.DataService;
import it.burningboots.entrance.client.service.DataServiceAsync;
import it.burningboots.entrance.shared.entity.Participant;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineHTML;

public class ParticipantTable extends PagingTable<Participant> {
	
	private static final int TABLE_ROWS = 1000;
	private int peopleTotal = 0;
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
		final Participant rowFinal = rowObj;
		if (rowFinal.getPaymentAmount() != null) {
			getInnerTable().setHTML(rowNum, 0, "<i class='fa fa-ticket' ></i>");
		}
		//COGNOME
		String cognome = "";
		if (rowFinal.getLastName() != null) cognome = rowFinal.getLastName();
		getInnerTable().setHTML(rowNum, 1, cognome);
		//NOME
		String nome = "";
		if (rowFinal.getFirstName() != null) nome = rowFinal.getFirstName();
		getInnerTable().setHTML(rowNum, 2, nome);
		//EMAIL
		if (rowFinal.getPaymentAmount() != null) {
			getInnerTable().setHTML(rowNum, 3, "<b>"+rowFinal.getEmail()+"</b> ");
		} else {
			getInnerTable().setHTML(rowNum, 3, rowFinal.getEmail());
		}
		//NASCITA
		String nascita = "";
		if (rowFinal.getBirthDt() != null) nascita += ClientConstants.FORMAT_DAY.format(rowFinal.getBirthDt())+" ";
		if (rowFinal.getBirthCity() != null) nascita += rowFinal.getBirthCity()+" ";
		getInnerTable().setHTML(rowNum, 4, nascita);
		//PAGAMENTO
		String pag = "";
		if (rowFinal.getPaymentAmount() != null) {
				pag += "<b>&euro;"+ClientConstants.FORMAT_CURRENCY.format(rowFinal.getPaymentAmount())+"</b> ";
				paymentTotal += rowFinal.getPaymentAmount();
				peopleTotal ++;
		}
		if (rowFinal.getPaymentDt() != null)
				pag += "("+ClientConstants.FORMAT_TIMESTAMP.format(rowFinal.getPaymentDt())+") ";
		InlineHTML paymentHtml = new InlineHTML(pag);
		//paymentHtml.setTitle(rowFinal.getItemNumber());
		getInnerTable().setWidget(rowNum, 5, paymentHtml);
		//ITEM NUMBER
		String itemNumber = "";
		if (rowFinal.getFirstName() != null && rowFinal.getPaymentDt() != null)
				itemNumber = "<b>"+rowFinal.getItemNumber()+"</b>";
		getInnerTable().setHTML(rowNum, 6, itemNumber);
	}
	
	@Override
	protected void addHeader() {
		// Set the data in the current row
		getInnerTable().setHTML(0, 1, "Last name");
		getInnerTable().setHTML(0, 2, "First name");
		getInnerTable().setHTML(0, 3, "Email");
		getInnerTable().setHTML(0, 4, "Birth");
		getInnerTable().setHTML(0, 5, "Payment");
		getInnerTable().setHTML(0, 6, "Secret");
	}
	
	@Override
	protected void addFooter(int rowNum) {
		getInnerTable().setHTML(rowNum, 1,
				"<b>TOTAL participants: "+(peopleTotal)+"</b>");
		getInnerTable().getFlexCellFormatter().setColSpan(rowNum, 1, 4);
		//PAGAMENTO
		getInnerTable().setHTML(rowNum, 2, "<b>TOTAL amount: &euro;"+
				ClientConstants.FORMAT_CURRENCY.format(paymentTotal)+"</b>");
		getInnerTable().getFlexCellFormatter().setColSpan(rowNum, 2, 2);
	}
	
	@Override
	protected void onEmptyResult() {}
	
	
	
	//Inner classes
	
	
	
	public static class ParticipantModel implements DataModel<Participant> {
		private DataServiceAsync dataService = GWT.create(DataService.class);
		private boolean confirmed = true;
		private String orderBy = "id";
		
		public ParticipantModel(boolean confirmed, String orderBy) {
			this.confirmed=confirmed;
			this.orderBy=orderBy;
		}

		@Override
		public void find(int offset, int pageSize, AsyncCallback<List<Participant>> callback) {
			//WaitSingleton.get().start();
			dataService.findParticipants(confirmed, orderBy, callback);
		}
	}

	
}
