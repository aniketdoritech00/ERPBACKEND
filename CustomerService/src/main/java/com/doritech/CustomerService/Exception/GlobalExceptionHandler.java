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

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleNotFound(ResourceNotFoundException ex) {
		logger.error("Resource Not Found: {}", ex.getMessage());
		return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseEntity(ex.getMessage(), 404, null));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleNoResourceFoundException(
			NoResourceFoundException ex) {
		logger.error("API Endpoint Not Found: {}", ex.getResourcePath());
		return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseEntity("API endpoint not found: " + ex.getResourcePath(), 404, null));
	}

	@ExceptionHandler(BadRequestException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleBadRequest(BadRequestException ex) {
		logger.error("Bad Request: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseEntity(ex.getMessage(), 400, null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleValidation(
			MethodArgumentNotValidException ex) {
		logger.error("Validation Error: ", ex);
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.toList();
		return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseEntity("Validation Failed", 400, errors));
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleValidationException(
			HandlerMethodValidationException ex) {
		logger.error("Handler Method Validation Error: ", ex);
		String errorMessage = ex.getAllErrors().stream().map(error -> error.getDefaultMessage()).findFirst()
				.orElse("Validation failed");
		return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseEntity(errorMessage, 400, null));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleIllegalArgumentException(
			IllegalArgumentException ex) {
		logger.error("Illegal Argument: {}", ex.getMessage(), ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseEntity(ex.getMessage(), 400, null));
	}

	@ExceptionHandler(org.springframework.web.bind.MissingRequestHeaderException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleMissingHeader(
			org.springframework.web.bind.MissingRequestHeaderException ex) {
		logger.error("Missing Header: {}", ex.getHeaderName(), ex);
		String message = "Required header '" + ex.getHeaderName() + "' is missing";
		return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseEntity(message, 400, null));
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleAlreadyExists(
			ResourceAlreadyExistsException ex) {
		logger.error("Resource Already Exists: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ResponseEntity(ex.getMessage(), 409, null));
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleDuplicateResource(
			DuplicateResourceException ex) {
		logger.error("Duplicate Resource: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ResponseEntity(ex.getMessage(), 409, null));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleDataIntegrityViolation(
			DataIntegrityViolationException ex) {
		logger.error("Data Integrity Violation: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ResponseEntity("A data conflict occurred. Please check your input.", 409, null));
	}

	@ExceptionHandler(ExternalServiceException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleExternalService(ExternalServiceException ex) {
		logger.error("External Service Error: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_GATEWAY)
				.body(new ResponseEntity(ex.getMessage(), 502, null));
	}

	@ExceptionHandler(DatabaseOperationException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleDatabase(DatabaseOperationException ex) {
		logger.error("Database Error: ", ex);
		String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
		return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseEntity(message, 500, null));
	}

	@ExceptionHandler(InternalServerException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleInternalServer(InternalServerException ex) {
		logger.error("Internal Server Error: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseEntity(ex.getMessage(), 500, null));
	}

	@ExceptionHandler(TransactionSystemException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleTransaction(TransactionSystemException ex) {
		logger.error("Transaction Failed: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseEntity("Transaction Failed", 500, null));
	}

	@ExceptionHandler(ApplicationException.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleApplicationException(ApplicationException ex) {
		logger.error("Application Exception [ErrorCode: {}]: ", ex.getErrorCode(), ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseEntity("[" + ex.getErrorCode() + "] " + ex.getMessage(), 500, null));
	}

	@ExceptionHandler(Exception.class)
	public org.springframework.http.ResponseEntity<ResponseEntity> handleAll(Exception ex) {
		logger.error("Unhandled Exception: ", ex);
		return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseEntity("Internal Server Error", 500, null));
	}
}