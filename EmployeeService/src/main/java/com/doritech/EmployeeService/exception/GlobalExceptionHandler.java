package com.doritech.EmployeeService.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.doritech.EmployeeService.entity.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity handleDuplicateResource(DuplicateResourceException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT.value(), null);
    }

    
 // ===== COMMON RESPONSE BUILDER =====
 	private ResponseEntity buildErrorResponse(String message, Object errors,
 			HttpStatus status, HttpServletRequest request) {

 		ApiResponse<Object> response = new ApiResponse<>();
 		response.setSuccess(false);
 		response.setMessage(message);
 		response.setErrors(errors);
 		response.setStatusCode(status.value());
 		response.setPath(request.getRequestURI());

 		return new ResponseEntity(message, status.value(), response);
 	}

 	// ===== VALIDATION EXCEPTION =====
 	@ExceptionHandler(MethodArgumentNotValidException.class)
 	public ResponseEntity handleValidationException(
 			MethodArgumentNotValidException ex, HttpServletRequest request) {

 		Map<String, String> errors = new HashMap<>();

 		ex.getBindingResult().getFieldErrors().forEach(error -> errors
 				.put(error.getField(), error.getDefaultMessage()));

 		return buildErrorResponse("Validation Failed", errors,
 				HttpStatus.BAD_REQUEST, request);
 	}

 	// ===============================
 	// ✅ RESOURCE NOT FOUND
 	// ===============================
 	@ExceptionHandler(ResourceNotFoundException.class)
 	public ResponseEntity handleNotFound(ResourceNotFoundException ex,
 			HttpServletRequest request) {

 		return buildErrorResponse(ex.getMessage(), null, HttpStatus.NOT_FOUND,
 				request);
 	}
 	
 	@ExceptionHandler(BusinessException.class)
	public ResponseEntity handleBusinessException(BusinessException ex,
			HttpServletRequest request) {

		return buildErrorResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST,
				request);
	}

 	@ExceptionHandler(ConfigDataResourceNotFoundException.class)
 	public ResponseEntity handleNotFound(ConfigDataResourceNotFoundException ex,
 			HttpServletRequest request) {
 		return buildErrorResponse("Data Not found", ex.getMessage(),
 				HttpStatus.NOT_FOUND, request);
 	}

 	// ===== FILE UPLOAD EXCEPTIONS =====
 	@ExceptionHandler(MultipartException.class)
 	public ResponseEntity handleMultipartException(MultipartException ex,
 			HttpServletRequest request) {

 		return buildErrorResponse(
 				"File upload error. Please check file format.", ex.getMessage(),
 				HttpStatus.BAD_REQUEST, request);
 	}

 	@SuppressWarnings("deprecation")
 	@ExceptionHandler(MaxUploadSizeExceededException.class)
 	public ResponseEntity handleMaxSizeException(
 			MaxUploadSizeExceededException ex, HttpServletRequest request) {

 		return buildErrorResponse("File size exceeds allowed limit.",
 				ex.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE, request);
 	}

 	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
 	public ResponseEntity handleUnsupportedMediaType(
 			HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {

 		return buildErrorResponse("Unsupported file type. Upload Excel only.",
 				ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
 	}

 	@ExceptionHandler(MissingServletRequestPartException.class)
 	public ResponseEntity handleMissingFile(
 			MissingServletRequestPartException ex, HttpServletRequest request) {

 		return buildErrorResponse("File is missing in request.",
 				ex.getMessage(), HttpStatus.BAD_REQUEST, request);
 	}

 	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity handleDataIntegrityViolation(
			DataIntegrityViolationException ex, HttpServletRequest request) {

		String userMessage = "Operation failed due to data integrity violation.";

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

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(false);
		response.setMessage(userMessage);
		response.setData(null);
		response.setErrors(null);
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity(userMessage, HttpStatus.BAD_REQUEST.value(),
				response);
	}

 	@ExceptionHandler(TransactionSystemException.class)
 	public ResponseEntity handleTransactionException(
 			TransactionSystemException ex, HttpServletRequest request) {

 		Throwable root = ex.getRootCause();

 		if (root instanceof jakarta.validation.ConstraintViolationException cve) {

 			Map<String, String> errors = new HashMap<>();

 			cve.getConstraintViolations().forEach(v -> errors
 					.put(v.getPropertyPath().toString(), v.getMessage()));

 			return buildErrorResponse("Validation Failed", errors,
 					HttpStatus.BAD_REQUEST, request);
 		}

 		return buildErrorResponse(
 				"Unable to process request. Please try again.",
 				root != null ? root.getMessage() : ex.getMessage(),
 				HttpStatus.INTERNAL_SERVER_ERROR, request);
 	}

 	@ExceptionHandler(ConstraintViolationException.class)
 	public ResponseEntity handleConstraintException(
 			ConstraintViolationException ex, HttpServletRequest request) {

 		return buildErrorResponse("Constraint violation error", ex.getMessage(),
 				HttpStatus.BAD_REQUEST, request);
 	}

 	@ExceptionHandler(IOException.class)
 	public ResponseEntity handleIOException(IOException ex,
 			HttpServletRequest request) {

 		return buildErrorResponse("Error reading uploaded file.",
 				ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
 	}

 	// ===== RUNTIME EXCEPTION =====
 	@ExceptionHandler(RuntimeException.class)
 	public ResponseEntity handleRuntimeException(RuntimeException ex,
 			HttpServletRequest request) {

 		return buildErrorResponse(ex.getMessage(), null,
 				HttpStatus.INTERNAL_SERVER_ERROR, request);
 	}

 	// ===== GENERIC EXCEPTION =====
 	@ExceptionHandler(Exception.class)
 	public ResponseEntity handleException(Exception ex,
 			HttpServletRequest request) {

 		return buildErrorResponse("Something went wrong", ex.getMessage(),
 				HttpStatus.INTERNAL_SERVER_ERROR, request);
 	}
}
