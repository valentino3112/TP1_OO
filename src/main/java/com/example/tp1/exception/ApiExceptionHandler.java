package com.example.tp1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Transforme les exceptions metier en reponses HTTP propres,
 * au lieu de laisser remonter une erreur 500 avec une stack trace.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /** Corps JSON renvoye en cas d'erreur : { "status": 404, "message": "..." }. */
    public record ApiError(int status, String message) {
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(CarNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CarNotAvailableException.class)
    public ResponseEntity<ApiError> handleConflict(CarNotAvailableException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<ApiError> handleBadRequest(InvalidDatesException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiError(status.value(), message));
    }
}
