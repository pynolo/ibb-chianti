package net.tarine.ibbchianti.client;

import net.tarine.ibbchianti.shared.ConfigBean;
import net.tarine.ibbchianti.shared.entity.Participant;

public class WizardSingleton {

	private static WizardSingleton instance = null;
	private Participant participantBean = null;
	private ConfigBean configBean = null;
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

	public String getWebSessionId() {
		return webSessionId;
	}

	public void setWebSessionId(String webSessionId) {
		this.webSessionId = webSessionId;
	}

	public ConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(ConfigBean configBean) {
		this.configBean = configBean;
	}

}
