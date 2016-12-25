package net.tarine.ibbchianti.shared.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.tarine.ibbchianti.shared.AppConstants;

@Entity
@Table(name = "discount")
public class Discount implements Serializable {
	private static final long serialVersionUID = -3252726838205397883L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "email", length = 64, nullable = false)
	private String email = "";
	@Column(name = "tickets")
	private Integer tickets = AppConstants.ACCOMMODATION_HUT;
	@Column(name = "note", length = 256)
	private String note = "";
	
	public Discount() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getTickets() {
		return tickets;
	}

	public void setTickets(Integer tickets) {
		this.tickets = tickets;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Discount)) {
            return false;
        }
        Discount other = (Discount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Discount[id=" + id + "]";
    }
}
