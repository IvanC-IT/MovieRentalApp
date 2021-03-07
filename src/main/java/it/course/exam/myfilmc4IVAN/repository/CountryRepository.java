package it.course.exam.myfilmc4IVAN.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilmc4IVAN.entity.Country;


@Repository
public interface CountryRepository extends JpaRepository<Country, String>{

	Optional<Country> findById(String countryId);

}
