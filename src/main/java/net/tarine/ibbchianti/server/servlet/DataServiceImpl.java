package net.tarine.ibbchianti.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;

import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.server.DataBusiness;
import net.tarine.ibbchianti.server.PropertyConfigReader;
import net.tarine.ibbchianti.server.persistence.GenericDao;
import net.tarine.ibbchianti.server.persistence.ParticipantDao;
import net.tarine.ibbchianti.server.persistence.SessionFactory;
import net.tarine.ibbchianti.server.persistence.WebSessionDao;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.ConfigBean;
import net.tarine.ibbchianti.shared.OrmException;
import net.tarine.ibbchianti.shared.SystemException;
import net.tarine.ibbchianti.shared.entity.Config;
import net.tarine.ibbchianti.shared.entity.Participant;
import net.tarine.ibbchianti.shared.entity.WebSession;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataServiceImpl.class);
	
	@Override
	public ConfigBean getConfigBean() throws SystemException {
		ConfigBean bean = new ConfigBean();
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			//From app file
			bean.setVersion(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_VERSION));
			//String closedString = PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_CLOSED);
			//if (closedString.equals("false")) bean.setClosed(false);
			//if (closedString.equals("true")) bean.setClosed(true);
			Config ticketMaxConfig = GenericDao.findById(ses, Config.class, AppConstants.CONFIG_TICKET_MAX);
			int ticketMax = Integer.parseInt(ticketMaxConfig.getVal());
			bean.setTicketMax(ticketMax);
			Config ticketPriceConfig = GenericDao.findById(ses, Config.class, AppConstants.CONFIG_TICKET_PRICE);
			double ticketPrice = Double.parseDouble(ticketPriceConfig.getVal());
			bean.setTicketPrice(ticketPrice);
			Config accessKeyConfig = GenericDao.findById(ses, Config.class, AppConstants.CONFIG_ACCESS_KEY);
			bean.setAccessKey(accessKeyConfig.getVal());
			Config stripeKeyConfig = GenericDao.findById(ses, Config.class, AppConstants.CONFIG_STRIPE_KEY);
			bean.setStripeKey(stripeKeyConfig.getVal());
			trn.commit();
		} catch (IOException|NumberFormatException e) { // catch exception in case properties file does not exist
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return bean;
	}
	
	@Override
	public Config findConfigByKey(String name) throws SystemException {
		Config config = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			config = GenericDao.findById(ses, Config.class, name);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return config;
	}
	
	//@Override
	//public void saveOrUpdateConfig(Config config) throws SystemException {
	//	Integer id = null;
	//	Session ses = SessionFactory.getSession();
	//	Transaction trn = ses.beginTransaction();
	//	try {
	//		if (config.getId() == null) {
	//			GenericDao.saveGeneric(ses, config);
	//		} else {
	//			GenericDao.updateGeneric(ses, id, config);
	//		}
	//		trn.commit();
	//	} catch (OrmException e) {
	//		trn.rollback();
	//		LOG.error(e.getMessage(), e);
	//		throw new SystemException(e.getMessage(), e);
	//	} finally {
	//		ses.close();
	//	}
	//}

	@Override
	public Participant findParticipantById(Integer id) throws SystemException {
		Participant p = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			p = GenericDao.findById(ses, Participant.class, id);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return p;
	}
	
	@Override
	public Participant findParticipantByItemNumber(String itemNumber, int delayMillis) throws SystemException {
		Participant p = null;
		if (delayMillis > 0) {
			try {
			    Thread.sleep(delayMillis);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			p = ParticipantDao.findByItemNumber(ses, itemNumber);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return p;
	}

	@Override
	public List<Participant> findParticipants(boolean confirmed) throws SystemException {
		List<Participant> pList = new ArrayList<Participant>();
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			pList = ParticipantDao.find(ses, confirmed);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return pList;
	}
	
	@Override
	public Participant saveOrUpdateParticipant(Participant prt) throws SystemException {
		Participant result = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			Date now = new Date();
			prt.setUpdateDt(now);
			if (prt.getItemNumber() == null) prt.setItemNumber("");
			if (prt.getItemNumber().equals("")) {
				String seed = prt.getFirstName().substring(0, 2)+
						prt.getLastName().substring(0, 2)+
						prt.getEmail();
				String itemNumber = DataBusiness.createCode(seed, AppConstants.ITEM_NUMBER_LENGHT);
				prt.setItemNumber(itemNumber);
			}
			Participant oldPrt = null;
			if (prt.getId() != null) oldPrt = GenericDao.findById(ses, Participant.class, prt.getId());
			Integer id = null;
			if (oldPrt == null) {
				prt.setEmailOriginal(prt.getEmail());
				prt.setCreationDt(now);
				id = (Integer) GenericDao.saveGeneric(ses, prt);
			} else {
				id = prt.getId();
				GenericDao.updateGeneric(ses, prt.getId(), prt);
			}
			result = GenericDao.findById(ses, Participant.class, id);
        	trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return result;
	}

//	@Override
//	public Participant createTransientParticipant() throws SystemException {
//		try {
//			String itemNumber = DataBusiness.createCode(this.getClass().getName(), AppConstants.ITEM_NUMBER_LENGHT);
//			Participant prt = new Participant();
//			prt.setItemNumber(itemNumber);
//			return prt;
//		} catch (Exception e) {
//			throw new SystemException(e.getMessage(), e);
//		}
//	}

	@Override
	public Integer countConfirmed() throws SystemException {
		Integer result = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			result = ParticipantDao.countConfirmed(ses);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return (result == null ? 0 : result);
	}

	//@Override
	//public Double countPaymentTotal() throws SystemException {
	//	Double result = null;
	//	Session ses = SessionFactory.getSession();
	//	Transaction trn = ses.beginTransaction();
	//	try {
	//		result = ParticipantDao.countPaymentTotal(ses);
	//		trn.commit();
	//	} catch (OrmException e) {
	//		trn.rollback();
	//		LOG.error(e.getMessage(), e);
	//		throw new SystemException(e.getMessage(), e);
	//	} finally {
	//		ses.close();
	//	}
	//	return (result == null ? 0 : result);
	//}
	
	//@Override
	//public List<Discount> findDiscounts() throws SystemException {
	//	List<Discount> pList = new ArrayList<Discount>();
	//	Session ses = SessionFactory.getSession();
	//	Transaction trn = ses.beginTransaction();
	//	try {
	//		pList = GenericDao.findByClass(ses, Discount.class, "email");
	//		trn.commit();
	//	} catch (OrmException e) {
	//		trn.rollback();
	//		LOG.error(e.getMessage(), e);
	//		throw new SystemException(e.getMessage(), e);
	//	} finally {
	//		ses.close();
	//	}
	//	return pList;
	//}
	//
	//@Override
	//public Boolean canHaveDiscount(String email) throws SystemException {
	//	Boolean result = false;
	//	Session ses = SessionFactory.getSession();
	//	Transaction trn = ses.beginTransaction();
	//	try {
	//		result = canHaveDiscount(ses, email);
	//		trn.commit();
	//	} catch (OrmException e) {
	//		trn.rollback();
	//		LOG.error(e.getMessage(), e);
	//		throw new SystemException(e.getMessage(), e);
	//	} finally {
	//		ses.close();
	//	}
	//	return result;
	//}
	//private boolean canHaveDiscount(Session ses, String email) throws OrmException {
	//	boolean result = false;
	//	Discount discount = DiscountDao.findDiscount(ses, email);
	//	if (discount != null) {
	//		List<Participant> confirmedList = ParticipantDao.findByEmail(ses, email, true);
	//		int confirmed = confirmedList.size();
	//		if (discount.getTickets() > confirmed) {
	//			result = true;
	//		}
	//	}
	//	return result;
	//}
	

	@Override
	public WebSession createWebSession(String seed) throws SystemException {
		WebSession ws = new WebSession();
		ws.setId(DataBusiness.createCode(new Date().getTime()+seed, 32));
		return null;
	}

	@Override
	public Boolean verifyWebSession(String idWebSession) throws SystemException {
		boolean result = false;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		WebSession ws = null;
		try {
			ws = GenericDao.findById(ses, WebSession.class, idWebSession);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		Date now = new Date();
		if (now.getTime()-ws.getTime().getTime() < AppConstants.WEBSESSION_TTL) {
			//not expired yet
			result = true;
		}
		return result;
	}

	@Override
	public Integer getQueuePosition(String idWebSession) throws SystemException {
		Integer result = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			result = WebSessionDao.getQueuePosition(ses, idWebSession);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return result;
	}

	@Override
	public String payWithStripe(String amount, String number, String expMonth,
			String expYear) throws SystemException {
		RequestOptions requestOptions = (new RequestOptionsBuilder()).setApiKey("YOUR-SECRET-KEY").build();
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", amount);
		chargeMap.put("currency", "eur");
		Map<String, Object> cardMap = new HashMap<String, Object>();
		cardMap.put("number", number);
		cardMap.put("exp_month", expMonth);
		cardMap.put("exp_year", expYear);
		chargeMap.put("card", cardMap);
		try {
			Charge charge = Charge.create(chargeMap, requestOptions);
			return charge.toString();
		} catch (StripeException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

}
