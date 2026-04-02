package com.doritech.CustomerService.Exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.doritech.CustomerService.Entity.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity handleAlreadyExists(
			ResourceAlreadyExistsException ex) {
		logger.error("Resource Already Exists: ", ex);
		return new ResponseEntity(ex.getMessage(), 409, null);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity handleNoResourceFoundException(
			NoResourceFoundException ex) {

		return new ResponseEntity(
				"API endpoint not found: " + ex.getResourcePath(), 404, null);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity handleNotFound(ResourceNotFoundException ex) {
		logger.error("Resource Not Found: {}", ex.getMessage());
		return new ResponseEntity(ex.getMessage(), 404, null);
	}
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity handleBadRequest(BadRequestException ex) {
		logger.error("Bad Request: ", ex);
		return new ResponseEntity(ex.getMessage(), 400, null);
	}

	@ExceptionHandler(DatabaseOperationException.class)
	public ResponseEntity handleDatabase(DatabaseOperationException ex) {
		logger.error("Database Error: ", ex);
		return new ResponseEntity("Database Operation Failed", 500, null);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity handleDataIntegrityViolation(
			DataIntegrityViolationException ex, HttpServletRequest request) {

		String userMessage = "Cannot delete this record because it is linked with other records.";

		Throwable rootCause = ex.getRootCause();
		String httpMethod = request.getMethod();

		if (rootCause != null && rootCause.getMessage() != null) {

			String message = rootCause.getMessage().toLowerCase();

			// FOREIGN KEY ERROR
			if (message.contains("foreign key")
					|| message.contains("constraint")) {

				if ("DELETE".equalsIgnoreCase(httpMethod)) {
					userMessage = "Cannot delete this record because it is linked with other records.";
				} else if ("PUT".equalsIgnoreCase(httpMethod)
						|| "PATCH".equalsIgnoreCase(httpMethod)
						|| "POST".equalsIgnoreCase(httpMethod)) {
					userMessage = "Cannot update this record because it is linked with other records.";
				}
			}

			// DUPLICATE ENTRY
			if (message.contains("duplicate") || message.contains("unique")) {
				userMessage = "Duplicate entry. This record already exists.";
			}
		}

		// ApiResponse<Object> response = new ApiResponse<>();
		// response.setSuccess(false);
		// response.setMessage(userMessage);
		// response.setData(null);
		// response.setErrors(null);
		// response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		// response.setPath(request.getRequestURI());

		return new ResponseEntity(userMessage, HttpStatus.BAD_REQUEST.value(),
				null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleValidation(MethodArgumentNotValidException ex) {

		logger.error("Validation Error: ", ex);

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getDefaultMessage()).toList();

		return new ResponseEntity("Validation Failed", 400, errors);
	}

	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity handleTransaction(TransactionSystemException ex) {
		logger.error("Transaction Failed: ", ex);
		return new ResponseEntity("Transaction Failed", 500, null);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity handleDuplicate(DuplicateResourceException ex) {
		logger.error("Duplicate Resource: ", ex);
		return new ResponseEntity(ex.getMessage(), 409, null);
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity handleInternalServer(InternalServerException ex) {
		logger.error("Internal Server Error: ", ex);
		return new ResponseEntity(ex.getMessage(), 500, null);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleValidationException(
			HandlerMethodValidationException ex) {

		String errorMessage = ex.getAllErrors().stream()
				.map(error -> error.getDefaultMessage()).findFirst()
				.orElse("Validation failed");

		ResponseEntity response = new ResponseEntity();
		response.setMessage(errorMessage);
		response.setStatusCode(400);
		response.setPayload(null);

		return org.springframework.http.ResponseEntity.badRequest()
				.body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public org.springframework.http.ResponseEntity<Object> handleIllegalArgumentException(
			IllegalArgumentException ex) {

		logger.error("Validation error: {}", ex.getMessage(), ex);

		ResponseEntity response = new ResponseEntity();
		response.setMessage(ex.getMessage());
		response.setStatusCode(400);
		response.setPayload(null);

		return new org.springframework.http.ResponseEntity<>(response,
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleAll(Exception ex) {
		logger.error("Unhandled Exception: ", ex);
		return new ResponseEntity("Internal Server Error", 500, null);
	}
}