package net.tarine.ibbchianti.server.persistence;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.TimestampType;

import net.tarine.ibbchianti.shared.AppConstants;
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
					"ws.creationDt < :t1 and "+//precedenti a questa
					"ws.creationDt > :t2 and "+//maggiori di ttl
					"ws.heartbeatDt > :t3 "+ //heartbeat maggiore di h-ttl
					"order by ws.creationDt asc";
			Query q = ses.createQuery(qs);
			Long now = new Date().getTime();
			q.setParameter("t1", ws.getCreationDt(), TimestampType.INSTANCE);
			q.setParameter("t2", new Date(now-AppConstants.WEBSESSION_TTL));
			q.setParameter("t3", new Date(now-AppConstants.HEARTBEAT_TTL));
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
