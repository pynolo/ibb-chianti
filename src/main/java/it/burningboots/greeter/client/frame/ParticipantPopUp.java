package it.burningboots.greeter.client.frame;

import it.burningboots.greeter.client.AuthSingleton;
import it.burningboots.greeter.client.IAuthenticatedWidget;
import it.burningboots.greeter.client.IRefreshable;
import it.burningboots.greeter.client.UiSingleton;
import it.burningboots.greeter.client.WaitSingleton;
import it.burningboots.greeter.client.service.DataService;
import it.burningboots.greeter.client.service.DataServiceAsync;
import it.burningboots.greeter.client.widgets.DateOnlyBox;
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

public class ParticipantPopUp extends PopupPanel implements IAuthenticatedWidget {

	private final DataServiceAsync dataService = GWT.create(DataService.class);
	
	private FlexTable table = new FlexTable();
	private Integer idParticipant = null;
	private Participant item = null;
	private IRefreshable parent = null;
	
	private TextBox emailText = null;
	private TextBox firstNameText = null;
	private TextBox lastNameText = null;
	private TextBox birthCityText = null;
	private DateOnlyBox birthDateText = null;
	private PasswordTextBox pwText = null;
	
	public ParticipantPopUp(Integer idParticipant, IRefreshable parent) {
		super(false);
		this.idParticipant=idParticipant;
		this.parent=parent;
		AuthSingleton.get().queueForAuthentication(this);
	}
	
	@Override
	public void onSuccessfulAuthentication() {
		loadParticipant();
	}
	
	private void draw() {
		int r=0;
		
		HTML titleHtml = new HTML("Partecipante <b>"+item.getItemNumber()+"</b>");
		titleHtml.setStyleName("frame-title");
		table.setWidget(r, 0, titleHtml);
		table.getFlexCellFormatter().setColSpan(r, 0, 5);
		r++;
		
		//Email
		table.setHTML(r, 0, "Email");
		emailText = new TextBox();
		emailText.setValue(item.getEmail());
		emailText.setMaxLength(64);
		table.setWidget(r, 1, emailText);
		r++;
		
		//Nome
		table.setHTML(r, 0, "Nome");
		firstNameText = new TextBox();
		firstNameText.setValue(item.getFirstName());
		firstNameText.setMaxLength(64);
		table.setWidget(r, 1, firstNameText);
		//Cognome
		table.setHTML(r, 3, "Cognome");
		lastNameText = new TextBox();
		lastNameText.setValue(item.getLastName());
		lastNameText.setMaxLength(64);
		table.setWidget(r, 4, lastNameText);
		r++;
		
		//Città
		table.setHTML(r, 0, "Città di nascita ");
		birthCityText = new TextBox();
		birthCityText.setValue(item.getBirthCity());
		table.setWidget(r, 1, birthCityText);
		//Data nascita
		table.setHTML(r, 3, "Data di nascita ");
		birthDateText = new DateOnlyBox();
		birthDateText.setValue(item.getBirthDt());
		table.setWidget(r, 4, birthDateText);
		r++;
		
		//Admin password
		table.setHTML(r, 0, "Codice autorizzazione ");
		pwText = new PasswordTextBox();
		table.setWidget(r, 1, pwText);
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
				close();
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
		if (pwText.getValue() == null) {
			throw new ValidationException("Modifica senza autorizzazione");
		} else {
			if (pwText.getValue().length() < 2) {
				throw new ValidationException("Modifica senza autorizzazione");
			}
		}
		if (emailText.getValue() == null) {
			throw new ValidationException("L'email e' vuota");
		} else {
			if (emailText.getValue().length() < 5) {
				throw new ValidationException("L'email non e' valida");
			}
		}
		if (firstNameText.getValue() == null) {
			throw new ValidationException("Il nome e' vuoto");
		} else {
			if (firstNameText.getValue().length() < 2) {
				throw new ValidationException("Il nome non e' valido");
			}
		}
		if (lastNameText.getValue() == null) {
			throw new ValidationException("Il cognome e' vuoto");
		} else {
			if (lastNameText.getValue().length() < 2) {
				throw new ValidationException("Il cognome non e' valido");
			}
		}
		if (birthCityText.getValue() == null) {
			throw new ValidationException("La citta' e' vuota");
		} else {
			if (birthCityText.getValue().length() < 2) {
				throw new ValidationException("La citta' non e' valida");
			}
		}
		if (birthDateText.getValue() == null) {
			throw new ValidationException("La data di nascita a' vuota");
		}
		
		//Salvataggio
		Participant newItem = new Participant();
		newItem.setAdminPassword(pwText.getValue());
		newItem.setBirthCity(birthCityText.getValue());
		newItem.setBirthDt(birthDateText.getValue());
		newItem.setCreationDt(item.getCreationDt());
		newItem.setEmail(emailText.getValue());
		newItem.setFirstName(firstNameText.getValue());
		newItem.setId(null);
		newItem.setItemNumber(item.getItemNumber());
		newItem.setLastName(lastNameText.getValue());
		newItem.setPaymentAmount(item.getPaymentAmount());
		newItem.setPaymentDetails(item.getPaymentDetails());
		newItem.setPaymentDt(item.getPaymentDt());
		newItem.setReplacedById(null);
		newItem.setUpdateDt(new Date());
		WaitSingleton.get().start();
		dataService.replaceParticipant(newItem, idParticipant, callback);
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
