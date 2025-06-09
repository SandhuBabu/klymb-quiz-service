package com.klymb.quiz_service.exception;

import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        ex.printStackTrace();
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AuthorizationDeniedException ex) {
        ex.printStackTrace();
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return extractConstraintViolation(ex);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String, String>> handleTransactionException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
            return extractConstraintViolation((org.hibernate.exception.ConstraintViolationException) cause);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Transaction failed."));
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Map<String, String>> handlePersistenceException(PersistenceException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
            return extractConstraintViolation((org.hibernate.exception.ConstraintViolationException) cause);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Persistence error."));
    }

    private ResponseEntity<Map<String, String>> extractConstraintViolation(Throwable ex) {
        String message = ex.getMessage();
        Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\)");
        Matcher matcher = pattern.matcher(message);

        Map<String, String> errors = new HashMap<>();
        if (matcher.find()) {
            String[] fields = matcher.group(1).split(",\\s*");
            String[] values = matcher.group(2).split(",\\s*");

            // Filter out tenant_id from both fields and values
            StringBuilder fieldDisplay = new StringBuilder();
            StringBuilder valueDisplay = new StringBuilder();

            for (int i = 0; i < fields.length; i++) {
                if (!"tenant_id".equalsIgnoreCase(fields[i])) {
                    if (fieldDisplay.length() > 0) {
                        fieldDisplay.append(", ");
                        valueDisplay.append(", ");
                    }
                    fieldDisplay.append(fields[i].replaceAll("_", " "));
                    valueDisplay.append(values[i]);
                }
            }

            if (fieldDisplay.length() > 0) {
                errors.put(fieldDisplay.toString(),
                        String.format("The %s '%s' is already in use.",
                                fieldDisplay.toString(),
                                valueDisplay.toString()
                        )
                );
            } else {
                errors.put("error", "Unique constraint violation occurred.");
            }
        } else {
            errors.put("error", "Unique constraint violation occurred.");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(FileFormatException.class)
    public ResponseEntity<Map<String, String>> handleFileFormatException(FileFormatException ex) {
        var errors = Map.of("file", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QuestionBankException.class)
    public ResponseEntity<Map<String, String>> handleQuestionBankException(QuestionBankException ex) {
        var errors = Map.of("error", ex.getMessage());
        return new ResponseEntity<>(errors, ex.getStatus());
    }
}
