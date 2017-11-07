package it.burningboots.greeter.shared.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "config")
public class Config implements Serializable {
	private static final long serialVersionUID = 2573558514344018622L;
	
	@Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false, length = 32)
	private String id;
	@Basic(optional = false)
	@Column(name = "val", nullable = false, length = 256)
	private String val;
	
	public Config() {
	}
	

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
	
	
	
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Config[id=" + id + "]";
    }
}
