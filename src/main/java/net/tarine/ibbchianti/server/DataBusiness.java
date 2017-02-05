package net.tarine.ibbchianti.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import net.tarine.ibbchianti.shared.SystemException;

public class DataBusiness {

	public static String escape(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	
	public static String createCode(String seed, int size) throws SystemException {
		if (seed==null) seed="";
		seed += new Date().getTime();
		String md5;
		try {
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//Update input string in message digest
			digest.update(seed.getBytes(), 0, seed.length());
			//Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);
			md5+="000000000000000000000000000000";
		} catch (NoSuchAlgorithmException e) {
			throw new SystemException(e.getMessage(), e);
		}
		String code = md5.substring(0, size);
		return code.toUpperCase();
	}
	
//	public static Integer countConfirmedParticipants() {
//		Integer count = null;
//		Session ses = HibernateSessionFactory.getSession();
//		try {
//			count = new ParticipantsDao().countConfirmedParticipants(ses);
//		} catch (SystemException e) {
//			throw new SystemException(e.getMessage(), e);
//		} finally {
//			ses.close();
//		}
//		return count;
//		return null;
//	}
	
//	public static Integer countReducedTickets() {
//		Integer reducedCount = null;
//		Session ses = HibernateSessionFactory.getSession();
//		try {
//			Config priceConfig = new ConfigDao().findByName(ses, AppConstants.CONFIG_PRICE_FOREIGNER_TICKET);
//			reducedCount = new ParticipantsDao().countTicketsByAmount(ses, priceConfig.getVal());
//		} catch (SystemException e) {
//			throw new SystemException(e.getMessage(), e);
//		} finally {
//			ses.close();
//		}
//		return reducedCount;
//		return null;
//	}
}
