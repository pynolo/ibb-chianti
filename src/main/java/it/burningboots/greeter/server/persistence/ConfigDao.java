package it.burningboots.greeter.server.persistence;

import it.burningboots.greeter.shared.OrmException;
import it.burningboots.greeter.shared.SystemException;
import it.burningboots.greeter.shared.entity.Config;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ConfigDao {
	
	public static Config findByKey(Session ses, String key) throws OrmException {
		Config config = null;
		try {
			config = GenericDao.findById(ses, Config.class, key);
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return config;
	}
	
	public static Config findByKey(String key) throws SystemException {
		Config config = null;
		Session ses = SessionFactory.getSession();
		Transaction trn = ses.beginTransaction();
		try {
			config = findByKey(ses, key);
			trn.commit();
		} catch (OrmException e) {
			trn.rollback();
			throw new SystemException(e.getMessage(), e);
		} finally {
			ses.close();
		}
		return config;
	}
}
