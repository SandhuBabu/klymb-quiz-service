package com.klymb.quiz_service.dto;

import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizReScheduleFormDto {
    @Future
    private LocalDateTime startDateTime;

    @Future
    private LocalDateTime endDateTime;
    private long duration;
}
