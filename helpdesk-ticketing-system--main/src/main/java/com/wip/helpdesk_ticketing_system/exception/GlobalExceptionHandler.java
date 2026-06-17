package com.wip.helpdesk_ticketing_system.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {



@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException ex) {
    return new ResponseEntity<>(
            new ApiResponse(ex.getMessage()),
            HttpStatus.NOT_FOUND
    );
}


@ExceptionHandler(TicketNotFoundException.class)
public ResponseEntity<ApiResponse> handleTicketNotFound(TicketNotFoundException ex) {
    return new ResponseEntity<>(
            new ApiResponse(ex.getMessage()),
            HttpStatus.NOT_FOUND
    );
}

@ExceptionHandler(AssignmentNotFoundException.class)
public ResponseEntity<ApiResponse> handleAssignmentNotFound(AssignmentNotFoundException ex) {
    return new ResponseEntity<>(
            new ApiResponse(ex.getMessage()),
            HttpStatus.NOT_FOUND
    );
}


@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
    );

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
}


@ExceptionHandler(Exception.class)
public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
    return new ResponseEntity<>(
            new ApiResponse(ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
    );
}


}
