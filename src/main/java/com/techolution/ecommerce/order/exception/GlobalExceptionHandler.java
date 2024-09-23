package com.techolution.ecommerce.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({InvalidOrderStatusException.class, OrderNotFoundException.class})
    public ResponseEntity<?> handleBadReruests(Exception ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), new HashMap<>());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), new HashMap<>());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return createResponse(HttpStatus.BAD_REQUEST, "Validation failed", Map.of("errors", errors));
    }

    private ResponseEntity<?> createResponse(HttpStatus status, String message, Map<String, Object> info) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.putAll(info);
        return new ResponseEntity<>(body, status);
    }
}
