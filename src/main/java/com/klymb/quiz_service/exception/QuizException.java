package com.klymb.quiz_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class QuizException extends RuntimeException{
    private final String message;
    private final HttpStatus status;

}
