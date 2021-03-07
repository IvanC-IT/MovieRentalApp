package it.course.exam.myfilmc4IVAN.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmResponse {

	private String filmId;

	private String description;

	private int releaseYear;

	private String title;

	private String countryName;

	private String languageName;

}
