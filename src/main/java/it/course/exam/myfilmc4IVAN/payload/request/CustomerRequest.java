package it.course.exam.myfilmc4IVAN.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

	@Email @NotEmpty
	@Size(max = 50)
	private String email;

	@Size(max = 45)
	private String firstName;

	@Size(max = 45)
	private String lastName;

}
