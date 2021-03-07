package it.course.exam.myfilmc4IVAN.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilmc4IVAN.entity.Film;
import it.course.exam.myfilmc4IVAN.entity.Store;
import it.course.exam.myfilmc4IVAN.payload.response.FilmInStoreResponse;
import it.course.exam.myfilmc4IVAN.payload.response.FilmResponse;

@Repository
public interface FilmRepository extends JpaRepository<Film, String> {

	boolean existsById(String filmId);

	
	List<Film> findDistinctAllByActorsLastNameIn(List<String> actorsLastName);

	@Query(value = "SELECT new it.course.exam.myfilmc4IVAN.payload.response.FilmResponse("
			+ "f.filmId, f.description, f.releaseYear, f.title, f.countryId.countryName, f.languageId.languageName "
			+ ") " 
			+ "FROM Film f WHERE f.filmId = :filmId")
	FilmResponse getFilmDetails(@Param("filmId") String filmId);

	@Query(value = "SELECT new it.course.exam.myfilmc4IVAN.payload.response.FilmInStoreResponse(" + "f.filmId,"
			+ "s.storeName) " 
			+ "FROM Film f " 
			+ "INNER JOIN f.stores ss "
			+ "INNER JOIN Store s ON ss.storeId = s.storeId " 
			+ "WHERE f.filmId = :filmId ")
	List<FilmInStoreResponse> getFilmInStore(@Param("filmId") String filmId);

	List<Film> findAllByOrderByTitleAsc(Pageable page);

	@Query(value = "SELECT new it.course.exam.myfilmc4IVAN.payload.response.FilmResponse("
			+ "f.filmId, f.description, f.releaseYear, f.title, f.countryId.countryName , f.languageId.languageName) "
			+ "FROM Film f " 
			+ "INNER JOIN Country c ON f.countryId.countryId = c.countryId "
			+ "WHERE f.countryId.countryId = :countryId ")
	List<FilmResponse> getFilmByCountry(@Param("countryId") String countryId);

	@Query(value = "SELECT new it.course.exam.myfilmc4IVAN.payload.response.FilmResponse("
			+ "f.filmId, f.description, f.releaseYear, f.title, f.countryId.countryName , f.languageId.languageName) "
			+ "FROM Film f " + "INNER JOIN Language l ON f.languageId.languageId = l.languageId "
			+ "WHERE f.languageId.languageId = :languageId ")
	List<FilmResponse> getFilmByLanguage(@Param("languageId") String languageId);

	boolean existsByStoresAndFilmId(Store store, String filmId);

	@Query(value = "SELECT fc.film_id From film_actor fc WHERE fc.actor_id = :actorId ", nativeQuery = true)
	List<String> getAllFilmIdByActorId(@Param("actorId") String actorId);

}
