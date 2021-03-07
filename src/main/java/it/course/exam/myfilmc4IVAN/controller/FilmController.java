package it.course.exam.myfilmc4IVAN.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilmc4IVAN.entity.Actor;
import it.course.exam.myfilmc4IVAN.entity.Country;
import it.course.exam.myfilmc4IVAN.entity.Film;
import it.course.exam.myfilmc4IVAN.entity.Language;
import it.course.exam.myfilmc4IVAN.payload.request.FilmRequest;
import it.course.exam.myfilmc4IVAN.payload.response.ApiResponseCustom;
import it.course.exam.myfilmc4IVAN.payload.response.FilmInStoreResponse;
import it.course.exam.myfilmc4IVAN.payload.response.FilmResponse;
import it.course.exam.myfilmc4IVAN.payload.response.ResponseEntityHandler;
import it.course.exam.myfilmc4IVAN.repository.ActorRepository;
import it.course.exam.myfilmc4IVAN.repository.CountryRepository;
import it.course.exam.myfilmc4IVAN.repository.FilmRepository;
import it.course.exam.myfilmc4IVAN.repository.LanguageRepository;
import it.course.exam.myfilmc4IVAN.service.FilmService;

@RestController
@Validated
public class FilmController {

	@Autowired
	FilmRepository filmRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired
	FilmService filmService;
	@Autowired
	ActorRepository actorRepository;

	@PostMapping("/add-update-film")
	@Transactional
	public ResponseEntity<ApiResponseCustom> addUpdateFilm(@RequestBody @Valid FilmRequest filmRequest,
			HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);

		Optional<Film> filmId = filmRepository.findById(filmRequest.getFilmId().toUpperCase());

		Optional<Country> countryId = countryRepository.findById(filmRequest.getCountryId().toUpperCase());
		if (countryId.isEmpty()) {
			response.setMsg("Country code not found.");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		Optional<Language> languageId = languageRepository.findById(filmRequest.getLanguageId().toUpperCase());
		if (languageId.isEmpty()) {
			response.setMsg("Languge code not found.");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		System.out.println(languageId.get().getLanguageId());
		System.out.println(countryId.get().getCountryId());
		if (filmId.isPresent()) {
			filmId.get().setTitle(filmRequest.getTitle().toUpperCase());
			filmId.get().setDescription(filmRequest.getDescription());
			filmId.get().setReleaseYear(filmRequest.getReleaseYear());
			filmId.get().setCountryId(countryId.get());
			filmId.get().setLanguageId(languageId.get());
			filmRepository.save(filmId.get());
			response.setMsg("Film successfully updated !");
			response.setStatus(HttpStatus.OK);
			return response.getResponseEntity();
		} else {
			if (filmRequest.getFilmId().toUpperCase().startsWith("FILM")) {
				filmRepository.save(new Film(filmRequest.getFilmId().toUpperCase(),
						filmRequest.getDescription().toUpperCase(), filmRequest.getReleaseYear(),
						filmRequest.getTitle().toUpperCase(), countryId.get(), languageId.get()));
				response.setMsg("Film successfully added!");
				response.setStatus(HttpStatus.OK);
			} else {
				response.setMsg("The filmId have to start with FILM ");
				response.setStatus(HttpStatus.FORBIDDEN);
			}
		}

		return response.getResponseEntity();

	}

	@GetMapping("/get-film/{filmId}")
	public ResponseEntity<ApiResponseCustom> getFilm(@PathVariable  @NotEmpty String filmId, HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		response.setMsg("Film not Found!");
		response.setStatus(HttpStatus.NOT_FOUND);

		boolean checkFilm = filmRepository.existsById(filmId);

		if (checkFilm) {
			FilmResponse film = filmRepository.getFilmDetails(filmId);
			response.setMsg(film);
			response.setStatus(HttpStatus.FOUND);
			return response.getResponseEntity();
		}

		return response.getResponseEntity();

	}

	@GetMapping("/find-film-in store/{filmId}")
	public ResponseEntity<ApiResponseCustom> findFilmInStore(@PathVariable @NotEmpty String filmId, HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		response.setMsg("Film not found in stores !");
		response.setStatus(HttpStatus.NOT_FOUND);
		boolean checkFilm = filmRepository.existsById(filmId);
		if (checkFilm) {
			List<FilmInStoreResponse> filmInStore = filmRepository.getFilmInStore(filmId);
			if(filmInStore.isEmpty()) {
				response.setMsg(filmId +" will be added in store soon !");
				response.setStatus(HttpStatus.NOT_FOUND);
				return response.getResponseEntity();
			}
			response.setMsg(filmInStore);
			response.setStatus(HttpStatus.FOUND);
		}
		return response.getResponseEntity();
	}

	@GetMapping("/find-films-by-actors")
	public ResponseEntity<ApiResponseCustom> findFilmsByActors(@RequestParam @NotEmpty List<String> actorsLastname,
			HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		if (actorsLastname.isEmpty()) {

			response.setMsg("List cannot be Empty!");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}

		List<Actor> actorsListLastname = actorRepository.findAllByLastNameIn(actorsLastname);
		
		//per ogni attore e creo una lista di liste contenenti i film degli attori
		List<List<String>> aListName = actorsListLastname.stream().map(actor -> {
			List<String> filmList = filmRepository.getAllFilmIdByActorId(actor.getActorId());
			return filmList;
		}).collect(Collectors.toList());
		
	
		if (aListName.isEmpty()) {
			response.setMsg("Not Film Found with this actor !");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		List<String> result;
		if (aListName.size() == 1) {
			result = aListName.get(0);

		} else {
			//Se ci sono piu film faccio l'intersezione
			result = aListName.get(0);
			for (int i = 1; i < aListName.size(); i++) {
				List<String> listIntersection = aListName.get(i);
				// risultato dell' intersezione di tutte le liste
				result = result.stream().distinct().filter(listIntersection::contains).collect(Collectors.toList());
			}

		}

		if (result.isEmpty()) {
			response.setMsg("No Film found with this actors");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		// trasformo result in stream e trasformo ogni stringa nel relativo Film
		List<FilmResponse> resp = result.stream().map(filmId -> filmRepository.findById(filmId))
				.filter(film -> film.isPresent()).map(film -> {

					FilmResponse filmResponse = new FilmResponse();
					filmResponse.setFilmId(film.get().getFilmId());
					filmResponse.setTitle(film.get().getTitle());
					filmResponse.setDescription(film.get().getDescription());
					filmResponse.setReleaseYear(film.get().getReleaseYear());
					filmResponse.setCountryName(film.get().getCountryId().getCountryName());
					filmResponse.setLanguageName(film.get().getLanguageId().getLanguageName());
					return filmResponse;
				}).collect(Collectors.toList());

		response.setMsg(resp);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();

	}

	@GetMapping("/get-films-paged-by-title-asc")
	public ResponseEntity<ApiResponseCustom> getFilmsPagedByTitle(@RequestParam  @NotNull int elementOnPage,
			@RequestParam int startIndex, HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		Pageable page = PageRequest.of(startIndex, elementOnPage);
		List<FilmResponse> filmResponse = filmService.mapToFilmResponse(filmRepository.findAllByOrderByTitleAsc(page));

		response.setMsg(filmResponse);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
	}

	@GetMapping("/find-films-by-country/{countryId}")
	public ResponseEntity<ApiResponseCustom> findFilmByCountry(@PathVariable   @NotEmpty String countryId,
			HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		List<FilmResponse> filmByCountryId = filmRepository.getFilmByCountry(countryId);
		response.setMsg(filmByCountryId);
		response.setStatus(HttpStatus.FOUND);
		if (filmByCountryId.isEmpty()) {
			response.setMsg("No Film  found with this CountryId " + countryId);
			response.setStatus(HttpStatus.NOT_FOUND);
		}

		return response.getResponseEntity();
	}

	@GetMapping("/find-films-by-language/{languageId}")
	public ResponseEntity<ApiResponseCustom> findFilmByLanguage(@PathVariable @NotEmpty String languageId,
			HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		List<FilmResponse> filmByLanguageId = filmRepository.getFilmByLanguage(languageId);

		response.setMsg(filmByLanguageId);
		response.setStatus(HttpStatus.FOUND);
		if (filmByLanguageId.isEmpty()) {
			response.setMsg("No Film  found with this LanguageId " + languageId);
			response.setStatus(HttpStatus.NOT_FOUND);
		}

		return response.getResponseEntity();
	}

}
