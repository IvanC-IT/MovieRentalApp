package it.course.exam.myfilmc4IVAN.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import it.course.exam.myfilmc4IVAN.entity.Film;
import it.course.exam.myfilmc4IVAN.payload.response.FilmResponse;

@Service
public class FilmService {
	
	
	
	public  List<FilmResponse> mapToFilmResponse(List<Film> listFilm)
	{	List<FilmResponse> listResponse = new  ArrayList<FilmResponse>();
		for (Film film : listFilm) {
				FilmResponse buildResp = new FilmResponse();
				buildResp.setFilmId(film.getFilmId());
				buildResp.setDescription(film.getDescription());
				buildResp.setReleaseYear(film.getReleaseYear());
				buildResp.setTitle(film.getTitle());
				buildResp.setCountryName(film.getCountryId().getCountryName());
				buildResp.setLanguageName(film.getLanguageId().getLanguageName());
				listResponse.add(buildResp);
	
				}
		
		return listResponse;
	}
	
	
	
}


