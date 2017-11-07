package it.burningboots.greeter.client.frame;

import it.burningboots.greeter.client.ClientConstants;
import it.burningboots.greeter.client.IRefreshable;
import it.burningboots.greeter.client.LocaleConstants;
import it.burningboots.greeter.client.UiSingleton;
import it.burningboots.greeter.client.WaitSingleton;
import it.burningboots.greeter.client.service.DataService;
import it.burningboots.greeter.client.service.DataServiceAsync;
import it.burningboots.greeter.client.widgets.DateOnlyBox;
import it.burningboots.greeter.shared.StringValidator;
import it.burningboots.greeter.shared.ValidationException;
import it.burningboots.greeter.shared.entity.Participant;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

public class ParticipantPopUp extends PopupPanel {

	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private LocaleConstants constants = GWT.create(LocaleConstants.class);
	
	private FlexTable table = new FlexTable();
	private Integer idParticipant = null;
	private Participant item = null;
	private IRefreshable parent = null;
	
	private TextBox emailText = null;
	private TextBox firstNameText = null;
	private TextBox lastNameText = null;
	private TextBox birthCityText = null;
	private DateOnlyBox birthDateText = null;
	private TextBox pwText = null;
	
	public ParticipantPopUp(Integer idParticipant, IRefreshable parent) {
		super(false);
		this.idParticipant=idParticipant;
		this.parent=parent;
		loadParticipant();
	}
	
	private void draw() {
		this.setModal(true);
		this.setGlassEnabled(true);
		this.add(table);
		int r=0;
		
		HTML titleHtml = new HTML("<h1>Sostituzione partecipante <b>"+item.getItemNumber()+"</b></h1>");
		//titleHtml.setStyleName("frame-title");
		table.setWidget(r, 0, titleHtml);
		table.getFlexCellFormatter().setColSpan(r, 0, 5);
		r++;
		
		//Email
		table.setHTML(r, 0, "Email");
		emailText = new TextBox();
		//emailText.setValue(item.getEmail());
		emailText.setMaxLength(64);
		emailText.setWidth("25rem");
		table.setWidget(r, 1, emailText);
		table.getFlexCellFormatter().setColSpan(r, 1, 4);
		r++;
		
		//Nome
		table.setHTML(r, 0, "Nome");
		firstNameText = new TextBox();
		//firstNameText.setValue(item.getFirstName());
		firstNameText.setMaxLength(64);
		table.setWidget(r, 1, firstNameText);
		//Cognome
		table.setHTML(r, 3, "Cognome");
		lastNameText = new TextBox();
		//lastNameText.setValue(item.getLastName());
		lastNameText.setMaxLength(64);
		table.setWidget(r, 4, lastNameText);
		r++;
		
		//Città
		table.setHTML(r, 0, "Città di nascita ");
		birthCityText = new TextBox();
		//birthCityText.setValue(item.getBirthCity());
		table.setWidget(r, 1, birthCityText);
		//Data nascita
		table.setHTML(r, 3, "Data di nascita ");
		birthDateText = new DateOnlyBox();
		birthDateText.setFormat(ClientConstants.BOX_FORMAT_DAY);
		//birthDateText.setValue(item.getBirthDt());
		table.setWidget(r, 4, birthDateText);
		r++;
		
		//Admin password
		table.setHTML(r, 0, "Codice autorizzazione ");
		pwText = new TextBox();
		table.setWidget(r, 1, pwText);
		//Cognome
		table.setHTML(r, 3, "Vecchio nome");
		String nome = "<b>"+item.getFirstName()+"&nbsp;"+item.getLastName()+"</b>";
		table.setHTML(r, 4, nome);
		r++;
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		// Bottone SALVA
		Button submitButton = new Button("Sostituisci", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					replace();
					close();
				} catch (Exception e) {
					UiSingleton.get().addError(e);
				}
			}
		});
		buttonPanel.add(submitButton);
		
		// Bottone ANNULLA
		Button cancelButton = new Button("Annulla", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});
		buttonPanel.add(cancelButton);
		
		table.setWidget(r,0,buttonPanel);
		table.getFlexCellFormatter().setColSpan(r, 0, 5);
		
		this.center();
		this.show();
	}
	
	private void close() {
		this.hide();
	}
	
	
	
	// METODI ASINCRONI
	
	
	
	private void replace() throws ValidationException {
		AsyncCallback<Participant> callback = new AsyncCallback<Participant>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				//close();
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Participant result) {			
				idParticipant = result.getId();
				parent.refresh();
				close();
				UiSingleton.get().addInfo("Sostituzione avvenuta con successo");
				WaitSingleton.get().stop();
			}
		};
		//Validazione
		String errorMessage = "";
		if (pwText.getValue() == null) {
			errorMessage += "Inserire il codice di autorizzazione<br/>";
		} else {
			if (pwText.getValue().length() < 1) {
				errorMessage += "Inserire il codice di autorizzazione<br/>";
			}
		}
		String email = emailText.getValue();
		try {
			StringValidator.validateEmail(email);
		} catch (ValidationException e) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += e.getMessage();
		}
		String firstName = firstNameText.getValue();
		try {
			StringValidator.validateName(firstName);
		} catch (ValidationException e) {
			errorMessage += e.getMessage();
		}
		String lastName = lastNameText.getValue();
		try {
			StringValidator.validateName(lastName);
		} catch (ValidationException e) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += e.getMessage();
		}
		String birthCity = birthCityText.getValue();
		if (birthCity.length() < 3) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += constants.personalErrorCity();
		}
		Date birthDt = birthDateText.getValue();
		if (birthDt == null) {
			if (errorMessage.length() > 0) errorMessage += "<br />";
			errorMessage += constants.personalErrorDate();
		}
		
		boolean isError = (errorMessage.length() > 0);
		if (isError) {
			UiSingleton.get().addWarning(errorMessage);
		} else {		
			//Salvataggio
			Participant newItem = new Participant();
			newItem.setAdminPassword(pwText.getValue());
			newItem.setBirthCity(birthCity);
			newItem.setBirthDt(birthDt);
			newItem.setCreationDt(item.getCreationDt());
			newItem.setEmail(email);
			newItem.setFirstName(firstName);
			newItem.setId(null);
			newItem.setItemNumber(item.getItemNumber());
			newItem.setLastName(lastName);
			newItem.setPaymentAmount(item.getPaymentAmount());
			newItem.setPaymentDetails(item.getPaymentDetails());
			newItem.setPaymentDt(item.getPaymentDt());
			newItem.setReplacedById(null);
			newItem.setUpdateDt(new Date());
			WaitSingleton.get().start();
			dataService.replaceParticipant(newItem, idParticipant, callback);
		}
	}

	private void loadParticipant() {
		AsyncCallback<Participant> callback = new AsyncCallback<Participant>() {
			@Override
			public void onFailure(Throwable caught) {
				UiSingleton.get().addError(caught);
				WaitSingleton.get().stop();
			}
			@Override
			public void onSuccess(Participant result) {
				item = result;
				draw();
				WaitSingleton.get().stop();
			}
		};
		WaitSingleton.get().start();
		dataService.findParticipantById(idParticipant, callback);
	}
	
}
