package com.klymb.quiz_service.exception;

import lombok.Getter;

@Getter
public class FileFormatException extends RuntimeException {
    private final String message;

    public FileFormatException(String message) {
        super(message);
        this.message = message;
    }
}
