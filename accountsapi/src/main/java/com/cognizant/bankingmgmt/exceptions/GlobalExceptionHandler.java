package com.cognizant.bankingmgmt.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cognizant.bankingmgmt.dtos.GenericMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNullException.class)
    public ResponseEntity<GenericMessage> handleCustomerNullException(AccountNullException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericMessage(ex.getMessage()));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<GenericMessage> handleCustomerNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GenericMessage(ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GenericMessage<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericMessage<>(errors, "Validation failed"));
	}

}