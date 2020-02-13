package br.com.jcpvix.githubapi.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import br.com.jcpvix.githubapi.dto.response.Response;
import br.com.jcpvix.githubapi.dto.response.ResponseError;

@RestControllerAdvice
public class DefaultExceptionHandler {
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<?> apiException(ApiException ex, WebRequest request) {
        ResponseError responseError = new ResponseError()
        		.setTimestamp(new Date())
        		.setMessage(ex.getMessage())
        		.setDetails(request.getDescription(false));        
        
		return new ResponseEntity<>(Response.error(responseError), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> defaultExceptionHandler(Exception ex, WebRequest request) {
        ResponseError responseError = new ResponseError()
        		.setTimestamp(new Date())
        		.setMessage(ex.getMessage())
        		.setDetails(request.getDescription(false));        
        
		return new ResponseEntity<>(Response.error(responseError), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}