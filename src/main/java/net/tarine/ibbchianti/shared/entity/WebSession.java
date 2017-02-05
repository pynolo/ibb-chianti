package net.tarine.ibbchianti.shared.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "web_session")
public class WebSession implements Serializable {
	private static final long serialVersionUID = 113261555593379988L;
	
	@Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false, length = 256)
	private String id;
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_dt", nullable = false)
	private Date creationDt = null;
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "heartbeat_dt", nullable = false)
	private Date heartbeatDt = null;
	
	public WebSession() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Date getCreationDt() {
		return creationDt;
	}

	public void setCreationDt(Date creationDt) {
		this.creationDt = creationDt;
	}

	public Date getHeartbeatDt() {
		return heartbeatDt;
	}

	public void setHeartbeatDt(Date heartbeatDt) {
		this.heartbeatDt = heartbeatDt;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WebSession)) {
            return false;
        }
        WebSession other = (WebSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WebSession[id=" + id + "]";
    }
}
