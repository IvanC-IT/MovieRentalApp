package it.course.exam.myfilmc4IVAN.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilmc4IVAN.entity.Language;
import it.course.exam.myfilmc4IVAN.payload.response.CustomerResponse;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
	Optional<Language> findById(String languageId);

	@Query(value = "SELECT DISTINCT new it.course.exam.myfilmc4IVAN.payload.response.CustomerResponse("
			+ "c.email, c.firstName, c.lastName ) " 
			+ "FROM Customer c "
			+ " JOIN Rental r ON r.rentalId.customerEmail.email = c JOIN Film f  ON r.rentalId.filmId.filmId =f "
			+ "AND f.languageId.languageId =:languageId ")
	List<CustomerResponse> getCustomerRentFilmsByLanguageFilm(@Param("languageId") String languageId);

}
