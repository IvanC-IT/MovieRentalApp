package it.course.exam.myfilmc4IVAN.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilmc4IVAN.entity.Rental;
import it.course.exam.myfilmc4IVAN.entity.RentalId;
import it.course.exam.myfilmc4IVAN.payload.response.FilmRentResponse;
import it.course.exam.myfilmc4IVAN.payload.response.FilmRentResponseNr;

@Repository
public interface RentalRepository extends JpaRepository<Rental, RentalId> {

	@Query(value = "SELECT COUNT(*) FROM rental  WHERE rental_date BETWEEN :begDate AND :endDate ", nativeQuery = true)
	int getRentalInDateRange(@Param("begDate") Date begDate, @Param("endDate") Date endDate);

	@Query(value = "SELECT new it.course.exam.myfilmc4IVAN.payload.response.FilmRentResponse(" 
			+ "f.filmId, f.title) "
			+ "FROM Film f " 
			+ "INNER JOIN Rental r ON f.filmId = r.rentalId.filmId.filmId "
			+ "WHERE r.rentalId.customerEmail.email = :customerId ")
	List<FilmRentResponse> getAllFilmsRentByOneCustomer(@Param("customerId") String customerId);

	@Query(value = "SELECT " + " r " + "FROM Rental r " 
			+ "WHERE r.rentalId.customerEmail.email = :emailId "
			+ "AND r.rentalId.filmId.filmId = :filmId " 
			+ " AND r.rentalId.storeId.storeId = :storeId "
			+ " AND r.rentalRetun IS NULL ")

	Optional<Rental> checkValueRetuturnalDate(@Param("emailId") String emailId, @Param("filmId") String filmId,
			@Param("storeId") String storeId);

	@Query(value = "SELECT new it.course.exam.myfilmc4IVAN.payload.response.FilmRentResponseNr("
			+ "f.filmId, f.title, COUNT(r) ) " 
			+ "FROM Film f "
			+ "JOIN Rental r ON r.rentalId.filmId.filmId = f.filmId " 
			+ "GROUP BY f.filmId ORDER BY COUNT(r) DESC ")
	List<FilmRentResponseNr> filmMaxNumberOfRent();

}
