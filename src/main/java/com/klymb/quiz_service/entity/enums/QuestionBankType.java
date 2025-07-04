package com.klymb.quiz_service.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionBankType {
    PUBLIC("Public"),
    PRIVATE("Private"),
    INTERNAL("Internal");

    private final String text;
}
