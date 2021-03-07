package it.course.exam.myfilmc4IVAN.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmRequest {

	@NotEmpty 
	@NotBlank
	@Size(min = 1, max = 10)
	private String filmId;

	@NotEmpty 
	@Size(min = 1, max = 255)
	private String description;

	@Min(1900)
	@Max(2021) // max a scelta, potrei implem. per una data futura
	private int releaseYear;

	@NotEmpty 
	@Size(min = 1, max = 128)
	private String title;

	@NotEmpty
	@NotBlank
	@Size(min = 2, max = 2)
	private String countryId;

	@NotEmpty
	@NotBlank
	@Size(min = 2, max = 2)
	private String languageId;

}
