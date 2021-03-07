package it.course.exam.myfilmc4IVAN.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilmc4IVAN.entity.Customer;
import it.course.exam.myfilmc4IVAN.payload.request.CustomerRequest;
import it.course.exam.myfilmc4IVAN.payload.response.ApiResponseCustom;
import it.course.exam.myfilmc4IVAN.payload.response.CustomerResponse;
import it.course.exam.myfilmc4IVAN.payload.response.ResponseEntityHandler;
import it.course.exam.myfilmc4IVAN.repository.CustomerRepository;
import it.course.exam.myfilmc4IVAN.repository.StoreRepository;

@RestController
@Validated
public class CustomerController {

	
	@Autowired CustomerRepository customerRepository;
	@Autowired StoreRepository storeRepository;
	
	@PostMapping("/add-update-customer")
	@Transactional
	public ResponseEntity<ApiResponseCustom> addUpdateCustomer(@RequestBody @Valid CustomerRequest customerRequest,	HttpServletRequest request) {
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		//Verifico se l'email esiste
		Optional<Customer> customer = customerRepository.findById(customerRequest.getEmail());
		
		//se esiste aggiorno gli altri campi
		if (customer.isPresent()) {
			if(customerRequest.getFirstName().isEmpty()) {
				customer.get().setFirstName(null);
			}else {
				customer.get().setFirstName(customerRequest.getFirstName().toUpperCase());
			}
			
			if(customerRequest.getLastName().isEmpty()) {
				customer.get().setLastName(null);
			}else {
				customer.get().setLastName(customerRequest.getLastName().toUpperCase());
			}
				
			customerRepository.save(customer.get());
			response.setMsg("Customer successfully updated !");
			response.setStatus(HttpStatus.OK);
			
		}else {//Se l'email non esiste creo un nuovo Customer
			if (customerRequest.getFirstName().isEmpty() && customerRequest.getLastName().isEmpty() ) {
					customerRepository.save(new Customer(customerRequest.getEmail().toLowerCase(),
				    		null,null));
				 	response.setMsg("Customer successfully added !");
					response.setStatus(HttpStatus.OK);
			}else if(!customerRequest.getFirstName().isEmpty() && customerRequest.getLastName().isEmpty()){
				
				    customerRepository.save(new Customer(customerRequest.getEmail().toLowerCase(),
				    		customerRequest.getFirstName().toUpperCase(),
				    		null));
					response.setMsg("Customer successfully added !");
					response.setStatus(HttpStatus.OK);
			}else if(customerRequest.getFirstName().isEmpty() && !customerRequest.getLastName().isEmpty()){
				
				    customerRepository.save(new Customer(customerRequest.getEmail().toLowerCase(),null,
				    		customerRequest.getLastName().toUpperCase()));
					response.setMsg("Customer successfully added !");
					response.setStatus(HttpStatus.OK);
			}else {
				    customerRepository.save(new Customer(customerRequest.getEmail().toLowerCase(),
				    		customerRequest.getFirstName().toUpperCase(),
				    		customerRequest.getLastName().toUpperCase()));
					response.setMsg("Customer successfully added !");
					response.setStatus(HttpStatus.OK);
			}
			
		}
	
		return response.getResponseEntity();
	}
	

	
	
	@GetMapping("/get-all-customers-by-store/{storeId}")
	public ResponseEntity<ApiResponseCustom> getAllCustomerByStore(@PathVariable @NotEmpty String storeId, 	HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		response.setMsg("Store Not found !");
		response.setStatus(HttpStatus.NOT_FOUND);
		
		//Verifico che lo store esista
		boolean store = storeRepository.existsById(storeId);
		
		if(store) {//se esiste prendo l'elenco dei Customer
			List<CustomerResponse> customerResponse = customerRepository.getAllCustomerByStore(storeId);
			if(customerResponse.isEmpty()) {
				response.setMsg("No Customers found in this store.");
				response.setStatus(HttpStatus.NOT_FOUND);
				return response.getResponseEntity();
			}
			response.setMsg(customerResponse);
			response.setStatus(HttpStatus.FOUND);
			
		}

		return response.getResponseEntity();
	
	}
	

	
}
