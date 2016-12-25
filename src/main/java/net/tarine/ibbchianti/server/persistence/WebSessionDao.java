package net.tarine.ibbchianti.server.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.TimestampType;

import net.tarine.ibbchianti.shared.OrmException;
import net.tarine.ibbchianti.shared.entity.WebSession;

public class WebSessionDao {
	
	@SuppressWarnings("unchecked")
	public static Integer getQueuePosition(Session ses, String idWebSession) 
			throws OrmException {
		Integer result = null;
		try {
			WebSession ws = GenericDao.findById(ses, WebSession.class, idWebSession);
			String qs = "select count(ws.id) from WebSession ws where "+
					"ws.time < :t1 "+
					"order by ws.time asc";
			Query q = ses.createQuery(qs);
			q.setParameter("t1", ws.getTime(), TimestampType.INSTANCE);
			List<Long> countList = (List<Long>) q.list();
			if (countList != null) {
				if (countList.size() > 0) result = countList.get(0).intValue();
			}
		} catch (HibernateException e) {
			throw new OrmException(e.getMessage(), e);
		}
		return result;
	}
	
}
