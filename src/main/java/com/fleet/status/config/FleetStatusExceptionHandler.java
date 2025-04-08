package com.fleet.status.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class FleetStatusExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericErrors(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
