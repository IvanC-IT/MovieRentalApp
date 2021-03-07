package it.course.exam.myfilmc4IVAN.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="country")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Country {
	
	
	@Id
	@Column(name="country_id",nullable=false, length=2)
	private String countryId;
	
	@Column(name="country_name",nullable=false, length=40, unique= true)
	private String countryName;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		return true;
	}

	
	

	

	
	
}
