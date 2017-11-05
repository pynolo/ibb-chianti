package it.burningboots.entrance.shared.entity;

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
	@Column(name = "price", nullable = false, length = 8)
	private String price;
	@Basic(optional = false)
    @Temporal(TemporalType.DATE)
	@Column(name = "max_date", nullable = false)
	private Date maxDate = null;
	@Basic(optional = false)
	@Column(name = "max_count", nullable = false)
	private Integer maxCount = null;
	
	public Level() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
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
