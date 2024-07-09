package com.taskmanagement.taskcreationservice.controller.exceptionhandler;

import java.sql.SQLException;

import org.hibernate.exception.GenericJDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.taskmanagement.taskcreationservice.dto.ErrorDto;
import com.taskmanagement.taskcreationservice.exception.BadRequestException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@SuppressWarnings("unchecked")
@Slf4j
public class GlobalExceptionHandler<T> {

	@ExceptionHandler(GenericJDBCException.class)
	public ResponseEntity<T> handleDatabaseException(GenericJDBCException genericJDBCException) {
		if (genericJDBCException.getCause() instanceof SQLException
				&& ((SQLException) genericJDBCException.getCause()).getSQLState().equalsIgnoreCase("45000")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
					genericJDBCException.getCause().getMessage(), null, System.currentTimeMillis()));
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((T) new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, genericJDBCException.getMessage(),
							genericJDBCException.getLocalizedMessage(), System.currentTimeMillis()));
		}
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<T> handleBadRequestException(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
				ex.getMessage(), "Invalid request payload", System.currentTimeMillis()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<T> handleNotValidException(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				(T) new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getMessage(), System.currentTimeMillis()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<T> handleNotValidException(ConstraintViolationException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				(T) new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getMessage(), System.currentTimeMillis()));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<T> handleInvalidArguementsException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				(T) new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getMessage(), System.currentTimeMillis()));
	}
	@ExceptionHandler(CallNotPermittedException.class)
	public ResponseEntity<T> handleCallNotPermittedException(CallNotPermittedException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
				(T) new ErrorDto(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), ex.getMessage(), System.currentTimeMillis()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<T> handleException(Exception ex) {
		log.error("Exception occurred with details", ex);
		if (ex instanceof BadRequestException) {
			return handleBadRequestException((BadRequestException) ex);

		} else if (ex instanceof ConstraintViolationException) {
			return handleNotValidException((ConstraintViolationException) ex);

		} else if (ex instanceof GenericJDBCException) {
			return handleDatabaseException((GenericJDBCException) ex);

		}  else if (ex instanceof MethodArgumentNotValidException) {
			return handleNotValidException((MethodArgumentNotValidException) ex);

		}  else if (ex instanceof IllegalArgumentException) {
			return handleInvalidArguementsException((IllegalArgumentException) ex);
			
		} else if (ex instanceof CallNotPermittedException) {
			return handleCallNotPermittedException((CallNotPermittedException)ex);
			
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((T) new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getLocalizedMessage(),
							System.currentTimeMillis()));
		}
	}
}
