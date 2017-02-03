package net.tarine.ibbchianti.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import net.tarine.ibbchianti.shared.ConfigBean;
import net.tarine.ibbchianti.shared.entity.Config;
import net.tarine.ibbchianti.shared.entity.Participant;
import net.tarine.ibbchianti.shared.entity.WebSession;

public interface DataServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see net.tarine.ibbchianti.client.service.DataService
     */
    
	void getConfigBean(AsyncCallback<ConfigBean> callback );
	void findConfigByKey(String key, AsyncCallback<Config> callback );
    //void saveOrUpdateConfig(Config config, AsyncCallback<Void> callback);

    void findParticipantById( java.lang.Integer id, AsyncCallback<Participant> callback );
    void findParticipantByItemNumber( java.lang.String itemNumber, int delayMillis, AsyncCallback<Participant> callback );
    void findParticipants( boolean confirmed, AsyncCallback<java.util.List<Participant>> callback );
    //void createTransientParticipant( AsyncCallback<Participant> callback );
    void saveOrUpdateParticipant(Participant prt, AsyncCallback<Participant> callback );
	void countConfirmed(AsyncCallback<Integer> callback);
	//void countPaymentTotal(AsyncCallback<Double> callback);
	
	//void findDiscounts(AsyncCallback<List<Discount>> callback);
    //void canHaveDiscount(String email, AsyncCallback<Boolean> callback);
    
    //WebSession
	void createWebSession(String seed, AsyncCallback<WebSession> callback);
	void verifyWebSession(String idWebSession, AsyncCallback<Boolean> callback);
	void getQueuePosition(String idWebSession, AsyncCallback<Integer> callback);
	
	//Payment
	void payWithStripe(String itemNumber, Double amount, String number,
			String expMonth, String expYear, AsyncCallback<String> callback);
	
    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util { 
        private static DataServiceAsync instance;

        public static final DataServiceAsync getInstance() {
            if ( instance == null ) {
                instance = (DataServiceAsync) GWT.create( DataService.class );
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instantiated
        }
    }

}
