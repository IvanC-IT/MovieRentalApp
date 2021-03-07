package it.course.exam.myfilmc4IVAN.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data@AllArgsConstructor@NoArgsConstructor
public class RentalId implements Serializable{

	
	

	private static final long serialVersionUID = 1L;

	@Column(name="rental_date", nullable=false, columnDefinition="DATE")
		private Date rentalDate;
	
	@ManyToOne
	@JoinColumn(name = "customer_email", nullable = false)
	private Customer customerEmail;
	
	@ManyToOne
	@JoinColumn(name = "film_id", nullable = false)
	private Film filmId;
	
	@ManyToOne
	@JoinColumn(name = "store_id", nullable = false)
	private Store storeId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RentalId other = (RentalId) obj;
		if (customerEmail == null) {
			if (other.customerEmail != null)
				return false;
		} else if (!customerEmail.equals(other.customerEmail))
			return false;
		if (filmId == null) {
			if (other.filmId != null)
				return false;
		} else if (!filmId.equals(other.filmId))
			return false;
		if (rentalDate == null) {
			if (other.rentalDate != null)
				return false;
		} else if (!rentalDate.equals(other.rentalDate))
			return false;
		if (storeId == null) {
			if (other.storeId != null)
				return false;
		} else if (!storeId.equals(other.storeId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerEmail == null) ? 0 : customerEmail.hashCode());
		result = prime * result + ((filmId == null) ? 0 : filmId.hashCode());
		result = prime * result + ((rentalDate == null) ? 0 : rentalDate.hashCode());
		result = prime * result + ((storeId == null) ? 0 : storeId.hashCode());
		return result;
	}

	


}


