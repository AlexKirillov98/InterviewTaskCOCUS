package com.example.interviewtask.exceptions;

import org.kohsuke.github.GHFileNotFoundException;
import org.openapitools.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.NotAcceptableStatusException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<ErrorResponse> handleThrowable(Throwable ex) {
        if (ex instanceof GHFileNotFoundException) {
            var error = new ErrorResponse().status("404").message("User not found: " + ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        var error = new ErrorResponse().status("500").message("Internal Server Error: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException() {
        var error = new ErrorResponse().status("406").message("Media Type Not Acceptable");
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }
}