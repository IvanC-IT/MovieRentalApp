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
@Table(name="customer")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Customer {
	
	
	@Id
	@Column(name="email",nullable=false, length=50)
	private String email;
	
	@Column(name="first_name", length=45)
	private String firstName = null;
	
	@Column(name="last_name",  length=45)
	private String lastName = null;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Customer other = (Customer) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	
	

}
