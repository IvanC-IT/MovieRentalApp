package it.course.exam.myfilmc4IVAN.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilmc4IVAN.entity.Customer;
import it.course.exam.myfilmc4IVAN.entity.Film;
import it.course.exam.myfilmc4IVAN.entity.Language;
import it.course.exam.myfilmc4IVAN.entity.Rental;
import it.course.exam.myfilmc4IVAN.entity.RentalId;
import it.course.exam.myfilmc4IVAN.entity.Store;
import it.course.exam.myfilmc4IVAN.payload.request.RentalRequest;
import it.course.exam.myfilmc4IVAN.payload.response.ApiResponseCustom;
import it.course.exam.myfilmc4IVAN.payload.response.CustomerResponse;
import it.course.exam.myfilmc4IVAN.payload.response.FilmRentResponse;
import it.course.exam.myfilmc4IVAN.payload.response.FilmRentResponseNr;
import it.course.exam.myfilmc4IVAN.payload.response.ResponseEntityHandler;
import it.course.exam.myfilmc4IVAN.repository.ActorRepository;
import it.course.exam.myfilmc4IVAN.repository.CustomerRepository;
import it.course.exam.myfilmc4IVAN.repository.FilmRepository;
import it.course.exam.myfilmc4IVAN.repository.LanguageRepository;
import it.course.exam.myfilmc4IVAN.repository.RentalRepository;
import it.course.exam.myfilmc4IVAN.repository.StoreRepository;
import it.course.exam.myfilmc4IVAN.service.RentalService;

@RestController
@Validated
public class RentalController {

	@Autowired
	FilmRepository filmRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired
	RentalService rentalService;
	@Autowired
	RentalRepository rentalRepository;
	@Autowired
	ActorRepository actorRepository;
	@Autowired
	CustomerRepository customerRepository;

	@PostMapping("/add-update-rental")
	
	public ResponseEntity<ApiResponseCustom> addUpdateFilm(@RequestBody @Valid RentalRequest rentalRequest,
			HttpServletRequest request) {
		/*
		 * Questa implentazione prevede l'iserimento del nuovo rental settando di default la colonna
		 * rental_return a Null.
		 * 
		 * Richimando la seconda volta il controller sullo stesso record la Rental_return verrà settata alla data attuale.
		 * 
		 * Richiamando una terza volta il controller sullo stesso record impedirà il noleggio dello stesso film nello stesso giorno
		 */
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		//Verifico che il film esista
		Optional<Film> filmId = filmRepository.findById(rentalRequest.getFilmId().toUpperCase());
		if (!filmId.isPresent()) {
			response.setMsg("Not Film Found !");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		//Verifico che lo Store esista
		Optional<Store> storeId = storeRepository.findById(rentalRequest.getStoreId().toUpperCase());
		if (!storeId.isPresent()) {
			response.setMsg("Not Store Found !");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		//Verifico che l'email del Customer esista
		Optional<Customer> email = customerRepository.findById(rentalRequest.getCustomerEmail().toLowerCase());
		if (!email.isPresent()) {
			response.setMsg("Not email Found !");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		//Verifico che il film sia presente nello Store del rental
		if (!filmRepository.existsByStoresAndFilmId(storeId.get(), filmId.get().getFilmId())) {
			response.setMsg("Film not present in store ");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		//Controllo che il film non sia gia stato affittato 
		Optional<Rental> checkDate = rentalRepository.checkValueRetuturnalDate(email.get().getEmail(),
				filmId.get().getFilmId(), storeId.get().getStoreId());
		
		//Controllo se esiste un rental
		if (!checkDate.isPresent()) {
			Rental rental = new Rental(new RentalId(new Date(), email.get(), filmId.get(), storeId.get()));

			try {//se il rental del film è stato chiuso nello stesso giorno 
				rentalRepository.save(rental);
			} catch (Exception e) {
				response.setMsg("You can't rent same film two times in same day ");
				response.setStatus(HttpStatus.OK);
				return response.getResponseEntity();
			}//se il rental non esiste lo creo
			response.setMsg("New Rental added");
			response.setStatus(HttpStatus.OK);
		} else {//se il rental è aperto lo chiudo ( perchè il rental_return è null)
			checkDate.get().setRentalRetun(new Date());
			rentalRepository.save(checkDate.get());
			response.setMsg(" Rental update successful");
			response.setStatus(HttpStatus.OK);

		}

		return response.getResponseEntity();

	}

	@GetMapping("/get-rentals-number-in-date-range")
	public ResponseEntity<ApiResponseCustom> getRentalsNumberInDataRange(
			@RequestParam @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") Date begDate,
			@RequestParam @Valid @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		if (endDate.before(begDate)) {
			response.setMsg("The end date is before of the start date !!");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}
		int total = rentalRepository.getRentalInDateRange(begDate, rentalService.adjustDate(endDate));
		response.setMsg("Total is: " + total);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
	}

	@GetMapping("/find-all-films-rent-by-one-customer/{customerId}")
	public ResponseEntity<ApiResponseCustom> findAllFilmsRentByOneCustomer(
			@PathVariable @NotEmpty @Email String customerId, HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		Optional<Customer> email = customerRepository.findById(customerId);
		if (!email.isPresent()) {
			response.setMsg("Not Customer Found !");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		List<FilmRentResponse> filmResp = rentalRepository.getAllFilmsRentByOneCustomer(customerId);
		response.setMsg(filmResp);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();

	}

	// @GetMapping("/find-film-with-max-number-of-rent")
	@GetMapping("/find-film-with-max-number-of-rent")
	public ResponseEntity<ApiResponseCustom> findFilmWithMaxNumberOfRent(HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		List<FilmRentResponseNr> filmRespNr = rentalRepository.filmMaxNumberOfRent();
		response.setMsg(filmRespNr.get(0));
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();

	}

	// @GetMapping("/get-customers-who-rent-films-by-language-film/{languageId}")
	@GetMapping("/get-customers-who-rent-films-by-language-film/{languageId}")
	public ResponseEntity<ApiResponseCustom> getCustomerWhoRentFilmsByLanguageFilm(
			@PathVariable @NotEmpty String languageId, HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		Optional<Language> language = languageRepository.findById(languageId);
		if (!language.isPresent()) {
			response.setMsg("Language not found !");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		List<CustomerResponse> filmLanguage = languageRepository.getCustomerRentFilmsByLanguageFilm(languageId);
		response.setMsg(filmLanguage);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();

	}

}
