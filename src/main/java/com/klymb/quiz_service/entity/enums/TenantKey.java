package com.klymb.quiz_service.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TenantKey {
    QUESTION_BANK_CATEGORY("Question Bank Category");

    private final String text;
}
