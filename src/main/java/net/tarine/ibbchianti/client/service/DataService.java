package net.tarine.ibbchianti.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import net.tarine.ibbchianti.shared.PropertyBean;
import net.tarine.ibbchianti.shared.SystemException;
import net.tarine.ibbchianti.shared.entity.Discount;
import net.tarine.ibbchianti.shared.entity.Participant;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("dataService")
public interface DataService extends RemoteService {
	
	//Properties
	public PropertyBean getPropertyBean() throws SystemException;
	
	//Config
	//public Config findConfigByKey(String key) throws SystemException;
	//public void saveOrUpdateConfig(Config config) throws SystemException;

	//Participants
	public Participant findParticipantById(Integer id) throws SystemException;
	public Participant findParticipantByItemNumber(String itemNumber, int delayMillis) throws SystemException;
	public List<Participant> findParticipants(boolean confirmed) throws SystemException;
	public Participant createTransientParticipant() throws SystemException;
	public Participant saveOrUpdateParticipant(Participant prt) throws SystemException;
	//public Integer countConfirmed(int accommodationType) throws SystemException;
	//public Double countPaymentTotal() throws SystemException;
	
	//Discount
	public List<Discount> findDiscounts() throws SystemException;
	public Boolean canHaveDiscount(String email) throws SystemException;
	
}
