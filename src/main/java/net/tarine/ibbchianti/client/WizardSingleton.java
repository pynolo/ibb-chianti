package net.tarine.ibbchianti.client;

import net.tarine.ibbchianti.shared.PropertyBean;
import net.tarine.ibbchianti.shared.entity.Participant;
import net.tarine.ibbchianti.shared.entity.WebSession;

public class WizardSingleton {

	private static WizardSingleton instance = null;
	private Participant participantBean = null;
	private PropertyBean propertyBean = null;
	private String webSessionId = null;
	
	private WizardSingleton() {}
	
	public static WizardSingleton get() {
		if (instance == null) {
			instance = new WizardSingleton();
			instance.setParticipantBean(new Participant());
		}
		return instance;
	}

	public Participant getParticipantBean() {
		return participantBean;
	}

	public void setParticipantBean(Participant participantBean) {
		this.participantBean = participantBean;
	}

	public PropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(PropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}

	public String getWebSessionId() {
		return webSessionId;
	}

	public void setWebSessionId(String webSessionId) {
		this.webSessionId = webSessionId;
	}

}
