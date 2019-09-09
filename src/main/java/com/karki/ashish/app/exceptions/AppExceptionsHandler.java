package com.karki.ashish.app.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.karki.ashish.app.ui.model.response.CustomErrorMessage;

@ControllerAdvice
public class AppExceptionsHandler {

	@ExceptionHandler(value = { UserServiceException.class })
	public ResponseEntity<Object> handleUserServiceExceptions(UserServiceException ex, WebRequest request) {
		CustomErrorMessage customErrorMessage = new CustomErrorMessage(new Date(), ex.getMessage());
		return new ResponseEntity<Object>(customErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
		CustomErrorMessage customErrorMessage = new CustomErrorMessage(new Date(), ex.getMessage());

		return new ResponseEntity<Object>(customErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
