package it.burningboots.greeter.shared.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name = "level")
public class Level implements IsSerializable {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 4342114560470488145L;
	
	@Id
    @Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "name", length = 64)
	private String name = "";
	@Basic(optional = false)
	@Column(name = "price", nullable = false)
	private Double price;
	@Basic(optional = false)
    @Temporal(TemporalType.DATE)
	@Column(name = "last_date", nullable = false)
	private Date lastDate = null;
	@Basic(optional = false)
	@Column(name = "last_count", nullable = false)
	private Integer lastCount = null;
	
	public Level() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public Integer getLastCount() {
		return lastCount;
	}

	public void setLastCount(Integer lastCount) {
		this.lastCount = lastCount;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Level)) {
            return false;
        }
        Level other = (Level) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Level[id=" + id + "]";
    }
}
