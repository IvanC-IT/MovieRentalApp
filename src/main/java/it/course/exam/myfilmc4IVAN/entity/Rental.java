package it.course.exam.myfilmc4IVAN.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rental")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

	@EmbeddedId
	private RentalId rentalId;

	@Column(name = "rental_return", columnDefinition = "DATE")
	private Date rentalRetun = null;

	public Rental(RentalId rentalId) {
		super();
		this.rentalId = rentalId;
	}

}
