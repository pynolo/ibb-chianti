package it.burningboots.greeter.server.servlet;

import it.burningboots.greeter.client.service.DataService;
import it.burningboots.greeter.server.DataBusiness;
import it.burningboots.greeter.server.EmailUtil;
import it.burningboots.greeter.server.persistence.GenericDao;
import it.burningboots.greeter.server.persistence.LevelDao;
import it.burningboots.greeter.server.persistence.ParticipantDao;
import it.burningboots.greeter.server.persistence.SessionFactory;
import it.burningboots.greeter.server.persistence.WebSessionDao;
import it.burningboots.greeter.shared.Amount;
import it.burningboots.greeter.shared.AppConstants;
import it.burningboots.greeter.shared.ConfigBean;
import it.burningboots.greeter.shared.OrmException;
import it.burningboots.greeter.shared.SystemException;
import it.burningboots.greeter.shared.entity.Config;
import it.burningboots.greeter.shared.entity.Level;
import it.burningboots.greeter.shared.entity.Participant;
import it.burningboots.greeter.shared.entity.WebSession;

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
			//bean.setVersion(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_VERSION));
			Config accessKeyConfig = GenericDao.findById(ses, Config.class, AppConstants.CONFIG_ACCESS_KEY);
			bean.setAccessKey(accessKeyConfig.getVal());
			//Config stripe
			Config stripePublicKeyConfig = GenericDao.findById(ses, Config.class, AppConstants.CONFIG_STRIPE_PUBLIC_KEY);
			bean.setStripePublicKey(stripePublicKeyConfig.getVal());
			trn.commit();
		} catch (NumberFormatException e) { // catch exception in case properties file does not exist
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
	public List<Participant> findParticipants(boolean confirmed, String orderBy) throws SystemException {
		List<Participant> pList = new ArrayList<Participant>();
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			pList = ParticipantDao.find(ses, confirmed, orderBy);
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

	@Override
	public String payWithStripe(String itemNumber, Amount amount, String number, String expMonth,
			String expYear) throws SystemException {
		String result = "";
		Participant prt = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			prt = ParticipantDao.findByItemNumber(ses, itemNumber);
			Config secretConfig = GenericDao.findById(ses, Config.class, AppConstants.CONFIG_STRIPE_SECRET_KEY);
			String secretKey = secretConfig.getVal();
			RequestOptions requestOptions = (new RequestOptionsBuilder()).setApiKey(secretKey).build();
			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("amount", amount.getAmountLong());
			chargeMap.put("currency", "eur");
			Map<String, Object> cardMap = new HashMap<String, Object>();
			cardMap.put("number", number);
			cardMap.put("exp_month", expMonth);
			cardMap.put("exp_year", expYear);
			chargeMap.put("card", cardMap);
			Map<String, String> initialMetadata = new HashMap<String, String>();
			initialMetadata.put("order_id", itemNumber);
			initialMetadata.put("description", "burningboots.it - "+prt.getFirstName()+" "+prt.getLastName());
			chargeMap.put("metadata", initialMetadata);
			
			//Charge
			Charge charge = Charge.create(chargeMap, requestOptions);
			result = charge.toJson();
			//Store participant
			Date now = new Date();
			Amount paidAmount = new Amount(charge.getAmount());
			prt.setPaymentAmount(paidAmount.getAmountDouble());
			prt.setPaymentDetails(charge.toJson());
			prt.setPaymentDt(now);
			GenericDao.updateGeneric(ses, prt.getId(), prt);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} catch (StripeException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}

		//Email confirmation
		if (prt != null) {
			if (prt.getPaymentAmount() != null && prt.getPaymentDetails() != null) {
				EmailUtil.sendConfirmationEmail(prt.getItemNumber(), prt.getPaymentAmount());
			}
		}

		return result;
	}

	@Override
	public String createWebSession(String seed) throws SystemException {
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		WebSession ws = null;
		try {
			ws = new WebSession();
			ws.setId(DataBusiness.createCode(new Date().getTime()+seed, 32));
			Date now = new Date();
			ws.setCreationDt(now);
			ws.setHeartbeatDt(now);
			GenericDao.saveGeneric(ses, ws);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return ws.getId();
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
		if (now.getTime()-ws.getCreationDt().getTime() < AppConstants.WEBSESSION_TTL) {
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
	public Date updateHeartbeat(String idWebSession) throws SystemException {
		if (idWebSession == null) throw new SystemException("Web session id is empty");
		Date result = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			WebSession ws = GenericDao.findById(ses, WebSession.class, idWebSession);
			if (ws == null) throw new SystemException("Web session does not exist");
			ws.setHeartbeatDt(new Date());
			GenericDao.updateGeneric(ses, ws.getId(), ws);
        	trn.commit();
        	result = ws.getHeartbeatDt();
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
	public Boolean deleteWebSession(String idWebSession) throws SystemException {
		boolean result = false;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		WebSession ws = null;
		try {
			ws = GenericDao.findById(ses, WebSession.class, idWebSession);
			if (ws != null) {
				GenericDao.deleteGeneric(ses, idWebSession, ws);
				result = true;
				WebSessionDao.deleteAllExpiredSessions(ses);
			}
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
	public Level getCurrentLevel() throws SystemException {
		Level result = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			Integer count = ParticipantDao.countConfirmed(ses);
			List<Level> levelList = LevelDao.findAll(ses);
			Date now = new Date();
			Level tmpLevel = new Level();
			tmpLevel.setId(1000);
			tmpLevel.setLastDate(now);
			tmpLevel.setLastCount(-1);
			for (Level l:levelList) {
				// livello valido: count < iscritti & data non trascorsa
				if ((count <= l.getLastCount()) && (!now.after(l.getLastDate()))) {
					//tra i validi, sceglie il livello minore
					if (l.getId() < tmpLevel.getId()) {
						tmpLevel = l;
					}
				}
			}
			if (tmpLevel.getPrice() != null) {
				result = tmpLevel;
			}
		} catch (OrmException e) {
			trn.rollback();
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return result;
	}
}
