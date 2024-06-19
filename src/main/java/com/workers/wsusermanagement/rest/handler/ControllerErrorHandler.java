package com.workers.wsusermanagement.rest.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<String> handle(ResponseStatusException e) {
        return handleResponseStatusException(e);
    }

    private ResponseEntity<String> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
}
