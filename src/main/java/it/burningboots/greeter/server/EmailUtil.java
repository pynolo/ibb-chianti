package it.burningboots.greeter.server;

import it.burningboots.greeter.server.persistence.ParticipantDao;
import it.burningboots.greeter.server.persistence.SessionFactory;
import it.burningboots.greeter.shared.OrmException;
import it.burningboots.greeter.shared.SystemException;
import it.burningboots.greeter.shared.entity.Participant;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmailUtil.class);
	private static final String EOL = "\r\n";
	
	public static void sendConfirmationEmail(String itemNumber, Double amount) 
			throws SystemException {
		Participant prt = null;
		String amountString = ServerConstants.FORMAT_CURRENCY.format(amount);
		org.hibernate.Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			prt = ParticipantDao.findByItemNumber(ses, itemNumber);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		if (prt != null) {
			String subject = "Italian Burning Boots confirmation";
			String body ="[ ENGLISH ]"+EOL+
				"Congratulations, you're in!"+EOL+
				"You registration has been confirmed with a donation of EUR "+amountString+EOL+
				EOL+
				"Email: "+prt.getEmail()+EOL+
				"First and last name: "+prt.getFirstName()+" "+prt.getLastName()+EOL+
				//"Birth: "+ServerConstants.FORMAT_DAY.format(participant.getBirthDt())+
				//		" in "+participant.getBirthCity()+EOL+
				EOL+
				"This is your Secret Code: "+itemNumber+EOL+
				"It works LIKE A TICKET to enter the event, so bring it with you."+EOL+
				"You can also give it someone else if you can't join IBB anymore and you want to be replaced."+EOL+
				"Resales for more than the donation amount are strictly forbidden."+EOL+
				EOL+
				"Don't forget to check the 'What to bring' section on https://burningboots.it !"+EOL+
				"The event LOCATION WILL BE REVEALED a few days before the event, keep your eyes on your mailbox."+EOL+
				EOL+
				EOL+
				"[ ITALIANO ]"+EOL+
				"Congratulazioni, sei dei nostri!"+EOL+
				"La tua registrazione e' stata confermata con la donazione di EUR "+amountString+EOL+
				EOL+
				"Email: "+prt.getEmail()+EOL+
				"Nome e cognome: "+prt.getFirstName()+" "+prt.getLastName()+EOL+
				//"Nascita: "+ServerConstants.FORMAT_DAY.format(participant.getBirthDt())+
				//		" a "+participant.getBirthCity()+EOL+
				EOL+
				"Questo e' il tuo Codice Segreto: "+itemNumber+EOL+
				"Funziona COME UN BIGLIETTO per entrare all'evento, quindi portalo con te."+EOL+
				"Puoi darlo a qualcun altro se non puoi piu' partecipare e vuoi essere sostituito/a."+EOL+
				"E' vietato cedere la partecipazione a IBB per un importo superiore a quello che hai donato."+EOL+
				EOL+
				"Non scordare di leggere la pagina 'Cosa portare' su https://burningboots.it !"+EOL+
				"Il luogo esatto dell'evento SARA' COMUNICATO POCHI GIORNI PRIMA, tieni d'occhio la casella email."+EOL+
				EOL+
				EOL+
				"Hugs & abbracci"+EOL+
				"info@burningboots.it";
			EmailUtil.sendEmail(prt.getEmail(), subject, body);
		}
	}
	
	public static void sendEmail(String to, String subject, String body) {
		Properties props = new Properties();
		props.put("mail.smtp.host", ServerConstants.SMTP_HOST);
		props.put("mail.smtp.port", ServerConstants.SMTP_PORT);
		switch (ServerConstants.SMTP_PROTOCOL) {
		    case "SMTP":
		        props.put("mail.smtp.ssl.enable", true);
		        break;
		    case "TLS":
		        props.put("mail.smtp.starttls.enable", true);
		        break;
		}
		
		Authenticator authenticator = null;
		if (ServerConstants.SMTP_AUTH) {
		    props.put("mail.smtp.auth", true);
		    authenticator = new Authenticator() {
		        private PasswordAuthentication pa = new PasswordAuthentication(ServerConstants.SMTP_USERNAME, ServerConstants.SMTP_PASSWORD);
		        @Override
		        public PasswordAuthentication getPasswordAuthentication() {
		            return pa;
		        }
		    };
		}
		
		Session session = Session.getInstance(props, authenticator);
		session.setDebug(ServerConstants.SMTP_DEBUG);
		
		MimeMessage message = new MimeMessage(session);
		try {
		    message.setFrom(new InternetAddress(ServerConstants.SMTP_FROM));
		    InternetAddress[] address = {new InternetAddress(to)};
		    message.setRecipients(Message.RecipientType.TO, address);
		    message.setSubject(subject);
		    message.setSentDate(new Date());
		    message.setText(body);
		    Transport.send(message);
		} catch (MessagingException ex) {
		    ex.printStackTrace();
		}
	}
	
}
