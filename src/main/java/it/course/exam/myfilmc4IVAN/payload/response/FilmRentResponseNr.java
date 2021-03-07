package it.course.exam.myfilmc4IVAN.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  @AllArgsConstructor @NoArgsConstructor
public class FilmRentResponseNr {

	private String filmId;
	
	private String title;
	
	private long numberOfRents;
	
	
}
