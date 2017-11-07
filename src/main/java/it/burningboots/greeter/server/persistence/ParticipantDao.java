package it.burningboots.greeter.server.persistence;

import it.burningboots.greeter.shared.OrmException;
import it.burningboots.greeter.shared.entity.Participant;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class ParticipantDao {
	
	@SuppressWarnings("unchecked")
	public static Participant findByItemNumber(Session ses, String itemNumber) 
			throws OrmException {
		Participant result = null;
		try {
			String qs = "from Participant where "+
					"itemNumber = :s1 "+
					"order by itemNumber";
			Query q = ses.createQuery(qs);
			q.setString("s1", itemNumber);
			List<Participant> entities = (List<Participant>) q.list();
			if (entities != null) {
				if (entities.size() > 0) result = entities.get(0);
			}
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Participant> find(Session ses, boolean confirmed, String orderBy)
			throws OrmException {
		List<Participant> entities = new ArrayList<Participant>();
		if (orderBy == null) orderBy = "id";
		if (orderBy.length() > 15) orderBy = "id";
		try {
			String qs = "from Participant p ";
			if (confirmed) qs += "where p.paymentAmount is not null ";
			qs += "order by "+orderBy;
			Query q = ses.createQuery(qs);
			entities = (List<Participant>) q.list();
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Participant> findByEmail(Session ses, String email, boolean confirmed)
			throws OrmException {
		List<Participant> entities = new ArrayList<Participant>();
		try {
			String qs = "from Participant p where "+
					"p.email like :s1 ";
			if (confirmed) qs += "and p.paymentAmount is not null ";
			qs += "order by p.creationDt";
			Query q = ses.createQuery(qs);
			q.setParameter("s1", email);
			entities = (List<Participant>) q.list();
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	public static Integer countConfirmed(Session ses)
			throws OrmException {
		Long result = null;
		try {
			String qs = "select count(p.id) from Participant p "+
				"where p.paymentAmount is not null ";
			Query q = ses.createQuery(qs);
			List<Object> list = q.list();
			if (list != null) {
				if (list.size() > 0) {
					result = (Long) list.get(0);
				}
			}
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return (result == null ? 0 : result.intValue());
	}
	
	@SuppressWarnings("unchecked")
	public static Double countPaymentTotal(Session ses)
			throws OrmException {
		Double result = null;
		try {
			String qs = "select sum(p.paymentAmount) from Participant p "+
				"where p.paymentAmount is not null";
			Query q = ses.createQuery(qs);
			List<Object> list = q.list();
			if (list != null) {
				if (list.size() > 0) {
					result = (Double) list.get(0);
				}
			}
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return result;
	}
}
