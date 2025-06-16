package com.klymb.quiz_service.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttemptStatus {

    STARTED("Started"),
    SUBMITTED("Submitted"),
    AUTO_SUBMIT("Auto Submit");

    private final String text;

}
