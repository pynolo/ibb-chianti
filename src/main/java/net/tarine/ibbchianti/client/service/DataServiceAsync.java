package net.tarine.ibbchianti.client.service;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import net.tarine.ibbchianti.shared.entity.Discount;
import net.tarine.ibbchianti.shared.entity.Participant;

public interface DataServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see net.tarine.ibbchianti.client.service.DataService
     */
    void getPropertyBean( AsyncCallback<net.tarine.ibbchianti.shared.PropertyBean> callback );
	//void findConfigByKey( java.lang.String key, AsyncCallback<net.tarine.ibbchianti.shared.entity.Config> callback );
    //void saveOrUpdateConfig(Config config, AsyncCallback<Void> callback);

    void findParticipantById( java.lang.Integer id, AsyncCallback<net.tarine.ibbchianti.shared.entity.Participant> callback );
    void findParticipantByItemNumber( java.lang.String itemNumber, int delayMillis, AsyncCallback<net.tarine.ibbchianti.shared.entity.Participant> callback );
    void findParticipants( boolean confirmed, AsyncCallback<java.util.List<net.tarine.ibbchianti.shared.entity.Participant>> callback );
    void createTransientParticipant( AsyncCallback<net.tarine.ibbchianti.shared.entity.Participant> callback );
    void saveOrUpdateParticipant( net.tarine.ibbchianti.shared.entity.Participant prt, AsyncCallback<Participant> callback );
	//void countConfirmed(int accommodationType, AsyncCallback<Integer> callback);
	//void countPaymentTotal(AsyncCallback<Double> callback);
	
	void findDiscounts(AsyncCallback<List<Discount>> callback);
    void canHaveDiscount(String email, AsyncCallback<Boolean> callback);
    
    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static DataServiceAsync instance;

        public static final DataServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (DataServiceAsync) GWT.create( DataService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }

}
