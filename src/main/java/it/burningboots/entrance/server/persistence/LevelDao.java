package it.burningboots.entrance.server.persistence;

import it.burningboots.entrance.shared.OrmException;
import it.burningboots.entrance.shared.entity.Level;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class LevelDao {
	
	@SuppressWarnings("unchecked")
	public static List<Level> findAll(Session ses)
			throws OrmException {
		List<Level> entities = new ArrayList<Level>();
		try {
			String qs = "from Level l order by id asc";
			Query q = ses.createQuery(qs);
			entities = (List<Level>) q.list();
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return entities;
	}
}
