package com.klymb.quiz_service.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QuizStatus {

    SCHEDULED("Scheduled"),
    LIVE("Live"),
    COMPLETED("Completed"),
    RE_SCHEDULED("Re-Scheduled"),
    CANCELLED("Cancelled");

    private final String text;

}
