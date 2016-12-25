package net.tarine.ibbchianti.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import net.tarine.ibbchianti.client.service.DataService;
import net.tarine.ibbchianti.server.DataBusiness;
import net.tarine.ibbchianti.server.EnvSingleton;
import net.tarine.ibbchianti.server.PropertyConfigReader;
import net.tarine.ibbchianti.server.persistence.DiscountDao;
import net.tarine.ibbchianti.server.persistence.GenericDao;
import net.tarine.ibbchianti.server.persistence.ParticipantDao;
import net.tarine.ibbchianti.server.persistence.SessionFactory;
import net.tarine.ibbchianti.shared.AppConstants;
import net.tarine.ibbchianti.shared.OrmException;
import net.tarine.ibbchianti.shared.PropertyBean;
import net.tarine.ibbchianti.shared.SystemException;
import net.tarine.ibbchianti.shared.entity.Discount;
import net.tarine.ibbchianti.shared.entity.Participant;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {
	
	private static final Logger LOG = LoggerFactory.getLogger(IpnServlet.class);
	
	@Override
	public PropertyBean getPropertyBean() throws SystemException {
		PropertyBean bean = new PropertyBean();
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			//From app file
			bean.setVersion(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_VERSION));
			String closedString = PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_CLOSED);
			if (closedString.equals("false")) bean.setClosed(false);
			if (closedString.equals("true")) bean.setClosed(true);
			bean.setTotalMax(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_TOTAL_MAX));
			bean.setHutMax(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_HUT_MAX));
			bean.setHutPrice(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_HUT_PRICE));
			bean.setHutPriceLow(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_HUT_PRICE_LOW));
			bean.setTentMax(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_TENT_MAX));
			bean.setTentPrice(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_TENT_PRICE));
			bean.setTentPriceLow(PropertyConfigReader.readPropertyConfig(ses, PropertyConfigReader.PROPERTY_TENT_PRICE_LOW));
			//From config file
			bean.setAccessKey(EnvSingleton.get().getAccessKey());

			int bedCount = ParticipantDao.countConfirmed(ses, AppConstants.ACCOMMODATION_HUT);
			bean.setHutCount(bedCount);
			int tentCount = ParticipantDao.countConfirmed(ses, AppConstants.ACCOMMODATION_TENT);
			bean.setTentCount(tentCount);
			trn.commit();
		} catch (IOException e) { // catch exception in case properties file does not exist
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
	
	//@Override
	//public Config findConfigByKey(String name) throws SystemException {
	//	Config config = null;
	//	Session ses = SessionFactory.getSession();
	//	Transaction trn = ses.beginTransaction();
	//	try {
	//		config = GenericDao.findById(ses, Config.class, name);
	//		trn.commit();
	//	} catch (OrmException e) {
	//		trn.rollback();
	//		LOG.error(e.getMessage(), e);
	//		throw new SystemException(e.getMessage(), e);
	//	} finally {
	//		ses.close();
	//	}
	//	return config;
	//}
	//
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
			if (prt.getPaymentDt() == null) {//Per chi non ha pagato imposta se è discount
				boolean discount = canHaveDiscount(ses, prt.getEmail());
				prt.setDiscount(discount);
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

	@Override
	public Participant createTransientParticipant() throws SystemException {
		try {
			String itemNumber = DataBusiness.createCode(this.getClass().getName(), AppConstants.ITEM_NUMBER_LENGHT);
			Participant prt = new Participant();
			prt.setItemNumber(itemNumber);
			prt.setAccommodationType(AppConstants.ACCOMMODATION_HUT);
			return prt;
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	//@Override
	//public Integer countConfirmed(int accommodationType) throws SystemException {
	//	Integer result = null;
	//	Session ses = SessionFactory.getSession();
	//	Transaction trn = ses.beginTransaction();
	//	try {
	//		result = ParticipantDao.countConfirmed(ses, accommodationType);
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
	
	@Override
	public List<Discount> findDiscounts() throws SystemException {
		List<Discount> pList = new ArrayList<Discount>();
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			pList = GenericDao.findByClass(ses, Discount.class, "email");
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
	public Boolean canHaveDiscount(String email) throws SystemException {
		Boolean result = false;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			result = canHaveDiscount(ses, email);
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
	private boolean canHaveDiscount(Session ses, String email) throws OrmException {
		boolean result = false;
		Discount discount = DiscountDao.findDiscount(ses, email);
		if (discount != null) {
			List<Participant> confirmedList = ParticipantDao.findByEmail(ses, email, true);
			int confirmed = confirmedList.size();
			if (discount.getTickets() > confirmed) {
				result = true;
			}
		}
		return result;
	}
}
