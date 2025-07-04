package com.klymb.quiz_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class QuestionBankException extends RuntimeException{
    private String message;
    private HttpStatus status;

}
