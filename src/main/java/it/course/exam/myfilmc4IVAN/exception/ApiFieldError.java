package it.course.exam.myfilmc4IVAN.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ApiFieldError {
	
	private int code;
	private HttpStatus status;
    private String message;
	
}
