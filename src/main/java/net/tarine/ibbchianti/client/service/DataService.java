package net.tarine.ibbchianti.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import net.tarine.ibbchianti.shared.ConfigBean;
import net.tarine.ibbchianti.shared.SystemException;
import net.tarine.ibbchianti.shared.entity.Config;
import net.tarine.ibbchianti.shared.entity.Participant;
import net.tarine.ibbchianti.shared.entity.WebSession;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("dataService")
public interface DataService extends RemoteService {
	
	//Config
	public ConfigBean getConfigBean() throws SystemException;
	public Config findConfigByKey(String key) throws SystemException;
	//public void saveOrUpdateConfig(Config config) throws SystemException;

	//Participants
	public Participant findParticipantById(Integer id) throws SystemException;
	public Participant findParticipantByItemNumber(String itemNumber, int delayMillis) throws SystemException;
	public List<Participant> findParticipants(boolean confirmed) throws SystemException;
	//public Participant createTransientParticipant() throws SystemException;
	public Participant saveOrUpdateParticipant(Participant prt) throws SystemException;
	public Integer countConfirmed() throws SystemException;

	//WebSession
	public WebSession createWebSession(String seed) throws SystemException;
	public Boolean verifyWebSession(String idWebSession) throws SystemException;
	public Integer getQueuePosition(String idWebSession) throws SystemException;
	
	//Payment
	public String payWithStripe(String amount, String number, String expMonth, String expYear)
		throws SystemException;
}
