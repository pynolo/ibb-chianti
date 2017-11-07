package it.burningboots.greeter.client.service;

import it.burningboots.greeter.shared.Amount;
import it.burningboots.greeter.shared.ConfigBean;
import it.burningboots.greeter.shared.SystemException;
import it.burningboots.greeter.shared.entity.Config;
import it.burningboots.greeter.shared.entity.Level;
import it.burningboots.greeter.shared.entity.Participant;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("dataService")
public interface DataService extends RemoteService {
	
	//Config
	public ConfigBean getConfigBean() throws SystemException;
	public Config findConfigByKey(String key) throws SystemException;
	//public void saveOrUpdateConfig(Config config) throws SystemException;
	public Level getCurrentLevel() throws SystemException;
	
	//Participants
	public Participant findParticipantById(Integer id) throws SystemException;
	public Participant findParticipantByItemNumber(String itemNumber, int delayMillis) throws SystemException;
	public List<Participant> findParticipants(boolean confirmed, String orderBy) throws SystemException;
	public Participant saveOrUpdateParticipant(Participant prt) throws SystemException;
	public Integer countConfirmed() throws SystemException;

	//Payment
	public String payWithStripe(String itemNumber, Amount amount, String number,
			String expMonth, String expYear) throws SystemException;

	//WebSession
	public String createWebSession(String seed) throws SystemException;
	public Boolean verifyWebSession(String idWebSession) throws SystemException;
	public Integer getQueuePosition(String idWebSession) throws SystemException;
	public Date updateHeartbeat(String idWebSession) throws SystemException;
	public Boolean deleteWebSession(String idWebSession) throws SystemException;
	
}
