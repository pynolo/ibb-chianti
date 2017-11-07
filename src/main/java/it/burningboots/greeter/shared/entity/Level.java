package it.burningboots.greeter.shared.entity;

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
@Table(name = "config")
public class Level implements Serializable {
	private static final long serialVersionUID = 2573558514344018622L;
	
	@Id
    @Column(name = "id", nullable = false)
	private Integer id;
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
