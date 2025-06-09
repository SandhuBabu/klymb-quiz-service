package com.klymb.quiz_service.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionBankStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String text;
}
