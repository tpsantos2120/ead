package com.ead.authuser.controllers;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<?> onCallNotPermittedException() {
        return ResponseEntity.internalServerError()
                .body("Course service is not available at the moment.");
    }
}
