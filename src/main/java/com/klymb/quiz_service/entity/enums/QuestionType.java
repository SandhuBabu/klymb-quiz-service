package com.klymb.quiz_service.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {
    SINGLE("Single Choice"),
    MULTIPLE("Multiple Choice");

    private final String text;
}
