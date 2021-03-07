package it.course.exam.myfilmc4IVAN.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalRequest {

	@Email @NotEmpty
	private String customerEmail;

	@NotEmpty
	private String storeId;

	@NotEmpty
	private String filmId;

}
