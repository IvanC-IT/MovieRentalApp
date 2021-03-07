package it.course.exam.myfilmc4IVAN.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilmc4IVAN.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository <Store,String> {

	

	Optional<Store> findByStoreIdAndStoreName(String storeId, String storeName);

	Optional<Store> findByStoreName(String storeName);
	
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO inventory ( store_id, film_id) VALUES (:storeId, :filmId ) ",nativeQuery=true)
	void addFilmToStore(@Param("storeId") String storeId,
			@Param("filmId") String filmId);
			


}
