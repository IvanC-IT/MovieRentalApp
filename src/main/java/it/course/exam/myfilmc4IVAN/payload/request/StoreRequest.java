package it.course.exam.myfilmc4IVAN.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  @AllArgsConstructor @NoArgsConstructor
public class StoreRequest {
	
	@NotEmpty @Size(min=7, max=10, message = "storeId must be insert like STORE00 or STORE01 etc..")
	private String storeId;
	
	@NotEmpty @Size(min=3, max=50)
	private String storeName;

}
