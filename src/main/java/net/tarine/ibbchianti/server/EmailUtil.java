package net.tarine.ibbchianti.server;

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

import net.tarine.ibbchianti.server.persistence.ParticipantDao;
import net.tarine.ibbchianti.server.persistence.SessionFactory;
import net.tarine.ibbchianti.shared.OrmException;
import net.tarine.ibbchianti.shared.SystemException;
import net.tarine.ibbchianti.shared.entity.Participant;

public class EmailUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmailUtil.class);
	private static final String EOL = "\r\n";
	
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
	
	public static void sendConfirmationEmail(String itemNumber, String mcGross) 
			throws SystemException {
		Participant participant = null;
		org.hibernate.Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			participant = ParticipantDao.findByItemNumber(ses, itemNumber);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		boolean discount = participant.getDiscount();
		if (participant != null) {
			String subject = "Italian Burning Boots confirmation";
			String body ="[ ENGLISH ]"+EOL+
				"Congratulations, you're in!"+EOL+
				"You registration has been confirmed with a donation of EUR "+mcGross+EOL+
				EOL+
				"Accommodation: "+(participant.getAccommodationType()==1 ? "mountain hut" : "tent")+EOL+
				"Email: "+participant.getEmail()+EOL+
				"Volunteering for: "+
						(participant.getVolunteering().equals("") ? 
						"Nothing :( please help" : participant.getVolunteering())+EOL+
				"Food restrictions: "+
						(participant.getFoodRestrictions().equals("") ?
						"--" : participant.getFoodRestrictions())+EOL+
				"First and last name: "+participant.getFirstName()+" "+participant.getLastName()+EOL+
				"Birth: "+ServerConstants.FORMAT_DAY.format(participant.getBirthDt())+
						" in "+participant.getBirthCity()+EOL;
			if (!discount) body += EOL+
					"You can change any of these values (including first & last name) with the REPLACEMENT wizard. "+
					"See the 'Registration' section on https://burningboots.it"+EOL+
					EOL+
					"This is your REPLACEMENT Code: "+itemNumber+EOL+
					"You can give it someone else if you can't join IBB anymore and you want to be replaced."+EOL+
					"Resales for more than the donation amount are strictly forbidden."+EOL;
			body += EOL+
				"Don't forget to check the 'What to bring' section on https://burningboots.it !"+EOL+
				"The event location will be revealed a few days before the event, keep your eyes on your emailbox."+EOL+
				EOL+
				EOL+
				"[ ITALIANO ]"+EOL+
				"Congratulazioni, sei dei nostri!"+EOL+
				"La tua registrazione e' stata confermata con la donazione di EUR "+mcGross+EOL+
				EOL+
				"Pernottamento: "+(participant.getAccommodationType()==1 ? "rifugio" : "tenda")+EOL+
				"Email: "+participant.getEmail()+EOL+
				"Volontariato: "+
						(participant.getVolunteering().equals("") ? 
						"Niente :( aiutaci per favore" : participant.getVolunteering())+EOL+
				"Restrizioni sul cibo: "+
						(participant.getFoodRestrictions().equals("") ?
						"--" : participant.getFoodRestrictions())+EOL+
				"Nome e cognome: "+participant.getFirstName()+" "+participant.getLastName()+EOL+
				"Nascita: "+ServerConstants.FORMAT_DAY.format(participant.getBirthDt())+
						" a "+participant.getBirthCity()+EOL;
			if (!discount) body += EOL+
					"Puoi modificare questi dati (incluso nome e cognome) con la procedura di SOSTITUZIONE. "+
					"La trovi nella sezione 'Iscrizione' su https://burningboots.it"+EOL+
					EOL+
					"Questo e' il tuo Codice di SOSTITUZIONE: "+itemNumber+EOL+
					"Puoi darlo a qualcun altro se non puoi piu' partecipare e vuoi essere sostituito/a."+EOL+
					"E' vietato cedere la partecipazione a IBB per un importo superiore a quello che hai donato."+EOL;
			body += EOL+
				"Non scordare di leggere la pagina 'Cosa portare' su https://burningboots.it !"+EOL+
				"Il luogo esatto dell'evento sara' comunicato pochi giorni prima, tieni d'occhio la casella email."+EOL+
				EOL+
				EOL+
				"Hugs & abbracci"+EOL+
				"info@burningboots.it";
			EmailUtil.sendEmail(participant.getEmail(), subject, body);
		}
	}
}
