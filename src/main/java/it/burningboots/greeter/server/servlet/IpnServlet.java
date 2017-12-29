package it.burningboots.greeter.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.burningboots.greeter.server.EmailUtil;
import it.burningboots.greeter.server.persistence.GenericDao;
import it.burningboots.greeter.server.persistence.ParticipantDao;
import it.burningboots.greeter.server.persistence.SessionFactory;
import it.burningboots.greeter.shared.AppConstants;
import it.burningboots.greeter.shared.BusinessException;
import it.burningboots.greeter.shared.OrmException;
import it.burningboots.greeter.shared.entity.IpnResponse;
import it.burningboots.greeter.shared.entity.Participant;

public class IpnServlet extends HttpServlet {
	private static final long serialVersionUID = -7594337685342241190L;

	private static final Logger LOG = LoggerFactory.getLogger(IpnServlet.class);
	
	//@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		LOG.warn("**IpnSevlet** has been launched");
		HttpPost post = new HttpPost(AppConstants.PAYPAL_URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String serializedIpn = "";
		params.add(new BasicNameValuePair("cmd", "_notify-validate")); // You need to add this parameter to tell PayPal to verify
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String name = e.nextElement();
			String value = request.getParameter(name);
			params.add(new BasicNameValuePair(name, value));
			serializedIpn += name+"=\""+value+"\" \r\n";
		}
		post.setEntity(new UrlEncodedFormEntity(params));
		String rc = getRC(client.execute(post)).trim();
		if ("VERIFIED".equals(rc)) {
			String itemNumber = null;
			String paymentStatus = null;
			String payerEmail = null;
			String mcGross = null;
			String mcCurrency = null;
			String paymentDate = null;
			String pendingReason = null;
			String paymentType = null;
			for (NameValuePair pair:params) {
				if (pair.getName().equals("item_number")) itemNumber=pair.getValue();
				if (pair.getName().equals("mc_currency")) mcCurrency=pair.getValue();
				if (pair.getName().equals("mc_gross")) mcGross=pair.getValue();
				if (pair.getName().equals("payer_email")) payerEmail=pair.getValue();
				if (pair.getName().equals("payment_date")) paymentDate=pair.getValue();
				if (pair.getName().equals("payment_status")) paymentStatus =pair.getValue();
				if (pair.getName().equals("payment_type")) paymentType=pair.getValue();
				if (pair.getName().equals("pending_reason")) pendingReason=pair.getValue();
			}
			try {
				IpnResponse ipnr = new IpnResponse(itemNumber, paymentStatus, payerEmail,
						mcGross, mcCurrency, paymentDate, pendingReason, paymentType, null);
				registerPayment(ipnr, serializedIpn);
				LOG.warn("**IpnSevlet** stored a payment for "+payerEmail);
				Double amount = -1D;
				try {
					amount = Double.valueOf(mcGross);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
				EmailUtil.sendConfirmationEmail(itemNumber, amount);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	private String getRC(HttpResponse response) throws IOException,
			IllegalStateException {
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String result = "";
		String line = null;
		while ((line = br.readLine()) != null) {
			result += line;
		}
		return result;
	}


	private void registerPayment(IpnResponse ipnr, String serializedIpn) throws BusinessException {
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			GenericDao.saveGeneric(ses, ipnr);
			Participant prt = ParticipantDao.findByItemNumber(ses, ipnr.getItemNumber());
			if (prt != null) {
				//Partecipante identificato => gli assegna pagamento
				ipnr.setParticipant(prt);
				//Set<IpnResponse> ipnResponseList = prt.getIpnResponses();
				//if (ipnResponseList == null) {
				//	ipnResponseList = new HashSet<IpnResponse>();
				//	prt.setIpnResponses(ipnResponseList);
				//}
				//ipnResponseList.add(ipnr);
				Double amount = Double.valueOf(ipnr.getMcGross());
				prt.setPaymentAmount(amount);
				prt.setPaymentDt(new Date());
				prt.setPaymentDetails(serializedIpn);
				GenericDao.updateGeneric(ses, prt.getId(), prt);
			} else {
				//Partecipante NON identificato => marca pagamento come non assegnato
				ipnr.setParticipant(null);
			}
			GenericDao.updateGeneric(ses, ipnr.getId(), ipnr);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			ses.close();
		}
	}
	
	
	/* Example info received from IPN:
	2015/02/12 06:34:41,164 INFO  [stdout] (http--127.13.54.1-8080-3) name:'cmd' value:'_notify-validate'
	2015/02/12 06:34:41,164 INFO  [stdout] (http--127.13.54.1-8080-3) name:'item_number' value:'FA26A1'
	2015/02/12 06:34:41,165 INFO  [stdout] (http--127.13.54.1-8080-3) name:'residence_country' value:'US'
	2015/02/12 06:34:41,165 INFO  [stdout] (http--127.13.54.1-8080-3) name:'verify_sign' value:'AVVFXYTfxjgtzxri1C-ynmsGko1MAHJYHn0goRRxu468hcx9u0lRmE2J'
	2015/02/12 06:34:41,165 INFO  [stdout] (http--127.13.54.1-8080-3) name:'payment_status' value:'Pending'
	2015/02/12 06:34:41,165 INFO  [stdout] (http--127.13.54.1-8080-3) name:'business' value:'paolo-facilitator@tarine.net'
	2015/02/12 06:34:41,165 INFO  [stdout] (http--127.13.54.1-8080-3) name:'protection_eligibility' value:'Ineligible'
	2015/02/12 06:34:41,166 INFO  [stdout] (http--127.13.54.1-8080-3) name:'transaction_subject' value:''
	2015/02/12 06:34:41,166 INFO  [stdout] (http--127.13.54.1-8080-3) name:'first_name' value:'test'
	2015/02/12 06:34:41,166 INFO  [stdout] (http--127.13.54.1-8080-3) name:'payer_id' value:'BRY27QGWWQVPC'
	2015/02/12 06:34:41,166 INFO  [stdout] (http--127.13.54.1-8080-3) name:'payer_email' value:'paolo-buyer@tarine.net'
	2015/02/12 06:34:41,166 INFO  [stdout] (http--127.13.54.1-8080-3) name:'txn_id' value:'28187574TR775311A'
	2015/02/12 06:34:41,166 INFO  [stdout] (http--127.13.54.1-8080-3) name:'quantity' value:'0'
	2015/02/12 06:34:41,167 INFO  [stdout] (http--127.13.54.1-8080-3) name:'receiver_email' value:'paolo-facilitator@tarine.net'
	2015/02/12 06:34:41,167 INFO  [stdout] (http--127.13.54.1-8080-3) name:'notify_version' value:'3.8'
	2015/02/12 06:34:41,167 INFO  [stdout] (http--127.13.54.1-8080-3) name:'txn_type' value:'web_accept'
	2015/02/12 06:34:41,167 INFO  [stdout] (http--127.13.54.1-8080-3) name:'mc_gross' value:'58.00'
	2015/02/12 06:34:41,167 INFO  [stdout] (http--127.13.54.1-8080-3) name:'mc_currency' value:'EUR'
	2015/02/12 06:34:41,167 INFO  [stdout] (http--127.13.54.1-8080-3) name:'payer_status' value:'verified'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'test_ipn' value:'1'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'custom' value:''
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'payment_date' value:'03:34:35 Feb 12, 2015 PST'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'charset' value:'windows-1252'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'payment_gross' value:''
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'ipn_track_id' value:'9cadb988c9e24'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'pending_reason' value:'multi_currency'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'tax' value:'0.00'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'item_name' value:'Donation'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'last_name' value:'buyer'
	2015/02/12 06:34:41,168 INFO  [stdout] (http--127.13.54.1-8080-3) name:'payment_type' value:'instant'
	2015/02/12 06:34:41,169 INFO  [stdout] (http--127.13.54.1-8080-3) name:'receiver_id' value:'6UN4BCCPZ5VXN'
	 */
}