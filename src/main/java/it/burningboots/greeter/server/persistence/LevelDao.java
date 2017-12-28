package it.burningboots.greeter.server.persistence;

import it.burningboots.greeter.shared.LimitExceededException;
import it.burningboots.greeter.shared.OrmException;
import it.burningboots.greeter.shared.entity.Level;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class LevelDao {
	
	@SuppressWarnings("unchecked")
	public static List<Level> findAll(Session ses) throws OrmException {
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
	
	public static int getMaximumParticipants(Session ses) throws OrmException {
		int result = 0;
		List<Level> levels = findAll(ses);
		for (Level l:levels) {
			if (l.getLastCount() > result) result = l.getLastCount();
		}
		return result;
	}
	
	public static Level getCurrentLevel(Session ses, int participantCount, Date time)
			throws LimitExceededException, OrmException {
		//tolgo 24 ore a "time" perché espresso in datetime, mentre lastDate è solo date
		Calendar cal = new GregorianCalendar();
		cal.setTime(time);
		cal.add(Calendar.HOUR, -24);
		time = cal.getTime();
		
		Level result = null;
		List<Level> levels = findAll(ses);
		for (Level l:levels) {
			if (l.getLastDate().before(time) && l.getLastCount() <= participantCount) {
				if (result == null) {
					result = l;
				} else {
					//comunque sempre il prezzo inferiore
					if (result.getPrice() > l.getPrice()) result = l;
				}
			}
		}
		if (result == null) throw new LimitExceededException("No more available levels");
		return result;
	}
}
