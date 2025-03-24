package com.example.order.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException ex) {
        String errorMessage;
        if (ex.request().url().contains("users")) {
            errorMessage = "The Users service is temporarily unavailable. Please try again later.";
        } else if (ex.request().url().contains("products")) {
            errorMessage = "The Product service is temporarily unavailable. Please try again later.";
        }else {
            errorMessage = "An error occurred while communicating with the external service.";
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorMessage);
    }

//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<String> handleFeignException(FeignException ex) {
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("The Users service is temporarily unavailable. Please try again later.");
//    }
}
