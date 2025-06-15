package com.klymb.quiz_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class UserGroupException extends RuntimeException{
    private String message;
    private HttpStatus status;

}
