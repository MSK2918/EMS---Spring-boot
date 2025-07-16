package com.susheel.ems.handler;


import com.susheel.ems.exception.ErrorCode;
import com.susheel.ems.exception.ExceptionResponse;
import com.susheel.ems.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // field validation error messages
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException (MethodArgumentNotValidException exception) {
        // create set for adding fields errors
        Set<String> errors = new HashSet<>();

        // get errors and add to set
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        // add errors set to exception response and return the response
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    // Duplicate email handling exception
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleException (DataIntegrityViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ErrorCode.DUPLICATE_FIELD.getCode())
                                .errorDescription(ErrorCode.DUPLICATE_FIELD.getDescription())
                                .build()
                );
    }

    // for employee not found with their Id
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleException (ResourceNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(HttpStatus.NOT_FOUND.value())
                                .errorDescription(exception.getMessage())
                                .build()
                );
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleException (NullPointerException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ErrorCode.EMPTY_EMPLOYEES.getCode())
                                .errorDescription(ErrorCode.EMPTY_EMPLOYEES.getDescription())
                                .error(exception.getMessage())
                                .build()
                );
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException (Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ErrorCode.INTERNAL_ERROR.getCode())
                                .errorDescription(ErrorCode.INTERNAL_ERROR.getDescription())
                                .build()
                );
    }
}


