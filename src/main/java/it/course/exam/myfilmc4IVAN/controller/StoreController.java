package it.course.exam.myfilmc4IVAN.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilmc4IVAN.entity.Film;
import it.course.exam.myfilmc4IVAN.entity.Store;
import it.course.exam.myfilmc4IVAN.payload.request.StoreRequest;
import it.course.exam.myfilmc4IVAN.payload.response.ApiResponseCustom;
import it.course.exam.myfilmc4IVAN.payload.response.ResponseEntityHandler;
import it.course.exam.myfilmc4IVAN.repository.FilmRepository;
import it.course.exam.myfilmc4IVAN.repository.StoreRepository;

@RestController
@Validated
public class StoreController {

	@Autowired
	StoreRepository storeRepository;
	@Autowired
	FilmRepository filmRepository;

	
	@PostMapping("/add-update-store")
	@Transactional
	public ResponseEntity<ApiResponseCustom> addUpdateStore(@RequestBody @Valid StoreRequest storeRequest,
			HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		response.setMsg("New Store successfully added ");
		Optional<Store> namesStore = storeRepository.findByStoreName(storeRequest.getStoreName());
		Optional<Store> storeId = storeRepository.findById(storeRequest.getStoreId().toUpperCase());
		if (namesStore.isPresent()) {
			response.setStatus(HttpStatus.FOUND);
			response.setMsg("An Store with this name already exist !");
		} else if (storeId.isPresent()) {
			response.setStatus(HttpStatus.FOUND);
			storeId.get().setStoreName(storeRequest.getStoreName().toUpperCase());
			storeRepository.save(storeId.get());
			response.setMsg("Strore successfully updated !");
		} else {
			if (storeRequest.getStoreId().toUpperCase().startsWith("STORE")) {
				storeRepository.save(
						new Store(storeRequest.getStoreId().toUpperCase(), storeRequest.getStoreName().toUpperCase()));
			} else {
				response.setStatus(HttpStatus.FORBIDDEN);
				response.setMsg("Store name have to start with STORE ");
			}
		}
		return response.getResponseEntity();

	}

	@PostMapping("/add-film-to-store/{storeId}/{filmId}")
	public ResponseEntity<ApiResponseCustom> addFilmToStore(@PathVariable @NotEmpty String storeId,
			@PathVariable @Valid String filmId, HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		Optional<Store> store = storeRepository.findById(storeId);
		Optional<Film> film = filmRepository.findById(filmId);
		if (!store.isPresent()) {
			response.setMsg("Not Store Found !");
			response.setStatus(HttpStatus.NOT_FOUND);
		}
		if (!film.isPresent()) {
			response.setMsg("Film Not Found !");
			response.setStatus(HttpStatus.NOT_FOUND);
		}
		try {// se il film esiste già nello store, gestisco l'eccezione e setto il messaggio
			if (store.isPresent() && film.isPresent()) {
				storeRepository.addFilmToStore(storeId.toUpperCase(), filmId.toUpperCase());
				response.setMsg("Film " + filmId + " successfully added to store " + storeId);
				response.setStatus(HttpStatus.OK);
			}
		} catch (Exception e) {

			response.setMsg("The " + store.get().getStoreId() + " already have this Film ! ");
			response.setStatus(HttpStatus.FORBIDDEN);
		}

		return response.getResponseEntity();
	}
}
