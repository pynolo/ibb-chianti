package it.burningboots.greeter.shared.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ipn_response")
public class IpnResponse implements Serializable {
	private static final long serialVersionUID = 6665621291812512821L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "item_number", length = 64)
    private String itemNumber;
	@Column(name = "payment_status", length = 64)
    private String paymentStatus;
	@Column(name = "payer_email", length = 64)
    private String payerEmail;
	@Column(name = "mc_gross", length = 64)
    private String mcGross;
	@Column(name = "mc_currency", length = 64)
    private String mcCurrency;
	@Column(name = "payment_date", length = 64)
    private String paymentDate;
	@Column(name = "pending_reason", length = 64)
    private String pendingReason;
	@Column(name = "payment_type", length = 64)
    private String paymentType;
	
	@JoinColumn(name = "id_participant", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Participant.class)
	private Participant participant;
	
	
	public IpnResponse() {
	}
	
	public IpnResponse(String itemNumber, String paymentStatus, String payerEmail, String mcGross,
			String mcCurrency, String paymentDate, String pendingReason, String paymentType,
			Participant participant) {
		this.itemNumber=itemNumber;
		this.paymentStatus=paymentStatus;
		this.payerEmail=payerEmail;
		this.mcGross=mcGross;
		this.mcCurrency=mcCurrency;
		this.paymentDate=paymentDate;
		this.pendingReason=pendingReason;
		this.paymentType=paymentType;
		this.participant=participant;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public String getMcGross() {
		return mcGross;
	}

	public void setMcGross(String mcGross) {
		this.mcGross = mcGross;
	}

	public String getMcCurrency() {
		return mcCurrency;
	}

	public void setMcCurrency(String mcCurrency) {
		this.mcCurrency = mcCurrency;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPendingReason() {
		return pendingReason;
	}

	public void setPendingReason(String pendingReason) {
		this.pendingReason = pendingReason;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IpnResponse)) {
            return false;
        }
        IpnResponse other = (IpnResponse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IpnResponse[id=" + id + "]";
    }
}
