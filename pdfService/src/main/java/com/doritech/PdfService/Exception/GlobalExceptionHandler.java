package com.doritech.PdfService.Exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.doritech.PdfService.Response.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity handleDuplicate(DuplicateResourceException ex) {
        logger.error("Duplicate Resource: {}", ex.getMessage(), ex);
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT.value(), null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleNotFound(ResourceNotFoundException ex) {
        logger.error("Resource Not Found: {}", ex.getMessage(), ex);
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequest(BadRequestException ex) {
        logger.error("Bad Request: {}", ex.getMessage(), ex);
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity handleDatabase(DatabaseOperationException ex) {
        logger.error("Database Error: {}", ex.getMessage(), ex);
        return new ResponseEntity("Database Operation Failed", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Database error";
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST.value(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .toList();

        return new ResponseEntity("Validation Failed", HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity handleTransaction(TransactionSystemException ex) {
        logger.error("Transaction Failed", ex);
        return new ResponseEntity("Transaction Failed", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity handlePdf(PdfGenerationException ex) {
        logger.error("PDF Error: {}", ex.getMessage(), ex);
        return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegal(IllegalArgumentException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAll(Exception ex) {
        logger.error("Unhandled Exception", ex);
        return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }
}