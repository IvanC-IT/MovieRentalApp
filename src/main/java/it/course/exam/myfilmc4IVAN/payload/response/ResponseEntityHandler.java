package it.course.exam.myfilmc4IVAN.payload.response;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityHandler {

	private ResponseEntity<ApiResponseCustom> responseEntity;
	private Object msg;
	private HttpStatus status;
	private HttpServletRequest request;
	
	public ResponseEntityHandler(Object msg, HttpServletRequest request, HttpStatus status) {
		this.msg = msg;
		this.status = status;
		this.request = request;
		this.initialize();
	}
	
	public ResponseEntityHandler(HttpServletRequest request) {
		msg = "Default message";
		status = HttpStatus.OK;
		this.request = request;
	}
	
	public ResponseEntity<ApiResponseCustom> getResponseEntity() {
		return responseEntity;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
		this.initialize();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
		this.initialize();
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		this.initialize();
	}

	private void initialize() {
		ApiResponseCustom apiResponseCustom = new ApiResponseCustom(Instant.now(), status.value(), status.getReasonPhrase(), 
				msg, request.getRequestURI());
		responseEntity = new ResponseEntity<ApiResponseCustom>(apiResponseCustom, status);
	}
	
}