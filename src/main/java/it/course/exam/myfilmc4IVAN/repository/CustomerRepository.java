package it.course.exam.myfilmc4IVAN.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilmc4IVAN.entity.Customer;
import it.course.exam.myfilmc4IVAN.payload.response.CustomerResponse;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	@Query(value = "SELECT DISTINCT new it.course.exam.myfilmc4IVAN.payload.response.CustomerResponse(" 
			+ "c.email, "
			+ "c.firstName, "
			+ "c.lastName " 
			+ ") " 
			+ "FROM Customer c  "
			+ "JOIN Rental r on r.rentalId.storeId.storeId=:storeId "
			+ "AND r.rentalId.customerEmail = c ")
	List<CustomerResponse> getAllCustomerByStore(@Param("storeId") String storeId);

}
